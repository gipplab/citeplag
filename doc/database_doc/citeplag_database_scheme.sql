-- phpMyAdmin SQL Dump
-- version 3.4.11.1deb2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Jul 12, 2014 at 09:11 AM
-- Server version: 5.5.31
-- PHP Version: 5.4.4-14+deb7u2

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `cbpd_prod`
--

-- --------------------------------------------------------

--
-- Table structure for table `citeplag_authors`
--

DROP TABLE IF EXISTS `citeplag_authors`;
CREATE TABLE IF NOT EXISTS `citeplag_authors` (
  `author_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `document_id` int(10) unsigned NOT NULL,
  `lastname` varchar(255) NOT NULL,
  `firstname` varchar(255) NOT NULL,
  PRIMARY KEY (`author_id`),
  KEY `document_id` (`document_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `citeplag_citation`
--

DROP TABLE IF EXISTS `citeplag_citation`;
CREATE TABLE IF NOT EXISTS `citeplag_citation` (
  `db_citation_id` int(11) NOT NULL AUTO_INCREMENT,
  `document_id` int(10) unsigned NOT NULL,
  `doc_reference_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `db_reference_id` int(10) unsigned DEFAULT NULL,
  `count` smallint(5) unsigned NOT NULL,
  `character` int(10) unsigned DEFAULT NULL,
  `word` mediumint(8) unsigned DEFAULT NULL,
  `sentence` mediumint(8) unsigned DEFAULT NULL,
  `paragraph` smallint(5) unsigned DEFAULT NULL,
  `section` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`db_citation_id`),
  KEY `db_reference_id` (`db_reference_id`),
  KEY `document_id` (`document_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `citeplag_citationpattern_member`
--

DROP TABLE IF EXISTS `citeplag_citationpattern_member`;
CREATE TABLE IF NOT EXISTS `citeplag_citationpattern_member` (
  `pattern_member_id` bigint(19) NOT NULL AUTO_INCREMENT,
  `pattern_id` bigint(19) unsigned NOT NULL,
  `count` int(11) NOT NULL,
  `document_id` int(11) unsigned DEFAULT NULL,
  `gap` smallint(5) unsigned NOT NULL,
  `old_db_citation_id` int(10) unsigned NOT NULL,
  `db_citation_id` int(11) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`pattern_member_id`),
  KEY `pattern_id` (`pattern_id`),
  KEY `document_id` (`document_id`),
  KEY `db_citation_id` (`db_citation_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `citeplag_document_data`
--

DROP TABLE IF EXISTS `citeplag_document_data`;
CREATE TABLE IF NOT EXISTS `citeplag_document_data` (
  `document_id` int(10) unsigned NOT NULL,
  `type` enum('pubmed','pmc','doi','medline','title','journal','file','xmlfile','txtfile','type','year','month','author_key','title_key','pages','date','booktitle') NOT NULL,
  `value` varchar(300) NOT NULL,
  KEY `value` (`value`),
  KEY `document_id` (`document_id`,`type`),
  KEY `type` (`type`,`value`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `citeplag_document_text`
--

DROP TABLE IF EXISTS `citeplag_document_text`;
CREATE TABLE IF NOT EXISTS `citeplag_document_text` (
  `document_id` int(10) unsigned NOT NULL,
  `fulltext` longtext NOT NULL,
  PRIMARY KEY (`document_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `citeplag_pattern`
--

DROP TABLE IF EXISTS `citeplag_pattern`;
CREATE TABLE IF NOT EXISTS `citeplag_pattern` (
  `pattern_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `document_id1` int(11) unsigned DEFAULT NULL,
  `document_id2` int(11) unsigned DEFAULT NULL,
  `procedure` tinyint(3) unsigned NOT NULL,
  `pattern_score` smallint(5) unsigned NOT NULL,
  `old_pattern_id` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`pattern_id`),
  KEY `document_id2` (`document_id2`),
  KEY `document_id1` (`document_id1`,`document_id2`,`procedure`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `citeplag_reference`
--

DROP TABLE IF EXISTS `citeplag_reference`;
CREATE TABLE IF NOT EXISTS `citeplag_reference` (
  `db_reference_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `cont_document_id` int(10) unsigned NOT NULL,
  `doc_reference_id` varchar(128) NOT NULL,
  `ref_document_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`db_reference_id`),
  KEY `ref_document_id` (`ref_document_id`),
  KEY `cont_document_id` (`cont_document_id`,`doc_reference_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `citeplag_textpattern_member`
--

DROP TABLE IF EXISTS `citeplag_textpattern_member`;
CREATE TABLE IF NOT EXISTS `citeplag_textpattern_member` (
  `pattern_member_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `pattern_id` bigint(19) unsigned NOT NULL,
  `document_id` int(11) unsigned DEFAULT NULL,
  `start_character` int(10) unsigned NOT NULL,
  `end_character` int(10) unsigned NOT NULL,
  PRIMARY KEY (`pattern_member_id`),
  KEY `pattern_id` (`pattern_id`),
  KEY `document_id` (`document_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
