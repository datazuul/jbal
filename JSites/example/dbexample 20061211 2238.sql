-- MySQL Administrator dump 1.4
--
-- ------------------------------------------------------
-- Server version	4.1.9-nt


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
  `codice` varchar(3) NOT NULL default '',
  `parentcodice` varchar(3) NOT NULL default ''
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `info`
--

/*!40000 ALTER TABLE `info` DISABLE KEYS */;
/*!40000 ALTER TABLE `info` ENABLE KEYS */;


--
-- Definition of table `tblcomponenti`
--

DROP TABLE IF EXISTS `tblcomponenti`;
CREATE TABLE `tblcomponenti` (
  `ID` int(10) unsigned NOT NULL default '0',
  `Type` varchar(45) NOT NULL default '',
  `Attributes` longtext,
  `HasChildren` tinyint(1) unsigned NOT NULL default '0',
  `HistoryCid` int(10) unsigned NOT NULL default '0',
  `InsertDate` datetime NOT NULL default '0000-00-00 00:00:00',
  PRIMARY KEY  (`ID`),
  KEY `Index_2` (`Type`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tblcomponenti`
--

/*!40000 ALTER TABLE `tblcomponenti` DISABLE KEYS */;
INSERT INTO `tblcomponenti` (`ID`,`Type`,`Attributes`,`HasChildren`,`HistoryCid`,`InsertDate`) VALUES 
 (1,'header',NULL,1,0,'0000-00-00 00:00:00'),
 (2,'navbar',NULL,1,0,'0000-00-00 00:00:00'),
 (3,'content',NULL,1,0,'0000-00-00 00:00:00'),
 (4,'footer',NULL,1,0,'0000-00-00 00:00:00'),
 (5,'tabs',NULL,0,0,'0000-00-00 00:00:00'),
 (6,'lingue',NULL,0,0,'0000-00-00 00:00:00'),
 (7,'strumenti',NULL,0,0,'0000-00-00 00:00:00'),
 (8,'ricerca',NULL,0,0,'0000-00-00 00:00:00'),
 (9,'banner',NULL,0,0,'2005-12-02 00:00:00'),
 (10,'footerContent',NULL,0,0,'0000-00-00 00:00:00'),
 (11,'navbar',NULL,1,0,'0000-00-00 00:00:00'),
 (12,'sidebar',NULL,0,0,'0000-00-00 00:00:00'),
 (13,'briciole',NULL,0,0,'0000-00-00 00:00:00');
/*!40000 ALTER TABLE `tblcomponenti` ENABLE KEYS */;


--
-- Definition of table `tblcontenuti`
--

DROP TABLE IF EXISTS `tblcontenuti`;
CREATE TABLE `tblcontenuti` (
  `ParentID` int(10) unsigned NOT NULL default '0',
  `ChildID` int(10) unsigned NOT NULL default '0',
  `IDStato` int(10) unsigned NOT NULL default '3',
  `OrderNumber` int(10) unsigned NOT NULL default '0',
  UNIQUE KEY `Index_4` (`ParentID`,`ChildID`),
  KEY `Index_1` (`ParentID`),
  KEY `Index_2` (`ChildID`),
  KEY `FK_tblcontenuti_3` (`IDStato`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tblcontenuti`
--

/*!40000 ALTER TABLE `tblcontenuti` DISABLE KEYS */;
INSERT INTO `tblcontenuti` (`ParentID`,`ChildID`,`IDStato`,`OrderNumber`) VALUES 
 (1,5,3,1),
 (1,6,3,3),
 (1,7,3,2),
 (1,8,3,4),
 (1,13,3,5),
 (2,9,3,1),
 (4,10,3,1),
 (11,12,3,1);
/*!40000 ALTER TABLE `tblcontenuti` ENABLE KEYS */;


--
-- Definition of table `tblpagine`
--

DROP TABLE IF EXISTS `tblpagine`;
CREATE TABLE `tblpagine` (
  `ID` int(10) unsigned NOT NULL auto_increment,
  `Name` varchar(100) NOT NULL default '',
  `ParentID` int(10) unsigned default '0',
  `Valid` tinyint(1) unsigned NOT NULL default '1',
  `HasChild` tinyint(1) unsigned NOT NULL default '0',
  `Codice` varchar(20) NOT NULL default '',
  `InSidebar` tinyint(1) unsigned NOT NULL default '1',
  PRIMARY KEY  (`ID`),
  KEY `Index_2` (`ParentID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tblpagine`
--

/*!40000 ALTER TABLE `tblpagine` DISABLE KEYS */;
INSERT INTO `tblpagine` (`ID`,`Name`,`ParentID`,`Valid`,`HasChild`,`Codice`,`InSidebar`) VALUES 
 (1,'Homepage',NULL,1,1,'HMP',1);
/*!40000 ALTER TABLE `tblpagine` ENABLE KEYS */;


--
-- Definition of table `tblredirects`
--

DROP TABLE IF EXISTS `tblredirects`;
CREATE TABLE `tblredirects` (
  `pid` int(10) unsigned NOT NULL default '0',
  `url` text NOT NULL,
  KEY `Index_1` (`pid`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tblredirects`
--

/*!40000 ALTER TABLE `tblredirects` DISABLE KEYS */;
/*!40000 ALTER TABLE `tblredirects` ENABLE KEYS */;


--
-- Definition of table `tblstati`
--

DROP TABLE IF EXISTS `tblstati`;
CREATE TABLE `tblstati` (
  `ID` int(10) unsigned NOT NULL auto_increment,
  `NomeStato` varchar(5) NOT NULL default '',
  `Uso` varchar(45) NOT NULL default '',
  PRIMARY KEY  (`ID`),
  KEY `Index_3` (`Uso`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tblstati`
--

/*!40000 ALTER TABLE `tblstati` DISABLE KEYS */;
INSERT INTO `tblstati` (`ID`,`NomeStato`,`Uso`) VALUES 
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
  `IDPagina` int(10) unsigned NOT NULL default '0',
  `IDComponente` int(10) unsigned NOT NULL default '0',
  UNIQUE KEY `Index_3` (`IDPagina`,`IDComponente`),
  KEY `Index_1` (`IDPagina`),
  KEY `Index_2` (`IDComponente`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tblstrutture`
--

/*!40000 ALTER TABLE `tblstrutture` DISABLE KEYS */;
INSERT INTO `tblstrutture` (`IDPagina`,`IDComponente`) VALUES 
 (1,1),
 (1,3),
 (1,4),
 (1,11);
/*!40000 ALTER TABLE `tblstrutture` ENABLE KEYS */;


--
-- Definition of table `utente_ruolo`
--

DROP TABLE IF EXISTS `utente_ruolo`;
CREATE TABLE `utente_ruolo` (
  `utente` varchar(50) NOT NULL default '',
  `permissioncode` int(10) unsigned NOT NULL default '0',
  `pid` int(10) unsigned NOT NULL default '0',
  PRIMARY KEY  (`utente`,`pid`),
  KEY `Index_3` (`utente`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `utente_ruolo`
--

/*!40000 ALTER TABLE `utente_ruolo` DISABLE KEYS */;
INSERT INTO `utente_ruolo` (`utente`,`permissioncode`,`pid`) VALUES 
 ('admin',7,0);
/*!40000 ALTER TABLE `utente_ruolo` ENABLE KEYS */;




/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
