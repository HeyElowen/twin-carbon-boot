/**
 * Mock 数据文件
 * 前端开发时，将此文件复制到 src/api/ 目录下使用
 */

// ==================== 1. 登录 ====================
export const mockLogin = {
  code: 200,
  message: "success",
  data: {
    token: "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    username: "admin",
    config: {
      sceneUrl: "http://localhost:8090/iserver/services/3D-test/rest/realspace"
    }
  }
}

// ==================== 2. 主数据地图点 ====================
export const mockPoints = {
  code: 200,
  message: "success",
  data: [
    {
      id: 1,
      name: "工业园区-A01",
      category: "工业区",
      area: 12500.5,
      emission: 4520.35,
      height: 18.5,
      year: 2025,
      quarter: "Q3",
      geom: "POINT(116.397 39.916)",
      lon: 116.397,
      lat: 39.916,
      createTime: "2025-01-15T08:30:00"
    },
    {
      id: 2,
      name: "农业科技示范园",
      category: "农业区",
      area: 85600.0,
      emission: 1280.6,
      height: 5.0,
      year: 2025,
      quarter: "Q3",
      geom: "POINT(116.412 39.928)",
      lon: 116.412,
      lat: 39.928,
      createTime: "2025-01-15T08:30:00"
    },
    {
      id: 3,
      name: "中央商务区-B座",
      category: "商业区",
      area: 32000.0,
      emission: 6850.2,
      height: 45.0,
      year: 2025,
      quarter: "Q3",
      geom: "POINT(116.385 39.905)",
      lon: 116.385,
      lat: 39.905,
      createTime: "2025-01-15T08:30:00"
    },
    {
      id: 4,
      name: "阳光花园小区",
      category: "住宅区",
      area: 56000.0,
      emission: 3240.8,
      height: 22.0,
      year: 2025,
      quarter: "Q3",
      geom: "POINT(116.420 39.912)",
      lon: 116.42,
      lat: 39.912,
      createTime: "2025-01-15T08:30:00"
    },
    {
      id: 5,
      name: "实验中学",
      category: "教育区",
      area: 18500.0,
      emission: 1580.4,
      height: 15.0,
      year: 2025,
      quarter: "Q3",
      geom: "POINT(116.350 39.930)",
      lon: 116.35,
      lat: 39.93,
      createTime: "2025-01-15T08:30:00"
    }
  ]
}

// ==================== 3. 自定义地图点 ====================
export const mockCustomPoints = {
  code: 200,
  message: "success",
  data: [
    {
      id: 1,
      name: "用户上传-工业区M01",
      category: "工业区",
      emission: 5200.5,
      geom: "POINT(116.355 39.895)",
      lon: 116.355,
      lat: 39.895,
      createTime: "2025-03-20T14:20:00"
    },
    {
      id: 2,
      name: "用户上传-农业区M02",
      category: "农业区",
      emission: 950.2,
      geom: "POINT(116.438 39.945)",
      lon: 116.438,
      lat: 39.945,
      createTime: "2025-03-20T14:20:00"
    }
  ]
}

// ==================== 4. 饼图统计 ====================
export const mockCategoryRatio = {
  code: 200,
  message: "success",
  data: [
    { category: "工业区", totalEmission: 45200.8 },
    { category: "商业区", totalEmission: 38500.5 },
    { category: "住宅区", totalEmission: 26800.2 },
    { category: "农业区", totalEmission: 8900.6 },
    { category: "教育区", totalEmission: 5600.4 }
  ]
}

// ==================== 5. 趋势折线图（全部类型） ====================
export const mockTrendAll = {
  code: 200,
  message: "success",
  data: [
    { period: "2022-Q1", totalEmission: 98500.2 },
    { period: "2022-Q2", totalEmission: 102300.5 },
    { period: "2022-Q3", totalEmission: 108600.8 },
    { period: "2022-Q4", totalEmission: 115200.3 },
    { period: "2023-Q1", totalEmission: 112500.6 },
    { period: "2023-Q2", totalEmission: 118900.4 },
    { period: "2023-Q3", totalEmission: 125000.2 },
    { period: "2023-Q4", totalEmission: 132400.8 },
    { period: "2024-Q1", totalEmission: 128600.5 },
    { period: "2024-Q2", totalEmission: 135200.3 },
    { period: "2024-Q3", totalEmission: 141800.6 },
    { period: "2024-Q4", totalEmission: 148500.2 },
    { period: "2025-Q1", totalEmission: 144200.8 },
    { period: "2025-Q2", totalEmission: 150800.5 },
    { period: "2025-Q3", totalEmission: 157300.2 }
  ]
}

// ==================== 5. 趋势折线图（按类型筛选） ====================
export const mockTrendIndustry = {
  code: 200,
  message: "success",
  data: [
    { period: "2022-Q1", totalEmission: 12500.5 },
    { period: "2022-Q2", totalEmission: 13200.8 },
    { period: "2022-Q3", totalEmission: 14100.2 },
    { period: "2022-Q4", totalEmission: 15800.6 },
    { period: "2023-Q1", totalEmission: 15200.3 },
    { period: "2023-Q2", totalEmission: 16800.5 },
    { period: "2023-Q3", totalEmission: 17500.8 },
    { period: "2023-Q4", totalEmission: 18200.2 },
    { period: "2024-Q1", totalEmission: 17800.6 },
    { period: "2024-Q2", totalEmission: 18500.3 },
    { period: "2024-Q3", totalEmission: 19200.8 },
    { period: "2024-Q4", totalEmission: 20100.5 },
    { period: "2025-Q1", totalEmission: 19500.2 },
    { period: "2025-Q2", totalEmission: 20800.6 },
    { period: "2025-Q3", totalEmission: 21500.3 }
  ]
}

// ==================== 6. 对象查询 ====================
export const mockQuery = {
  code: 200,
  message: "success",
  data: [
    {
      id: 3,
      name: "中央商务区-B座",
      category: "商业区",
      area: 32000.0,
      emission: 6850.2,
      height: 45.0,
      year: 2025,
      quarter: "Q3",
      geom: "POINT(116.385 39.905)",
      lon: 116.385,
      lat: 39.905,
      createTime: "2025-01-15T08:30:00"
    },
    {
      id: 12,
      name: "中央商务区-C座",
      category: "商业区",
      area: 28000.0,
      emission: 5620.8,
      height: 38.0,
      year: 2025,
      quarter: "Q3",
      geom: "POINT(116.387 39.907)",
      lon: 116.387,
      lat: 39.907,
      createTime: "2025-01-15T08:30:00"
    }
  ]
}

// ==================== 7. Excel 上传预览（成功） ====================
export const mockImportSuccess = {
  code: 200,
  message: "success",
  data: {
    batchId: "preview-batch-uuid-2025",
    totalCount: 15,
    validCount: 15,
    invalidCount: 0,
    previewPoints: [
      {
        name: "工业区-M01",
        category: "工业区",
        emission: 5200.5,
        lon: 116.355,
        lat: 39.895
      },
      {
        name: "农业区-M02",
        category: "农业区",
        emission: 950.2,
        lon: 116.438,
        lat: 39.945
      },
      {
        name: "商业区-M03",
        category: "商业区",
        emission: 3200.0,
        lon: 116.298,
        lat: 39.878
      }
    ],
    errors: []
  }
}

// ==================== 7. Excel 上传预览（有错误） ====================
export const mockImportError = {
  code: 200,
  message: "success",
  data: {
    batchId: "preview-batch-uuid-2025",
    totalCount: 15,
    validCount: 12,
    invalidCount: 3,
    previewPoints: [
      {
        name: "工业区-M01",
        category: "工业区",
        emission: 5200.5,
        lon: 116.355,
        lat: 39.895
      }
    ],
    errors: [
      { row: 5, field: "用电量", message: "数值不能为负数" },
      { row: 8, field: "经度", message: "经度超出合理范围" },
      { row: 12, field: "季度", message: "季度只能为 Q1/Q2/Q3/Q4" }
    ]
  }
}

// ==================== 8. 确认入库 ====================
export const mockConfirm = {
  code: 200,
  message: "success",
  data: null
}

// ==================== 9. 401 未登录 ====================
export const mock401 = {
  code: 401,
  message: "未登录或登录已过期",
  data: null
}

// ==================== 10. 500 业务错误 ====================
export const mock500 = {
  code: 500,
  message: "用户名或密码错误",
  data: null
}

// ==================== 汇总对象（用于 Axios 拦截器方案） ====================
export const mockData = {
  'post:/login': mockLogin,
  'get:/monitoring/points': mockPoints,
  'get:/monitoring/custom/points': mockCustomPoints,
  'get:/monitoring/statistics/category-ratio': mockCategoryRatio,
  'get:/monitoring/statistics/trend': mockTrendAll,
  'get:/monitoring/query': mockQuery,
  'post:/monitoring/import': mockImportSuccess,
  'post:/monitoring/import/confirm': mockConfirm
}
