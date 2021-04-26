CREATE DATABASE `itau` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
CREATE TABLE `endereco` (
  `id` int NOT NULL AUTO_INCREMENT,
  `bairro` varchar(45) DEFAULT NULL,
  `cep` varchar(11) DEFAULT NULL,
  `complemento` varchar(45) DEFAULT NULL,
  `cpf` varchar(11) DEFAULT NULL,
  `ddd` int DEFAULT NULL,
  `ibge` varchar(45) DEFAULT NULL,
  `localidade` varchar(100) DEFAULT NULL,
  `logradouro` varchar(100) DEFAULT NULL,
  `nome` varchar(100) DEFAULT NULL,
  `rg` varchar(15) DEFAULT NULL,
  `uf` varchar(2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
