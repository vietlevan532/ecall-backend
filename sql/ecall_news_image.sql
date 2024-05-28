-- MySQL dump 10.13  Distrib 8.0.22, for Linux (x86_64)
--
-- Host: localhost    Database: ecall
-- ------------------------------------------------------
-- Server version	8.0.25-0ubuntu0.20.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `news_image`
--

DROP TABLE IF EXISTS `news_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `news_image` (
  `news_id` int NOT NULL,
  `content` longblob,
  PRIMARY KEY (`news_id`),
  CONSTRAINT `fk_news_image_1` FOREIGN KEY (`news_id`) REFERENCES `news` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `news_image`
--

LOCK TABLES `news_image` WRITE;
/*!40000 ALTER TABLE `news_image` DISABLE KEYS */;
INSERT INTO `news_image` VALUES (16,''),(20,''),(21,_binary 'data:image/png;base64,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');
/*!40000 ALTER TABLE `news_image` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-05-22 23:11:04