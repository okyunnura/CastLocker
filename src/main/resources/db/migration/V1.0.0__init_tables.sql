CREATE TABLE `user` (
  `username` VARCHAR(255) NOT NULL,
  `password` VARCHAR(255),
  `role`     VARCHAR(255),
  PRIMARY KEY (`username`)
) ENGINE =InnoDB;