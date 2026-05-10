# 前端 Mock 数据 + 请求指南

> 前端无法连接后端时，将 `mock-data.js` 复制到 `src/api/` 目录下使用。  
> 文档结构：**Mock 数据 → 使用教程 → 请求代码**

---

## 第一部分：Mock 数据（JSON）

### 1. 登录

**接口：** `POST /login`

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "token": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "username": "admin",
    "config": {
      "sceneUrl": "http://localhost:8090/iserver/services/3D-test/rest/realspace"
    }
  }
}
```

---

### 2. 主数据地图点

**接口：** `GET /monitoring/points?year=2025&quarter=Q3`

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "name": "工业园区-A01",
      "category": "工业区",
      "area": 12500.5,
      "emission": 4520.35,
      "height": 18.5,
      "year": 2025,
      "quarter": "Q3",
      "geom": "POINT(116.397 39.916)",
      "lon": 116.397,
      "lat": 39.916,
      "createTime": "2025-01-15T08:30:00"
    },
    {
      "id": 2,
      "name": "农业科技示范园",
      "category": "农业区",
      "area": 85600.0,
      "emission": 1280.6,
      "height": 5.0,
      "year": 2025,
      "quarter": "Q3",
      "geom": "POINT(116.412 39.928)",
      "lon": 116.412,
      "lat": 39.928,
      "createTime": "2025-01-15T08:30:00"
    },
    {
      "id": 3,
      "name": "中央商务区-B座",
      "category": "商业区",
      "area": 32000.0,
      "emission": 6850.2,
      "height": 45.0,
      "year": 2025,
      "quarter": "Q3",
      "geom": "POINT(116.385 39.905)",
      "lon": 116.385,
      "lat": 39.905,
      "createTime": "2025-01-15T08:30:00"
    },
    {
      "id": 4,
      "name": "阳光花园小区",
      "category": "住宅区",
      "area": 56000.0,
      "emission": 3240.8,
      "height": 22.0,
      "year": 2025,
      "quarter": "Q3",
      "geom": "POINT(116.420 39.912)",
      "lon": 116.42,
      "lat": 39.912,
      "createTime": "2025-01-15T08:30:00"
    },
    {
      "id": 5,
      "name": "实验中学",
      "category": "教育区",
      "area": 18500.0,
      "emission": 1580.4,
      "height": 15.0,
      "year": 2025,
      "quarter": "Q3",
      "geom": "POINT(116.350 39.930)",
      "lon": 116.35,
      "lat": 39.93,
      "createTime": "2025-01-15T08:30:00"
    }
  ]
}
```

---

### 3. 自定义地图点

**接口：** `GET /monitoring/custom/points`

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "name": "用户上传-工业区M01",
      "category": "工业区",
      "emission": 5200.5,
      "geom": "POINT(116.355 39.895)",
      "lon": 116.355,
      "lat": 39.895,
      "createTime": "2025-03-20T14:20:00"
    },
    {
      "id": 2,
      "name": "用户上传-农业区M02",
      "category": "农业区",
      "emission": 950.2,
      "geom": "POINT(116.438 39.945)",
      "lon": 116.438,
      "lat": 39.945,
      "createTime": "2025-03-20T14:20:00"
    }
  ]
}
```

---

### 4. 饼图统计

**接口：** `GET /monitoring/statistics/category-ratio?year=2025&quarter=ALL`

```json
{
  "code": 200,
  "message": "success",
  "data": [
    { "category": "工业区", "totalEmission": 45200.8 },
    { "category": "商业区", "totalEmission": 38500.5 },
    { "category": "住宅区", "totalEmission": 26800.2 },
    { "category": "农业区", "totalEmission": 8900.6 },
    { "category": "教育区", "totalEmission": 5600.4 }
  ]
}
```

---

### 5. 趋势折线图

**全部类型：** `GET /monitoring/statistics/trend?startYear=2022&endYear=2025&category=`

```json
{
  "code": 200,
  "message": "success",
  "data": [
    { "period": "2022-Q1", "totalEmission": 98500.2 },
    { "period": "2022-Q2", "totalEmission": 102300.5 },
    { "period": "2022-Q3", "totalEmission": 108600.8 },
    { "period": "2022-Q4", "totalEmission": 115200.3 },
    { "period": "2023-Q1", "totalEmission": 112500.6 },
    { "period": "2023-Q2", "totalEmission": 118900.4 },
    { "period": "2023-Q3", "totalEmission": 125000.2 },
    { "period": "2023-Q4", "totalEmission": 132400.8 },
    { "period": "2024-Q1", "totalEmission": 128600.5 },
    { "period": "2024-Q2", "totalEmission": 135200.3 },
    { "period": "2024-Q3", "totalEmission": 141800.6 },
    { "period": "2024-Q4", "totalEmission": 148500.2 },
    { "period": "2025-Q1", "totalEmission": 144200.8 },
    { "period": "2025-Q2", "totalEmission": 150800.5 },
    { "period": "2025-Q3", "totalEmission": 157300.2 }
  ]
}
```

**按类型筛选：** `GET /monitoring/statistics/trend?startYear=2022&endYear=2025&category=工业区`

```json
{
  "code": 200,
  "message": "success",
  "data": [
    { "period": "2022-Q1", "totalEmission": 12500.5 },
    { "period": "2022-Q2", "totalEmission": 13200.8 },
    { "period": "2022-Q3", "totalEmission": 14100.2 },
    { "period": "2022-Q4", "totalEmission": 15800.6 },
    { "period": "2023-Q1", "totalEmission": 15200.3 },
    { "period": "2023-Q2", "totalEmission": 16800.5 },
    { "period": "2023-Q3", "totalEmission": 17500.8 },
    { "period": "2023-Q4", "totalEmission": 18200.2 },
    { "period": "2024-Q1", "totalEmission": 17800.6 },
    { "period": "2024-Q2", "totalEmission": 18500.3 },
    { "period": "2024-Q3", "totalEmission": 19200.8 },
    { "period": "2024-Q4", "totalEmission": 20100.5 },
    { "period": "2025-Q1", "totalEmission": 19500.2 },
    { "period": "2025-Q2", "totalEmission": 20800.6 },
    { "period": "2025-Q3", "totalEmission": 21500.3 }
  ]
}
```

---

### 6. 对象查询

**接口：** `GET /monitoring/query?name=中央商务区`

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 3,
      "name": "中央商务区-B座",
      "category": "商业区",
      "area": 32000.0,
      "emission": 6850.2,
      "height": 45.0,
      "year": 2025,
      "quarter": "Q3",
      "geom": "POINT(116.385 39.905)",
      "lon": 116.385,
      "lat": 39.905,
      "createTime": "2025-01-15T08:30:00"
    },
    {
      "id": 12,
      "name": "中央商务区-C座",
      "category": "商业区",
      "area": 28000.0,
      "emission": 5620.8,
      "height": 38.0,
      "year": 2025,
      "quarter": "Q3",
      "geom": "POINT(116.387 39.907)",
      "lon": 116.387,
      "lat": 39.907,
      "createTime": "2025-01-15T08:30:00"
    }
  ]
}
```

---

### 7. Excel 上传预览

**成功：** `POST /monitoring/import`

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "batchId": "preview-batch-uuid-2025",
    "totalCount": 15,
    "validCount": 15,
    "invalidCount": 0,
    "previewPoints": [
      { "name": "工业区-M01", "category": "工业区", "emission": 5200.5, "lon": 116.355, "lat": 39.895 },
      { "name": "农业区-M02", "category": "农业区", "emission": 950.2, "lon": 116.438, "lat": 39.945 },
      { "name": "商业区-M03", "category": "商业区", "emission": 3200.0, "lon": 116.298, "lat": 39.878 }
    ],
    "errors": []
  }
}
```

**校验失败：** `POST /monitoring/import`

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "batchId": "preview-batch-uuid-2025",
    "totalCount": 15,
    "validCount": 12,
    "invalidCount": 3,
    "previewPoints": [
      { "name": "工业区-M01", "category": "工业区", "emission": 5200.5, "lon": 116.355, "lat": 39.895 }
    ],
    "errors": [
      { "row": 5, "field": "用电量", "message": "数值不能为负数" },
      { "row": 8, "field": "经度", "message": "经度超出合理范围" },
      { "row": 12, "field": "季度", "message": "季度只能为 Q1/Q2/Q3/Q4" }
    ]
  }
}
```

---

### 8. 确认入库

**接口：** `POST /monitoring/import/confirm`

```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

### 错误响应

**401 未登录：**

```json
{
  "code": 401,
  "message": "未登录或登录已过期",
  "data": null
}
```

**500 业务错误：**

```json
{
  "code": 500,
  "message": "用户名或密码错误",
  "data": null
}
```

---

## 第二部分：使用教程

### 步骤 1：复制文件

将本文档同级的 **`mock-data.js`** 复制到前端项目的 `src/api/` 目录下。

### 步骤 2：修改 request.js

```javascript
import axios from 'axios'
import { mockData } from '@/api/mock-data.js'

// ========== Mock 开关 ==========
const USE_MOCK = true  // true = 使用 mock，false = 连真实后端
// ==============================

const request = axios.create({
  baseURL: 'http://localhost:8080',
  timeout: 10000
})

request.interceptors.request.use(config => {
  // Mock 模式：直接返回假数据，不发请求
  if (USE_MOCK) {
    const key = `${config.method.toLowerCase()}:${config.url}`
    const mock = mockData[key]
    if (mock) {
      return Promise.resolve({ data: mock, status: 200, config })
    }
  }
  
  // 真实请求：带上 token
  const token = localStorage.getItem('token')
  if (token) {
    config.headers['X-Token'] = token
  }
  return config
})

// 响应拦截器：统一处理 401
request.interceptors.response.use(
  response => response.data,
  error => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token')
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

export default request
```

### 步骤 3：切换真实后端

```javascript
const USE_MOCK = false  // 改为 false 即连接真实后端
```

---

## 第三部分：请求代码（axios）

在 `src/api/api.js` 里封装：

```javascript
import request from '@/utils/request'

// ==================== 登录 ====================
export function login(username, password) {
  return request({
    method: 'post',
    url: '/login',
    data: { username, password }
  })
}

// ==================== 主数据地图点 ====================
export function getPoints(year, quarter) {
  return request({
    method: 'get',
    url: '/monitoring/points',
    params: { year, quarter }
  })
}

// ==================== 自定义地图点 ====================
export function getCustomPoints() {
  return request({
    method: 'get',
    url: '/monitoring/custom/points'
  })
}

// ==================== 饼图统计 ====================
export function getCategoryRatio(year, quarter) {
  return request({
    method: 'get',
    url: '/monitoring/statistics/category-ratio',
    params: { year, quarter }
  })
}

// ==================== 趋势折线图 ====================
export function getTrend(startYear, endYear, category) {
  return request({
    method: 'get',
    url: '/monitoring/statistics/trend',
    params: { startYear, endYear, category }
  })
}

// ==================== 对象查询 ====================
export function queryByName(name) {
  return request({
    method: 'get',
    url: '/monitoring/query',
    params: { name }
  })
}

// ==================== Excel 上传预览 ====================
export function importExcel(file) {
  const formData = new FormData()
  formData.append('file', file)
  return request({
    method: 'post',
    url: '/monitoring/import',
    data: formData,
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}

// ==================== 确认入库 ====================
export function confirmImport(batchId) {
  return request({
    method: 'post',
    url: '/monitoring/import/confirm',
    data: { batchId }
  })
}
```

---

## 使用示例

```javascript
import { login, getPoints, getCategoryRatio } from '@/api/api'

// 登录
login('admin', '123456').then(res => {
  if (res.code === 200) {
    localStorage.setItem('token', res.data.token)
    console.log('场景地址：', res.data.config.sceneUrl)
  }
})

// 获取地图点
getPoints(2025, 'Q3').then(res => {
  if (res.code === 200) {
    res.data.forEach(p => {
      console.log(p.name, p.lon, p.lat, p.emission)
      // addEntity(p.lon, p.lat, p.emission, p.category)
    })
  }
})

// 饼图
getCategoryRatio(2025, 'ALL').then(res => {
  if (res.code === 200) {
    const chartData = res.data.map(item => ({
      name: item.category,
      value: item.totalEmission
    }))
    // myChart.setOption({ series: [{ type: 'pie', data: chartData }] })
  }
})
```

---

## mock-data.js 导出说明

| 导出名 | 用途 |
|--------|------|
| `mockData` | **汇总对象**（用于 Axios 拦截器），key 为 `方法:路径` |
| `mockLogin` | 登录响应 |
| `mockPoints` | 主数据地图点 |
| `mockCustomPoints` | 自定义地图点 |
| `mockCategoryRatio` | 饼图统计 |
| `mockTrendAll` | 趋势图（全部类型） |
| `mockTrendIndustry` | 趋势图（工业区筛选示例） |
| `mockQuery` | 对象查询 |
| `mockImportSuccess` | 上传预览（成功） |
| `mockImportError` | 上传预览（有错误） |
| `mockConfirm` | 确认入库 |
| `mock401` / `mock500` | 错误响应 |
