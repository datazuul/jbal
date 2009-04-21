-- MySQL Administrator dump 1.4
--
-- ------------------------------------------------------
-- Server version	5.0.27-max


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


--
-- Create schema dbexample
--

CREATE DATABASE IF NOT EXISTS dbexample;
USE dbexample;

--
-- Definition of table `info`
--

DROP TABLE IF EXISTS `info`;
CREATE TABLE `info` (
  `PCode` varchar(3) NOT NULL,
  `PaPCode` varchar(3) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `info`
--

/*!40000 ALTER TABLE `info` DISABLE KEYS */;

--
-- Definition of table `tblcomponenti`
--

DROP TABLE IF EXISTS `tblcomponenti`;
CREATE TABLE `tblcomponenti` (
  `CID` int(10) unsigned NOT NULL default '0',
  `Type` varchar(45) NOT NULL default '',
  `Attributes` longtext,
  `HasChildren` tinyint(1) unsigned NOT NULL default '0',
  `HistoryCid` int(10) unsigned NOT NULL default '0',
  `InsertDate` datetime NOT NULL default '0000-00-00 00:00:00',
  PRIMARY KEY  (`CID`),
  KEY `Index_2` (`Type`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tblcomponenti`
--

/*!40000 ALTER TABLE `tblcomponenti` DISABLE KEYS */;
INSERT INTO `tblcomponenti` (`CID`,`Type`,`Attributes`,`HasChildren`,`HistoryCid`,`InsertDate`) VALUES 
 (1,'header',NULL,1,0,'0000-00-00 00:00:00'),
 (2,'navbar',NULL,1,0,'0000-00-00 00:00:00'),
 (3,'content',NULL,1,0,'0000-00-00 00:00:00'),
 (4,'footer',NULL,1,0,'0000-00-00 00:00:00'),
 (5,'tabs',NULL,0,0,'0000-00-00 00:00:00'),
 (6,'lingue',NULL,0,0,'0000-00-00 00:00:00'),
 (7,'strumenti',NULL,0,0,'0000-00-00 00:00:00'),
 (8,'ricerca',NULL,0,0,'0000-00-00 00:00:00'),
 (9,'section',NULL,0,0,'2006-07-20 00:00:00'),
 (10,'footerContent',NULL,0,0,'0000-00-00 00:00:00'),
 (11,'navbar',NULL,1,0,'0000-00-00 00:00:00'),
 (12,'sidebar',NULL,0,0,'0000-00-00 00:00:00');


--
-- Definition of table `tblcontenuti`
--

DROP TABLE IF EXISTS `tblcontenuti`;
CREATE TABLE `tblcontenuti` (
  `PaCID` int(10) unsigned NOT NULL default '0',
  `CID` int(10) unsigned NOT NULL default '0',
  `StateID` int(10) unsigned NOT NULL default '3',
  `OrderNumber` int(10) unsigned NOT NULL default '0',
  UNIQUE KEY `Index_4` USING BTREE (`PaCID`,`CID`),
  KEY `Index_1` USING BTREE (`PaCID`),
  KEY `Index_2` USING BTREE (`CID`),
  KEY `FK_tblcontenuti_3` USING BTREE (`StateID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tblcontenuti`
--

/*!40000 ALTER TABLE `tblcontenuti` DISABLE KEYS */;
INSERT INTO `tblcontenuti` (`PaCID`,`CID`,`StateID`,`OrderNumber`) VALUES 
 (1,5,3,1),
 (1,6,5,3),
 (1,7,3,2),
 (1,8,3,4),
 (2,12,3,1),
 (3,9,4,6);
 (11,12,3,1);
--
-- Definition of table `tblpagine`
--

DROP TABLE IF EXISTS `tblpagine`;
CREATE TABLE `tblpagine` (
  `PID` int(10) unsigned NOT NULL auto_increment,
  `Name` varchar(100) NOT NULL default '',
  `PaPID` int(10) unsigned default '0',
  `Valid` tinyint(1) unsigned NOT NULL default '1',
  `HasChild` tinyint(1) unsigned NOT NULL default '0',
  `PCode` varchar(20) NOT NULL,
  `InSidebar` tinyint(1) unsigned NOT NULL default '1',
  PRIMARY KEY  (`PID`),
  UNIQUE KEY `Codice` USING BTREE (`PCode`),
  KEY `Index_2` USING BTREE (`PaPID`)
) ENGINE=MyISAM AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tblpagine`
--

/*!40000 ALTER TABLE `tblpagine` DISABLE KEYS */;
INSERT INTO `tblpagine` (`PID`,`Name`,`PaPID`,`Valid`,`HasChild`,`PCode`,`InSidebar`) VALUES 
 (1,'Homepage',NULL,1,1,'HMP',1);
/*!40000 ALTER TABLE `tblpagine` ENABLE KEYS */;


--
-- Definition of table `tblredirects`
--

DROP TABLE IF EXISTS `tblredirects`;
CREATE TABLE `tblredirects` (
  `PID` int(10) unsigned NOT NULL default '0',
  `Url` text NOT NULL,
  KEY `Index_1` USING BTREE (`PID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tblredirects`
--

/*!40000 ALTER TABLE `tblredirects` DISABLE KEYS */;

/*!40000 ALTER TABLE `tblredirects` ENABLE KEYS */;


--
-- Definition of table `tblroles`
--

DROP TABLE IF EXISTS `tblroles`;
CREATE TABLE `tblroles` (
  `User` varchar(50) NOT NULL,
  `PermissionCode` int(10) unsigned NOT NULL default '0',
  `PID` int(10) unsigned NOT NULL default '0',
  PRIMARY KEY  (`User`,`PID`),
  KEY `Index_2` (`User`),
  KEY `Index_3` USING BTREE (`User`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tblroles`
--

/*!40000 ALTER TABLE `tblroles` DISABLE KEYS */;
INSERT INTO `tblroles` (`User`,`PermissionCode`,`PID`) VALUES 
 ('s123847',7,0);
/*!40000 ALTER TABLE `tblroles` ENABLE KEYS */;


--
-- Definition of table `tblstati`
--

DROP TABLE IF EXISTS `tblstati`;
CREATE TABLE `tblstati` (
  `StateID` int(10) unsigned NOT NULL auto_increment,
  `StateName` varchar(5) NOT NULL,
  `Scope` varchar(45) NOT NULL,
  PRIMARY KEY  (`StateID`),
  KEY `Index_3` USING BTREE (`Scope`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tblstati`
--

/*!40000 ALTER TABLE `tblstati` DISABLE KEYS */;
INSERT INTO `tblstati` (`StateID`,`StateName`,`Scope`) VALUES 
 (1,'WRK','modifica'),
 (2,'PND','modifica'),
 (3,'ACT','validazione'),
 (4,'OLD','archivio'),
 (5,'DEL','cancellazione');
/*!40000 ALTER TABLE `tblstati` ENABLE KEYS */;


--
-- Definition of table `tblstrutture`
--

DROP TABLE IF EXISTS `tblstrutture`;
CREATE TABLE `tblstrutture` (
  `PID` int(10) unsigned NOT NULL default '0',
  `CID` int(10) unsigned NOT NULL default '0',
  UNIQUE KEY `Index_3` USING BTREE (`PID`,`CID`),
  KEY `Index_1` USING BTREE (`PID`),
  KEY `Index_2` USING BTREE (`CID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tblstrutture`
--

/*!40000 ALTER TABLE `tblstrutture` DISABLE KEYS */;
INSERT INTO `tblstrutture` (`PID`,`CID`) VALUES 
 (1,1),
 (1,2),
 (1,3),
 (1,4);
/*!40000 ALTER TABLE `tblstrutture` ENABLE KEYS */;




/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
