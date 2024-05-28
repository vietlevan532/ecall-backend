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
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `login` varchar(50) NOT NULL,
  `password_hash` varchar(60) NOT NULL,
  `full_name` varchar(100) NOT NULL,
  `gender` varchar(10) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `address` varchar(100) DEFAULT NULL,
  `created_by` varchar(50) DEFAULT NULL,
  `created_date` varchar(50) DEFAULT NULL,
  `last_modified_by` varchar(50) DEFAULT NULL,
  `last_modified_date` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `login_UNIQUE` (`login`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (3,'provider','$2a$10$mfLHou8NFABbHMfSce0xTOpYfQCSOKH9PheEpp6oMJfjPIxTlYUEW','provider',NULL,NULL,NULL,'anonymousUser','2021-05-04 09:38:07.553','anonymousUser','2021-05-04 09:38:07.553'),(4,'admin','$2a$10$hruq8YXs9QlhwwasVcqnQee4T.BvuVbA2Zch6l0ZlquWqEcQGN2K.','admin',NULL,NULL,NULL,'anonymousUser','2021-05-04 09:43:08.723','anonymousUser','2021-05-04 09:43:08.723'),(28,'minh','$2a$10$10HGLzLhVrJtmZNZeZNmbuEDi0W1v7bKPby9RDZod90HrkTJBIGja','minh',NULL,NULL,NULL,'anonymousUser','2021-05-11 15:32:33.703612','anonymousUser','2021-05-11 15:32:33.703612'),(29,'linh1','$2a$10$YBm63IdvlqmUWfjH71jYTOIIWbp/sz0S4Gbrq6dd4yAbqYgJecKs.','linh1',NULL,NULL,NULL,'anonymousUser','2021-05-11 15:33:25.488115','anonymousUser','2021-05-11 15:33:25.488115'),(30,'trang1','$2a$10$PVX9AYzduJj3.mcn1o3wBuhnstEsq2u6.TeWp/cagvlWnUtEz043W','trang',NULL,NULL,NULL,'anonymousUser','2021-05-11 15:37:24.444462','anonymousUser','2021-05-11 15:37:24.444462'),(31,'trang2','$2a$10$wVZk4Oiy5rbK.eikFJ08FuEIExLXkBHrfe01uMCFsl1mXN1JCooEy','trang',NULL,NULL,NULL,'anonymousUser','2021-05-11 15:38:07.011525','anonymousUser','2021-05-11 15:38:07.011525'),(32,'trang3','$2a$10$WIVpadv5lwr2pcntyD/RSu6fTF2v.Q/K2Y0pr7R6eMd2E84M1TrTG','trang',NULL,NULL,NULL,'anonymousUser','2021-05-11 15:40:14.531965','anonymousUser','2021-05-11 15:40:14.531965'),(33,'trang4','$2a$10$cnRZnczbVE83DjeoDIyeZ.4UNlOYr8Ylc2U703pQwNrnJQPyh522G','trang',NULL,NULL,NULL,'anonymousUser','2021-05-11 15:40:54.227917','anonymousUser','2021-05-11 15:40:54.227917'),(34,'trang5','$2a$10$KdQ62pnxfH6tzahjuJXQ/OS.DuZO.6dNFPKanS7xAWlEkWn6.4bmS','trang',NULL,NULL,NULL,'anonymousUser','2021-05-11 15:41:22.535612','anonymousUser','2021-05-11 15:41:22.535612'),(35,'tien','$2a$10$nF8BHq7j3iYmqBW5yYFxOODzF10p/HyvHaegX/ZS92rwbUJEjtLFW','tien',NULL,NULL,NULL,'anonymousUser','2021-05-11 15:46:23.544073','anonymousUser','2021-05-11 15:46:23.544073'),(36,'tien1','$2a$10$NfrYgbjrhDwkoDI/rMotDOr0oYxiuxYLUTre3QVDKdvXNQiChZaPK','tien1',NULL,NULL,NULL,'anonymousUser','2021-05-11 15:47:17.450652','anonymousUser','2021-05-11 15:47:17.450652'),(37,'dung','$2a$10$t8pt.HAERoo8Nx86JYZ6nOsSsv/wC1p7JaG27aGmK0EZGDa9qd7Yu','dung',NULL,NULL,NULL,'anonymousUser','2021-05-11 15:47:48.680612','anonymousUser','2021-05-11 15:47:48.680612'),(38,'dung1','$2a$10$FVcO4NhLrOzImRpcRW/R2.NzfWxiaZHoVhIWNnSJJjoE95cNSXozS','dung1',NULL,NULL,NULL,'anonymousUser','2021-05-11 15:48:27.998094','anonymousUser','2021-05-11 15:48:27.998094'),(39,'dung2','$2a$10$snjoCRiwwsJy6iwsQ1OL8uIy8VkdDt5T0CawRN3.8bvTO/c1Nz.Qe','dung1',NULL,NULL,NULL,'anonymousUser','2021-05-11 15:49:24.571177','anonymousUser','2021-05-11 15:49:24.571177'),(40,'dung3','$2a$10$FL4PaXZ.Doy8zO.NXoI5Te1PbkT0RPOmJvWqLxR1Yx44Ny3ylT8vG','dung1',NULL,NULL,NULL,'anonymousUser','2021-05-11 15:49:40.173788','anonymousUser','2021-05-11 15:49:40.173788');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
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
