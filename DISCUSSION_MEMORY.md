# 碳排放三维可视化系统 — 讨论记忆存档

> 本文档记录与 Kimi 就本项目后端设计的全部讨论结论，供后续 session 恢复上下文使用。

---

## 1. 项目定位

- **竞赛**：超图杯减碳竞赛作品
- **后端**：Spring Boot 3 + MyBatis + PostgreSQL + PostGIS
- **前端**：SuperMap iClient3D (WebGL/WebGPU) + Vue3
- **当前阶段**：完成架构设计，等待用户提供公式和样例数据后开始编码

---

## 2. 数据库设计（已确定）

### 2.1 数据分层

| 数据类型 | 表名 | 用途 | 参与筛选/统计 |
|---------|------|------|--------------|
| 主数据（系统预置） | `carbonpoint` | 默认展示、筛选、统计 | ✅ 是 |
| 用户自定义数据 | `carbon_custom_point` | 仅地图叠加显示 | ❌ 否 |

### 2.2 建表 SQL

```sql
CREATE EXTENSION IF NOT EXISTS postgis;

CREATE TABLE carbonpoint (
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(50),           -- 地块名称
    category    VARCHAR(50),           -- 用地类型
    area        NUMERIC,               -- 面积（平方米）
    emission    NUMERIC,               -- 碳排放量（吨）
    height      NUMERIC,               -- 建筑高度（米）
    year        INT,                   -- 年度
    quarter     VARCHAR(10),           -- 季度
    geom        GEOMETRY(Point, 4326), -- 空间坐标（WGS84）
    create_time TIMESTAMP DEFAULT NOW()
);
CREATE INDEX carbonpoint_geom_idx ON carbonpoint USING GIST (geom);

CREATE TABLE carbon_custom_point (
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    category    VARCHAR(50)  NOT NULL,
    year        INT          NOT NULL,
    quarter     VARCHAR(10)  NOT NULL,
    emission    DECIMAL(12,4) NOT NULL,
    geom        GEOMETRY(Point, 4326) NOT NULL,
    raw_params  JSONB,                 -- 原始计算参数存档
    create_time TIMESTAMP DEFAULT NOW()
);
```

### 2.3 设计要点

- `raw_params` 为 **JSONB**，不同用地类型的参数结构可完全不同
- 公式优化后可遍历本表，取出 `raw_params` 重新计算 `emission`
- 全年数据不做汇总行，`quarter=ALL` 时后端实时聚合

---

## 3. 用地类型（5种）

1. 工业区
2. 农业区
3. 商业区
4. 住宅区
5. 教育区

---

## 4. 项目包结构（已建空壳）

```
com.test.twincarbonboot
├── contorller
│   ├── LoginController.java               ← 登录/认证接口（空壳已建）
│   └── MonitoringController.java          ← 监测层业务接口（空壳已建）
├── service
│   ├── MonitoringService.java             ← 业务接口（空壳已建）
│   └── impl
│       └── MonitoringServiceImpl.java     ← 业务实现（空壳已建）
├── mapper
│   └── MonitoringMapper.java              ← 数据访问层（空壳已建）
├── pojo
│   ├── Result.java                        ← 通用响应封装（已保留）
│   ├── CarbonEmissionPoint.java           ← 主数据实体
│   └── CarbonCustomPoint.java             ← 自定义数据实体
├── calculator
│   └── CarbonCalculator.java              ← 碳排放计算公式（单独封装）
├── utils
│   └── ExcelParser.java                   ← EasyExcel 解析工具
└── exception
    ├── GlobalExceptionHandler.java         ← 全局异常（已保留）
    └── MonitoringException.java            ← 业务异常（已保留）
```

---

## 5. 碳排放计算模块设计

- **位置**：`calculator/CarbonCalculator.java`
- **设计**：单独封装，集中管理 5 类计算公式
- **接口**：`calculate(String category, Map<String, Object> params)`
- **内部**：switch 分发到 5 个私有方法（工业区/农业区/商业区/住宅区/教育区）

---

## 6. 接口清单（共 8 个）

| 方法 | 路径 | 功能 |
|------|------|------|
| GET | `/monitoring/ObservationPoint` | 主数据地图点（支持 `year` + `quarter` 筛选，`quarter=ALL` 聚合） |
| GET | `/monitoring/custom/points` | 自定义地图点 |
| GET | `/monitoring/statistics/category-ratio` | 饼图：各用地类型碳排放占比 |
| GET | `/monitoring/statistics/trend` | 折线图：季度碳排放趋势（支持按类型筛选） |
| GET | `/monitoring/query` | 对象查询：按名称搜索地块详情 |
| POST | `/monitoring/import` | Excel 上传预览（校验 + 计算 + 返回预览 JSON） |
| POST | `/monitoring/import/confirm` | 确认保存，批量入库 `carbon_custom_point` |
| POST | `/login` | 用户登录，校验账号密码，返回 token + 用户信息 + 系统配置 |

---

## 7. Excel 导入设计

### 7.1 模板规范

- **每种用地类型一个 Sheet**，Sheet 名称 = `category`
- **固定列**（每类都有）：地块名称、经度、纬度、年度、季度
- **变动列**（各类型特有计算参数）：等用户提供公式后确定

### 7.2 导入流程

```
前端上传 .xlsx
    → 后端 EasyExcel 按 Sheet 名识别类型
    → 字段校验（必填、坐标范围、数值合法性）
    → 按类型调用 CarbonCalculator 计算 emission
    → 组装预览数据（坐标 + 排放量）返回前端
    → 前端地图渲染预览点（不入库）
    → 用户点击"保存"
    → 后端批量 INSERT 进 carbon_custom_point（含 raw_params JSONB）
    → 前端清空预览层，重新加载自定义数据
```

### 7.3 预览与保存分离

- 预览数据：**不入库**，仅临时返回给前端渲染
- 保存后：数据进入 `carbon_custom_point`，作为独立图层叠加
- 自定义数据**不参与**右侧统计面板和筛选逻辑

---

## 8. 开发顺序

| Phase | 内容 | 接口 |
|-------|------|------|
| Phase 1 | 基础查询（让前端先把地图跑起来） | `/config/scene`、`/monitoring/ObservationPoint`、`/monitoring/custom/points` |
| Phase 2 | 右侧数据统计 | `/monitoring/statistics/*`、`/monitoring/query` |
| Phase 3 | Excel 导入 | `/monitoring/import`、`/monitoring/import/confirm` |

---

## 9. 前端交互要点

### 9.1 三维底图
- 登录时 `POST /login` 一并返回系统配置（含场景 URL）
- 前端登录成功后直接取 `response.data.config.sceneUrl` 加载场景
- 不再提供独立的 `/config/scene` 接口

### 9.2 地图数据
- 默认加载 `/monitoring/ObservationPoint`（主数据）
- 筛选下拉框（年度 + 季度）改变时重新调接口
- `quarter=ALL` 时后端聚合全年
- 用户自定义数据通过 `/monitoring/custom/points` 单独加载，用不同样式叠加

### 9.3 右侧统计
- 饼图：`/monitoring/statistics/category-ratio`
- 趋势折线图：`/monitoring/statistics/trend`
- 对象查询：`/monitoring/query?name=xxx`

---

## 10. 待用户提供材料

- [ ] **Excel 列头**：5 个用地类型各自的参数列（列名 + 单位）
- [ ] **计算公式**：每种类型的碳排放计算公式（系数、变量关系）
- [ ] **主数据样例**：系统预置默认数据（5-10 条，含坐标和排放量）
- [ ] **三维场景 URL**：SuperMap iServer 发布的三维场景服务地址
- [ ] **筛选细节**：年度范围（如 2022-2025），是否支持多年对比

---

## 11. 关键设计决策记录

| 决策点 | 结论 | 原因 |
|--------|------|------|
| 登录接口位置 | 单独 `LoginController` | 登录属于认证层，与业务监测层分离 |
| 配置获取方式 | 登录时合并返回 | 减少前端请求次数，初始化时一次性拿到 token + config |
| config 存放位置 | `LoginController` / `LoginService` | 配置作为登录初始化数据的一部分，不再单独建 ConfigController |
| 未登录权限 | 仅开放 `/login` | 其他所有接口返回 401，全系统受保护 |
| 自定义数据是否参与统计 | ❌ 否 | 保持主数据权威性，自定义数据仅用于地图叠加 |
| raw_params 存储方式 | JSONB | 5 类参数差异大，JSONB 无空列、表结构稳定、扩展性好 |
| Excel 组织方式 | 每类一个 Sheet | 结构清晰、用户友好、后端解析明确 |
| 计算公式存放位置 | 单独 `CarbonCalculator` 类 | 集中管理、便于单元测试、公式优化时可复用 |
| 全年数据是否存汇总行 | ❌ 不存 | 保持数据原子性，`quarter=ALL` 时后端实时聚合 |
| 预览数据是否入库 | ❌ 不入 | 两步导入：预览 → 确认保存，降低误操作风险 |

---

## 12. 项目目录位置

后端项目路径：`D:\AboutMyGrow\Supermap\Project\twin-carbon-boot`

---

## 13. 已生成文档

- `C:\Users\32934\Desktop\碳排放系统后端开发指导手册.docx` — 完整开发手册
- `twin-carbon-boot\DISCUSSION_MEMORY.md` — 本文件，快速恢复上下文

---

> 下次讨论时，可直接参考本文档恢复记忆，或从 Phase 1 开始逐步指导编码。
kimi -r fe2a1b22-815e-414f-bd80-d2a406553fb1