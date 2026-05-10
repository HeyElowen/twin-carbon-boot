# 接口文档（请求 + 响应）

> 前后端对接参考

---

## 通用说明

### 请求头

| Header | 必填 | 说明 |
|--------|------|------|
| `Content-Type` | 是 | `application/json`（文件上传用 `multipart/form-data`） |
| `X-Token` | 除 `/login` 外 | 登录接口返回的 token |

### 统一响应格式

```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

### 错误响应示例

```json
// 401 未登录
{
  "code": 401,
  "message": "未登录或登录已过期",
  "data": null
}

// 500 业务错误
{
  "code": 500,
  "message": "用户名或密码错误",
  "data": null
}
```

---

## 1. 登录

**POST** `/login`

### 请求体

```json
{
  "username": "admin",
  "password": "123456"
}
```

### 响应体

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "token": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "username": "admin",
    "config": {
      "sceneUrl": "http://localhost:8090/iserver/services/3D-xxxx/rest/realspace"
    }
  }
}
```

---

## 2. 主数据地图点

**GET** `/monitoring/points`

### URL 参数

```
?year=2025&quarter=Q3
```

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `year` | int | ✅ | 年度，如 2025 |
| `quarter` | string | ✅ | Q1 / Q2 / Q3 / Q4 / ALL |

### 响应体

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "name": "HOST",
      "category": "工业区",
      "area": 8369.05,
      "emission": 3523.31,
      "height": 12.0,
      "year": 2025,
      "quarter": "Q3",
      "geom": "POINT(116.397 39.916)",
      "lon": 116.397,
      "lat": 39.916,
      "createTime": "2026-05-09T04:20:19"
    }
  ]
}
```

---

## 3. 自定义地图点

**GET** `/monitoring/custom/points`

### 响应体

同 `/monitoring/points`，数据来源为 `carbon_custom_point`。

---

## 4. 饼图统计

**GET** `/monitoring/statistics/category-ratio`

### URL 参数

```
?year=2025&quarter=ALL
```

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `year` | int | ✅ | 年度 |
| `quarter` | string | ❌ | 季度，不传或 ALL 则统计全年 |

### 响应体

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "category": "工业区",
      "totalEmission": 15000.50
    },
    {
      "category": "农业区",
      "totalEmission": 3200.00
    },
    {
      "category": "商业区",
      "totalEmission": 8900.25
    }
  ]
}
```

---

## 5. 趋势折线图

**GET** `/monitoring/statistics/trend`

### URL 参数

```
?startYear=2022&endYear=2025&category=工业区
```

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `startYear` | int | ✅ | 起始年度 |
| `endYear` | int | ✅ | 结束年度 |
| `category` | string | ❌ | 用地类型，不传则查全部 |

### 响应体

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "period": "2022-Q1",
      "totalEmission": 8000.00
    },
    {
      "period": "2022-Q2",
      "totalEmission": 8500.00
    },
    {
      "period": "2022-Q3",
      "totalEmission": 9200.50
    }
  ]
}
```

---

## 6. 对象查询

**GET** `/monitoring/query`

### URL 参数

```
?name=商业区
```

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `name` | string | ✅ | 地块名称关键字，支持模糊匹配 |

### 响应体

```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "name": "商业区-A12地块",
      "category": "商业区",
      "area": 5000.0,
      "emission": 1245.0,
      "height": 15.0,
      "year": 2025,
      "quarter": "Q2",
      "geom": "POINT(116.4 39.9)",
      "lon": 116.4,
      "lat": 39.9,
      "createTime": "2026-05-09T04:20:19"
    }
  ]
}
```

---

## 7. Excel 上传预览

**POST** `/monitoring/import`

### 请求头

```
Content-Type: multipart/form-data
X-Token: a1b2c3d4-xxxx
```

### 请求体

```
file: [二进制 Excel 文件 .xlsx]
```

### 响应体

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "batchId": "preview-batch-uuid",
    "totalCount": 50,
    "validCount": 48,
    "invalidCount": 2,
    "previewPoints": [
      {
        "name": "工业区-B01",
        "category": "工业区",
        "emission": 5200.5,
        "lon": 116.5,
        "lat": 39.8
      }
    ],
    "errors": [
      {
        "row": 5,
        "field": "用电量",
        "message": "数值不能为负数"
      }
    ]
  }
}
```

---

## 8. 确认入库

**POST** `/monitoring/import/confirm`

### 请求头

```
Content-Type: application/json
X-Token: a1b2c3d4-xxxx
```

### 请求体

```json
{
  "batchId": "preview-batch-uuid"
}
```

### 响应体

```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```
