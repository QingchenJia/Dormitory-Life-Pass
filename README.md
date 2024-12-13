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
|  username   | varchar  |  10  | 用户名（默认为学号） |    N     |    唯一索引    |
|  password   | varchar  |  32  |         密码         |    N     |                |
|   gender    | TinyInt  |      | 性别（1：男，0：女） |    N     |                |
|    phone    |   char   |  11  |       电话号码       |    N     |                |
|   room_id   |  BigInt  |      |    寝室唯一标识符    |    Y     | 外键（寝室表） |
| create_time | datetime |      |       创建时间       |    Y     |                |
| update_time | datetime |      |       更新时间       |    Y     |                |
| create_user |  BigInt  |      |       创建用户       |    Y     |                |
| update_user |  BigInt  |      |       更新用户       |    Y     |                |
| is_deleted  | TinyInt  |      |    逻辑删除标志位    |    N     |                |

```sql
CREATE TABLE `student`  (
  `id` bigint NOT NULL COMMENT '唯一标识符',
  `student_num` char(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '学号',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '姓名',
  `username` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名（默认为学号）',
  `password` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `gender` tinyint NOT NULL COMMENT '性别（1：男，0：女）',
  `phone` char(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '电话号码',
  `room_id` bigint NULL DEFAULT NULL COMMENT '寝室唯一标识符',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_user` bigint NULL DEFAULT NULL COMMENT '创建用户',
  `update_user` bigint NULL DEFAULT NULL COMMENT '更新用户',
  `is_deleted` tinyint NOT NULL COMMENT '逻辑删除标志位',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_student_num`(`student_num` ASC) USING BTREE,
  UNIQUE INDEX `uk_username`(`username` ASC) USING BTREE,
  INDEX `student_fk_room`(`room_id` ASC) USING BTREE,
  CONSTRAINT `student_fk_room` FOREIGN KEY (`room_id`) REFERENCES `room` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '学生表' ROW_FORMAT = Dynamic;
```

#### 2.员工表(employee)

|   字段名    |   类型   | 长度 |                      描述                       | 可否为空 |   索引   |
| :---------: | :------: | :--: | :---------------------------------------------: | :------: | :------: |
|     id      |  BigInt  |      |                   唯一标识符                    |    N     |   主键   |
|   job_num   | varchar  |  8   |                      工号                       |    N     | 唯一索引 |
|    name     | varchar  |  20  |                      姓名                       |    N     |          |
|  username   | varchar  |  10  |                     用户名                      |    N     | 唯一索引 |
|  passowrd   | varchar  |  32  |                      密码                       |    N     |          |
|   gender    | TinyInt  |      |              性别（1：男，0：女）               |    N     |          |
|    phone    |   char   |  11  |                    电话号码                     |    N     |          |
|    type     | TinyInt  |      | 身份（1：宿舍管理员，2：维修工，0：超级管理员） |    N     |          |
| create_time | datetime |      |                    创建时间                     |    Y     |          |
| update_time | datetime |      |                    更新时间                     |    Y     |          |
| create_user |  BigInt  |      |                    创建用户                     |    Y     |          |
| update_user |  BigInt  |      |                    更新用户                     |    Y     |          |
| is_deleted  | TinyInt  |      |                 逻辑删除标志位                  |    N     |          |

```sql
CREATE TABLE `employee`  (
  `id` bigint NOT NULL COMMENT '唯一标识符',
  `job_num` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '工号',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '姓名',
  `username` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `gender` tinyint NOT NULL COMMENT '性别（1：男，0：女）',
  `phone` char(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '电话号码',
  `type` tinyint NOT NULL COMMENT '身份（1：宿舍管理员，2：维修工，0：超级管理员）',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_user` bigint NULL DEFAULT NULL COMMENT '创建用户',
  `update_user` bigint NULL DEFAULT NULL COMMENT '更新用户',
  `is_deleted` tinyint NOT NULL COMMENT '逻辑删除标志位',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `job_num`(`job_num` ASC) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '员工表' ROW_FORMAT = Dynamic;
```

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

```sql
CREATE TABLE `room`  (
  `id` bigint NOT NULL COMMENT '唯一标识符',
  `floor` tinyint NOT NULL COMMENT '楼层',
  `number` tinyint NOT NULL COMMENT '序号',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '名称',
  `max_num` tinyint NOT NULL COMMENT '容量',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态（1：可入住，0：不可入住），默认为1',
  `building_id` bigint NOT NULL COMMENT '宿舍楼唯一标识符',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_user` bigint NULL DEFAULT NULL COMMENT '创建用户',
  `update_user` bigint NULL DEFAULT NULL COMMENT '更新用户',
  `is_deleted` tinyint NOT NULL COMMENT '逻辑删除标志位',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `room_fk_building`(`building_id` ASC) USING BTREE,
  CONSTRAINT `room_fk_building` FOREIGN KEY (`building_id`) REFERENCES `building` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '寝室表' ROW_FORMAT = Dynamic;
```

#### 4.宿舍楼(building)

|   字段名    |   类型   | 长度 |           描述           | 可否为空 |     索引     |
| :---------: | :------: | :--: | :----------------------: | :------: | :----------: |
|     id      |  BigInt  |      |        唯一标识符        |    N     |     主键     |
|  location   | varchar  |  64  |           位置           |    N     |              |
|   number    | TinyInt  |      |           序号           |    N     |              |
|    name     | varchar  |  20  |           名称           |    Y     |              |
| employee_id |  BigInt  |      | 安排宿舍管理员唯一标识符 |    Y     | 外键（员工） |
| create_time | datetime |      |         创建时间         |    Y     |              |
| update_time | datetime |      |         更新时间         |    Y     |              |
| create_user |  BigInt  |      |         创建用户         |    Y     |              |
| update_user |  BigInt  |      |         更新用户         |    Y     |              |
| is_deleted  | TinyInt  |      |      逻辑删除标志位      |    N     |              |

```sql
CREATE TABLE `building`  (
  `id` bigint NOT NULL COMMENT '唯一标识符',
  `location` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '位置',
  `number` tinyint NOT NULL COMMENT '序号',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '名称',
  `employee_id` bigint NULL DEFAULT NULL COMMENT '安排宿舍管理员唯一标识符',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_user` bigint NULL DEFAULT NULL COMMENT '创建用户',
  `update_user` bigint NULL DEFAULT NULL COMMENT '更新用户',
  `is_deleted` tinyint NOT NULL COMMENT '逻辑删除标志位',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `building_fk_employee`(`employee_id` ASC) USING BTREE,
  CONSTRAINT `building_fk_employee` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '宿舍楼表' ROW_FORMAT = Dynamic;
```

#### 5.调换寝室事项(room_change)

|     字段名      |   类型   | 长度 |                         描述                          | 可否为空 |     索引     |
| :-------------: | :------: | :--: | :---------------------------------------------------: | :------: | :----------: |
|       id        |  BigInt  |      |                      唯一标识符                       |    N     |     主键     |
|   old_room_id   |  BigInt  |      |                  当前寝室唯一标识符                   |    N     | 外键（寝室） |
|   new_room_id   |  BigInt  |      |                  目标寝室唯一标识符                   |    N     | 外键（寝室） |
|   student_id    |  BigInt  |      |               提出申请的学生唯一标识符                |    N     | 外键（学生） |
| old_employee_id |  BigInt  |      |            处理事项的宿舍管理员唯一标识符             |    N     | 外键（员工） |
| new_employee_id |  BigInt  |      |            目标寝室的宿舍管理员唯一标识符             |    N     | 外键（员工） |
|     status      | TinyInt  |      | 状态（1：已申请，2：批准，3：驳回，4：接受，5：拒绝） |    N     |              |
|   create_time   | datetime |      |                       创建时间                        |    N     |              |
|   update_time   | datetime |      |                       更新时间                        |    N     |              |
|   create_user   |  BigInt  |      |                       创建用户                        |    N     |              |
|   update_user   |  BigInt  |      |                       更新用户                        |    N     |              |
|   is_deleted    | TinyInt  |      |                    逻辑删除标志位                     |    N     |              |

```sql
CREATE TABLE `room_change`  (
  `id` bigint NOT NULL COMMENT '唯一标识符',
  `old_room_id` bigint NOT NULL COMMENT '当前寝室唯一标识符',
  `new_room_id` bigint NOT NULL COMMENT '目标寝室唯一标识符',
  `student_id` bigint NOT NULL COMMENT '提出申请的学生唯一标识符',
  `old_employee_id` bigint NOT NULL COMMENT '处理事项的宿舍管理员唯一标识符',
  `new_employee_id` bigint NOT NULL COMMENT '目标寝室的宿舍管理员唯一标识符',
  `status` tinyint NOT NULL COMMENT '状态（1：已申请，2：批准，3：驳回，4：接受，5：拒绝）',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `create_user` bigint NOT NULL COMMENT '创建用户',
  `update_user` bigint NOT NULL COMMENT '更新用户',
  `is_deleted` tinyint NOT NULL COMMENT '逻辑删除标志位',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `room_change_fk_old_room`(`old_room_id` ASC) USING BTREE,
  INDEX `room_change_fk_new_room`(`new_room_id` ASC) USING BTREE,
  INDEX `room_change_fk_student`(`student_id` ASC) USING BTREE,
  INDEX `room_change_fk_old_employee`(`old_employee_id` ASC) USING BTREE,
  INDEX `room_change_fk_new_employee`(`new_employee_id` ASC) USING BTREE,
  CONSTRAINT `room_change_fk_new_employee` FOREIGN KEY (`new_employee_id`) REFERENCES `employee` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `room_change_fk_new_room` FOREIGN KEY (`new_room_id`) REFERENCES `room` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `room_change_fk_old_employee` FOREIGN KEY (`old_employee_id`) REFERENCES `employee` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `room_change_fk_old_room` FOREIGN KEY (`old_room_id`) REFERENCES `room` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `room_change_fk_student` FOREIGN KEY (`student_id`) REFERENCES `student` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '调换寝室事项表' ROW_FORMAT = Dynamic;
```

#### 6.寝室报修事项(room_repair)

|     字段名     |   类型   | 长度 |                  描述                   | 可否为空 |     索引     |
| :------------: | :------: | :--: | :-------------------------------------: | :------: | :----------: |
|       id       |  BigInt  |      |               唯一标识符                |    N     |     主键     |
|    room_id     |  BigInt  |      |             寝室唯一标识符              |    N     | 外键（寝室） |
|   student_id   |  BigInt  |      |          申请的学生唯一标识符           |    N     | 外键（学生） |
| employee_dm_id |  BigInt  |      |         宿舍管理员的唯一标识符          |    N     | 外键（员工） |
| employee_mw_id |  BigInt  |      |        安排的维修工的唯一标识符         |    N     | 外键（员工） |
|     status     | TinyInt  |      | 状态（1：已申请，2：已安排，3：已完成） |    N     |              |
|  create_time   | datetime |      |                创建时间                 |    N     |              |
|  update_time   | datetime |      |                更新时间                 |    N     |              |
|  create_user   |  BigInt  |      |                创建用户                 |    N     |              |
|  update_user   |  BigInt  |      |                更新用户                 |    N     |              |
|   is_deleted   | TinyInt  |      |             逻辑删除标志位              |    N     |              |

```sql
CREATE TABLE `room_repair`  (
  `id` bigint NOT NULL COMMENT '唯一标识符',
  `room_id` bigint NOT NULL COMMENT '寝室唯一标识符',
  `student_id` bigint NOT NULL COMMENT '申请的学生唯一标识符',
  `employee_dm_id` bigint NOT NULL COMMENT '宿舍管理员的唯一标识符',
  `employee_mw_id` bigint NOT NULL COMMENT '安排的维修工的唯一标识符',
  `status` tinyint NOT NULL COMMENT '状态（1：已申请，2：已安排，3：已完成）',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `create_user` bigint NOT NULL COMMENT '创建用户',
  `update_user` bigint NOT NULL COMMENT '更新用户',
  `is_deleted` tinyint NOT NULL COMMENT '逻辑删除标志位',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `room_repair_fk_room`(`room_id` ASC) USING BTREE,
  INDEX `room_repair_fk_student`(`student_id` ASC) USING BTREE,
  INDEX `room_repair_fk_employee_dm`(`employee_dm_id` ASC) USING BTREE,
  INDEX `room_repair_fk_employee_mw`(`employee_mw_id` ASC) USING BTREE,
  CONSTRAINT `room_repair_fk_employee_dm` FOREIGN KEY (`employee_dm_id`) REFERENCES `employee` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `room_repair_fk_employee_mw` FOREIGN KEY (`employee_mw_id`) REFERENCES `employee` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `room_repair_fk_room` FOREIGN KEY (`room_id`) REFERENCES `room` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `room_repair_fk_student` FOREIGN KEY (`student_id`) REFERENCES `student` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '寝室报修事项表' ROW_FORMAT = Dynamic;
```

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

```sql
CREATE TABLE `lost_found`  (
  `id` bigint NOT NULL COMMENT '唯一标识符',
  `description` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '描述信息',
  `type` tinyint NOT NULL COMMENT '类型（1：找到，2：丢失）',
  `student_id` bigint NOT NULL COMMENT '上传的学生唯一标识符',
  `phone` char(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '联系电话',
  `status` tinyint NOT NULL COMMENT '状态（1：已上传，2：已解决）',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `create_user` bigint NOT NULL COMMENT '创建用户',
  `update_user` bigint NOT NULL COMMENT '更新用户',
  `is_deleted` tinyint NOT NULL COMMENT '逻辑删除标志位',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `lost_found_fk_student`(`student_id` ASC) USING BTREE,
  CONSTRAINT `lost_found_fk_student` FOREIGN KEY (`student_id`) REFERENCES `student` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '失物招领信息表' ROW_FORMAT = Dynamic;
```

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

```sql
CREATE TABLE `check_in`  (
  `id` bigint NOT NULL COMMENT '唯一标识符',
  `employee_id` bigint NOT NULL COMMENT '宿舍管理员唯一标识符',
  `building_id` bigint NOT NULL COMMENT '宿舍楼唯一标识符',
  `deadline` datetime NOT NULL COMMENT '截止时间',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态（1：进行中，0：已结束），默认为1',
  `sigin_in_num` int NULL DEFAULT NULL COMMENT '签到人数',
  `total_num` int NULL DEFAULT NULL COMMENT '总人数',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `create_user` bigint NOT NULL COMMENT '创建用户',
  `update_user` bigint NOT NULL COMMENT '更新用户',
  `is_deleted` tinyint NOT NULL COMMENT '逻辑删除标志位',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `check_in_fk_employee`(`employee_id` ASC) USING BTREE,
  INDEX `check_in_fk_building`(`building_id` ASC) USING BTREE,
  CONSTRAINT `check_in_fk_building` FOREIGN KEY (`building_id`) REFERENCES `building` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `check_in_fk_employee` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '签到活动信息表' ROW_FORMAT = Dynamic;
```

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

```sql
CREATE TABLE `check_in_detail`  (
  `id` bigint NOT NULL COMMENT '唯一标识符',
  `check_in_id` bigint NOT NULL COMMENT '签到活动唯一标识符',
  `student_id` bigint NOT NULL COMMENT '学生唯一标识符',
  `description` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '签到详情描述',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `create_user` bigint NOT NULL COMMENT '创建用户',
  `update_user` bigint NOT NULL COMMENT '更新用户',
  `is_deleted` tinyint NOT NULL COMMENT '逻辑删除标志位',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `check_in_detail_fk_check_in`(`check_in_id` ASC) USING BTREE,
  INDEX `check_in_detail_fk_student`(`student_id` ASC) USING BTREE,
  CONSTRAINT `check_in_detail_fk_check_in` FOREIGN KEY (`check_in_id`) REFERENCES `check_in` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `check_in_detail_fk_student` FOREIGN KEY (`student_id`) REFERENCES `student` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '签到详情表' ROW_FORMAT = Dynamic;
```

### 四、技术选型

```text
前端：Vue+elementUI/React、axios
后端：Java、SpringBoot、SpringMVC、Mybatis-Plus
数据库：Mysql、Redis
API文档：Knife4j
```

