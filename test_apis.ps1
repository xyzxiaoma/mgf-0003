$headers = @{'Content-Type' = 'application/json'}

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  校园社团活动管理系统 - 接口测试" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# ============ 1. 认证接口 ============
Write-Host "=== 1. 认证接口测试 ===" -ForegroundColor Cyan
Write-Host ""

# 1.1 注册 - 管理员
Write-Host "--- 1.1 注册 - 管理员 ---" -ForegroundColor Green
$body = @{
    username = 'admin1'
    password = '123456'
    name = '系统管理员'
    studentNo = 'A001'
    phone = '13800138001'
    role = 'ADMIN'
} | ConvertTo-Json
Write-Host "请求: POST http://localhost:8080/api/auth/register"
Write-Host "数据: $body"
try {
    $response = Invoke-RestMethod -Uri 'http://localhost:8080/api/auth/register' -Method Post -Body $body -Headers $headers
    Write-Host "返回: $($response | ConvertTo-Json -Depth 10)"
} catch {
    $errorMsg = $_.Exception.Message
    Write-Host "返回: 用户名已存在（预期）- $errorMsg"
}
Write-Host ""

# 1.2 注册 - 学生1
Write-Host "--- 1.2 注册 - 学生1 ---" -ForegroundColor Green
$body = @{
    username = 'student1'
    password = '123456'
    name = '张三'
    studentNo = '2024001'
    phone = '13800138002'
    role = 'STUDENT'
} | ConvertTo-Json
Write-Host "请求: POST http://localhost:8080/api/auth/register"
Write-Host "数据: $body"
try {
    $response = Invoke-RestMethod -Uri 'http://localhost:8080/api/auth/register' -Method Post -Body $body -Headers $headers
    Write-Host "返回: $($response | ConvertTo-Json -Depth 10)"
} catch {
    $errorMsg = $_.Exception.Message
    Write-Host "返回: 用户名已存在（预期）- $errorMsg"
}
Write-Host ""

# 1.3 注册 - 学生2
Write-Host "--- 1.3 注册 - 学生2 ---" -ForegroundColor Green
$body = @{
    username = 'student2'
    password = '123456'
    name = '李四'
    studentNo = '2024002'
    phone = '13800138003'
    role = 'STUDENT'
} | ConvertTo-Json
Write-Host "请求: POST http://localhost:8080/api/auth/register"
Write-Host "数据: $body"
try {
    $response = Invoke-RestMethod -Uri 'http://localhost:8080/api/auth/register' -Method Post -Body $body -Headers $headers
    Write-Host "返回: $($response | ConvertTo-Json -Depth 10)"
} catch {
    $errorMsg = $_.Exception.Message
    Write-Host "返回: 用户名已存在（预期）- $errorMsg"
}
Write-Host ""

# 1.4 登录 - 管理员
Write-Host "--- 1.4 登录 - 管理员 ---" -ForegroundColor Green
$body = @{username = 'admin1'; password = '123456'} | ConvertTo-Json
Write-Host "请求: POST http://localhost:8080/api/auth/login"
Write-Host "数据: $body"
$response = Invoke-RestMethod -Uri 'http://localhost:8080/api/auth/login' -Method Post -Body $body -Headers $headers
$adminToken = $response.data.token
Write-Host "返回: $($response | ConvertTo-Json -Depth 10)"
Write-Host ""

# 1.5 登录 - 学生1
Write-Host "--- 1.5 登录 - 学生1 ---" -ForegroundColor Green
$body = @{username = 'student1'; password = '123456'} | ConvertTo-Json
Write-Host "请求: POST http://localhost:8080/api/auth/login"
Write-Host "数据: $body"
$response = Invoke-RestMethod -Uri 'http://localhost:8080/api/auth/login' -Method Post -Body $body -Headers $headers
$student1Token = $response.data.token
Write-Host "返回: $($response | ConvertTo-Json -Depth 10)"
Write-Host ""

# 1.6 登录 - 学生2
Write-Host "--- 1.6 登录 - 学生2 ---" -ForegroundColor Green
$body = @{username = 'student2'; password = '123456'} | ConvertTo-Json
Write-Host "请求: POST http://localhost:8080/api/auth/login"
Write-Host "数据: $body"
$response = Invoke-RestMethod -Uri 'http://localhost:8080/api/auth/login' -Method Post -Body $body -Headers $headers
$student2Token = $response.data.token
Write-Host "返回: $($response | ConvertTo-Json -Depth 10)"
Write-Host ""

# 准备请求头
$adminHeaders = @{'Content-Type' = 'application/json'; 'Authorization' = "Bearer $adminToken"}
$student1Headers = @{'Content-Type' = 'application/json'; 'Authorization' = "Bearer $student1Token"}
$student2Headers = @{'Content-Type' = 'application/json'; 'Authorization' = "Bearer $student2Token"}

# 1.7 获取当前用户信息 - 管理员
Write-Host "--- 1.7 获取当前用户 - 管理员 ---" -ForegroundColor Green
Write-Host "请求: GET http://localhost:8080/api/auth/me"
$response = Invoke-RestMethod -Uri 'http://localhost:8080/api/auth/me' -Method Get -Headers $adminHeaders
Write-Host "返回: $($response | ConvertTo-Json -Depth 10)"
Write-Host ""

# 1.8 未登录访问
Write-Host "--- 1.8 未登录访问测试 ---" -ForegroundColor Yellow
Write-Host "请求: GET http://localhost:8080/api/activities (无Token)"
try {
    $response = Invoke-RestMethod -Uri 'http://localhost:8080/api/activities' -Method Get -Headers $headers -ErrorAction Stop
    Write-Host "返回: $($response | ConvertTo-Json -Depth 10)"
} catch {
    $respStream = $_.Exception.Response.Content.ReadAsStreamAsync().Result
    $reader = New-Object System.IO.StreamReader($respStream)
    $result = $reader.ReadToEnd()
    Write-Host "返回: $result"
}
Write-Host ""

# ============ 2. 活动管理接口 ============
Write-Host "=== 2. 活动管理接口测试 ===" -ForegroundColor Cyan
Write-Host ""

# 2.1 创建活动 - 管理员
Write-Host "--- 2.1 创建活动 - 管理员 ---" -ForegroundColor Green
$body = @{
    name = '2024年校园歌手大赛'
    clubName = '文艺社'
    activityDate = '2026-06-25'
    location = '学校大礼堂'
    maxCapacity = 100
    description = '欢迎各位同学踊跃报名参加！'
    status = 'NOT_STARTED'
} | ConvertTo-Json
Write-Host "请求: POST http://localhost:8080/api/activities"
Write-Host "数据: $body"
$response = Invoke-RestMethod -Uri 'http://localhost:8080/api/activities' -Method Post -Body $body -Headers $adminHeaders
$activity1Id = $response.data.id
Write-Host "返回: $($response | ConvertTo-Json -Depth 10)"
Write-Host "活动ID: $activity1Id"
Write-Host ""

# 2.2 创建活动2 - 管理员
Write-Host "--- 2.2 创建活动2 - 管理员 ---" -ForegroundColor Green
$body = @{
    name = '程序设计竞赛'
    clubName = '计算机协会'
    activityDate = '2026-06-28'
    location = '教学楼A101'
    maxCapacity = 50
    description = '提升编程能力，赢取丰厚奖品！'
    status = 'NOT_STARTED'
} | ConvertTo-Json
Write-Host "请求: POST http://localhost:8080/api/activities"
Write-Host "数据: $body"
$response = Invoke-RestMethod -Uri 'http://localhost:8080/api/activities' -Method Post -Body $body -Headers $adminHeaders
$activity2Id = $response.data.id
Write-Host "返回: $($response | ConvertTo-Json -Depth 10)"
Write-Host "活动ID: $activity2Id"
Write-Host ""

# 2.3 学生创建活动（无权限）
Write-Host "--- 2.3 学生创建活动（无权限） ---" -ForegroundColor Yellow
$body = @{
    name = '测试活动'
    clubName = '测试社团'
    activityDate = '2026-07-01'
    location = '测试地点'
    maxCapacity = 10
    description = '测试'
    status = 'NOT_STARTED'
} | ConvertTo-Json
Write-Host "请求: POST http://localhost:8080/api/activities (学生Token)"
try {
    $response = Invoke-RestMethod -Uri 'http://localhost:8080/api/activities' -Method Post -Body $body -Headers $student1Headers -ErrorAction Stop
    Write-Host "返回: $($response | ConvertTo-Json -Depth 10)"
} catch {
    $respStream = $_.Exception.Response.Content.ReadAsStreamAsync().Result
    $reader = New-Object System.IO.StreamReader($respStream)
    $result = $reader.ReadToEnd()
    Write-Host "返回: $result"
}
Write-Host ""

# 2.4 获取活动列表 - 学生
Write-Host "--- 2.4 获取活动列表 - 学生 ---" -ForegroundColor Green
Write-Host "请求: GET http://localhost:8080/api/activities"
$response = Invoke-RestMethod -Uri 'http://localhost:8080/api/activities' -Method Get -Headers $student1Headers
Write-Host "返回: $($response | ConvertTo-Json -Depth 10)"
Write-Host ""

# 2.5 按日期获取活动
Write-Host "--- 2.5 按日期获取活动 ---" -ForegroundColor Green
Write-Host "请求: GET http://localhost:8080/api/activities/day?date=2026-06-25"
$response = Invoke-RestMethod -Uri 'http://localhost:8080/api/activities/day?date=2026-06-25' -Method Get -Headers $student1Headers
Write-Host "返回: $($response | ConvertTo-Json -Depth 10)"
Write-Host ""

# 2.6 修改活动 - 管理员
Write-Host "--- 2.6 修改活动 - 管理员 ---" -ForegroundColor Green
$body = @{
    name = '2024年校园歌手大赛（改）'
    description = '活动时间调整，欢迎报名！'
} | ConvertTo-Json
Write-Host "请求: PUT http://localhost:8080/api/activities/$activity1Id"
Write-Host "数据: $body"
$response = Invoke-RestMethod -Uri "http://localhost:8080/api/activities/$activity1Id" -Method Put -Body $body -Headers $adminHeaders
Write-Host "返回: $($response | ConvertTo-Json -Depth 10)"
Write-Host ""

# 2.7 修改活动状态
Write-Host "--- 2.7 修改活动状态 ---" -ForegroundColor Green
$body = @{status = 'IN_PROGRESS'} | ConvertTo-Json
Write-Host "请求: PATCH http://localhost:8080/api/activities/$activity1Id/status"
Write-Host "数据: $body"
$response = Invoke-RestMethod -Uri "http://localhost:8080/api/activities/$activity1Id/status" -Method Patch -Body $body -Headers $adminHeaders
Write-Host "返回: $($response | ConvertTo-Json -Depth 10)"
Write-Host ""

# ============ 3. 报名管理接口 ============
Write-Host "=== 3. 报名管理接口测试 ===" -ForegroundColor Cyan
Write-Host ""

# 3.1 学生1报名活动1
Write-Host "--- 3.1 学生1报名活动1 ---" -ForegroundColor Green
Write-Host "请求: POST http://localhost:8080/api/activities/$activity1Id/registrations"
$response = Invoke-RestMethod -Uri "http://localhost:8080/api/activities/$activity1Id/registrations" -Method Post -Headers $student1Headers
$reg1Id = $response.data.id
Write-Host "返回: $($response | ConvertTo-Json -Depth 10)"
Write-Host "报名ID: $reg1Id"
Write-Host ""

# 3.2 学生2报名活动1
Write-Host "--- 3.2 学生2报名活动1 ---" -ForegroundColor Green
Write-Host "请求: POST http://localhost:8080/api/activities/$activity1Id/registrations"
$response = Invoke-RestMethod -Uri "http://localhost:8080/api/activities/$activity1Id/registrations" -Method Post -Headers $student2Headers
$reg2Id = $response.data.id
Write-Host "返回: $($response | ConvertTo-Json -Depth 10)"
Write-Host "报名ID: $reg2Id"
Write-Host ""

# 3.3 重复报名测试
Write-Host "--- 3.3 重复报名测试 ---" -ForegroundColor Yellow
Write-Host "请求: POST http://localhost:8080/api/activities/$activity1Id/registrations (学生1重复报名)"
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/activities/$activity1Id/registrations" -Method Post -Headers $student1Headers -ErrorAction Stop
    Write-Host "返回: $($response | ConvertTo-Json -Depth 10)"
} catch {
    $respStream = $_.Exception.Response.Content.ReadAsStreamAsync().Result
    $reader = New-Object System.IO.StreamReader($respStream)
    $result = $reader.ReadToEnd()
    Write-Host "返回: $result"
}
Write-Host ""

# 3.4 学生查看自己的报名记录
Write-Host "--- 3.4 学生查看自己的报名记录 ---" -ForegroundColor Green
Write-Host "请求: GET http://localhost:8080/api/users/me/registrations"
$response = Invoke-RestMethod -Uri 'http://localhost:8080/api/users/me/registrations' -Method Get -Headers $student1Headers
Write-Host "返回: $($response | ConvertTo-Json -Depth 10)"
Write-Host ""

# 3.5 管理员查看活动报名人员
Write-Host "--- 3.5 管理员查看活动报名人员 ---" -ForegroundColor Green
Write-Host "请求: GET http://localhost:8080/api/activities/$activity1Id/registrations"
$response = Invoke-RestMethod -Uri "http://localhost:8080/api/activities/$activity1Id/registrations" -Method Get -Headers $adminHeaders
Write-Host "返回: $($response | ConvertTo-Json -Depth 10)"
Write-Host ""

# ============ 4. 签到管理接口 ============
Write-Host "=== 4. 签到管理接口测试 ===" -ForegroundColor Cyan
Write-Host ""

# 4.1 管理员给学生1签到
Write-Host "--- 4.1 管理员给学生1签到 ---" -ForegroundColor Green
$body = @{userId = 2; remark = '正常签到'} | ConvertTo-Json
Write-Host "请求: POST http://localhost:8080/api/activities/$activity1Id/checkins"
Write-Host "数据: $body"
$response = Invoke-RestMethod -Uri "http://localhost:8080/api/activities/$activity1Id/checkins" -Method Post -Body $body -Headers $adminHeaders
Write-Host "返回: $($response | ConvertTo-Json -Depth 10)"
Write-Host ""

# 4.2 重复签到测试
Write-Host "--- 4.2 重复签到测试 ---" -ForegroundColor Yellow
$body = @{userId = 2; remark = '重复签到'} | ConvertTo-Json
Write-Host "请求: POST http://localhost:8080/api/activities/$activity1Id/checkins (学生1重复签到)"
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/activities/$activity1Id/checkins" -Method Post -Body $body -Headers $adminHeaders -ErrorAction Stop
    Write-Host "返回: $($response | ConvertTo-Json -Depth 10)"
} catch {
    $respStream = $_.Exception.Response.Content.ReadAsStreamAsync().Result
    $reader = New-Object System.IO.StreamReader($respStream)
    $result = $reader.ReadToEnd()
    Write-Host "返回: $result"
}
Write-Host ""

# 4.3 未报名学生签到测试
Write-Host "--- 4.3 未报名学生签到测试 ---" -ForegroundColor Yellow
$body = @{userId = 2; remark = '测试'} | ConvertTo-Json
Write-Host "请求: POST http://localhost:8080/api/activities/$activity2Id/checkins (活动2学生1未报名)"
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/activities/$activity2Id/checkins" -Method Post -Body $body -Headers $adminHeaders -ErrorAction Stop
    Write-Host "返回: $($response | ConvertTo-Json -Depth 10)"
} catch {
    $respStream = $_.Exception.Response.Content.ReadAsStreamAsync().Result
    $reader = New-Object System.IO.StreamReader($respStream)
    $result = $reader.ReadToEnd()
    Write-Host "返回: $result"
}
Write-Host ""

# 4.4 查看活动签到记录
Write-Host "--- 4.4 查看活动签到记录 ---" -ForegroundColor Green
Write-Host "请求: GET http://localhost:8080/api/activities/$activity1Id/checkins"
$response = Invoke-RestMethod -Uri "http://localhost:8080/api/activities/$activity1Id/checkins" -Method Get -Headers $adminHeaders
Write-Host "返回: $($response | ConvertTo-Json -Depth 10)"
Write-Host ""

# 4.5 查看签到状态统计
Write-Host "--- 4.5 查看签到状态统计 ---" -ForegroundColor Green
Write-Host "请求: GET http://localhost:8080/api/activities/$activity1Id/checkins/status"
$response = Invoke-RestMethod -Uri "http://localhost:8080/api/activities/$activity1Id/checkins/status" -Method Get -Headers $adminHeaders
Write-Host "返回: $($response | ConvertTo-Json -Depth 10)"
Write-Host ""

# ============ 5. 统计接口 ============
Write-Host "=== 5. 统计接口测试 ===" -ForegroundColor Cyan
Write-Host ""

# 5.1 月度统计
Write-Host "--- 5.1 月度统计 ---" -ForegroundColor Green
Write-Host "请求: GET http://localhost:8080/api/stats?month=2026-06"
$response = Invoke-RestMethod -Uri 'http://localhost:8080/api/stats?month=2026-06' -Method Get -Headers $adminHeaders
Write-Host "返回: $($response | ConvertTo-Json -Depth 10)"
Write-Host ""

# ============ 6. 取消报名和删除活动 ============
Write-Host "=== 6. 取消报名和删除活动 ===" -ForegroundColor Cyan
Write-Host ""

# 6.1 取消报名
Write-Host "--- 6.1 学生2取消报名 ---" -ForegroundColor Green
Write-Host "请求: DELETE http://localhost:8080/api/registrations/$reg2Id"
$response = Invoke-RestMethod -Uri "http://localhost:8080/api/registrations/$reg2Id" -Method Delete -Headers $student2Headers
Write-Host "返回: $($response | ConvertTo-Json -Depth 10)"
Write-Host ""

# 6.2 删除活动
Write-Host "--- 6.2 删除活动2 ---" -ForegroundColor Green
Write-Host "请求: DELETE http://localhost:8080/api/activities/$activity2Id"
$response = Invoke-RestMethod -Uri "http://localhost:8080/api/activities/$activity2Id" -Method Delete -Headers $adminHeaders
Write-Host "返回: $($response | ConvertTo-Json -Depth 10)"
Write-Host ""

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  所有接口测试完成！" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Cyan
