CREATE TABLE `user` (
  `id`         BIGINT       NOT NULL AUTO_INCREMENT,
  `username`   VARCHAR(200) NOT NULL,
  `password`   VARCHAR(500) NOT NULL,
  `expired_at` DATETIME     NOT NULL,
  `role`       VARCHAR(100) NOT NULL,
  `parent_id`  BIGINT,
  PRIMARY KEY (`id`)
) ENGINE =InnoDB;
ALTER TABLE `user`
  ADD CONSTRAINT UK_sb8bbouer5wak8vyiiy4pf2bx UNIQUE (`username`);
ALTER TABLE `user`
  ADD CONSTRAINT `FK7olsdtq8burvrkr2v0xc2puy1` FOREIGN KEY (`parent_id`) REFERENCES `user` (`id`);