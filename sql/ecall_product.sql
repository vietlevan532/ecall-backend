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
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `price` varchar(50) NOT NULL,
  `category_id` int NOT NULL,
  `short_des` tinytext NOT NULL,
  `full_des` text NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`),
  KEY `fk_product_1_idx` (`category_id`),
  CONSTRAINT `fk_product_1` FOREIGN KEY (`category_id`) REFERENCES `category` (`category_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (1,'Nút chuông gọi y tá ST-100','1000000',3,'Nút chuông gọi y tá ST-1 series giúp nâng cao chất lượng phục vụ bệnh nhân và đáp ứng tiêu chí chất lượng của bộ y tế. Nút chuông gọi y tá được sử dụng phổ biến nhất hiện nay','Chuông gọi phục vụ Quick bell C600 với thiết kế mỏng, nhỏ gọn và trang nhã, với 1 chức năng gọi phục vụ duy nhất, có giá thành khá rẻ, hiện đang là sản phẩm bán chạy nhất cho các nhà hàng hiện nay.\n\nHình ảnh nút chuông gọi phục vụ Quick bell C600\n\n\n\nĐặc điểm nổi bật của nút chuông gọi nhân viên phục vụ C600\n- Có khoảng cách kết nối lên đến 150m\n\n- Được làm từ nhựa cứng có khả năng chống nước, chống xước\n\n- Sử dụng Pin cúc áo để hoạt động, có thể thay Pin đơn giản và dễ dàng\n\n\n\nThông số kỹ thuật nút chuông gọi phục vụ Quick bell C600\nKích thước: 58 * 58 * 11\n\nTần số: 433MHz\n\nPower: 12V23A\n\nPhạm vi của các cách: mét 1500 (khu vực mở)\n\nMàu sắc: đen, trắng, bạc\n\nChất liệu: PVC\n\n\n\nNút chuông gọi nhân viên phục vụ Quick bell C-600 có một hạn chế đó là chỉ kết nội được với màn hình hiển thị Quick bell, và tín hiệu truyền tin cũng không được rõ ràng sắc nét cho lăm, độ bên không cao, tuy nhiên giá thành của nó thì lại khá là rẻ\n'),(4,'Nút chuông gọi y tá ST-103','750000',2,'Sản phẩm max xịn không mua thì thôi','Ok em eo mua thi sao'),(5,'Nút chuông gọi y tá ST-104','5000000',5,'Sản phẩm max xịn không mua thì thôi','Ok em mua luôn'),(6,'Nút chuông gọi y tá ST-105','5000000',1,'Sản phẩm max xịn không mua thì thôi','Ok em mua luôn'),(7,'Nút chuông gọi y tá ST-106','5000000',3,'Sản phẩm max xịn không mua thì thôi','Ok em mua luôn'),(8,'Nút chuông gọi y tá ST-107','7000000',2,'Sản phẩm max xịn không mua thì thôi','Không mua phí đời'),(9,'Nút chuông gọi y tá ST-108','7000000',4,'Sản phẩm max xịn không mua thì thôi','Ok mua thì mua'),(10,'Nút chuông gọi y tá ST-109','8000000',5,'Đểu cũng phải mua nghe chưa','Bố đ thích mua thì sao'),(11,'Nút chuông gọi y tá ST-110','9000000',3,'Sản phẩm này không mua thì thôi','Chốt đơn cho anh'),(12,'Nút chuông gọi y tá ST-111','1233455',2,'Sản phẩm cuối cùng của tối hôm nay','Giá cả ib'),(13,'Nút chuông gọi y tá ST-112','4355555',4,'Sản phẩm này cho không','Cho thì lấy'),(19,'ádfasdfasd','1000000000',2,'ádafsdfasd','ádfasdfasdfsad'),(20,'ádfasdfasdasdfasdf','1000000000',2,'ádafsdfasd','ádfasdfasdfsad');
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
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
