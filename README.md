### 一、交互主体

```text
角色：学生、员工（宿舍管理员、维修工、超级管理员）
实物：寝室、宿舍楼
活动：调换宿舍、寝室报修、失物招领、签到活动
```

### 二、功能简介

#### 1.学生方面

```text
1）查询当前所住寝室及宿舍楼基本信息
2）提出调换寝室的申请，由本宿舍楼的宿舍管理员进行审批
3）提交寝室内部物品损坏的报修申请，本宿舍楼的宿舍管理员进行审批
4）添加和删除失物招领信息（状态的更新）
```

#### 2.宿舍管理员

```text
1）查询所管理的宿舍楼和该宿舍楼下所有寝室的基本信息
2）检索寝室内部入住学生的基本信息
3）处理本宿舍楼内学生的调换宿舍申请要求
4）发布签到活动了解宿舍内学生的情况
```

#### 3.超级管理员

```text
1）管理宿舍管理员和维修工的基本信息
2）分配宿舍管理员到对应宿舍楼
3）检索调换宿舍、寝室报修、签到活动的详情
```

### 三、数据库设计

#### 1.学生表(student)

|   字段名    |   类型   | 长度 |         描述         | 可否为空 |      索引      |
| :---------: | :------: | :--: | :------------------: | :------: | :------------: |
|     id      |  BigInt  |      |      唯一标识符      |    N     |      主键      |
| student_num |   char   |  12  |         学号         |    N     |    唯一索引    |
|    name     | varchar  |  20  |         姓名         |    N     |                |
|  username   | varchar  |  20  | 用户名（默认为学号） |    N     |    唯一索引    |
|  password   | varchar  | 255  |         密码         |    N     |                |
|   gender    | TinyInt  |      | 性别（1：男，0：女） |    N     |                |
|    phone    |   char   |  11  |       电话号码       |    N     |                |
|   room_id   |  BigInt  |      |    寝室唯一标识符    |    Y     | 外键（寝室表） |
| create_time | datetime |      |       创建时间       |    Y     |                |
| update_time | datetime |      |       更新时间       |    Y     |                |
| create_user |  BigInt  |      |       创建用户       |    Y     |                |
| update_user |  BigInt  |      |       更新用户       |    Y     |                |
| is_deleted  | TinyInt  |      |    逻辑删除标志位    |    N     |                |

#### 2.员工表(employee)

|   字段名    |   类型   | 长度 |                      描述                       | 可否为空 |   索引   |
| :---------: | :------: | :--: | :---------------------------------------------: | :------: | :------: |
|     id      |  BigInt  |      |                   唯一标识符                    |    N     |   主键   |
|   job_num   | varchar  |  8   |                      工号                       |    N     | 唯一索引 |
|    name     | varchar  |  20  |                      姓名                       |    N     |          |
|  username   | varchar  |  20  |                     用户名                      |    N     | 唯一索引 |
|  passowrd   | varchar  | 255  |                      密码                       |    N     |          |
|   gender    | TinyInt  |      |              性别（1：男，0：女）               |    N     |          |
|    phone    |   char   |  11  |                    电话号码                     |    N     |          |
|    type     | TinyInt  |      | 身份（1：宿舍管理员，2：维修工，0：超级管理员） |    N     |          |
|   status    | TinyInt  |      |      状态（1：已安排，0：未安排，默认为0）      |    N     |          |
| create_time | datetime |      |                    创建时间                     |    Y     |          |
| update_time | datetime |      |                    更新时间                     |    Y     |          |
| create_user |  BigInt  |      |                    创建用户                     |    Y     |          |
| update_user |  BigInt  |      |                    更新用户                     |    Y     |          |
| is_deleted  | TinyInt  |      |                 逻辑删除标志位                  |    N     |          |

#### 3.寝室表(room)

|   字段名    |   类型   | 长度 |                  描述                   | 可否为空 |      索引      |
| :---------: | :------: | :--: | :-------------------------------------: | :------: | :------------: |
|     id      |  BigInt  |      |               唯一标识符                |    N     |      主键      |
|    floor    | TinyInt  |      |                  楼层                   |    N     |                |
|   number    | TinyInt  |      |                  序号                   |    N     |                |
|    name     | varchar  |  20  |                  名称                   |    Y     |                |
|   max_num   | TinyInt  |      |                  容量                   |    N     |                |
|   status    | TinyInt  |      | 状态（1：可入住，0：不可入住），默认为1 |    N     |                |
| building_id |  BigInt  |      |            宿舍楼唯一标识符             |    N     | 外键（宿舍楼） |
| create_time | datetime |      |                创建时间                 |    Y     |                |
| update_time | datetime |      |                更新时间                 |    Y     |                |
| create_user |  BigInt  |      |                创建用户                 |    Y     |                |
| update_user |  BigInt  |      |                更新用户                 |    Y     |                |
| is_deleted  | TinyInt  |      |             逻辑删除标志位              |    N     |                |

#### 4.宿舍楼(building)

|   字段名    |   类型   | 长度 |               描述               | 可否为空 |     索引     |
| :---------: | :------: | :--: | :------------------------------: | :------: | :----------: |
|     id      |  BigInt  |      |            唯一标识符            |    N     |     主键     |
|  location   | varchar  |  64  |               位置               |    N     |              |
|    name     | varchar  |  20  |               名称               |    Y     |   唯一索引   |
|    type     | TinyInt  |      | 类型（1：男宿舍楼，0：女宿舍楼） |    N     |              |
| employee_id |  BigInt  |      |     安排宿舍管理员唯一标识符     |    Y     | 外键（员工） |
| create_time | datetime |      |             创建时间             |    Y     |              |
| update_time | datetime |      |             更新时间             |    Y     |              |
| create_user |  BigInt  |      |             创建用户             |    Y     |              |
| update_user |  BigInt  |      |             更新用户             |    Y     |              |
| is_deleted  | TinyInt  |      |          逻辑删除标志位          |    N     |              |

#### 5.调换寝室事项(room_change)

|     字段名      |   类型   | 长度 |                         描述                          | 可否为空 |     索引     |
| :-------------: | :------: | :--: | :---------------------------------------------------: | :------: | :----------: |
|       id        |  BigInt  |      |                      唯一标识符                       |    N     |     主键     |
|   old_room_id   |  BigInt  |      |                  当前寝室唯一标识符                   |    N     | 外键（寝室） |
|   new_room_id   |  BigInt  |      |                  目标寝室唯一标识符                   |    N     | 外键（寝室） |
|   student_id    |  BigInt  |      |               提出申请的学生唯一标识符                |    N     | 外键（学生） |
|     reason      | varchar  | 255  |                       申请原因                        |    Y     |              |
| old_employee_id |  BigInt  |      |            处理事项的宿舍管理员唯一标识符             |    N     | 外键（员工） |
| new_employee_id |  BigInt  |      |            目标寝室的宿舍管理员唯一标识符             |    N     | 外键（员工） |
|     status      | TinyInt  |      | 状态（1：已申请，2：批准，3：驳回，4：接受，5：拒绝） |    N     |              |
|   create_time   | datetime |      |                       创建时间                        |    N     |              |
|   update_time   | datetime |      |                       更新时间                        |    N     |              |
|   create_user   |  BigInt  |      |                       创建用户                        |    N     |              |
|   update_user   |  BigInt  |      |                       更新用户                        |    N     |              |
|   is_deleted    | TinyInt  |      |                    逻辑删除标志位                     |    N     |              |

#### 6.寝室报修事项(room_repair)

|     字段名     |   类型   | 长度 |                  描述                   | 可否为空 |     索引     |
| :------------: | :------: | :--: | :-------------------------------------: | :------: | :----------: |
|       id       |  BigInt  |      |               唯一标识符                |    N     |     主键     |
|    room_id     |  BigInt  |      |             寝室唯一标识符              |    N     | 外键（寝室） |
|   student_id   |  BigInt  |      |          申请的学生唯一标识符           |    N     | 外键（学生） |
|  description   | varchar  | 255  |                损毁情况                 |    N     |              |
| employee_dm_id |  BigInt  |      |         宿舍管理员的唯一标识符          |    N     | 外键（员工） |
| employee_mw_id |  BigInt  |      |        安排的维修工的唯一标识符         |    N     | 外键（员工） |
|     status     | TinyInt  |      | 状态（1：已申请，2：已安排，3：已完成） |    N     |              |
|  create_time   | datetime |      |                创建时间                 |    N     |              |
|  update_time   | datetime |      |                更新时间                 |    N     |              |
|  create_user   |  BigInt  |      |                创建用户                 |    N     |              |
|  update_user   |  BigInt  |      |                更新用户                 |    N     |              |
|   is_deleted   | TinyInt  |      |             逻辑删除标志位              |    N     |              |

#### 7.失物招领信息(lost_found)

|   字段名    |   类型   | 长度 |             描述             | 可否为空 |     索引     |
| :---------: | :------: | :--: | :--------------------------: | :------: | :----------: |
|     id      |  BigInt  |      |          唯一标识符          |    N     |     主键     |
| description | varchar  |  64  |           描述信息           |    N     |              |
|    type     | TinyInt  |      |   类型（1：找到，2：丢失）   |    N     |              |
| student_id  |  BigInt  |      |     上传的学生唯一标识符     |    N     | 外键（学生） |
|    phone    |   char   |  11  |           联系电话           |    N     |              |
|   status    | TinyInt  |      | 状态（1：已上传，2：已解决） |    N     |              |
| create_time | datetime |      |           创建时间           |    N     |              |
| update_time | datetime |      |           更新时间           |    N     |              |
| create_user |  BigInt  |      |           创建用户           |    N     |              |
| update_user |  BigInt  |      |           更新用户           |    N     |              |
| is_deleted  | TinyInt  |      |        逻辑删除标志位        |    N     |              |

#### 8.签到活动信息(check_in)

|    字段名    |   类型   | 长度 |                 描述                  | 可否为空 |      索引      |
| :----------: | :------: | :--: | :-----------------------------------: | :------: | :------------: |
|      id      |  BigInt  |      |              唯一标识符               |    N     |      主键      |
| employee_id  |  BigInt  |      |         宿舍管理员唯一标识符          |    N     |  外键（员工）  |
| building_id  |  BigInt  |      |           宿舍楼唯一标识符            |    N     | 外键（宿舍楼） |
|   deadline   | datetime |      |               截止时间                |    N     |                |
|    status    | TinyInt  |      | 状态（1：进行中，0：已结束），默认为1 |    N     |                |
| sigin_in_num |   int    |      |               签到人数                |    Y     |                |
|  total_num   |   int    |      |                总人数                 |    Y     |                |
| create_time  | datetime |      |               创建时间                |    N     |                |
| update_time  | datetime |      |               更新时间                |    N     |                |
| create_user  |  BigInt  |      |               创建用户                |    N     |                |
| update_user  |  BigInt  |      |               更新用户                |    N     |                |
|  is_deleted  | TinyInt  |      |            逻辑删除标志位             |    N     |                |

#### 9.签到详情(check_in_detail)

|   字段名    |   类型   | 长度 |        描述        | 可否为空 |       索引       |
| :---------: | :------: | :--: | :----------------: | :------: | :--------------: |
|     id      |  BigInt  |      |     唯一标识符     |    N     |       主键       |
| check_in_id |  BigInt  |      | 签到活动唯一标识符 |    N     | 外键（签到活动） |
| student_id  |  BigInt  |      |   学生唯一标识符   |    N     |   外键（学生）   |
| description | varchar  |  64  |    签到详情描述    |    Y     |                  |
| create_time | datetime |      |      创建时间      |    N     |                  |
| update_time | datetime |      |      更新时间      |    N     |                  |
| create_user |  BigInt  |      |      创建用户      |    N     |                  |
| update_user |  BigInt  |      |      更新用户      |    N     |                  |
| is_deleted  | TinyInt  |      |   逻辑删除标志位   |    N     |                  |

### 四、技术选型

```text
前端：Vue+elementUI/React、axios
后端：Java、SpringBoot、SpringMVC、Mybatis-Plus
数据库：Mysql、Redis
API文档：Knife4j
```

