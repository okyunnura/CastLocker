CREATE TABLE `user` (
  `username`   VARCHAR(255) NOT NULL,
  `password`   VARCHAR(255) NOT NULL,
  `role`       VARCHAR(255) NOT NULL,
  `expired_at` DATETIME     NOT NULL,
  PRIMARY KEY (`username`)
)
  ENGINE = InnoDB;
