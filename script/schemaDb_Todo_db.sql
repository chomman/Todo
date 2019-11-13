--
-- Database: `todo_db`
--

CREATE DATABASE IF NOT EXISTS `todo_db`;
USE `todo_db`;


-- ENTITIES

--
-- Struttura della tabella `category`
--

CREATE TABLE IF NOT EXISTS `category` (
	`desc` varchar(130) ,
	`name` varchar(130)  NOT NULL,
	`parent` varchar(130) ,
	
	`_id` int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT 

);


--
-- Struttura della tabella `tag`
--

CREATE TABLE IF NOT EXISTS `tag` (
	`name` varchar(130)  NOT NULL,
	
	`_id` int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT 

);


--
-- Struttura della tabella `todo`
--

CREATE TABLE IF NOT EXISTS `todo` (
	`details` varchar(130)  NOT NULL,
	`status` int  NOT NULL,
	`title` varchar(130) ,
	
	`_id` int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT 

);


--
-- Struttura della tabella `user`
--

CREATE TABLE IF NOT EXISTS `user` (
	`mail` varchar(130) ,
	`name` varchar(130) ,
	`password` varchar(130)  NOT NULL,
	`roles` varchar(130) ,
	`surname` varchar(130) ,
	`username` varchar(130)  NOT NULL,
	
	`_id` int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT 

);


-- Security

ALTER TABLE `user` MODIFY COLUMN `password` varchar(128)  NOT NULL;

INSERT INTO `todo_db`.`user` (`username`, `password`, `_id`) VALUES ('admin', '62f264d7ad826f02a8af714c0a54b197935b717656b80461686d450f7b3abde4c553541515de2052b9af70f710f0cd8a1a2d3f4d60aa72608d71a63a9a93c0f5', 1);

CREATE TABLE IF NOT EXISTS `roles` (
	`role` varchar(30) ,
	
	-- RELAZIONI

	`_user` int(11)  NOT NULL REFERENCES user(_id),
	`_id` int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT 

);
INSERT INTO `todo_db`.`roles` (`role`, `_user`, `_id`) VALUES ('ADMIN', '1', 1);





-- relation 1:m parent Category - Category
ALTER TABLE `category` ADD COLUMN `parent` int(11)  REFERENCES category(_id);

-- relation m:m category Todo - Category
CREATE TABLE IF NOT EXISTS `Todo_category` (
    `_id` int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `id_Todo` int(11)  NOT NULL REFERENCES todo(_id),
    `id_Category` int(11)  NOT NULL REFERENCES category(_id)
);

-- relation m:m tags Todo - Tag
CREATE TABLE IF NOT EXISTS `Todo_tags` (
    `_id` int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `id_Todo` int(11)  NOT NULL REFERENCES todo(_id),
    `id_Tag` int(11)  NOT NULL REFERENCES tag(_id)
);


