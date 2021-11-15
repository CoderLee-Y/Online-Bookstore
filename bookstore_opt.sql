use bookstore_opt;

-- ----------------------------
-- Table structure for book
-- ----------------------------
DROP TABLE IF EXISTS `book`;
CREATE TABLE `book`
(
    `id`          int unsigned AUTO_INCREMENT NOT NULL,
    `isbn`        char(15)                    NOT NULL DEFAULT '',
    `name`        varchar(255)                NOT NULL DEFAULT '',
    `type`        varchar(255)                NOT NULL DEFAULT '',
    `author`      varchar(255)                NOT NULL DEFAULT '',
    `price`       decimal(12, 2)              NOT NULL DEFAULT 0,
    `description` varchar(10000)              NOT NULL DEFAULT '',
    `inventory`   mediumint unsigned          NOT NULL DEFAULT 0,
    `image`       varchar(255)                NOT NULL DEFAULT '',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4;

-- ----------------------------
-- Table structure for user_auth 0: user, 1: admin
-- ----------------------------
DROP TABLE IF EXISTS `user_auth`;
CREATE TABLE `user_auth`
(
    `user_id`   int unsigned AUTO_INCREMENT NOT NULL,
    `username`  varchar(255) UNIQUE         NOT NULL,
    `password`  varchar(255)                NOT NULL,
    `user_type` tinyint unsigned            NOT NULL DEFAULT 0,
    PRIMARY KEY (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    `user_id`  int unsigned NOT NULL,
    `nickname` varchar(255) NOT NULL DEFAULT '',
    `email`    varchar(255) NOT NULL DEFAULT '',
    `tel`      char(15)     NOT NULL DEFAULT '',
    `address`  varchar(255) NOT NULL DEFAULT '',
    `icon`     varchar(255) NOT NULL DEFAULT '',
    `abandon`  int(11)      NOT NULL DEFAULT 0,
    PRIMARY KEY (`user_id`),
    FOREIGN KEY (`user_id`) REFERENCES user_auth (user_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4;

DROP TABLE IF EXISTS `cart_table`;
CREATE TABLE `cart_table`
(
    `id`      int unsigned AUTO_INCREMENT NOT NULL,
    `user_id` int unsigned                NOT NULL,
    `book_id` int unsigned                NOT NULL,
    `amount`  mediumint unsigned          NOT NULL DEFAULT 1,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES user (user_id),
    FOREIGN KEY (book_id) REFERENCES book (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4;

-- ----------------------------
-- Records of order, status: 0-transit 1-finished
-- ----------------------------
DROP TABLE IF EXISTS `order`;
DROP TABLE IF EXISTS `order_table`;
CREATE TABLE `order_table`
(
    `order_id`     int unsigned AUTO_INCREMENT NOT NULL,
    `user_id`      int unsigned                NOT NULL,
    `status`       tinyint DEFAULT 0,
    `order_time`   TIMESTAMP                   NOT NULL,
    `address`      varchar(255)                NOT NULL DEFAULT '',
    `phone_number` char(15)                    NOT NULL DEFAULT '',
    PRIMARY KEY (order_id),
    FOREIGN KEY (user_id) REFERENCES user (user_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4;

-- ----------------------------
-- Records of order
-- ----------------------------
DROP TABLE IF EXISTS `order_record`;
CREATE TABLE `order_record`
(
    `record_id` char(32)           NOT NULL,
    `book_id`   int unsigned       NOT NULL,
    `amount`    mediumint unsigned NOT NULL DEFAULT 1,
    `price`     decimal(12, 2)     NOT NULL,
    PRIMARY KEY (record_id),
    FOREIGN KEY (book_id) REFERENCES book (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4;
