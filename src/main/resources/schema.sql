DROP TABLE IF EXISTS `userroles`; 
CREATE TABLE `userroles` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `role` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1; 


DROP TABLE IF EXISTS `users`; 
CREATE TABLE `users` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `loginId` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `role` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_users_1` (`role`),
  CONSTRAINT `FK_users_1` FOREIGN KEY (`role`) REFERENCES `userroles` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1; 

DROP TABLE IF EXISTS `roomtype`; 
CREATE TABLE `roomtype` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `type` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1; 


DROP TABLE IF EXISTS `roomstatus`; 
CREATE TABLE `roomstatus` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `status` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `locations`; 
CREATE TABLE `locations` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `suite` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1; 
 

DROP TABLE IF EXISTS `provider`; 
CREATE TABLE `provider` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;


DROP TABLE IF EXISTS `rooms`; 
CREATE TABLE `rooms` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `roomType` int(10) unsigned NOT NULL,
  `location` int(10) unsigned NOT NULL,
  `status` int(10) unsigned NOT NULL,
  `fromDate` date NOT NULL,
  `toDate` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_rooms_1` (`roomType`),
  KEY `FK_rooms_2` (`location`),
  KEY `FK_rooms_3` (`status`),
  CONSTRAINT `FK_rooms_1` FOREIGN KEY (`roomType`) REFERENCES `roomtype` (`id`),
  CONSTRAINT `FK_rooms_2` FOREIGN KEY (`location`) REFERENCES `locations` (`id`),
  CONSTRAINT `FK_rooms_3` FOREIGN KEY (`status`) REFERENCES `roomstatus` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC; 


DROP TABLE IF EXISTS `assigenedrooms`; 
CREATE TABLE `assigenedrooms` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `roomId` int(10) unsigned NOT NULL,
  `fromDate` datetime NOT NULL,
  `toDate` datetime NOT NULL,
  `provider` int(10) unsigned NOT NULL,
  `session` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_assigenedrooms_1` (`roomId`),
  KEY `FK_assigenedrooms_3` (`provider`),
  CONSTRAINT `FK_assigenedrooms_1` FOREIGN KEY (`roomId`) REFERENCES `rooms` (`id`),
  CONSTRAINT `FK_assigenedrooms_3` FOREIGN KEY (`provider`) REFERENCES `provider` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC; 

DROP TABLE IF EXISTS `audit`;
CREATE TABLE  `audit` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `room` int(10) unsigned NOT NULL,
  `location` int(10) unsigned NOT NULL,
  `date` datetime NOT NULL,
  `user` int(10) unsigned NOT NULL,
  `action` varchar(45) NOT NULL,
  `comments` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_audit_1` (`location`),
  KEY `FK_audit_2` (`room`),
  KEY `FK_audit_3` (`user`),
  CONSTRAINT `FK_audit_1` FOREIGN KEY (`location`) REFERENCES `locations` (`id`),
  CONSTRAINT `FK_audit_2` FOREIGN KEY (`room`) REFERENCES `rooms` (`id`),
  CONSTRAINT `FK_audit_3` FOREIGN KEY (`user`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;