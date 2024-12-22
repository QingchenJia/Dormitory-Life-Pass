/*
 Navicat Premium Dump SQL

 Source Server         : localhost_mysql
 Source Server Type    : MySQL
 Source Server Version : 80036 (8.0.36)
 Source Host           : localhost:3306
 Source Schema         : dormitory_life_pass

 Target Server Type    : MySQL
 Target Server Version : 80036 (8.0.36)
 File Encoding         : 65001
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for building
-- ----------------------------
DROP TABLE IF EXISTS `building`;
CREATE TABLE `building`  (
  `id` bigint NOT NULL COMMENT '唯一标识符',
  `location` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '位置',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '名称',
  `type` tinyint NOT NULL COMMENT '类型（1：男宿舍楼，0：女宿舍楼）',
  `employee_id` bigint NULL DEFAULT NULL COMMENT '安排宿舍管理员唯一标识符',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_user` bigint NULL DEFAULT NULL COMMENT '创建用户',
  `update_user` bigint NULL DEFAULT NULL COMMENT '更新用户',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除标志位',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name`(`name` ASC) USING BTREE,
  INDEX `building_fk_employee`(`employee_id` ASC) USING BTREE,
  CONSTRAINT `building_fk_employee` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '宿舍楼表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for check_in
-- ----------------------------
DROP TABLE IF EXISTS `check_in`;
CREATE TABLE `check_in`  (
  `id` bigint NOT NULL COMMENT '唯一标识符',
  `employee_id` bigint NOT NULL COMMENT '宿舍管理员唯一标识符',
  `building_id` bigint NOT NULL COMMENT '宿舍楼唯一标识符',
  `deadline` datetime NOT NULL COMMENT '截止时间',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态（1：进行中，0：已结束），默认为1',
  `sign_in_num` int NULL DEFAULT NULL COMMENT '签到人数',
  `total_num` int NULL DEFAULT NULL COMMENT '总人数',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `create_user` bigint NOT NULL COMMENT '创建用户',
  `update_user` bigint NOT NULL COMMENT '更新用户',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除标志位',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `check_in_fk_employee`(`employee_id` ASC) USING BTREE,
  INDEX `check_in_fk_building`(`building_id` ASC) USING BTREE,
  CONSTRAINT `check_in_fk_building` FOREIGN KEY (`building_id`) REFERENCES `building` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `check_in_fk_employee` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '签到活动信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for check_in_detail
-- ----------------------------
DROP TABLE IF EXISTS `check_in_detail`;
CREATE TABLE `check_in_detail`  (
  `id` bigint NOT NULL COMMENT '唯一标识符',
  `check_in_id` bigint NOT NULL COMMENT '签到活动唯一标识符',
  `student_id` bigint NOT NULL COMMENT '学生唯一标识符',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '签到详情描述',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `create_user` bigint NOT NULL COMMENT '创建用户',
  `update_user` bigint NOT NULL COMMENT '更新用户',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除标志位',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `check_in_detail_fk_check_in`(`check_in_id` ASC) USING BTREE,
  INDEX `check_in_detail_fk_student`(`student_id` ASC) USING BTREE,
  CONSTRAINT `check_in_detail_fk_check_in` FOREIGN KEY (`check_in_id`) REFERENCES `check_in` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `check_in_detail_fk_student` FOREIGN KEY (`student_id`) REFERENCES `student` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '签到详情表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for employee
-- ----------------------------
DROP TABLE IF EXISTS `employee`;
CREATE TABLE `employee`  (
  `id` bigint NOT NULL COMMENT '唯一标识符',
  `job_num` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '工号',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '姓名',
  `username` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `gender` tinyint NOT NULL COMMENT '性别（1：男，0：女）',
  `phone` char(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '电话号码',
  `type` tinyint NOT NULL COMMENT '身份（1：宿舍管理员，2：维修工，0：超级管理员）',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '状态（1：已安排，0：未安排，默认为0）',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_user` bigint NULL DEFAULT NULL COMMENT '创建用户',
  `update_user` bigint NULL DEFAULT NULL COMMENT '更新用户',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除标志位',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `job_num`(`job_num` ASC) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '员工表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for lost_found
-- ----------------------------
DROP TABLE IF EXISTS `lost_found`;
CREATE TABLE `lost_found`  (
  `id` bigint NOT NULL COMMENT '唯一标识符',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '描述信息',
  `type` tinyint NOT NULL COMMENT '类型（1：找到，2：丢失）',
  `student_id` bigint NOT NULL COMMENT '上传的学生唯一标识符',
  `phone` char(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '联系电话',
  `status` tinyint NOT NULL COMMENT '状态（1：已上传，2：已解决）',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `create_user` bigint NOT NULL COMMENT '创建用户',
  `update_user` bigint NOT NULL COMMENT '更新用户',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除标志位',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `lost_found_fk_student`(`student_id` ASC) USING BTREE,
  CONSTRAINT `lost_found_fk_student` FOREIGN KEY (`student_id`) REFERENCES `student` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '失物招领信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for room
-- ----------------------------
DROP TABLE IF EXISTS `room`;
CREATE TABLE `room`  (
  `id` bigint NOT NULL COMMENT '唯一标识符',
  `floor` tinyint NOT NULL COMMENT '楼层',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '名称',
  `max_num` tinyint NOT NULL COMMENT '容量',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态（1：可入住，0：不可入住），默认为1',
  `building_id` bigint NOT NULL COMMENT '宿舍楼唯一标识符',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_user` bigint NULL DEFAULT NULL COMMENT '创建用户',
  `update_user` bigint NULL DEFAULT NULL COMMENT '更新用户',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除标志位',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name`(`name` ASC) USING BTREE,
  INDEX `room_fk_building`(`building_id` ASC) USING BTREE,
  CONSTRAINT `room_fk_building` FOREIGN KEY (`building_id`) REFERENCES `building` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '寝室表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for room_change
-- ----------------------------
DROP TABLE IF EXISTS `room_change`;
CREATE TABLE `room_change`  (
  `id` bigint NOT NULL COMMENT '唯一标识符',
  `old_room_id` bigint NOT NULL COMMENT '当前寝室唯一标识符',
  `new_room_id` bigint NOT NULL COMMENT '目标寝室唯一标识符',
  `student_id` bigint NOT NULL COMMENT '提出申请的学生唯一标识符',
  `reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '申请原因',
  `old_employee_id` bigint NOT NULL COMMENT '处理事项的宿舍管理员唯一标识符',
  `new_employee_id` bigint NOT NULL COMMENT '目标寝室的宿舍管理员唯一标识符',
  `status` tinyint NOT NULL COMMENT '状态（1：已申请，2：批准，3：驳回，4：接受，5：拒绝）',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `create_user` bigint NOT NULL COMMENT '创建用户',
  `update_user` bigint NOT NULL COMMENT '更新用户',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除标志位',
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

-- ----------------------------
-- Table structure for room_repair
-- ----------------------------
DROP TABLE IF EXISTS `room_repair`;
CREATE TABLE `room_repair`  (
  `id` bigint NOT NULL COMMENT '唯一标识符',
  `room_id` bigint NOT NULL COMMENT '寝室唯一标识符',
  `student_id` bigint NOT NULL COMMENT '申请的学生唯一标识符',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '损毁情况',
  `employee_dm_id` bigint NOT NULL COMMENT '宿舍管理员的唯一标识符',
  `employee_mw_id` bigint NULL DEFAULT NULL COMMENT '安排的维修工的唯一标识符',
  `status` tinyint NOT NULL COMMENT '状态（1：已申请，2：已安排，3：已完成）',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `create_user` bigint NOT NULL COMMENT '创建用户',
  `update_user` bigint NOT NULL COMMENT '更新用户',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除标志位',
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

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student`  (
  `id` bigint NOT NULL COMMENT '唯一标识符',
  `student_num` char(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '学号',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '姓名',
  `username` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名（默认为学号）',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `gender` tinyint NOT NULL COMMENT '性别（1：男，0：女）',
  `phone` char(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '电话号码',
  `room_id` bigint NULL DEFAULT NULL COMMENT '寝室唯一标识符',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_user` bigint NULL DEFAULT NULL COMMENT '创建用户',
  `update_user` bigint NULL DEFAULT NULL COMMENT '更新用户',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除标志位',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_student_num`(`student_num` ASC) USING BTREE,
  UNIQUE INDEX `uk_username`(`username` ASC) USING BTREE,
  INDEX `student_fk_room`(`room_id` ASC) USING BTREE,
  CONSTRAINT `student_fk_room` FOREIGN KEY (`room_id`) REFERENCES `room` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '学生表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
