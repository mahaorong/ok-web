-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- 服务器版本:                        10.3.13-MariaDB - mariadb.org binary distribution
-- 服务器操作系统:                      Win64
-- HeidiSQL 版本:                  9.4.0.5125
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- 导出 21zsjc 的数据库结构
CREATE DATABASE IF NOT EXISTS `21zsjc` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `21zsjc`;

-- 导出  表 21zsjc.authority 结构
CREATE TABLE IF NOT EXISTS `authority` (
  `id` varchar(255) NOT NULL,
  `operateable` int(11) DEFAULT NULL,
  `wid` varchar(255) DEFAULT NULL,
  `uid` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKmlr72g6bi3ejy6yk7s8j1flgv` (`uid`),
  CONSTRAINT `FKmlr72g6bi3ejy6yk7s8j1flgv` FOREIGN KEY (`uid`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  21zsjc.authority 的数据：~110 rows (大约)
DELETE FROM `authority`;
/*!40000 ALTER TABLE `authority` DISABLE KEYS */;
INSERT INTO `authority` (`id`, `operateable`, `wid`, `uid`) VALUES
	('0414733515bd47a281bb96a8d289c866', 1, 'fd2ae99652754a588db3103dacc6bfb0', 'ca103b10e7e84b079bd209dba2a346e5'),
	('04ab2463a03c421eaa2d2f521e399e73', 1, '67d8ba0129b744a5980297577f0e2296', 'ca103b10e7e84b079bd209dba2a346e5'),
	('0a3c7197ed154c8e9c79c8c0160ab24a', 1, 'bef7f3acf4e14d1f9f7f93c458df19a8', 'ca103b10e7e84b079bd209dba2a346e5'),
	('0b1900fca29843f9b6dba96a4557463a', 1, 'eb668dbf496f4e69ae3bff1694fd18ac', 'ca103b10e7e84b079bd209dba2a346e5'),
	('0dfb4ee50b0b4158a7b523fa21be1870', 1, '047717bb29a74305a8b339f227fbfa12', 'ca103b10e7e84b079bd209dba2a346e5'),
	('0e24edeb4bc546f6b2cc028f35bd5e2b', 1, '9e9252b780fe4afd9e749b7d0ddd1d7e', 'ca103b10e7e84b079bd209dba2a346e5'),
	('0fe058ee1e8147e2b0c4cd1436de99a0', 1, '861d0cbc0ecb4bf1a96659017d5f4807', 'ca103b10e7e84b079bd209dba2a346e5'),
	('10743f1da7d64db9bf8720971d05b5c3', 0, 'f8387d7c785a405e95df9b05d893e728', 'ca103b10e7e84b079bd209dba2a346e5'),
	('11c169cf79cd4e05990ef84e26a07fa9', 1, '497f20436ec44fdb88a7420edbbf1bca', 'ca103b10e7e84b079bd209dba2a346e5'),
	('174a1d30b7c949c3ad60f9ba6973f641', 1, 'a266564f246b435d948cf9096a0f4944', 'ca103b10e7e84b079bd209dba2a346e5'),
	('1a82b3e921d1466085b9efadaed327c0', 1, '6b23430e94f74c91b3b3e9a47683b52a', 'ca103b10e7e84b079bd209dba2a346e5'),
	('1bfcf468eb164cc8843c06ec61bf6d59', 1, '80a258dd365a4cd39f127efd746d42a7', 'ca103b10e7e84b079bd209dba2a346e5'),
	('1da99ff69d014a40b49e23ff86fddb8c', 1, '798b283d79d044e38bce8c5a77a48b7d', 'ca103b10e7e84b079bd209dba2a346e5'),
	('1f47b9b5df2b4300a6f0cb861c4283f6', 1, 'a66141e7358a4b41886d53428079abc4', 'ca103b10e7e84b079bd209dba2a346e5'),
	('22065c6e93c5478990873e6ac50740b1', 1, '0be58c7f70ef42928d18071c0b7ff5a3', 'ca103b10e7e84b079bd209dba2a346e5'),
	('223d8ae37377435aa5e4e2b6d6bf1adb', 1, 'a63e341c1185440bbca2e5214e482176', 'ca103b10e7e84b079bd209dba2a346e5'),
	('23281437ef7c48f29dcbeec0ae33abee', 1, '0da6e64f6e4d4f2f8ea7101b0f50d247', 'ca103b10e7e84b079bd209dba2a346e5'),
	('2453b214f5a349749226c8caeb5b986e', 1, '8e6dd488a47e4798937f2d92cf1d2546', 'ca103b10e7e84b079bd209dba2a346e5'),
	('246c421fd88448b486c2239b244caf79', 1, 'cefb13c8f2eb48ff9b32f16bb86e1f5d', 'ca103b10e7e84b079bd209dba2a346e5'),
	('2648b1cb53064e3f847231ffa4b55fb4', 1, '2ccb6f597c3c4ac798cfffcb855d6355', 'ca103b10e7e84b079bd209dba2a346e5'),
	('270f8da7f968407ab5af4a471aaee71b', 1, '8d45e839a1ab4016ae933ea756ced041', 'ca103b10e7e84b079bd209dba2a346e5'),
	('274222f375214a76a71a96857135596d', 1, '4763526dddb24bf992ee4313b03068ac', 'ca103b10e7e84b079bd209dba2a346e5'),
	('274eb77da7ad4a868ab3e1ca6e88b659', 1, '98eb26e31e994ebb9703b7903d947133', 'ca103b10e7e84b079bd209dba2a346e5'),
	('2b8f2e00ac0e47cb8a3e0ac2d448c40d', 1, 'f42b968b73394999ae3f497c074b40c3', 'ca103b10e7e84b079bd209dba2a346e5'),
	('3262c13d52f240988e62495f5bb2af94', 1, 'd00895c509cb4b5991b6651e70770211', 'ca103b10e7e84b079bd209dba2a346e5'),
	('336d745310d9403a880e9fb1f4c15bb5', 1, 'a58796c11f624bfab65aedb4b9071267', 'ca103b10e7e84b079bd209dba2a346e5'),
	('342bc3f3de86408ca6ee9fa22902050c', 1, '877e838d14314f53b0f30e8de5691508', 'ca103b10e7e84b079bd209dba2a346e5'),
	('36a54c569d8e4a928ee36f1b2dcdb443', 1, '75bf5301278d4fa1b3c184c080ed8526', 'ca103b10e7e84b079bd209dba2a346e5'),
	('39466e6b6b32446e81fbf594173bc78b', 1, '958a2ace6d434d52809a8d1e69f911a1', 'ca103b10e7e84b079bd209dba2a346e5'),
	('3cdcda3da7fc468c939c70b165aece09', 1, 'ec711e60fdda47e48bc9f47bc37ec8d6', 'ca103b10e7e84b079bd209dba2a346e5'),
	('3d4f6b05cff540808f8d6d54f64b765d', 1, 'eafc16fa37e349a5ac0f43d19706ed0d', 'ca103b10e7e84b079bd209dba2a346e5'),
	('3d762559708e466e8d5886fb3895ced0', 1, '7e6f6c6ec4944f28bb1c33fa0c6bc408', 'ca103b10e7e84b079bd209dba2a346e5'),
	('3f92871f5edd4d05ac7dcc800abc79da', 1, '4860c585fff044968758ecbc5c9188b4', 'ca103b10e7e84b079bd209dba2a346e5'),
	('4781ccb9ef79466e8e171a215de29a23', 1, '6fba1bfde7984b5cb2a53e641521d036', 'ca103b10e7e84b079bd209dba2a346e5'),
	('481ccc61645443dfbba0c24a1cedd798', 1, '70f6e847d10c49ce9e104ef66fbacaf7', 'ca103b10e7e84b079bd209dba2a346e5'),
	('48c5e6b8b26848e3b9a84b56a988aae8', 1, '7446a6a66d7d42a5ac3798c05bcec221', 'ca103b10e7e84b079bd209dba2a346e5'),
	('4e5658780ae843d2b9b51dcc825efb5a', 1, 'acd56358b5684c2f84267efce9b68d06', 'ca103b10e7e84b079bd209dba2a346e5'),
	('507930bd904f42168ca5334afdb77fe6', 1, 'd3fba3386eb949a2ae63db667751e476', 'ca103b10e7e84b079bd209dba2a346e5'),
	('50fe4a16f19d463592d69396b47cc57e', 1, 'eb9ed2e77fe34df7afae27c413b20404', 'ca103b10e7e84b079bd209dba2a346e5'),
	('51c82c12b73e411eb72de282ab595603', 1, '2c632ce766834e6aa32ed843d6246cf4', 'ca103b10e7e84b079bd209dba2a346e5'),
	('522252b674f94671b8cc5fa67601e571', 1, 'd81769894bdf4de39b4c8dc7a358f6c1', 'ca103b10e7e84b079bd209dba2a346e5'),
	('5a68d289ee1449f39ead54423f82a618', 1, '02ac1eebda454b159e64895b2a50992c', 'ca103b10e7e84b079bd209dba2a346e5'),
	('5cbbf0525b6a470194279e1c33818bee', 1, '7ed5f42a8d074a59ab6ce7fc3324c139', 'ca103b10e7e84b079bd209dba2a346e5'),
	('5fd70c503d044965894021c65eae567d', 1, '62392ed6c0094c7ba5f8cfc68a089e35', 'ca103b10e7e84b079bd209dba2a346e5'),
	('602ba1d5b17d462da77320b43361b3fd', 1, '4ec3814bc7e64f4caad4c4f90e1c1b7a', 'ca103b10e7e84b079bd209dba2a346e5'),
	('635e49b5508747f9a786c14b35a6104b', 1, 'e3b3764bc6ba4d3f977bd55e75c8390e', 'ca103b10e7e84b079bd209dba2a346e5'),
	('65179789af4a49d0a991100e9e644ec9', 1, '7d27275d2d1c49a3a37e05a8da80506e', 'ca103b10e7e84b079bd209dba2a346e5'),
	('6b568d4fe9774a99a06e299004883295', 1, '4fd149956e5c47259b977d4f9487bd27', 'ca103b10e7e84b079bd209dba2a346e5'),
	('6b796b49741840fbb3f0f6fbab824f5e', 1, 'd11fdeb980f94a7489a52493fee6c059', 'ca103b10e7e84b079bd209dba2a346e5'),
	('6f729c258d63448683491782f8927b78', 1, 'e0bfa7e51d6443edbc431c9500c5baa2', 'ca103b10e7e84b079bd209dba2a346e5'),
	('6f907ef7159e41309d779a902f81eeae', 1, '6e45ade7fcc443da85910471a8bf71b6', 'ca103b10e7e84b079bd209dba2a346e5'),
	('716b4e46b5864e99a250cb626211417a', 0, '479eaa51110c45b1bfc85a7b2215b284', 'ca103b10e7e84b079bd209dba2a346e5'),
	('73b18fea6b774c4cb5968d2b9c1efcd8', 1, '72496066847840ae87c45040d72364e4', 'ca103b10e7e84b079bd209dba2a346e5'),
	('74cb00faf8824394a462bc79ace8c613', 1, '9463f712e351458aa9cf4705862df69f', 'ca103b10e7e84b079bd209dba2a346e5'),
	('74f4e0196bcd44d2909adf72232f6ef7', 1, 'c338306e60bf4f899f5d588ff129d190', 'ca103b10e7e84b079bd209dba2a346e5'),
	('7d566dca59ed449db5739de818e56e3b', 1, '07387d24a0514c9fa3ba9b79d4d516a7', 'ca103b10e7e84b079bd209dba2a346e5'),
	('7eb308fe97c74061b6f040e01dd24411', 1, 'ddb5335887f04da18077bf0719e185a9', 'ca103b10e7e84b079bd209dba2a346e5'),
	('82495a7806db4cfd82e1539a83eb7fd9', 1, 'f1a318d4130d46e781216bf5e623d46d', 'ca103b10e7e84b079bd209dba2a346e5'),
	('841d2ec7dad04a329e475738009493f7', 1, '4a8fcdd1c3734b8eab56c6468537a89f', 'ca103b10e7e84b079bd209dba2a346e5'),
	('86ebd2ab1f9e44bcb2c8003c2d17e807', 1, '4860efbe87964b2fbd98c225706815b6', 'ca103b10e7e84b079bd209dba2a346e5'),
	('883b6ca4f9694b19a0867427f64771be', 1, 'b0a341effd044b889fdd46fa6afd31f0', 'ca103b10e7e84b079bd209dba2a346e5'),
	('88fe27ee46b445959e00283c3e979fcb', 1, '710ec61f3b0140dbbc566ae722487a3e', 'ca103b10e7e84b079bd209dba2a346e5'),
	('8a40d39bf0a1469bbf447f145e74bd87', 1, 'fabb19255ff9454292a1a696521d8fd7', 'ca103b10e7e84b079bd209dba2a346e5'),
	('8a778bf3312b4637aecf3adf0a43e472', 1, '201cbb99941647ef9dca2c310f80a5d3', 'ca103b10e7e84b079bd209dba2a346e5'),
	('8aee3ec0ca4b41749afd757d5563da2f', 1, '69e90bec28704228aca5d8cf0f36416e', 'ca103b10e7e84b079bd209dba2a346e5'),
	('8ce1e470970e4efdb43c9e2e6a44e16e', 1, '9d6536ea6fbf4319ac4f8f4223c0933a', 'ca103b10e7e84b079bd209dba2a346e5'),
	('929213a929924656a38f6ea3cf05babe', 1, 'a7d239b43e02473d8602293848308c22', 'ca103b10e7e84b079bd209dba2a346e5'),
	('96ef5b4233fc4c158d43f4c02c231947', 1, '01991c231ec04c8190ed97c1de966cfb', 'ca103b10e7e84b079bd209dba2a346e5'),
	('99f22cffb6b743a3a4ae2e4aa73cfac6', 1, 'a6bdcb21b68944e59aa768fcb910427c', 'ca103b10e7e84b079bd209dba2a346e5'),
	('9b4a59128da44b42b435fc9a17aca860', 1, '607ebd6db7f84adeb5eeff3326cc1a45', 'ca103b10e7e84b079bd209dba2a346e5'),
	('a02e1a2f455f4efdbf9075fcdefc232c', 1, '2da1ffc2d6474499817d1b0209679603', 'ca103b10e7e84b079bd209dba2a346e5'),
	('a390deedf65a4d93b8538bb8da4b74db', 1, 'daac186a09b947d4903725387bc3e066', 'ca103b10e7e84b079bd209dba2a346e5'),
	('a425cff94e5c4940ab0d2dc650d86560', 1, '03facabdf55742048745302f4f470f65', 'ca103b10e7e84b079bd209dba2a346e5'),
	('a7c357a905584427891c6e9917acd80d', 1, '335cc895000a4be5b3191b09182a4749', 'ca103b10e7e84b079bd209dba2a346e5'),
	('a7fc9b097bd1473b906645c2bdb94561', 1, 'e9a9b7fec9cb491599873d0a2eb86482', 'ca103b10e7e84b079bd209dba2a346e5'),
	('aa07774589de48caab5bd7987aa35759', 1, '0549a35db3184cf88b2c5722b884e0e0', 'ca103b10e7e84b079bd209dba2a346e5'),
	('ababd50b857b4cfaacde43f1c27fd515', 1, '5c19f692d1264c3b9d2b5601c73f44c8', 'ca103b10e7e84b079bd209dba2a346e5'),
	('ae63c4d8be5248fdad6df5fe67123f26', 1, '90d547a20dc641d4ac417164c41ab0da', 'ca103b10e7e84b079bd209dba2a346e5'),
	('b4206a8ad19c4f50a0c2e51e4c3e98c8', 1, 'bf7db0e5c5df40849ae8dfe7664821b6', 'ca103b10e7e84b079bd209dba2a346e5'),
	('b821bcae43444319aebf04ed7976ddd8', 1, '936d11b0ab2e4942ad0ffe4918fb97cd', 'ca103b10e7e84b079bd209dba2a346e5'),
	('ba7402866cb44d98a5e2c78c98868274', 1, '85fd1fddf5b84277bccd030d3322306e', 'ca103b10e7e84b079bd209dba2a346e5'),
	('bc3d5c09f28a48e1847c0541c7e9ad9e', 1, 'bdff6578df0b46b29dd3c3716a46a3d8', 'ca103b10e7e84b079bd209dba2a346e5'),
	('c0430d38b5b24bd482f12ff630b69043', 1, 'af6df7441c6043b6b1409e2e941cd5b1', 'ca103b10e7e84b079bd209dba2a346e5'),
	('c6051e0253414ff084bba446fb89ad2c', 1, '4ac410ae4acb4f059033a786ca27a107', 'ca103b10e7e84b079bd209dba2a346e5'),
	('c7ae1bdb580043e4b1c6fec2af008f58', 1, 'ca55691f1c0e45ab94b7838a6f11d171', 'ca103b10e7e84b079bd209dba2a346e5'),
	('c8567e4824b048a6919818a45c7542c8', 1, 'b31834aab2124f289fd9e0f105960d5a', 'ca103b10e7e84b079bd209dba2a346e5'),
	('c878cd90ef7e45f987b417e959bef1fd', 1, '0e40f6277c534178b0ace7d2dda84b4e', 'ca103b10e7e84b079bd209dba2a346e5'),
	('c8e16696a57445329846ebe6e08f3d04', 1, 'a5c3cf2316f94e76b351986d41acbbfa', 'ca103b10e7e84b079bd209dba2a346e5'),
	('c9309d4496be43fabd6a4dfdd00827e9', 1, '412b756e618c49e7b758c2d9292b0ff3', 'ca103b10e7e84b079bd209dba2a346e5'),
	('c9ea7b91391a482e9fc06e27ceb76ac6', 1, 'efb63f00ec274ed8b5147a1b993cffca', 'ca103b10e7e84b079bd209dba2a346e5'),
	('ca3da1382d3943c98b726837356d73c5', 1, '40a1efccb02e41a49f325e1d38b39f1c', 'ca103b10e7e84b079bd209dba2a346e5'),
	('d4d689282e7b4e659312d38ed016a3b1', 1, 'bdd0dea3377f484bb42b033c772209a2', 'ca103b10e7e84b079bd209dba2a346e5'),
	('d88ea4fbe6224673a420b1205f721086', 1, '3c3b8eaea91b43c0aefb4e0c42e9c6a9', 'ca103b10e7e84b079bd209dba2a346e5'),
	('d91bb1944d2643eba8f8967455db26fd', 1, 'a4dd79d1a84b444bbe3595b3b3f7c30a', 'ca103b10e7e84b079bd209dba2a346e5'),
	('d961c250b0314a6894997f46c67fb88c', 1, 'ef5a8b2846314e9983debc2cca316669', 'ca103b10e7e84b079bd209dba2a346e5'),
	('da4bbe210e0840c490eba0a68fb4ae92', 1, '0338ea866a104f49982e68511a9ee443', 'ca103b10e7e84b079bd209dba2a346e5'),
	('da7d6ad58d544f6a8b2f51f81d30a398', 1, 'a9970106659941de9d391731e6a239bb', 'ca103b10e7e84b079bd209dba2a346e5'),
	('dc19b457636e4e31817f389323bf4c92', 1, '5cb3cfaa1d064cb5a2feb24581ac55fa', 'ca103b10e7e84b079bd209dba2a346e5'),
	('dcc8d5e2c0f14e368c178941562870ff', 1, '2a1566ab465746b2bd94f7a62ceec99a', 'ca103b10e7e84b079bd209dba2a346e5'),
	('df589944320f4d33a0b0417983c58b6b', 1, '25a7696821bd4630acbb64e5f46387a6', 'ca103b10e7e84b079bd209dba2a346e5'),
	('e26782c4763f46d39379c3d751b8e6bb', 1, '2fa5392a0fdf49d68edeefae6d296054', 'ca103b10e7e84b079bd209dba2a346e5'),
	('e3c4889392f04e569f99ce27d03d9755', 1, '7ab412d529c6436c909f4e48bd9f7203', 'ca103b10e7e84b079bd209dba2a346e5'),
	('e58a6c79b5b443848de9288b7106725c', 1, '5c2e50d0694e4394a6c01c36ec9ac59e', 'ca103b10e7e84b079bd209dba2a346e5'),
	('e8b39510510141ef8389231606e4815b', 1, '4bc095341dfd4cd9a8ffc4aae0060cd0', 'ca103b10e7e84b079bd209dba2a346e5'),
	('ea8d0c94cc2741e6ba66d504d78caa46', 1, '735a4912471349e1929883e383461eaa', 'ca103b10e7e84b079bd209dba2a346e5'),
	('ecfc714ad8fb4d77b96f000061b79aff', 1, '82c7d79304b2447fa82fc4e1c28bca43', 'ca103b10e7e84b079bd209dba2a346e5'),
	('ee93f825435346afaa6738f8b836a6b4', 1, '45957eb1eebf436db2e3f0764f2f7d83', 'ca103b10e7e84b079bd209dba2a346e5'),
	('f4831409345d4ef4b5df3f1834ac43c8', 1, 'ddba70738a404650af43910e51f405db', 'ca103b10e7e84b079bd209dba2a346e5'),
	('f511a4473e0c4c48a6922a245ac1733b', 1, '4e998d9e6ad04484bcb1210a8e958093', 'ca103b10e7e84b079bd209dba2a346e5'),
	('f5d1718d83f541bfb22e3f149594daba', 1, '825f91bb5f48484b8f5c0c07095c6030', 'ca103b10e7e84b079bd209dba2a346e5');
/*!40000 ALTER TABLE `authority` ENABLE KEYS */;

-- 导出  表 21zsjc.blog 结构
CREATE TABLE IF NOT EXISTS `blog` (
  `blog_id` varchar(255) NOT NULL,
  `cate_name` varchar(255) DEFAULT NULL,
  `cateids` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `comment_count` int(11) DEFAULT NULL,
  `content` longtext NOT NULL,
  `create_time` datetime NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `diff_lang_articles` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `html_content` longtext NOT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `is_hidden` int(11) DEFAULT NULL,
  `is_top` int(11) DEFAULT NULL,
  `keywords` varchar(255) DEFAULT NULL,
  `language` varchar(255) DEFAULT NULL,
  `number` int(11) DEFAULT NULL,
  `read_count` int(11) DEFAULT NULL,
  `review` int(11) DEFAULT NULL,
  `sort` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `summary` varchar(255) DEFAULT NULL,
  `tags` varchar(255) DEFAULT NULL,
  `title` varchar(255) NOT NULL,
  `vote_count` int(11) DEFAULT NULL,
  `user_id` varchar(255) NOT NULL,
  PRIMARY KEY (`blog_id`),
  KEY `FKpxk2jtysqn41oop7lvxcp6dqq` (`user_id`),
  CONSTRAINT `FKpxk2jtysqn41oop7lvxcp6dqq` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  21zsjc.blog 的数据：~0 rows (大约)
DELETE FROM `blog`;
/*!40000 ALTER TABLE `blog` DISABLE KEYS */;
/*!40000 ALTER TABLE `blog` ENABLE KEYS */;

-- 导出  表 21zsjc.category 结构
CREATE TABLE IF NOT EXISTS `category` (
  `id` varchar(32) NOT NULL,
  `cateitems` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  21zsjc.category 的数据：~0 rows (大约)
DELETE FROM `category`;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` (`id`, `cateitems`, `name`) VALUES
	('778c0171c57b4dea96f9788bb2a1167e', '[{"id":"bd740121a304443f9ffaec98b92e75ce","pid":"","tag":null,"text":"专家队伍","status":null,"state":null,"type":"test","list":true,"title":"","description":"","keywords":"","outlink":"","nodes":[{"id":"8656e498f76247b48d6d045297765553","pid":"bd740121a304443f9ffaec98b92e75ce","tag":"-","text":"展示所有","status":null,"state":null,"type":"test","list":true,"title":"","description":"","keywords":"","outlink":"","nodes":[]},{"id":"914085c1192c4758921010e5f45e5ed0","pid":"bd740121a304443f9ffaec98b92e75ce","tag":"-","text":"玉器","status":null,"state":null,"type":"test","list":true,"title":"","description":"","keywords":"","outlink":"","nodes":[]},{"id":"6b17d4fe110248939dd9f85f3b2bcf6a","pid":"bd740121a304443f9ffaec98b92e75ce","tag":"-","text":"瓷器","status":null,"state":null,"type":"test","list":true,"title":"","description":"","keywords":"","outlink":"","nodes":[]},{"id":"ce7e6b050f8a4da0832b03b9f028fa44","pid":"bd740121a304443f9ffaec98b92e75ce","tag":"-","text":"青铜器","status":null,"state":null,"type":"test","list":true,"title":"","description":"","keywords":"","outlink":"","nodes":[]}]},{"id":"360d8584b2db4e92ace254181eb937ef","pid":"","tag":null,"text":"热门收藏","status":null,"state":null,"type":"test","list":true,"title":"","description":"","keywords":"","outlink":"","nodes":[{"id":"ed834b1de4804445851ebd8f7b207811","pid":"360d8584b2db4e92ace254181eb937ef","tag":"-","text":"展示所有","status":null,"state":null,"type":"test","list":true,"title":"","description":"","keywords":"","outlink":"","nodes":[]},{"id":"650eab8aa93a4b048e88f8e6db904a43","pid":"360d8584b2db4e92ace254181eb937ef","tag":"-","text":"陶瓷收藏","status":null,"state":null,"type":"test","list":true,"title":"","description":"","keywords":"","outlink":"","nodes":[]},{"id":"2001ee4aa2d34ff7a7d510d5cc91b259","pid":"360d8584b2db4e92ace254181eb937ef","tag":"-","text":"字画收藏","status":null,"state":null,"type":"test","list":true,"title":"","description":"","keywords":"","outlink":"","nodes":[]},{"id":"ce2156b20ead480582f4f3d9c9fde9f8","pid":"360d8584b2db4e92ace254181eb937ef","tag":"-","text":"玉石收藏","status":null,"state":null,"type":"test","list":true,"title":"","description":"","keywords":"","outlink":"","nodes":[]},{"id":"1f93f8839c804d43963e311941c9cae6","pid":"360d8584b2db4e92ace254181eb937ef","tag":"-","text":"名木收藏","status":null,"state":null,"type":"test","list":true,"title":"","description":"","keywords":"","outlink":"","nodes":[]},{"id":"49883f13ef314b54ba9db0fef5a24036","pid":"360d8584b2db4e92ace254181eb937ef","tag":"-","text":"杂项收藏","status":null,"state":null,"type":"test","list":true,"title":"","description":"","keywords":"","outlink":"","nodes":[]},{"id":"5ca66e2bf06c417fac6d7fd9414d250a","pid":"360d8584b2db4e92ace254181eb937ef","tag":"-","text":"铜器收藏","status":null,"state":null,"type":"test","list":true,"title":"","description":"","keywords":"","outlink":"","nodes":[]}]},{"id":"3d06daf91f57425487cb825382f67678","pid":"","tag":null,"text":"社会活动","status":null,"state":null,"type":"test","list":true,"title":"","description":"","keywords":"","outlink":"","nodes":[{"id":"823263dfbf184524a7d45cf1cf4ade2d","pid":"3d06daf91f57425487cb825382f67678","tag":"-","text":"全部活动","status":null,"state":null,"type":"test","list":true,"title":"","description":"","keywords":"","outlink":"","nodes":[]},{"id":"ef0f101fc8d94dc4b8db73383cffae81","pid":"3d06daf91f57425487cb825382f67678","tag":"-","text":"交流活动","status":null,"state":null,"type":"test","list":true,"title":"","description":"","keywords":"","outlink":"","nodes":[]},{"id":"2cf63cb7619347de83982cc22baf1d40","pid":"3d06daf91f57425487cb825382f67678","tag":"-","text":"鉴定活动","status":null,"state":null,"type":"test","list":true,"title":"","description":"","keywords":"","outlink":"","nodes":[]},{"id":"ed739eb682434438a5666a23c79bb0ed","pid":"3d06daf91f57425487cb825382f67678","tag":"-","text":"专家活动","status":null,"state":null,"type":"test","list":true,"title":"","description":"","keywords":"","outlink":"","nodes":[]}]},{"id":"3db82fb73b6c41909fcbf7ea535c9cf9","pid":"","tag":null,"text":"名人交流","status":null,"state":null,"type":"test","list":true,"title":"","description":"","keywords":"","outlink":"","nodes":[{"id":"4f1838c1f57f4efa9f7bf38a3a0b5eec","pid":"3db82fb73b6c41909fcbf7ea535c9cf9","tag":"-","text":"全部活动","status":null,"state":null,"type":"test","list":true,"title":"","description":"","keywords":"","outlink":"","nodes":[]},{"id":"6a50aa6f85cc4b2bbfa91bd082cd6446","pid":"3db82fb73b6c41909fcbf7ea535c9cf9","tag":"-","text":"每日推荐","status":null,"state":null,"type":"test","list":true,"title":"","description":"","keywords":"","outlink":"","nodes":[]},{"id":"13557d25b7dd4baea1757aae02d57bfe","pid":"3db82fb73b6c41909fcbf7ea535c9cf9","tag":"-","text":"名家分享","status":null,"state":null,"type":"test","list":true,"title":"","description":"","keywords":"","outlink":"","nodes":[]},{"id":"75a17cb5df3745fcb3d9f19a45f054f9","pid":"3db82fb73b6c41909fcbf7ea535c9cf9","tag":"-","text":"行业新闻","status":null,"state":null,"type":"test","list":true,"title":"","description":"","keywords":"","outlink":"","nodes":[]}]},{"id":"cfea321b023d4accbaa771d087b72feb","pid":"","tag":null,"text":"鉴定须知","status":null,"state":null,"type":"test","list":true,"title":"","description":"","keywords":"","outlink":"","nodes":[{"id":"198a8cf03af1428086b96b0b3778aaad","pid":"cfea321b023d4accbaa771d087b72feb","tag":"-","text":"展示所有","status":null,"state":null,"type":"test","list":true,"title":"","description":"","keywords":"","outlink":"","nodes":[]},{"id":"86e1e931a3974ac3b6583b981da3be2e","pid":"cfea321b023d4accbaa771d087b72feb","tag":"-","text":"新分类","status":null,"state":null,"type":"test","list":true,"title":"","description":"","keywords":"","outlink":"","nodes":[]}]},{"id":"20b567758ed84784a281aa65490a89ca","pid":"","tag":null,"text":"活动视频","status":null,"state":null,"type":"test","list":true,"title":"","description":"","keywords":"","outlink":"","nodes":[{"id":"74cc271bec12434e9e0c5c1a94f89849","pid":"20b567758ed84784a281aa65490a89ca","tag":"-","text":"全部视频","status":null,"state":null,"type":"test","list":true,"title":"","description":"","keywords":"","outlink":"","nodes":[]},{"id":"ec70c0035a8c4a0caf8ec2e441559a6c","pid":"20b567758ed84784a281aa65490a89ca","tag":"-","text":"视频活动","status":null,"state":null,"type":"test","list":true,"title":"","description":"","keywords":"","outlink":"","nodes":[]},{"id":"956565eac6f74199a1ec68af6bbb5fb8","pid":"20b567758ed84784a281aa65490a89ca","tag":"-","text":"名家分享","status":null,"state":null,"type":"test","list":true,"title":"","description":"","keywords":"","outlink":"","nodes":[]}]}]', '分类');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;

-- 导出  表 21zsjc.comment 结构
CREATE TABLE IF NOT EXISTS `comment` (
  `id` varchar(255) NOT NULL,
  `content` varchar(500) NOT NULL,
  `create_time` datetime NOT NULL,
  `start` int(11) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  `blog_id` varchar(255) NOT NULL,
  `pid` varchar(255) DEFAULT NULL,
  `user_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKkap39f76wn135k7ru564fbjb7` (`blog_id`),
  KEY `FKp4qv1kq7ab549ljq0w8lny89d` (`pid`),
  KEY `FK8kcum44fvpupyw6f5baccx25c` (`user_id`),
  CONSTRAINT `FK8kcum44fvpupyw6f5baccx25c` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
  CONSTRAINT `FKkap39f76wn135k7ru564fbjb7` FOREIGN KEY (`blog_id`) REFERENCES `blog` (`blog_id`),
  CONSTRAINT `FKp4qv1kq7ab549ljq0w8lny89d` FOREIGN KEY (`pid`) REFERENCES `comment` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  21zsjc.comment 的数据：~0 rows (大约)
DELETE FROM `comment`;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
/*!40000 ALTER TABLE `comment` ENABLE KEYS */;

-- 导出  表 21zsjc.customer 结构
CREATE TABLE IF NOT EXISTS `customer` (
  `id` varchar(255) NOT NULL,
  `alibaba` varchar(255) DEFAULT NULL,
  `company` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `tencent1` varchar(255) DEFAULT NULL,
  `tencent2` varchar(255) DEFAULT NULL,
  `wechat` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  21zsjc.customer 的数据：~0 rows (大约)
DELETE FROM `customer`;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` (`id`, `alibaba`, `company`, `email`, `phone`, `tencent1`, `tencent2`, `wechat`) VALUES
	('9d4fbba840074909ad4c6589a555fc45', NULL, '英锐恩科技有限公司', NULL, NULL, NULL, NULL, NULL);
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;

-- 导出  表 21zsjc.file_upload 结构
CREATE TABLE IF NOT EXISTS `file_upload` (
  `id` varchar(255) NOT NULL,
  `cateids` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `extname` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `size` bigint(20) NOT NULL,
  `status` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  21zsjc.file_upload 的数据：~0 rows (大约)
DELETE FROM `file_upload`;
/*!40000 ALTER TABLE `file_upload` DISABLE KEYS */;
/*!40000 ALTER TABLE `file_upload` ENABLE KEYS */;

-- 导出  表 21zsjc.form 结构
CREATE TABLE IF NOT EXISTS `form` (
  `id` varchar(255) NOT NULL,
  `cateids` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `language` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `shape` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `user_id` varchar(255) DEFAULT NULL,
  `pid` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKo5anyad104hybdn8ti64yxtp2` (`pid`),
  CONSTRAINT `FKo5anyad104hybdn8ti64yxtp2` FOREIGN KEY (`pid`) REFERENCES `form` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  21zsjc.form 的数据：~0 rows (大约)
DELETE FROM `form`;
/*!40000 ALTER TABLE `form` DISABLE KEYS */;
/*!40000 ALTER TABLE `form` ENABLE KEYS */;

-- 导出  表 21zsjc.form_item 结构
CREATE TABLE IF NOT EXISTS `form_item` (
  `id` varchar(255) NOT NULL,
  `create_time` datetime NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `option` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  `result` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `user_id` varchar(255) DEFAULT NULL,
  `verify` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `form_id` varchar(255) NOT NULL,
  `pid` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKp4uj9qdhaab59nvfd4fy94bap` (`form_id`),
  KEY `FK69fube5n5bvh2xt0inhpayewh` (`pid`),
  CONSTRAINT `FK69fube5n5bvh2xt0inhpayewh` FOREIGN KEY (`pid`) REFERENCES `form_item` (`id`),
  CONSTRAINT `FKp4uj9qdhaab59nvfd4fy94bap` FOREIGN KEY (`form_id`) REFERENCES `form` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  21zsjc.form_item 的数据：~0 rows (大约)
DELETE FROM `form_item`;
/*!40000 ALTER TABLE `form_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `form_item` ENABLE KEYS */;

-- 导出  表 21zsjc.image 结构
CREATE TABLE IF NOT EXISTS `image` (
  `id` varchar(255) NOT NULL,
  `cateids` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `href_url` varchar(255) DEFAULT NULL,
  `image_name` varchar(255) DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  21zsjc.image 的数据：~11 rows (大约)
DELETE FROM `image`;
/*!40000 ALTER TABLE `image` DISABLE KEYS */;
INSERT INTO `image` (`id`, `cateids`, `create_time`, `description`, `href_url`, `image_name`, `image_url`, `status`) VALUES
	('00415911139b4c5bb8d53c43490ccf0a', '[]', '2019-06-11 10:35:35', NULL, NULL, 'd6a8cbb8a7ca97a447abe3be4cada71b', '/imagePath/d6a8cbb8a7ca97a447abe3be4cada71b.jpg', 0),
	('19925905c0074e9ca3b9c4549290d7c8', '[]', '2019-06-11 10:20:46', NULL, NULL, '3f0f467c90601a8e1e2d2ce714d7f35f', '/imagePath/3f0f467c90601a8e1e2d2ce714d7f35f.png', 0),
	('1bf365b2874944a6bf183c5b16736b2e', '[]', '2019-06-11 10:25:30', NULL, NULL, 'head3', '/imagePath/head3.png', 0),
	('2feba242196d4a3b841f421f5869094b', '[]', '2019-06-11 10:57:29', NULL, NULL, '13d863269512481589dda41da44f1e22', '/imagePath/13d863269512481589dda41da44f1e22.png', 0),
	('550cf83f75f74c52a68d2df84f41cfa2', '[]', '2019-06-11 11:01:54', NULL, NULL, 'buttom-03', '/imagePath/buttom-03.png', 0),
	('84f53d82bfb94ef689c8789451e80a9e', '[]', '2019-06-11 10:16:04', NULL, NULL, '750a2755a0c12728dd4c57523f786e52', '/imagePath/750a2755a0c12728dd4c57523f786e52.jpg', 0),
	('8887177c092b4f938e8e2fd5b1c507b4', '[]', '2019-06-11 11:00:00', NULL, NULL, '50cac2035d1d3f3416c73cbab693f432', '/imagePath/50cac2035d1d3f3416c73cbab693f432.jpg', 0),
	('b26f00a8cffc4570bd8f6802f52f8ad7', '[]', '2019-06-11 10:23:25', NULL, NULL, 'cdc11e21ad2716fc5d0dde4c66797462', '/imagePath/cdc11e21ad2716fc5d0dde4c66797462.png', 0),
	('c12be55f19ff493b8f25c82d2934d5e6', '[]', '2019-06-11 10:26:11', NULL, NULL, 'c7c305471b25122d9431b60ac876353a', '/imagePath/c7c305471b25122d9431b60ac876353a.jpg', 0),
	('df70a13dfcf84014b27e4631d3524c28', '[]', '2019-06-11 10:24:02', NULL, NULL, '01505aa00d87a25da1e387d2905ecba5', '/imagePath/01505aa00d87a25da1e387d2905ecba5.jpg', 0),
	('e3f3e802b59c4abe8ad5bdeb1c701e8b', '[]', '2019-06-11 11:00:47', NULL, NULL, 'fankuai', '/imagePath/fankuai.png', 0),
	('e5c961b0aec24fe1b292ff2853111eaf', '[]', '2019-06-11 11:04:11', NULL, NULL, 'phone01', '/imagePath/phone01.png', 0),
	('ee3b6db8f78b4d55b6275f35bf21d9e6', '[]', '2019-06-11 11:04:11', NULL, NULL, 'youxiang', '/imagePath/youxiang.png', 0);
/*!40000 ALTER TABLE `image` ENABLE KEYS */;

-- 导出  表 21zsjc.product 结构
CREATE TABLE IF NOT EXISTS `product` (
  `id` varchar(255) NOT NULL,
  `article_title` varchar(255) DEFAULT NULL,
  `blog_id` varchar(255) DEFAULT NULL,
  `cateids` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `dev_imgs` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `files` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `imgs` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `json` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `language` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `tag` varchar(255) DEFAULT NULL,
  `update_time` datetime NOT NULL,
  `wid` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  21zsjc.product 的数据：~0 rows (大约)
DELETE FROM `product`;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
/*!40000 ALTER TABLE `product` ENABLE KEYS */;

-- 导出  表 21zsjc.template 结构
CREATE TABLE IF NOT EXISTS `template` (
  `id` varchar(32) NOT NULL,
  `data` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `keywords` varchar(255) DEFAULT NULL,
  `language` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `path` varchar(255) DEFAULT NULL,
  `position` int(11) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `tmp_group` int(11) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  21zsjc.template 的数据：~8 rows (大约)
DELETE FROM `template`;
/*!40000 ALTER TABLE `template` DISABLE KEYS */;
INSERT INTO `template` (`id`, `data`, `description`, `keywords`, `language`, `name`, `path`, `position`, `title`, `tmp_group`, `url`) VALUES
	('34f25c2ca6f342ae8eb28c0206119932', '[{"type":"category","ids":["bd740121a304443f9ffaec98b92e75ce"],"order":1,"number":12},{"type":"category","ids":["3d06daf91f57425487cb825382f67678"],"order":2,"number":8},{"type":"category","ids":["360d8584b2db4e92ace254181eb937ef"],"order":3,"number":16},{"type":"category","ids":["3d06daf91f57425487cb825382f67678"],"order":4,"number":4}]', '', '', 'zh_CN', '首页', 'D:\\1\\21zsjc.com/www/templates/zh_CN/index.html', NULL, NULL, 1, 'index'),
	('54177251c6454f4db5a0dd55e4a32b34', '[{"type":"category","ids":["bd740121a304443f9ffaec98b92e75ce"],"order":1,"number":6}]', '', '', 'zh_CN', '详情', 'D:\\1\\21zsjc.com/www/templates/zh_CN/detail.html', NULL, NULL, 1, 'detail'),
	('7c9cbdea115749a3af35f4016d71edeb', '[{"type":"category","ids":["360d8584b2db4e92ace254181eb937ef"],"order":1,"number":12}]', '', '', 'zh_CN', '热门收藏', 'D:\\1 - 副本\\21zsjc.com/www/templates/zh_CN/news.html', NULL, NULL, 1, 'news'),
	('909a0b3afc7e4cc28a2fb843a90b3fe2', '[{"type":"category","ids":["cfea321b023d4accbaa771d087b72feb"],"order":1,"number":12}]', '', '', 'zh_CN', '鉴定须知', 'D:\\1 - 副本\\21zsjc.com/www/templates/zh_CN/custom.html', NULL, NULL, 1, 'custom'),
	('ad29a33f46ae45409c2a1b7fb69656ee', '[{"type":"category","ids":["20b567758ed84784a281aa65490a89ca"],"order":1,"number":100}]', '', '', 'zh_CN', '活动视频', 'D:\\1 - 副本\\21zsjc.com/www/templates/zh_CN/video.html', NULL, NULL, 1, 'video'),
	('b5ad74c505b848a7a1029de8f5e21d07', '[]', '', '', 'zh_CN', '联系我们', 'D:\\1 - 副本\\21zsjc.com/www/templates/zh_CN/contact.html', NULL, NULL, 1, 'contact'),
	('bd84ca13cd8c4d57a9d2d1479f4f0f54', '[{"type":"category","ids":["bd740121a304443f9ffaec98b92e75ce"],"order":1,"number":12}]', '', '', 'zh_CN', '专家队伍', 'D:\\1\\21zsjc.com/www/templates/zh_CN/products.html', 0, NULL, 1, 'products'),
	('c0392d5ae5c64a198b8d096990f481d8', '[]', '', '', 'zh_CN', '关于中视鉴藏', 'D:\\1\\21zsjc.com/www/templates/zh_CN/about.html', NULL, NULL, 1, 'about'),
	('ca1af69ff40d424bb765fe99d7dda055', '[{"type":"category","ids":["3d06daf91f57425487cb825382f67678"],"order":1,"number":12}]', '', '', 'zh_CN', '社会活动', 'D:\\1 - 副本\\21zsjc.com/www/templates/zh_CN/activity.html', NULL, NULL, 1, 'activity'),
	('cd5c4abb68574ec8ac0d2da3fce9b809', '[{"type":"category","ids":["3db82fb73b6c41909fcbf7ea535c9cf9"],"order":1,"number":12}]', '', '', 'zh_CN', '名人交流', 'D:\\1 - 副本\\21zsjc.com/www/templates/zh_CN/celebrity.html', NULL, NULL, 1, 'celebrity');
/*!40000 ALTER TABLE `template` ENABLE KEYS */;

-- 导出  表 21zsjc.user 结构
CREATE TABLE IF NOT EXISTS `user` (
  `user_id` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `usable` int(11) DEFAULT NULL,
  `username` varchar(16) NOT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  21zsjc.user 的数据：~0 rows (大约)
DELETE FROM `user`;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` (`user_id`, `password`, `usable`, `username`) VALUES
	('ca103b10e7e84b079bd209dba2a346e5', '$2a$10$qAEULRIgmSaf4EGRGaIqVulbDk.Exw2uPvcyUcj9tgXniBpl/DK72', 0, 'admin');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;

-- 导出  表 21zsjc.vote 结构
CREATE TABLE IF NOT EXISTS `vote` (
  `id` varchar(255) NOT NULL,
  `create_time` datetime NOT NULL,
  `blog_id` varchar(255) NOT NULL,
  `user_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK817iuq8sewtgx040d3lb30wva` (`blog_id`),
  KEY `FKcsaksoe2iepaj8birrmithwve` (`user_id`),
  CONSTRAINT `FK817iuq8sewtgx040d3lb30wva` FOREIGN KEY (`blog_id`) REFERENCES `blog` (`blog_id`),
  CONSTRAINT `FKcsaksoe2iepaj8birrmithwve` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  21zsjc.vote 的数据：~0 rows (大约)
DELETE FROM `vote`;
/*!40000 ALTER TABLE `vote` DISABLE KEYS */;
/*!40000 ALTER TABLE `vote` ENABLE KEYS */;

-- 导出  表 21zsjc.website 结构
CREATE TABLE IF NOT EXISTS `website` (
  `id` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `operateable` int(11) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  21zsjc.website 的数据：~108 rows (大约)
DELETE FROM `website`;
/*!40000 ALTER TABLE `website` DISABLE KEYS */;
INSERT INTO `website` (`id`, `description`, `operateable`, `type`, `url`) VALUES
	('01991c231ec04c8190ed97c1de966cfb', '已审核文章导出excel表格文件', 1, 'query', '/excel/outputArticle'),
	('02ac1eebda454b159e64895b2a50992c', '表单检验', 1, 'add', '/formItem/saveRegular'),
	('0338ea866a104f49982e68511a9ee443', '查询属于该表单的全部表单项', 1, 'query', '/formItem/findByForm'),
	('03facabdf55742048745302f4f470f65', '修改公司名称', 1, 'add', '/admin/editCompany'),
	('047717bb29a74305a8b339f227fbfa12', '删除文件', 1, 'delete', '/admin/deleteFile'),
	('0549a35db3184cf88b2c5722b884e0e0', '批量删除图片', 1, 'delete', '/image/batchDelete'),
	('07387d24a0514c9fa3ba9b79d4d516a7', '图片添加分类', 1, 'query', '/image/addImageCategory'),
	('0be58c7f70ef42928d18071c0b7ff5a3', '产品查看文章', 1, 'query', '/product/findProductBlog'),
	('0da6e64f6e4d4f2f8ea7101b0f50d247', '生成用户提交表单的excel', 1, 'query', '/excel/getForm'),
	('0e40f6277c534178b0ace7d2dda84b4e', '添加文章', 1, 'save', '/admin/addBlog'),
	('201cbb99941647ef9dca2c310f80a5d3', '回收站恢复', 1, 'update', '/recovery/restore'),
	('25a7696821bd4630acbb64e5f46387a6', '保存html编辑文本', 1, 'save', '/template/saveEditor'),
	('2a1566ab465746b2bd94f7a62ceec99a', '首页', 1, 'query', '/admin/blog/detail/**'),
	('2c632ce766834e6aa32ed843d6246cf4', '上传模板', 1, 'save', '/template/uploadTemplate'),
	('2ccb6f597c3c4ac798cfffcb855d6355', '授权', 1, 'add', '/user/detail'),
	('2da1ffc2d6474499817d1b0209679603', '搜索', 1, 'query', '/search/searchAll'),
	('2fa5392a0fdf49d68edeefae6d296054', '首页', 1, 'query', '/admin/index'),
	('335cc895000a4be5b3191b09182a4749', '产品分类查询', 1, 'query', '/admin/categoryProduct'),
	('3c3b8eaea91b43c0aefb4e0c42e9c6a9', '客服管理上传', 1, 'update', '/admin/uploadWechat'),
	('40a1efccb02e41a49f325e1d38b39f1c', '分类图片', 1, 'query', '/image/categoryImage'),
	('412b756e618c49e7b758c2d9292b0ff3', '将用户提交的表单处理(设置为已读或异常)', 1, 'update', '/form/processForm'),
	('45957eb1eebf436db2e3f0764f2f7d83', '客服管理修改', 1, 'update', '/admin/customerEdit'),
	('4763526dddb24bf992ee4313b03068ac', '删除图片', 1, 'delete', '/image/delete'),
	('4860c585fff044968758ecbc5c9188b4', '删除分类', 1, 'delete', '/admin/deletecategory'),
	('4860efbe87964b2fbd98c225706815b6', '保存模板数据（表单）', 1, 'save', '/template/saveForm'),
	('497f20436ec44fdb88a7420edbbf1bca', '单文件上传', 1, 'add', '/file/upload'),
	('4a8fcdd1c3734b8eab56c6468537a89f', '文章详情', 1, 'query', '/admin/blog/detail'),
	('4ac410ae4acb4f059033a786ca27a107', '删除分类模板数据', 1, 'delete', '/template/deleteData'),
	('4bc095341dfd4cd9a8ffc4aae0060cd0', '将Excel表格放入回收站', 1, 'delete', '/product/deleteWorktable'),
	('4e998d9e6ad04484bcb1210a8e958093', '删除文章', 1, 'delete', '/admin/delete/blog'),
	('4ec3814bc7e64f4caad4c4f90e1c1b7a', '删除产品', 1, 'delete', '/product/deleteProduct'),
	('4fd149956e5c47259b977d4f9487bd27', '已审核文章隐藏文章', 1, 'update', '/admin/isHiddenArticle'),
	('5c2e50d0694e4394a6c01c36ec9ac59e', '查询所有模板', 1, 'query', '/template/findAll'),
	('5cb3cfaa1d064cb5a2feb24581ac55fa', '显示模板数据', 1, 'query', '/template/showData'),
	('607ebd6db7f84adeb5eeff3326cc1a45', '修改分类', 1, 'update', '/admin/category/change'),
	('62392ed6c0094c7ba5f8cfc68a089e35', '查询所有图片', 1, 'query', '/image/findAll'),
	('67d8ba0129b744a5980297577f0e2296', '在线编辑', 1, 'query', '/template/readHtmlContent'),
	('69e90bec28704228aca5d8cf0f36416e', '保存模板数据（文章）', 1, 'save', '/template/saveArticle'),
	('6b23430e94f74c91b3b3e9a47683b52a', '回收站批量删除', 1, 'delete', '/recovery/batchDelete'),
	('6e45ade7fcc443da85910471a8bf71b6', '删除模板数据', 1, 'delete', '/template/deleteBlogData'),
	('6fba1bfde7984b5cb2a53e641521d036', '修改产品名称', 1, 'update', '/product/addName'),
	('70f6e847d10c49ce9e104ef66fbacaf7', '批量删除表单', 1, 'delete', '/form/batchDeteForm'),
	('710ec61f3b0140dbbc566ae722487a3e', '全选产品文件列表', 1, 'add', '/product/addPackageDatas'),
	('72496066847840ae87c45040d72364e4', '回收站查询', 1, 'query', '/recovery/findAll'),
	('735a4912471349e1929883e383461eaa', '审核文章', 1, 'update', '/admin/reviewArticle'),
	('7446a6a66d7d42a5ac3798c05bcec221', '表单查找语言', 1, 'query', '/form/findByLang'),
	('75bf5301278d4fa1b3c184c080ed8526', '多文件上传', 1, 'add', '/file/multiUpload'),
	('798b283d79d044e38bce8c5a77a48b7d', '查询分类', 1, 'query', '/admin/findCategoryName'),
	('7ab412d529c6436c909f4e48bd9f7203', '文件下载', 1, 'query', '/file/fileDownload'),
	('7d27275d2d1c49a3a37e05a8da80506e', '批量删除已审核文章', 1, 'delete', '/admin/batchDelete'),
	('7e6f6c6ec4944f28bb1c33fa0c6bc408', '表单添加语言', 1, 'add', '/form/saveLang'),
	('7ed5f42a8d074a59ab6ce7fc3324c139', '查询所有文章', 1, 'query', '/admin/findAllArticle'),
	('80a258dd365a4cd39f127efd746d42a7', '修改图片描述', 1, 'update', '/image/editDescription'),
	('825f91bb5f48484b8f5c0c07095c6030', '后台添加一条表单项', 1, 'save', '/formItem/add'),
	('82c7d79304b2447fa82fc4e1c28bca43', '产品Excel下载', 1, 'query', '/excel/getProduct'),
	('85fd1fddf5b84277bccd030d3322306e', '上传图片', 1, 'save', '/image/save'),
	('861d0cbc0ecb4bf1a96659017d5f4807', '文章审核和管理', 1, 'query', '/admin/findBlog'),
	('877e838d14314f53b0f30e8de5691508', '修改图片名称', 1, 'update', '/image/editName'),
	('8d45e839a1ab4016ae933ea756ced041', '批量处理表单', 1, 'add', '/form/batchHandleForm'),
	('8e6dd488a47e4798937f2d92cf1d2546', '保存模板数据（文件）', 1, 'save', '/template/saveFileData'),
	('90d547a20dc641d4ac417164c41ab0da', '产品添加文章', 1, 'add', '/product/addPackageBlog'),
	('936d11b0ab2e4942ad0ffe4918fb97cd', '模板数据管理', 1, 'query', '/template/templateData'),
	('9463f712e351458aa9cf4705862df69f', '产品管理分类下查找产品', 1, 'query', '/admin/categoryProduct'),
	('958a2ace6d434d52809a8d1e69f911a1', '产品导入记录', 1, 'query', '/product/categoryWork'),
	('98eb26e31e994ebb9703b7903d947133', '显示表单项的内容\r\n', 1, 'query', '/formItem/findById'),
	('9d6536ea6fbf4319ac4f8f4223c0933a', '保存模板数据（分类）', 1, 'save', '/template/saveCategory'),
	('9e9252b780fe4afd9e749b7d0ddd1d7e', '查询全部上传的Excel表格', 1, 'query', '/product/findWorktable'),
	('a266564f246b435d948cf9096a0f4944', '修改文章', 1, 'update', '/admin/modify'),
	('a4dd79d1a84b444bbe3595b3b3f7c30a', '文件管理', 1, 'query', '/admin/findFile'),
	('a58796c11f624bfab65aedb4b9071267', '查找下拉表单项内容', 1, 'query', '/formItem/findOption'),
	('a5c3cf2316f94e76b351986d41acbbfa', '保存模板数据（产品）', 1, 'save', '/template/saveProductData'),
	('a63e341c1185440bbca2e5214e482176', '文章取消置顶', 1, 'delete', '/admin/isNotTop'),
	('a66141e7358a4b41886d53428079abc4', '删除文件到回收站', 1, 'delete', '/admin/delete'),
	('a6bdcb21b68944e59aa768fcb910427c', '模板分组操作', 1, 'update', '/template/templateGroup'),
	('a7d239b43e02473d8602293848308c22', '文件管理添加分类', 1, 'update', '/file/addFileCategory'),
	('a9970106659941de9d391731e6a239bb', '显示表单项内容', 1, 'query', '/form/showForm'),
	('acd56358b5684c2f84267efce9b68d06', '禁用用户', 1, 'update', '/user/banUser'),
	('af6df7441c6043b6b1409e2e941cd5b1', '新文件页面', 1, 'query', '/admin/findNewFile'),
	('b0a341effd044b889fdd46fa6afd31f0', '产品添加语言', 1, 'add', '/product/saveLang'),
	('b31834aab2124f289fd9e0f105960d5a', '产品查找文件', 1, 'query', '/product/findProductFile'),
	('bdd0dea3377f484bb42b033c772209a2', '查询所有用户', 1, 'add', '/user/findAll'),
	('bdff6578df0b46b29dd3c3716a46a3d8', '回收站删除', 1, 'delete', '/recovery/delete'),
	('bef7f3acf4e14d1f9f7f93c458df19a8', '文件批量删除', 1, 'delete', '/file/batchDeleteFile'),
	('bf7db0e5c5df40849ae8dfe7664821b6', '产品详情', 1, 'query', '/admin/findProductDetail'),
	('c338306e60bf4f899f5d588ff129d190', '产品查找语言', 1, '', '/product/findByLang'),
	('ca55691f1c0e45ab94b7838a6f11d171', '保存模板数据（图片）', 1, 'save', '/template/saveImageData'),
	('cefb13c8f2eb48ff9b32f16bb86e1f5d', '查询所有表单', 1, 'query', '/form/findAllForm'),
	('d00895c509cb4b5991b6651e70770211', '工作簿添加分类', 1, 'update', '/product/addWorkCategory'),
	('d11fdeb980f94a7489a52493fee6c059', '回收站批量恢复', 1, 'add', '/recovery/batchRestore'),
	('d3fba3386eb949a2ae63db667751e476', '分页查询全部表单', 1, 'query', '/form/findPageForm'),
	('d81769894bdf4de39b4c8dc7a358f6c1', '文章置顶', 1, 'update', '/admin/isTop'),
	('daac186a09b947d4903725387bc3e066', '产品添加文件下载', 1, 'save', '/product/addPackageData'),
	('ddb5335887f04da18077bf0719e185a9', '保存表单项的各个属性(除下拉列表项)', 1, 'save', '/formItem/saveFormItem'),
	('ddba70738a404650af43910e51f405db', '删除一条表单项', 1, 'delete', '/formItem/delete'),
	('e0bfa7e51d6443edbc431c9500c5baa2', '查询单篇博客', 1, 'query', '/admin/findOneBlog'),
	('e3b3764bc6ba4d3f977bd55e75c8390e', '模板文件代码下载', 1, 'query', '/template/downloadTemplate'),
	('e9a9b7fec9cb491599873d0a2eb86482', '查询所有分类', 1, 'query', '/admin/categorys'),
	('eafc16fa37e349a5ac0f43d19706ed0d', '创建分类', 1, 'save', '/admin/create_category'),
	('eb668dbf496f4e69ae3bff1694fd18ac', '后台添加一条表单', 1, 'save', '/form/addForms'),
	('eb9ed2e77fe34df7afae27c413b20404', '保存下拉列表项', 1, 'save', '/formItem/saveOption'),
	('ec711e60fdda47e48bc9f47bc37ec8d6', '产品Excel上传', 1, 'add', '/excel/uploadProduct'),
	('ef5a8b2846314e9983debc2cca316669', '修改模板', 1, 'update', '/template/editTemplate'),
	('efb63f00ec274ed8b5147a1b993cffca', '客户管理查询', 1, 'query', '/admin/customerAll'),
	('f1a318d4130d46e781216bf5e623d46d', '文章批量转移', 1, 'update', '/admin/changeArticleCategory'),
	('f42b968b73394999ae3f497c074b40c3', '修改表单名', 1, 'update', '/form/addName'),
	('f8387d7c785a405e95df9b05d893e728', '产品数据包下载', 1, 'query', '/product/proPackageDownload'),
	('fabb19255ff9454292a1a696521d8fd7', '删除模板', 1, 'delete', '/template/delete'),
	('fd2ae99652754a588db3103dacc6bfb0', '将表单放入回收站中', 1, 'delete', '/form/delete');
/*!40000 ALTER TABLE `website` ENABLE KEYS */;

-- 导出  表 21zsjc.worktable 结构
CREATE TABLE IF NOT EXISTS `worktable` (
  `id` varchar(255) NOT NULL,
  `cateids` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `file_name` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  21zsjc.worktable 的数据：~0 rows (大约)
DELETE FROM `worktable`;
/*!40000 ALTER TABLE `worktable` DISABLE KEYS */;
/*!40000 ALTER TABLE `worktable` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
