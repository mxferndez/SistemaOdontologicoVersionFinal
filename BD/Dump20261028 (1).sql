CREATE DATABASE  IF NOT EXISTS `sistema_odontologico` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `sistema_odontologico`;
-- MySQL dump 10.13  Distrib 8.0.44, for Win64 (x86_64)
--
-- Host: localhost    Database: sistema_odontologico
-- ------------------------------------------------------
-- Server version	9.5.0

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
SET @MYSQLDUMP_TEMP_LOG_BIN = @@SESSION.SQL_LOG_BIN;
SET @@SESSION.SQL_LOG_BIN= 0;

--
-- GTID state at the beginning of the backup 
--

SET @@GLOBAL.GTID_PURGED=/*!80000 '+'*/ '86ab5091-d07e-11f0-a564-e4e749369cd3:1-1418';

--
-- Table structure for table `actividades`
--

DROP TABLE IF EXISTS `actividades`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `actividades` (
  `id_actividad` bigint unsigned NOT NULL AUTO_INCREMENT,
  `id_usuario` bigint unsigned DEFAULT NULL,
  `id_paciente` bigint unsigned DEFAULT NULL,
  `id_cita` bigint unsigned DEFAULT NULL,
  `id_pago` bigint unsigned DEFAULT NULL,
  `tipo_evento` enum('PACIENTE_REGISTRADO','CITA_CREADA','CITA_CANCELADA','CITA_REPROGRAMADA','ATENCION_INICIADA','ATENCION_FINALIZADA','PAGO_REGISTRADO','OTRO') COLLATE utf8mb4_unicode_ci NOT NULL,
  `descripcion` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `fecha_hora` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_actividad`),
  KEY `idx_actividades_fecha` (`fecha_hora`),
  KEY `idx_actividades_usuario_fecha` (`id_usuario`,`fecha_hora`),
  KEY `fk_actividades_paciente` (`id_paciente`),
  KEY `fk_actividades_cita` (`id_cita`),
  KEY `fk_actividades_pago` (`id_pago`),
  CONSTRAINT `fk_actividades_cita` FOREIGN KEY (`id_cita`) REFERENCES `citas` (`id_cita`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_actividades_paciente` FOREIGN KEY (`id_paciente`) REFERENCES `pacientes` (`id_persona`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_actividades_pago` FOREIGN KEY (`id_pago`) REFERENCES `pagos` (`id_pago`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_actividades_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=76 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `actividades`
--

LOCK TABLES `actividades` WRITE;
/*!40000 ALTER TABLE `actividades` DISABLE KEYS */;
INSERT INTO `actividades` VALUES (1,4,14,NULL,NULL,'PACIENTE_REGISTRADO','Se registro a la paciente Daniela Rios Castillo','2026-10-25 00:07:49'),(2,4,14,13,NULL,'CITA_CREADA','Cita de blanqueamiento dental con la Dra. Rivas','2026-10-25 00:07:49'),(3,4,7,14,NULL,'CITA_CREADA','Cita de resina simple con la Dra. Vega','2026-10-25 00:07:49'),(4,4,11,17,NULL,'CITA_REPROGRAMADA','Cita de control reprogramada a pedido de la madre','2026-10-25 00:07:49'),(5,4,5,NULL,7,'PAGO_REGISTRADO','Pago parcial de la cuota 2 por S/ 150.00','2026-10-25 00:07:49'),(6,3,13,11,NULL,'ATENCION_INICIADA','Inicio de atencion: profilaxis dental','2026-10-25 00:07:49'),(7,4,14,NULL,NULL,'PACIENTE_REGISTRADO','Se registro a la paciente Daniela Rios Castillo','2026-10-25 00:14:05'),(8,4,14,13,NULL,'CITA_CREADA','Cita de blanqueamiento dental con la Dra. Rivas','2026-10-25 00:14:05'),(9,4,7,14,NULL,'CITA_CREADA','Cita de resina simple con la Dra. Vega','2026-10-25 00:14:05'),(10,4,11,17,NULL,'CITA_REPROGRAMADA','Cita de control reprogramada a pedido de la madre','2026-10-25 00:14:05'),(11,4,5,NULL,7,'PAGO_REGISTRADO','Pago parcial de la cuota 2 por S/ 150.00','2026-10-25 00:14:05'),(12,3,13,11,NULL,'ATENCION_INICIADA','Inicio de atencion: profilaxis dental','2026-10-25 00:14:05'),(13,1,NULL,NULL,NULL,'OTRO','Se actualizó el estado del registro de agenda 1.','2026-10-28 10:21:11'),(14,1,NULL,NULL,NULL,'OTRO','Se actualizó el estado del registro de agenda 19.','2026-10-28 10:21:15'),(15,1,NULL,NULL,NULL,'OTRO','Se actualizó el estado del registro de agenda 2.','2026-10-28 10:21:21'),(16,1,NULL,NULL,NULL,'OTRO','Se actualizó el estado del registro de agenda 20.','2026-10-28 10:21:25'),(17,1,NULL,NULL,NULL,'OTRO','Se actualizó el estado del registro de agenda 3.','2026-10-28 10:21:29'),(18,1,NULL,NULL,NULL,'OTRO','Se actualizó el estado del registro de agenda 21.','2026-10-28 10:21:33'),(19,1,NULL,NULL,NULL,'OTRO','Se actualizó el estado del registro de agenda 4.','2026-10-28 10:21:37'),(20,1,NULL,NULL,NULL,'OTRO','Se actualizó el estado del registro de agenda 22.','2026-10-28 10:21:40'),(21,1,NULL,NULL,NULL,'OTRO','Se actualizó el estado del registro de agenda 23.','2026-10-28 10:21:44'),(22,1,NULL,NULL,NULL,'OTRO','Se actualizó el estado del registro de agenda 5.','2026-10-28 10:21:49'),(23,1,NULL,NULL,NULL,'OTRO','Se actualizó el estado del registro de agenda 24.','2026-10-28 10:21:53'),(24,1,NULL,NULL,NULL,'OTRO','Se creó la cuenta de usuario Rfranco.','2026-10-28 10:29:50'),(25,1,NULL,NULL,NULL,'OTRO','Se actualizó la configuración general de la clínica.','2026-10-28 10:30:31'),(26,4,6,19,19,'PAGO_REGISTRADO','Se registró un pago por 80.0000.','2026-10-28 10:47:13'),(27,4,16,35,NULL,'OTRO','Cita 35 cambió a EN_ESPERA','2026-10-28 10:47:55'),(28,4,16,36,NULL,'OTRO','Cita 36 cambió a EN_ESPERA','2026-10-28 10:47:58'),(29,1,NULL,NULL,NULL,'OTRO','Se actualizó el estado del registro de agenda 6.','2026-10-28 10:50:06'),(30,1,NULL,NULL,NULL,'OTRO','Se actualizó el estado del registro de agenda 39.','2026-10-28 10:50:10'),(31,1,NULL,NULL,NULL,'OTRO','Se actualizó el estado del registro de agenda 13.','2026-10-28 10:50:13'),(32,1,NULL,NULL,NULL,'OTRO','Se actualizó el estado del registro de agenda 31.','2026-10-28 10:50:19'),(33,1,NULL,NULL,NULL,'OTRO','Se actualizó el estado del registro de agenda 14.','2026-10-28 10:50:23'),(34,1,NULL,NULL,NULL,'OTRO','Se actualizó el estado del registro de agenda 32.','2026-10-28 10:50:27'),(35,1,NULL,NULL,NULL,'OTRO','Se actualizó el estado del registro de agenda 15.','2026-10-28 10:50:31'),(36,1,NULL,NULL,NULL,'OTRO','Se actualizó el estado del registro de agenda 33.','2026-10-28 10:50:35'),(37,1,NULL,NULL,NULL,'OTRO','Se actualizó el estado del registro de agenda 16.','2026-10-28 10:50:38'),(38,1,NULL,NULL,NULL,'OTRO','Se actualizó el estado del registro de agenda 34.','2026-10-28 10:50:44'),(39,1,NULL,NULL,NULL,'OTRO','Se actualizó el estado del registro de agenda 17.','2026-10-28 10:50:47'),(40,1,NULL,NULL,NULL,'OTRO','Se actualizó el estado del registro de agenda 35.','2026-10-28 10:50:51'),(41,1,NULL,NULL,NULL,'OTRO','Se actualizó el estado del registro de agenda 18.','2026-10-28 10:50:54'),(42,1,NULL,NULL,NULL,'OTRO','Se actualizó el estado del registro de agenda 36.','2026-10-28 10:50:58'),(43,1,NULL,NULL,NULL,'OTRO','Se actualizó el estado del registro de agenda 7.','2026-10-28 10:51:03'),(44,1,NULL,NULL,NULL,'OTRO','Se actualizó el estado del registro de agenda 8.','2026-10-28 10:51:07'),(45,1,NULL,NULL,NULL,'OTRO','Se actualizó el estado del registro de agenda 25.','2026-10-28 10:51:10'),(46,1,NULL,NULL,NULL,'OTRO','Se actualizó el estado del registro de agenda 26.','2026-10-28 10:51:13'),(47,1,NULL,NULL,NULL,'OTRO','Se actualizó el estado del registro de agenda 9.','2026-10-28 10:51:17'),(48,1,NULL,NULL,NULL,'OTRO','Se actualizó el estado del registro de agenda 27.','2026-10-28 10:51:20'),(49,1,NULL,NULL,NULL,'OTRO','Se actualizó el estado del registro de agenda 10.','2026-10-28 10:51:24'),(50,1,NULL,NULL,NULL,'OTRO','Se actualizó el estado del registro de agenda 28.','2026-10-28 10:51:27'),(51,1,NULL,NULL,NULL,'OTRO','Se actualizó el estado del registro de agenda 11.','2026-10-28 10:51:31'),(52,1,NULL,NULL,NULL,'OTRO','Se actualizó el estado del registro de agenda 29.','2026-10-28 10:51:34'),(53,1,NULL,NULL,NULL,'OTRO','Se actualizó el estado del registro de agenda 12.','2026-10-28 10:51:37'),(54,1,NULL,NULL,NULL,'OTRO','Se actualizó el estado del registro de agenda 30.','2026-10-28 10:51:41'),(55,1,NULL,NULL,NULL,'OTRO','Se actualizó el estado del registro de agenda 38.','2026-10-28 10:51:45'),(56,1,NULL,NULL,NULL,'OTRO','Se actualizó el estado del registro de agenda 39.','2026-10-28 10:53:42'),(57,1,NULL,NULL,NULL,'OTRO','Se actualizó la disponibilidad del odontólogo 6 para el día 7.','2026-10-28 10:54:03'),(58,1,NULL,NULL,NULL,'OTRO','Se creó la disponibilidad del odontólogo 7 para el día 1.','2026-10-28 10:54:29'),(59,1,NULL,NULL,NULL,'OTRO','Se actualizó la disponibilidad del odontólogo 7 para el día 1.','2026-10-28 10:55:17'),(60,1,NULL,NULL,NULL,'OTRO','Se creó la disponibilidad del odontólogo 6 para el día 2.','2026-10-28 10:56:02'),(61,2,16,35,NULL,'ATENCION_INICIADA','Se inició la atención de la cita 35.','2026-10-28 10:58:31'),(62,2,16,35,NULL,'ATENCION_FINALIZADA','Se finalizó la atención de la cita 35.','2026-10-28 11:02:03'),(63,1,NULL,NULL,NULL,'OTRO','Se actualizó la disponibilidad del odontólogo 6 para el día 2.','2026-10-28 11:06:06'),(64,1,NULL,NULL,NULL,'OTRO','Se actualizó el estado del registro de agenda 41.','2026-10-28 11:06:12'),(65,1,NULL,NULL,NULL,'OTRO','Se actualizó el estado del registro de agenda 39.','2026-10-28 11:06:15'),(66,1,NULL,NULL,NULL,'OTRO','Se actualizó el estado del registro de agenda 40.','2026-10-28 11:06:23'),(67,1,NULL,NULL,NULL,'OTRO','Se creó la disponibilidad del odontólogo 7 para el día 1.','2026-10-28 11:07:07'),(68,1,NULL,NULL,NULL,'OTRO','Se actualizó la disponibilidad del odontólogo 7 para el día 1.','2026-10-28 11:08:05'),(69,1,NULL,NULL,NULL,'OTRO','Se creó la disponibilidad del odontólogo 7 para el día 2.','2026-10-28 11:08:50'),(70,1,NULL,NULL,NULL,'OTRO','Se actualizó la disponibilidad del odontólogo 7 para el día 6.','2026-10-28 11:09:35'),(71,1,NULL,NULL,NULL,'OTRO','Se actualizó la disponibilidad del odontólogo 7 para el día 5.','2026-10-28 11:09:53'),(72,1,NULL,NULL,NULL,'OTRO','Se actualizó la disponibilidad del odontólogo 7 para el día 4.','2026-10-28 11:10:06'),(73,4,16,37,NULL,'CITA_CREADA','Se registró una cita para 2026-10-29 10:00','2026-10-28 11:11:08'),(74,7,NULL,NULL,NULL,'OTRO','Se actualizó la disponibilidad del odontólogo 7 para el día 6.','2026-10-28 11:46:51'),(75,3,7,14,NULL,'ATENCION_INICIADA','Se inició la atención de la cita 14.','2026-10-28 12:04:23');
/*!40000 ALTER TABLE `actividades` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `antecedentes_medicos`
--

DROP TABLE IF EXISTS `antecedentes_medicos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `antecedentes_medicos` (
  `id_paciente` bigint unsigned NOT NULL,
  `antecedentes_personales` text COLLATE utf8mb4_unicode_ci,
  `antecedentes_familiares` text COLLATE utf8mb4_unicode_ci,
  `alergias` text COLLATE utf8mb4_unicode_ci,
  `enfermedades_cronicas` text COLLATE utf8mb4_unicode_ci,
  `cirugias_previas` text COLLATE utf8mb4_unicode_ci,
  `observaciones` text COLLATE utf8mb4_unicode_ci,
  `actualizado_por_usuario` bigint unsigned DEFAULT NULL,
  `actualizado_en` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_paciente`),
  KEY `fk_antecedentes_usuario` (`actualizado_por_usuario`),
  CONSTRAINT `fk_antecedentes_paciente` FOREIGN KEY (`id_paciente`) REFERENCES `pacientes` (`id_persona`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_antecedentes_usuario` FOREIGN KEY (`actualizado_por_usuario`) REFERENCES `usuarios` (`id_usuario`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `antecedentes_medicos`
--

LOCK TABLES `antecedentes_medicos` WRITE;
/*!40000 ALTER TABLE `antecedentes_medicos` DISABLE KEYS */;
INSERT INTO `antecedentes_medicos` VALUES (5,'Fumador ocasional','Padre con diabetes tipo 2',NULL,NULL,NULL,NULL,2,'2026-10-25 05:07:49'),(6,'Sangrado de encias al cepillarse',NULL,NULL,NULL,'Cesarea en 2016',NULL,2,'2026-10-25 05:07:49'),(7,NULL,'Madre con hipertension','Alergia a la penicilina',NULL,'Apendicectomia en 2005','Evitar penicilinas y derivados',3,'2026-10-25 05:07:49'),(8,NULL,NULL,NULL,NULL,NULL,NULL,2,'2026-10-25 05:07:49'),(9,'Toma medicacion de forma regular','Padre fallecido por infarto',NULL,'Hipertension arterial',NULL,'Controlar presion antes de procedimientos',2,'2026-10-25 05:07:49'),(11,'Respiracion bucal',NULL,NULL,NULL,NULL,'Menor de edad, acude con su madre',3,'2026-10-25 05:07:49'),(13,'Bruxismo nocturno',NULL,'Alergia al latex',NULL,NULL,'Usar guantes sin latex',3,'2026-10-25 05:07:49');
/*!40000 ALTER TABLE `antecedentes_medicos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `atenciones`
--

DROP TABLE IF EXISTS `atenciones`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `atenciones` (
  `id_atencion` bigint unsigned NOT NULL AUTO_INCREMENT,
  `id_cita` bigint unsigned NOT NULL,
  `descripcion_problema` text COLLATE utf8mb4_unicode_ci,
  `nota_evolucion` text COLLATE utf8mb4_unicode_ci,
  `estado` enum('ABIERTA','FINALIZADA') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'ABIERTA',
  `inicio_atencion` datetime DEFAULT NULL,
  `fin_atencion` datetime DEFAULT NULL,
  `creado_en` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `actualizado_en` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_atencion`),
  UNIQUE KEY `uq_atenciones_cita` (`id_cita`),
  CONSTRAINT `fk_atenciones_cita` FOREIGN KEY (`id_cita`) REFERENCES `citas` (`id_cita`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `chk_atenciones_rango` CHECK (((`fin_atencion` is null) or (`inicio_atencion` is null) or (`fin_atencion` >= `inicio_atencion`)))
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `atenciones`
--

LOCK TABLES `atenciones` WRITE;
/*!40000 ALTER TABLE `atenciones` DISABLE KEYS */;
INSERT INTO `atenciones` VALUES (1,1,'Dolor al masticar en el lado inferior derecho y sangrado de encias','Se realiza examen clinico y radiografico. Se propone plan de tratamiento por etapas.','FINALIZADA','2026-10-11 09:00:00','2026-10-11 09:40:00','2026-10-25 05:07:49','2026-10-25 05:07:49'),(2,2,'Control de rutina y limpieza','Se remueve sarro supragingival. Se indica tecnica de cepillado.','FINALIZADA','2026-10-15 10:00:00','2026-10-15 10:45:00','2026-10-25 05:07:49','2026-10-25 05:07:49'),(3,3,'Caries visible en premolar superior','Restauracion con resina bajo anestesia local. Sin complicaciones.','FINALIZADA','2026-10-18 15:00:00','2026-10-18 15:40:00','2026-10-25 05:07:49','2026-10-25 05:07:49'),(4,4,'Dolor espontaneo y cambio de coloracion en incisivo central superior','Se realiza conductometria y preparacion de conductos. Se indica medicacion.','FINALIZADA','2026-10-20 11:00:00','2026-10-20 12:00:00','2026-10-25 05:07:49','2026-10-25 05:07:49'),(5,5,'Dolor intenso en molar inferior derecho','Primera sesion del plan. Acceso cameral, limpieza y medicacion intraconducto.','FINALIZADA','2026-10-22 09:30:00','2026-10-22 11:00:00','2026-10-25 05:07:49','2026-10-25 05:07:49'),(6,8,'Control mensual de ortodoncia','Ajuste de arcos. Paciente refiere leve molestia.','FINALIZADA','2026-10-24 17:00:00','2026-10-24 17:30:00','2026-10-25 05:07:49','2026-10-25 05:07:49'),(7,11,'Limpieza dental por acumulacion de sarro',NULL,'ABIERTA','2026-10-25 15:05:00',NULL,'2026-10-25 05:07:49','2026-10-25 05:07:49'),(9,35,'primera atención realizada','molestias en las encias','FINALIZADA','2026-10-28 10:58:31','2026-10-28 11:02:03','2026-10-28 15:58:31','2026-10-28 16:02:03'),(10,14,NULL,NULL,'ABIERTA','2026-10-28 12:04:23',NULL,'2026-10-28 17:04:23','2026-10-28 17:04:23');
/*!40000 ALTER TABLE `atenciones` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categorias_servicio`
--

DROP TABLE IF EXISTS `categorias_servicio`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categorias_servicio` (
  `id_categoria` smallint unsigned NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `descripcion` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id_categoria`),
  UNIQUE KEY `uq_categorias_servicio_nombre` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categorias_servicio`
--

LOCK TABLES `categorias_servicio` WRITE;
/*!40000 ALTER TABLE `categorias_servicio` DISABLE KEYS */;
INSERT INTO `categorias_servicio` VALUES (1,'Diagnostico y prevencion','Evaluaciones, limpiezas y prevencion de caries',1),(2,'Operatoria dental','Restauraciones con resina',1),(3,'Endodoncia','Tratamiento de conductos',1),(4,'Cirugia oral','Extracciones y procedimientos quirurgicos',1),(5,'Ortodoncia','Correccion de la posicion dental con brackets',1),(6,'Estetica dental','Procedimientos esteticos',1);
/*!40000 ALTER TABLE `categorias_servicio` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `citas`
--

DROP TABLE IF EXISTS `citas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `citas` (
  `id_cita` bigint unsigned NOT NULL AUTO_INCREMENT,
  `id_paciente` bigint unsigned NOT NULL,
  `id_odontologo` bigint unsigned NOT NULL,
  `id_consultorio` smallint unsigned NOT NULL,
  `id_servicio` int unsigned DEFAULT NULL,
  `id_cita_anterior` bigint unsigned DEFAULT NULL,
  `creado_por_usuario` bigint unsigned DEFAULT NULL,
  `cancelado_por_usuario` bigint unsigned DEFAULT NULL,
  `fecha_hora_inicio` datetime NOT NULL,
  `duracion_min` smallint unsigned NOT NULL DEFAULT '30',
  `canal` enum('VENTANILLA','TELEFONO','WHATSAPP') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `tipo_cita` enum('CONSULTA','CONTROL','TRATAMIENTO','EMERGENCIA') COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `estado` enum('PROGRAMADA','CONFIRMADA','EN_ESPERA','EN_ATENCION','ATENDIDA','CANCELADA','AUSENTE','REPROGRAMADA') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'PROGRAMADA',
  `hora_llegada` datetime DEFAULT NULL,
  `motivo_cancelacion` text COLLATE utf8mb4_unicode_ci,
  `observaciones` text COLLATE utf8mb4_unicode_ci,
  `creado_en` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `actualizado_en` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_cita`),
  KEY `idx_citas_agenda` (`id_odontologo`,`fecha_hora_inicio`,`estado`),
  KEY `idx_citas_paciente_fecha` (`id_paciente`,`fecha_hora_inicio`),
  KEY `idx_citas_consultorio_fecha` (`id_consultorio`,`fecha_hora_inicio`),
  KEY `idx_citas_estado_fecha` (`estado`,`fecha_hora_inicio`),
  KEY `fk_citas_servicio` (`id_servicio`),
  KEY `fk_citas_cita_anterior` (`id_cita_anterior`),
  KEY `fk_citas_creado_por` (`creado_por_usuario`),
  KEY `fk_citas_cancelado_por` (`cancelado_por_usuario`),
  CONSTRAINT `fk_citas_cancelado_por` FOREIGN KEY (`cancelado_por_usuario`) REFERENCES `usuarios` (`id_usuario`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_citas_cita_anterior` FOREIGN KEY (`id_cita_anterior`) REFERENCES `citas` (`id_cita`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_citas_consultorio` FOREIGN KEY (`id_consultorio`) REFERENCES `consultorios` (`id_consultorio`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_citas_creado_por` FOREIGN KEY (`creado_por_usuario`) REFERENCES `usuarios` (`id_usuario`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_citas_odontologo` FOREIGN KEY (`id_odontologo`) REFERENCES `odontologos` (`id_usuario`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_citas_paciente` FOREIGN KEY (`id_paciente`) REFERENCES `pacientes` (`id_persona`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_citas_servicio` FOREIGN KEY (`id_servicio`) REFERENCES `servicios` (`id_servicio`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `chk_citas_duracion` CHECK ((`duracion_min` > 0))
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `citas`
--

LOCK TABLES `citas` WRITE;
/*!40000 ALTER TABLE `citas` DISABLE KEYS */;
INSERT INTO `citas` VALUES (1,5,2,1,1,NULL,4,NULL,'2026-10-11 09:00:00',30,NULL,'CONSULTA','ATENDIDA','2026-10-11 08:50:00',NULL,NULL,'2026-10-25 05:07:49','2026-10-28 05:04:47'),(2,6,2,1,2,NULL,4,NULL,'2026-10-15 10:00:00',45,'TELEFONO','CONTROL','ATENDIDA','2026-10-15 09:58:00',NULL,NULL,'2026-10-25 05:07:49','2026-10-28 05:04:47'),(3,7,3,2,4,NULL,4,NULL,'2026-10-18 15:00:00',40,'WHATSAPP','TRATAMIENTO','ATENDIDA','2026-10-18 14:55:00',NULL,'Paciente alergico a la penicilina','2026-10-25 05:07:49','2026-10-28 05:04:47'),(4,8,2,1,6,NULL,4,NULL,'2026-10-20 11:00:00',60,NULL,'TRATAMIENTO','ATENDIDA','2026-10-20 10:50:00',NULL,NULL,'2026-10-25 05:07:49','2026-10-28 05:04:47'),(5,5,2,1,7,1,4,NULL,'2026-10-22 09:30:00',90,NULL,'TRATAMIENTO','ATENDIDA','2026-10-22 09:25:00',NULL,'Sesion 1 del plan de tratamiento','2026-10-25 05:07:49','2026-10-28 05:04:47'),(6,9,3,2,8,NULL,4,4,'2026-10-23 16:00:00',40,'TELEFONO','TRATAMIENTO','CANCELADA',NULL,'El paciente indico que no podra asistir por motivos laborales',NULL,'2026-10-25 05:07:49','2026-10-28 05:04:47'),(7,10,2,1,1,NULL,4,NULL,'2026-10-24 10:30:00',30,'WHATSAPP','CONSULTA','AUSENTE',NULL,NULL,'No se presento ni aviso','2026-10-25 05:07:49','2026-10-28 05:04:47'),(8,11,3,2,11,NULL,4,NULL,'2026-10-24 17:00:00',30,NULL,'CONTROL','ATENDIDA','2026-10-24 16:55:00',NULL,NULL,'2026-10-25 05:07:49','2026-10-28 05:04:47'),(9,12,2,1,1,NULL,4,NULL,'2026-10-25 09:00:00',30,NULL,'CONSULTA','CONFIRMADA',NULL,NULL,NULL,'2026-10-25 05:07:49','2026-10-28 05:04:47'),(10,5,2,1,5,NULL,4,NULL,'2026-10-25 10:00:00',60,NULL,'TRATAMIENTO','EN_ESPERA','2026-10-25 09:50:00',NULL,'Sesion 2 del plan de tratamiento','2026-10-25 05:07:49','2026-10-28 05:04:47'),(11,13,3,2,2,NULL,4,NULL,'2026-10-25 15:00:00',45,'TELEFONO','CONTROL','EN_ATENCION','2026-10-25 14:50:00',NULL,'Alergia al latex','2026-10-25 05:07:49','2026-10-28 05:04:47'),(12,6,3,2,3,NULL,4,NULL,'2026-10-25 16:00:00',30,'WHATSAPP','CONTROL','PROGRAMADA',NULL,NULL,NULL,'2026-10-25 05:07:49','2026-10-28 05:04:47'),(13,14,1,1,12,NULL,4,NULL,'2026-10-27 15:00:00',60,NULL,'TRATAMIENTO','PROGRAMADA',NULL,NULL,NULL,'2026-10-25 05:07:49','2026-10-28 05:04:47'),(14,7,3,2,4,NULL,4,NULL,'2026-10-28 15:30:00',40,'WHATSAPP','TRATAMIENTO','EN_ATENCION','2026-10-28 08:47:16',NULL,NULL,'2026-10-25 05:07:49','2026-10-28 17:04:22'),(15,5,2,1,5,NULL,4,NULL,'2026-11-01 10:00:00',60,NULL,'TRATAMIENTO','PROGRAMADA',NULL,NULL,'Sesion 3 del plan de tratamiento','2026-10-25 05:07:49','2026-10-28 05:04:47'),(16,11,2,1,11,NULL,4,NULL,'2026-10-29 11:00:00',30,'TELEFONO','CONTROL','REPROGRAMADA',NULL,NULL,'Reprogramada a pedido de la madre','2026-10-25 05:07:49','2026-10-28 05:04:47'),(17,11,2,1,11,16,4,NULL,'2026-10-31 11:00:00',30,'TELEFONO','CONTROL','PROGRAMADA',NULL,NULL,NULL,'2026-10-25 05:07:49','2026-10-28 05:04:47'),(18,5,2,1,1,NULL,4,NULL,'2026-10-11 09:00:00',30,NULL,'CONSULTA','ATENDIDA','2026-10-11 08:50:00',NULL,NULL,'2026-10-25 05:14:05','2026-10-28 05:04:47'),(19,6,2,1,2,NULL,4,NULL,'2026-10-15 10:00:00',45,'TELEFONO','CONTROL','ATENDIDA','2026-10-15 09:58:00',NULL,NULL,'2026-10-25 05:14:05','2026-10-28 05:04:47'),(20,7,3,2,4,NULL,4,NULL,'2026-10-18 15:00:00',40,'WHATSAPP','TRATAMIENTO','ATENDIDA','2026-10-18 14:55:00',NULL,'Paciente alergico a la penicilina','2026-10-25 05:14:05','2026-10-28 05:04:47'),(21,8,2,1,6,NULL,4,NULL,'2026-10-20 11:00:00',60,NULL,'TRATAMIENTO','ATENDIDA','2026-10-20 10:50:00',NULL,NULL,'2026-10-25 05:14:05','2026-10-28 05:04:47'),(22,5,2,1,7,1,4,NULL,'2026-10-22 09:30:00',90,NULL,'TRATAMIENTO','ATENDIDA','2026-10-22 09:25:00',NULL,'Sesion 1 del plan de tratamiento','2026-10-25 05:14:05','2026-10-28 05:04:47'),(23,9,3,2,8,NULL,4,4,'2026-10-23 16:00:00',40,'TELEFONO','TRATAMIENTO','CANCELADA',NULL,'El paciente indico que no podra asistir por motivos laborales',NULL,'2026-10-25 05:14:05','2026-10-28 05:04:47'),(24,10,2,1,1,NULL,4,NULL,'2026-10-24 10:30:00',30,'WHATSAPP','CONSULTA','AUSENTE',NULL,NULL,'No se presento ni aviso','2026-10-25 05:14:05','2026-10-28 05:04:47'),(25,11,3,2,11,NULL,4,NULL,'2026-10-24 17:00:00',30,NULL,'CONTROL','ATENDIDA','2026-10-24 16:55:00',NULL,NULL,'2026-10-25 05:14:05','2026-10-28 05:04:47'),(26,12,2,1,1,NULL,4,NULL,'2026-10-25 09:00:00',30,NULL,'CONSULTA','CONFIRMADA',NULL,NULL,NULL,'2026-10-25 05:14:05','2026-10-28 05:04:47'),(27,5,2,1,5,NULL,4,NULL,'2026-10-25 10:00:00',60,NULL,'TRATAMIENTO','EN_ESPERA','2026-10-25 09:50:00',NULL,'Sesion 2 del plan de tratamiento','2026-10-25 05:14:05','2026-10-28 05:04:47'),(28,13,3,2,2,NULL,4,NULL,'2026-10-25 15:00:00',45,'TELEFONO','CONTROL','EN_ATENCION','2026-10-25 14:50:00',NULL,'Alergia al latex','2026-10-25 05:14:05','2026-10-28 05:04:47'),(29,6,3,2,3,NULL,4,NULL,'2026-10-25 16:00:00',30,'WHATSAPP','CONTROL','PROGRAMADA',NULL,NULL,NULL,'2026-10-25 05:14:05','2026-10-28 05:04:47'),(30,14,1,1,12,NULL,4,NULL,'2026-10-27 15:00:00',60,NULL,'TRATAMIENTO','PROGRAMADA',NULL,NULL,NULL,'2026-10-25 05:14:05','2026-10-28 05:04:47'),(31,7,3,2,4,NULL,4,NULL,'2026-10-28 15:30:00',40,'WHATSAPP','TRATAMIENTO','EN_ESPERA','2026-10-28 08:48:47',NULL,NULL,'2026-10-25 05:14:05','2026-10-28 13:48:47'),(32,5,2,1,5,NULL,4,NULL,'2026-11-01 10:00:00',60,NULL,'TRATAMIENTO','PROGRAMADA',NULL,NULL,'Sesion 3 del plan de tratamiento','2026-10-25 05:14:05','2026-10-28 05:04:47'),(33,11,2,1,11,NULL,4,NULL,'2026-10-29 11:00:00',30,'TELEFONO','CONTROL','REPROGRAMADA',NULL,NULL,'Reprogramada a pedido de la madre','2026-10-25 05:14:05','2026-10-28 05:04:47'),(34,11,2,1,11,16,4,NULL,'2026-10-31 11:00:00',30,'TELEFONO','CONTROL','PROGRAMADA',NULL,NULL,NULL,'2026-10-25 05:14:05','2026-10-28 05:04:47'),(35,16,2,1,NULL,NULL,4,NULL,'2026-10-28 08:30:00',30,'VENTANILLA','CONTROL','ATENDIDA','2026-10-28 10:47:55',NULL,NULL,'2026-10-28 05:51:34','2026-10-28 16:02:03'),(36,16,2,1,NULL,NULL,4,NULL,'2026-10-28 10:00:00',30,'VENTANILLA','CONSULTA','EN_ESPERA','2026-10-28 10:47:58',NULL,NULL,'2026-10-28 13:51:27','2026-10-28 15:47:58'),(37,16,7,1,NULL,NULL,4,NULL,'2026-10-29 10:00:00',30,'VENTANILLA','CONSULTA','PROGRAMADA',NULL,NULL,NULL,'2026-10-28 16:11:08','2026-10-28 16:11:08');
/*!40000 ALTER TABLE `citas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `configuracion_clinica`
--

DROP TABLE IF EXISTS `configuracion_clinica`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `configuracion_clinica` (
  `id_configuracion` tinyint unsigned NOT NULL DEFAULT '1',
  `moneda` char(3) COLLATE utf8mb4_unicode_ci NOT NULL,
  `hora_limite_reserva_remota` time NOT NULL,
  `dias_bloqueo_infraccion` smallint unsigned NOT NULL DEFAULT '0',
  `tolerancia_llegada_minutos` smallint unsigned NOT NULL DEFAULT '0',
  `horas_limite_cancelar_cambiar` smallint unsigned NOT NULL DEFAULT '0',
  `margen_limpieza_minutos` smallint unsigned NOT NULL DEFAULT '0',
  `modificado_por_usuario` bigint unsigned DEFAULT NULL,
  `actualizado_en` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_configuracion`),
  KEY `fk_configuracion_usuario` (`modificado_por_usuario`),
  CONSTRAINT `fk_configuracion_usuario` FOREIGN KEY (`modificado_por_usuario`) REFERENCES `usuarios` (`id_usuario`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `chk_configuracion_unica` CHECK ((`id_configuracion` = 1))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `configuracion_clinica`
--

LOCK TABLES `configuracion_clinica` WRITE;
/*!40000 ALTER TABLE `configuracion_clinica` DISABLE KEYS */;
INSERT INTO `configuracion_clinica` VALUES (1,'PEN','18:00:00',15,15,3,10,1,'2026-10-28 15:30:31');
/*!40000 ALTER TABLE `configuracion_clinica` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `consultorios`
--

DROP TABLE IF EXISTS `consultorios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `consultorios` (
  `id_consultorio` smallint unsigned NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `estado` enum('ACTIVO','INACTIVO') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'ACTIVO',
  PRIMARY KEY (`id_consultorio`),
  UNIQUE KEY `uq_consultorios_nombre` (`nombre`),
  KEY `idx_consultorios_estado` (`estado`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `consultorios`
--

LOCK TABLES `consultorios` WRITE;
/*!40000 ALTER TABLE `consultorios` DISABLE KEYS */;
INSERT INTO `consultorios` VALUES (1,'Consultorio 1','ACTIVO'),(2,'Consultorio 2','ACTIVO'),(3,'Sala de Rayos X','INACTIVO');
/*!40000 ALTER TABLE `consultorios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cuotas_plan`
--

DROP TABLE IF EXISTS `cuotas_plan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cuotas_plan` (
  `id_cuota` bigint unsigned NOT NULL AUTO_INCREMENT,
  `id_plan` bigint unsigned NOT NULL,
  `numero_cuota` smallint unsigned NOT NULL,
  `fecha_vencimiento` date DEFAULT NULL,
  `monto` decimal(10,2) NOT NULL,
  `estado` enum('PENDIENTE','PARCIAL','PAGADA') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'PENDIENTE',
  PRIMARY KEY (`id_cuota`),
  UNIQUE KEY `uq_cuotas_plan_numero` (`id_plan`,`numero_cuota`),
  KEY `idx_cuotas_estado_vencimiento` (`estado`,`fecha_vencimiento`),
  CONSTRAINT `fk_cuotas_plan` FOREIGN KEY (`id_plan`) REFERENCES `planes_tratamiento` (`id_plan`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `chk_cuotas_monto` CHECK ((`monto` > 0)),
  CONSTRAINT `chk_cuotas_numero` CHECK ((`numero_cuota` > 0))
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cuotas_plan`
--

LOCK TABLES `cuotas_plan` WRITE;
/*!40000 ALTER TABLE `cuotas_plan` DISABLE KEYS */;
INSERT INTO `cuotas_plan` VALUES (1,1,1,'2026-10-22',300.00,'PAGADA'),(2,1,2,'2026-10-27',300.00,'PARCIAL'),(3,1,3,'2026-11-08',230.00,'PENDIENTE'),(5,2,1,'2026-11-24',155.00,'PENDIENTE'),(6,2,2,'2026-12-24',155.00,'PENDIENTE'),(7,2,3,'2027-01-23',155.00,'PENDIENTE'),(8,2,4,'2027-02-22',155.00,'PENDIENTE'),(9,2,5,'2027-03-24',155.00,'PENDIENTE'),(10,2,6,'2027-04-23',155.00,'PENDIENTE'),(11,2,7,'2027-05-23',155.00,'PENDIENTE'),(12,2,8,'2027-06-22',155.00,'PENDIENTE'),(13,2,9,'2027-07-22',155.00,'PENDIENTE'),(14,2,10,'2027-08-21',155.00,'PENDIENTE'),(15,2,11,'2027-09-20',155.00,'PENDIENTE'),(16,2,12,'2027-10-20',155.00,'PENDIENTE');
/*!40000 ALTER TABLE `cuotas_plan` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `diagnosticos`
--

DROP TABLE IF EXISTS `diagnosticos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `diagnosticos` (
  `id_diagnostico` int unsigned NOT NULL AUTO_INCREMENT,
  `codigo` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `nombre` varchar(180) COLLATE utf8mb4_unicode_ci NOT NULL,
  `descripcion` text COLLATE utf8mb4_unicode_ci,
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id_diagnostico`),
  UNIQUE KEY `uq_diagnosticos_nombre` (`nombre`),
  UNIQUE KEY `uq_diagnosticos_codigo` (`codigo`),
  KEY `idx_diagnosticos_activo` (`activo`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `diagnosticos`
--

LOCK TABLES `diagnosticos` WRITE;
/*!40000 ALTER TABLE `diagnosticos` DISABLE KEYS */;
INSERT INTO `diagnosticos` VALUES (1,'K02.0','Caries limitada al esmalte','Lesion cariosa que no supera el esmalte',1),(2,'K02.1','Caries de la dentina','Lesion cariosa que alcanza la dentina',1),(3,'K04.0','Pulpitis','Inflamacion de la pulpa dental',1),(4,'K04.5','Periodontitis apical cronica','Lesion periapical de origen pulpar',1),(5,'K05.1','Gingivitis cronica','Inflamacion de la encia por placa bacteriana',1),(6,'K03.6','Depositos sobre los dientes (sarro)','Calculo dental supragingival o subgingival',1),(7,'K01.1','Diente impactado','Pieza que no logra erupcionar',1),(8,'M26.3','Anomalia de la posicion del diente','Apinamiento o rotaciones dentarias',1);
/*!40000 ALTER TABLE `diagnosticos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `diagnosticos_atencion`
--

DROP TABLE IF EXISTS `diagnosticos_atencion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `diagnosticos_atencion` (
  `id_diagnostico_atencion` bigint unsigned NOT NULL AUTO_INCREMENT,
  `id_atencion` bigint unsigned NOT NULL,
  `id_diagnostico` int unsigned NOT NULL,
  `tipo` enum('GENERAL','PIEZA') COLLATE utf8mb4_unicode_ci NOT NULL,
  `pieza_fdi` char(2) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `observaciones` text COLLATE utf8mb4_unicode_ci,
  `creado_en` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_diagnostico_atencion`),
  KEY `idx_diagnosticos_atencion` (`id_atencion`,`tipo`,`pieza_fdi`),
  KEY `fk_diagnosticos_atencion_catalogo` (`id_diagnostico`),
  CONSTRAINT `fk_diagnosticos_atencion_atencion` FOREIGN KEY (`id_atencion`) REFERENCES `atenciones` (`id_atencion`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_diagnosticos_atencion_catalogo` FOREIGN KEY (`id_diagnostico`) REFERENCES `diagnosticos` (`id_diagnostico`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `chk_diagnostico_pieza_fdi` CHECK (((`pieza_fdi` is null) or (`pieza_fdi` in (_utf8mb4'11',_utf8mb4'12',_utf8mb4'13',_utf8mb4'14',_utf8mb4'15',_utf8mb4'16',_utf8mb4'17',_utf8mb4'18',_utf8mb4'21',_utf8mb4'22',_utf8mb4'23',_utf8mb4'24',_utf8mb4'25',_utf8mb4'26',_utf8mb4'27',_utf8mb4'28',_utf8mb4'31',_utf8mb4'32',_utf8mb4'33',_utf8mb4'34',_utf8mb4'35',_utf8mb4'36',_utf8mb4'37',_utf8mb4'38',_utf8mb4'41',_utf8mb4'42',_utf8mb4'43',_utf8mb4'44',_utf8mb4'45',_utf8mb4'46',_utf8mb4'47',_utf8mb4'48')))),
  CONSTRAINT `chk_diagnostico_tipo_pieza` CHECK ((((`tipo` = _utf8mb4'GENERAL') and (`pieza_fdi` is null)) or ((`tipo` = _utf8mb4'PIEZA') and (`pieza_fdi` is not null))))
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `diagnosticos_atencion`
--

LOCK TABLES `diagnosticos_atencion` WRITE;
/*!40000 ALTER TABLE `diagnosticos_atencion` DISABLE KEYS */;
INSERT INTO `diagnosticos_atencion` VALUES (1,1,5,'GENERAL',NULL,'Encias inflamadas en ambas arcadas','2026-10-25 05:07:49'),(2,1,3,'PIEZA','46','Caries profunda con compromiso pulpar','2026-10-25 05:07:49'),(3,1,2,'PIEZA','26','Caries en superficie mesial','2026-10-25 05:07:49'),(4,2,6,'GENERAL',NULL,'Sarro supragingival moderado','2026-10-25 05:07:49'),(5,3,2,'PIEZA','14','Caries oclusodistal','2026-10-25 05:07:49'),(6,4,4,'PIEZA','21','Lesion periapical visible en radiografia','2026-10-25 05:07:49'),(7,5,3,'PIEZA','46','Pulpitis irreversible','2026-10-25 05:07:49'),(8,6,8,'GENERAL',NULL,'Apinamiento anteroinferior','2026-10-25 05:07:49'),(9,7,6,'GENERAL',NULL,'Sarro en cara lingual de incisivos inferiores','2026-10-25 05:07:49'),(10,1,5,'GENERAL',NULL,'Encias inflamadas en ambas arcadas','2026-10-25 05:14:05'),(11,1,3,'PIEZA','46','Caries profunda con compromiso pulpar','2026-10-25 05:14:05'),(12,1,2,'PIEZA','26','Caries en superficie mesial','2026-10-25 05:14:05'),(13,2,6,'GENERAL',NULL,'Sarro supragingival moderado','2026-10-25 05:14:05'),(14,3,2,'PIEZA','14','Caries oclusodistal','2026-10-25 05:14:05'),(15,4,4,'PIEZA','21','Lesion periapical visible en radiografia','2026-10-25 05:14:05'),(16,5,3,'PIEZA','46','Pulpitis irreversible','2026-10-25 05:14:05'),(17,6,8,'GENERAL',NULL,'Apinamiento anteroinferior','2026-10-25 05:14:05'),(18,7,6,'GENERAL',NULL,'Sarro en cara lingual de incisivos inferiores','2026-10-25 05:14:05'),(19,9,2,'PIEZA','15',NULL,'2026-10-28 15:59:39');
/*!40000 ALTER TABLE `diagnosticos_atencion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `disponibilidad_habitual`
--

DROP TABLE IF EXISTS `disponibilidad_habitual`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `disponibilidad_habitual` (
  `id_disponibilidad` bigint unsigned NOT NULL AUTO_INCREMENT,
  `id_odontologo` bigint unsigned NOT NULL,
  `id_consultorio` smallint unsigned NOT NULL,
  `dia_semana` tinyint unsigned NOT NULL,
  `hora_inicio` time NOT NULL,
  `hora_fin` time NOT NULL,
  `maximo_citas` smallint unsigned NOT NULL DEFAULT '1',
  `vigente_desde` date NOT NULL,
  `vigente_hasta` date DEFAULT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id_disponibilidad`),
  KEY `idx_disponibilidad_agenda` (`id_odontologo`,`dia_semana`,`hora_inicio`,`hora_fin`),
  KEY `idx_disponibilidad_consultorio` (`id_consultorio`,`dia_semana`),
  CONSTRAINT `fk_disponibilidad_consultorio` FOREIGN KEY (`id_consultorio`) REFERENCES `consultorios` (`id_consultorio`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_disponibilidad_odontologo` FOREIGN KEY (`id_odontologo`) REFERENCES `odontologos` (`id_usuario`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `chk_disponibilidad_dia` CHECK ((`dia_semana` between 1 and 7)),
  CONSTRAINT `chk_disponibilidad_horas` CHECK ((`hora_fin` > `hora_inicio`)),
  CONSTRAINT `chk_disponibilidad_maximo` CHECK ((`maximo_citas` > 0)),
  CONSTRAINT `chk_disponibilidad_vigencia` CHECK (((`vigente_hasta` is null) or (`vigente_hasta` >= `vigente_desde`)))
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `disponibilidad_habitual`
--

LOCK TABLES `disponibilidad_habitual` WRITE;
/*!40000 ALTER TABLE `disponibilidad_habitual` DISABLE KEYS */;
INSERT INTO `disponibilidad_habitual` VALUES (1,2,1,1,'08:00:00','13:00:00',8,'2025-01-01',NULL,0),(2,2,1,2,'08:00:00','13:00:00',8,'2025-01-01',NULL,0),(3,2,1,3,'08:00:00','13:00:00',8,'2025-01-01',NULL,0),(4,2,1,4,'08:00:00','13:00:00',8,'2025-01-01',NULL,0),(5,2,1,5,'08:00:00','13:00:00',8,'2025-01-01',NULL,0),(6,2,1,6,'08:00:00','13:00:00',8,'2025-01-01',NULL,0),(7,3,2,1,'14:00:00','19:00:00',8,'2025-01-01',NULL,0),(8,3,2,2,'14:00:00','19:00:00',8,'2025-01-01',NULL,0),(9,3,2,3,'14:00:00','19:00:00',8,'2025-01-01',NULL,0),(10,3,2,4,'14:00:00','19:00:00',8,'2025-01-01',NULL,0),(11,3,2,5,'14:00:00','19:00:00',8,'2025-01-01',NULL,0),(12,3,2,6,'14:00:00','19:00:00',8,'2025-01-01',NULL,0),(13,1,1,1,'14:00:00','18:00:00',6,'2025-01-01',NULL,0),(14,1,1,2,'14:00:00','18:00:00',6,'2025-01-01',NULL,0),(15,1,1,3,'14:00:00','18:00:00',6,'2025-01-01',NULL,0),(16,1,1,4,'14:00:00','18:00:00',6,'2025-01-01',NULL,0),(17,1,1,5,'14:00:00','18:00:00',6,'2025-01-01',NULL,0),(18,1,1,6,'14:00:00','18:00:00',6,'2025-01-01',NULL,0),(19,2,1,1,'08:00:00','13:00:00',8,'2025-01-01',NULL,0),(20,2,1,2,'08:00:00','13:00:00',8,'2025-01-01',NULL,0),(21,2,1,3,'08:00:00','13:00:00',8,'2025-01-01',NULL,0),(22,2,1,4,'08:00:00','13:00:00',8,'2025-01-01',NULL,0),(23,2,1,5,'08:00:00','13:00:00',8,'2025-01-01',NULL,0),(24,2,1,6,'08:00:00','13:00:00',8,'2025-01-01',NULL,0),(25,3,2,1,'14:00:00','19:00:00',8,'2025-01-01',NULL,0),(26,3,2,2,'14:00:00','19:00:00',8,'2025-01-01',NULL,0),(27,3,2,3,'14:00:00','19:00:00',8,'2025-01-01',NULL,0),(28,3,2,4,'14:00:00','19:00:00',8,'2025-01-01',NULL,0),(29,3,2,5,'14:00:00','19:00:00',8,'2025-01-01',NULL,0),(30,3,2,6,'14:00:00','19:00:00',8,'2025-01-01',NULL,0),(31,1,1,1,'14:00:00','18:00:00',6,'2025-01-01',NULL,0),(32,1,1,2,'14:00:00','18:00:00',6,'2025-01-01',NULL,0),(33,1,1,3,'14:00:00','18:00:00',6,'2025-01-01',NULL,0),(34,1,1,4,'14:00:00','18:00:00',6,'2025-01-01',NULL,0),(35,1,1,5,'14:00:00','18:00:00',6,'2025-01-01',NULL,0),(36,1,1,6,'14:00:00','18:00:00',6,'2025-01-01',NULL,0),(37,1,1,7,'07:00:00','21:00:00',8,'2026-10-27',NULL,0),(38,3,2,7,'09:00:00','12:30:00',5,'2026-10-27',NULL,0),(39,6,1,7,'09:00:00','18:00:00',5,'2026-10-28',NULL,0),(40,7,1,6,'09:00:00','13:00:00',5,'2026-10-31',NULL,1),(41,6,2,2,'09:00:00','14:00:00',5,'2026-10-28',NULL,0),(42,7,1,4,'08:00:00','18:00:00',5,'2026-10-29',NULL,1),(43,7,1,5,'09:00:00','18:00:00',5,'2026-10-30',NULL,1);
/*!40000 ALTER TABLE `disponibilidad_habitual` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `excepciones_agenda`
--

DROP TABLE IF EXISTS `excepciones_agenda`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `excepciones_agenda` (
  `id_excepcion` bigint unsigned NOT NULL AUTO_INCREMENT,
  `id_odontologo` bigint unsigned NOT NULL,
  `tipo` enum('BLOQUEO','HORARIO_ESPECIAL') COLLATE utf8mb4_unicode_ci NOT NULL,
  `fecha_hora_inicio` datetime NOT NULL,
  `fecha_hora_fin` datetime NOT NULL,
  `motivo` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `estado` enum('ACTIVA','ANULADA') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'ACTIVA',
  `creado_por_usuario` bigint unsigned DEFAULT NULL,
  `creado_en` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_excepcion`),
  KEY `idx_excepciones_odontologo_fecha` (`id_odontologo`,`fecha_hora_inicio`,`estado`),
  KEY `fk_excepciones_creado_por` (`creado_por_usuario`),
  CONSTRAINT `fk_excepciones_creado_por` FOREIGN KEY (`creado_por_usuario`) REFERENCES `usuarios` (`id_usuario`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_excepciones_odontologo` FOREIGN KEY (`id_odontologo`) REFERENCES `odontologos` (`id_usuario`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `chk_excepciones_rango` CHECK ((`fecha_hora_fin` > `fecha_hora_inicio`))
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `excepciones_agenda`
--

LOCK TABLES `excepciones_agenda` WRITE;
/*!40000 ALTER TABLE `excepciones_agenda` DISABLE KEYS */;
INSERT INTO `excepciones_agenda` VALUES (1,2,'BLOQUEO','2026-10-30 08:00:00','2026-10-30 13:00:00','Capacitacion en implantologia','ACTIVA',1,'2026-10-25 05:07:49'),(2,3,'HORARIO_ESPECIAL','2026-11-02 09:00:00','2026-11-02 13:00:00','Campana de salud oral','ACTIVA',1,'2026-10-25 05:07:49'),(3,2,'BLOQUEO','2026-10-30 08:00:00','2026-10-30 13:00:00','Capacitacion en implantologia','ACTIVA',1,'2026-10-25 05:14:05'),(4,3,'HORARIO_ESPECIAL','2026-11-02 09:00:00','2026-11-02 13:00:00','Campana de salud oral','ACTIVA',1,'2026-10-25 05:14:05'),(5,6,'BLOQUEO','2026-10-05 04:00:00','2026-10-13 01:00:00','no se xd','ACTIVA',1,'2026-10-28 04:13:50');
/*!40000 ALTER TABLE `excepciones_agenda` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hallazgos_odontograma`
--

DROP TABLE IF EXISTS `hallazgos_odontograma`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hallazgos_odontograma` (
  `id_hallazgo` bigint unsigned NOT NULL AUTO_INCREMENT,
  `id_atencion` bigint unsigned NOT NULL,
  `pieza_fdi` char(2) COLLATE utf8mb4_unicode_ci NOT NULL,
  `superficie` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `hallazgo` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `creado_en` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_hallazgo`),
  KEY `idx_hallazgos_atencion_pieza` (`id_atencion`,`pieza_fdi`),
  CONSTRAINT `fk_hallazgos_atencion` FOREIGN KEY (`id_atencion`) REFERENCES `atenciones` (`id_atencion`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `chk_hallazgos_pieza_fdi` CHECK ((`pieza_fdi` in (_utf8mb4'11',_utf8mb4'12',_utf8mb4'13',_utf8mb4'14',_utf8mb4'15',_utf8mb4'16',_utf8mb4'17',_utf8mb4'18',_utf8mb4'21',_utf8mb4'22',_utf8mb4'23',_utf8mb4'24',_utf8mb4'25',_utf8mb4'26',_utf8mb4'27',_utf8mb4'28',_utf8mb4'31',_utf8mb4'32',_utf8mb4'33',_utf8mb4'34',_utf8mb4'35',_utf8mb4'36',_utf8mb4'37',_utf8mb4'38',_utf8mb4'41',_utf8mb4'42',_utf8mb4'43',_utf8mb4'44',_utf8mb4'45',_utf8mb4'46',_utf8mb4'47',_utf8mb4'48')))
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hallazgos_odontograma`
--

LOCK TABLES `hallazgos_odontograma` WRITE;
/*!40000 ALTER TABLE `hallazgos_odontograma` DISABLE KEYS */;
INSERT INTO `hallazgos_odontograma` VALUES (1,1,'46','Oclusal','Caries profunda con compromiso pulpar','2026-10-25 05:07:49'),(2,1,'26','Mesial','Caries de dentina','2026-10-25 05:07:49'),(3,1,'16','Oclusal','Restauracion en resina en buen estado','2026-10-25 05:07:49'),(4,1,'18',NULL,'Pieza ausente','2026-10-25 05:07:49'),(5,2,'36','Oclusal','Sellante presente','2026-10-25 05:07:49'),(6,3,'14','Oclusal-Distal','Caries de dentina','2026-10-25 05:07:49'),(7,4,'21',NULL,'Cambio de coloracion y lesion periapical','2026-10-25 05:07:49'),(8,5,'46','Oclusal','Acceso cameral realizado','2026-10-25 05:07:49'),(9,6,'32',NULL,'Rotacion leve','2026-10-25 05:07:49'),(10,6,'42',NULL,'Rotacion leve','2026-10-25 05:07:49'),(11,7,'31','Lingual','Calculo supragingival','2026-10-25 05:07:49'),(12,7,'41','Lingual','Calculo supragingival','2026-10-25 05:07:49'),(13,1,'46','Oclusal','Caries profunda con compromiso pulpar','2026-10-25 05:14:05'),(14,1,'26','Mesial','Caries de dentina','2026-10-25 05:14:05'),(15,1,'16','Oclusal','Restauracion en resina en buen estado','2026-10-25 05:14:05'),(16,1,'18',NULL,'Pieza ausente','2026-10-25 05:14:05'),(17,2,'36','Oclusal','Sellante presente','2026-10-25 05:14:05'),(18,3,'14','Oclusal-Distal','Caries de dentina','2026-10-25 05:14:05'),(19,4,'21',NULL,'Cambio de coloracion y lesion periapical','2026-10-25 05:14:05'),(20,5,'46','Oclusal','Acceso cameral realizado','2026-10-25 05:14:05'),(21,6,'32',NULL,'Rotacion leve','2026-10-25 05:14:05'),(22,6,'42',NULL,'Rotacion leve','2026-10-25 05:14:05'),(23,7,'31','Lingual','Calculo supragingival','2026-10-25 05:14:05'),(24,7,'41','Lingual','Calculo supragingival','2026-10-25 05:14:05'),(25,9,'15','Oclusal','en mal estado encontrada','2026-10-28 15:59:39');
/*!40000 ALTER TABLE `hallazgos_odontograma` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `odontologos`
--

DROP TABLE IF EXISTS `odontologos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `odontologos` (
  `id_usuario` bigint unsigned NOT NULL,
  `numero_colegiatura` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id_usuario`),
  UNIQUE KEY `uq_odontologos_colegiatura` (`numero_colegiatura`),
  CONSTRAINT `fk_odontologos_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `odontologos`
--

LOCK TABLES `odontologos` WRITE;
/*!40000 ALTER TABLE `odontologos` DISABLE KEYS */;
INSERT INTO `odontologos` VALUES (7,'COL-6545968'),(1,'COP-18452'),(2,'COP-27619'),(3,'COP-31054'),(6,'PML-15265');
/*!40000 ALTER TABLE `odontologos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pacientes`
--

DROP TABLE IF EXISTS `pacientes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pacientes` (
  `id_persona` bigint unsigned NOT NULL,
  `documento_tutor` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `estado` enum('ACTIVO','INACTIVO') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'ACTIVO',
  `creado_en` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `actualizado_en` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_persona`),
  KEY `idx_pacientes_estado` (`estado`),
  CONSTRAINT `fk_pacientes_persona` FOREIGN KEY (`id_persona`) REFERENCES `personas` (`id_persona`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pacientes`
--

LOCK TABLES `pacientes` WRITE;
/*!40000 ALTER TABLE `pacientes` DISABLE KEYS */;
INSERT INTO `pacientes` VALUES (5,NULL,'ACTIVO','2026-10-25 05:07:49','2026-10-25 05:07:49'),(6,NULL,'ACTIVO','2026-10-25 05:07:49','2026-10-25 05:07:49'),(7,'','INACTIVO','2026-10-25 05:07:49','2026-10-26 00:00:21'),(8,NULL,'ACTIVO','2026-10-25 05:07:49','2026-10-25 05:07:49'),(9,'','INACTIVO','2026-10-25 05:07:49','2026-10-28 04:01:07'),(10,NULL,'ACTIVO','2026-10-25 05:07:49','2026-10-25 05:07:49'),(11,'41236587','INACTIVO','2026-10-25 05:07:49','2026-10-26 05:02:30'),(12,NULL,'ACTIVO','2026-10-25 05:07:49','2026-10-25 05:07:49'),(13,NULL,'ACTIVO','2026-10-25 05:07:49','2026-10-25 05:07:49'),(14,NULL,'ACTIVO','2026-10-25 05:07:49','2026-10-25 05:07:49'),(16,'','ACTIVO','2026-10-26 00:01:41','2026-10-26 00:01:41');
/*!40000 ALTER TABLE `pacientes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pagos`
--

DROP TABLE IF EXISTS `pagos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pagos` (
  `id_pago` bigint unsigned NOT NULL AUTO_INCREMENT,
  `id_usuario_registra` bigint unsigned DEFAULT NULL,
  `id_cita` bigint unsigned DEFAULT NULL,
  `id_cuota` bigint unsigned DEFAULT NULL,
  `fecha_pago` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `monto` decimal(10,2) NOT NULL,
  `metodo_pago` enum('EFECTIVO','TARJETA','TRANSFERENCIA','BILLETERA_DIGITAL','OTRO') COLLATE utf8mb4_unicode_ci NOT NULL,
  `referencia` varchar(120) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `estado` enum('REGISTRADO','ANULADO') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'REGISTRADO',
  `observaciones` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id_pago`),
  KEY `idx_pagos_fecha_estado` (`fecha_pago`,`estado`),
  KEY `idx_pagos_cita` (`id_cita`),
  KEY `idx_pagos_cuota` (`id_cuota`),
  KEY `fk_pagos_usuario` (`id_usuario_registra`),
  CONSTRAINT `fk_pagos_cita` FOREIGN KEY (`id_cita`) REFERENCES `citas` (`id_cita`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_pagos_cuota` FOREIGN KEY (`id_cuota`) REFERENCES `cuotas_plan` (`id_cuota`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_pagos_usuario` FOREIGN KEY (`id_usuario_registra`) REFERENCES `usuarios` (`id_usuario`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `chk_pagos_monto` CHECK ((`monto` > 0)),
  CONSTRAINT `chk_pagos_un_destino` CHECK ((((`id_cita` is not null) and (`id_cuota` is null)) or ((`id_cita` is null) and (`id_cuota` is not null))))
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pagos`
--

LOCK TABLES `pagos` WRITE;
/*!40000 ALTER TABLE `pagos` DISABLE KEYS */;
INSERT INTO `pagos` VALUES (1,4,1,NULL,'2026-10-11 09:45:00',40.00,'EFECTIVO',NULL,'REGISTRADO',NULL),(2,4,2,NULL,'2026-10-15 10:50:00',80.00,'BILLETERA_DIGITAL','Yape 2910457','REGISTRADO',NULL),(3,4,3,NULL,'2026-10-18 15:45:00',90.00,'TARJETA','POS-004512','REGISTRADO',NULL),(4,4,4,NULL,'2026-10-20 12:10:00',350.00,'TRANSFERENCIA','BCP-0091823','REGISTRADO',NULL),(5,4,8,NULL,'2026-10-24 17:35:00',100.00,'EFECTIVO',NULL,'REGISTRADO',NULL),(6,4,NULL,1,'2026-10-22 11:10:00',300.00,'EFECTIVO',NULL,'REGISTRADO',NULL),(7,4,NULL,2,'2026-10-24 12:00:00',150.00,'BILLETERA_DIGITAL','Plin 7741203','REGISTRADO',NULL),(8,4,7,NULL,'2026-10-24 10:40:00',40.00,'EFECTIVO',NULL,'ANULADO','Registrado por error: el paciente no asistio'),(9,4,1,NULL,'2026-10-11 09:45:00',40.00,'EFECTIVO',NULL,'REGISTRADO',NULL),(10,4,2,NULL,'2026-10-15 10:50:00',80.00,'BILLETERA_DIGITAL','Yape 2910457','REGISTRADO',NULL),(11,4,3,NULL,'2026-10-18 15:45:00',90.00,'TARJETA','POS-004512','REGISTRADO',NULL),(12,4,4,NULL,'2026-10-20 12:10:00',350.00,'TRANSFERENCIA','BCP-0091823','REGISTRADO',NULL),(13,4,8,NULL,'2026-10-24 17:35:00',100.00,'EFECTIVO',NULL,'REGISTRADO',NULL),(14,4,NULL,1,'2026-10-22 11:10:00',300.00,'EFECTIVO',NULL,'REGISTRADO',NULL),(15,4,NULL,2,'2026-10-24 12:00:00',150.00,'BILLETERA_DIGITAL','Plin 7741203','REGISTRADO',NULL),(16,4,7,NULL,'2026-10-24 10:40:00',40.00,'EFECTIVO',NULL,'ANULADO','Registrado por error: el paciente no asistio'),(17,4,18,NULL,'2026-10-25 19:04:23',40.00,'TARJETA',NULL,'REGISTRADO','Pago registrado desde caja.'),(18,4,21,NULL,'2026-10-25 21:11:42',400.00,'EFECTIVO',NULL,'REGISTRADO','Pago registrado desde caja.'),(19,4,19,NULL,'2026-10-28 10:47:13',80.00,'EFECTIVO',NULL,'REGISTRADO','Pago registrado desde caja.');
/*!40000 ALTER TABLE `pagos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `personas`
--

DROP TABLE IF EXISTS `personas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `personas` (
  `id_persona` bigint unsigned NOT NULL AUTO_INCREMENT,
  `tipo_documento` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
  `numero_documento` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
  `nombres` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `apellido_paterno` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `apellido_materno` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `fecha_nacimiento` date DEFAULT NULL,
  `telefono` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `correo` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `direccion` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `creado_en` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `actualizado_en` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_persona`),
  UNIQUE KEY `uq_personas_documento` (`tipo_documento`,`numero_documento`),
  KEY `idx_personas_apellidos` (`apellido_paterno`,`apellido_materno`,`nombres`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `personas`
--

LOCK TABLES `personas` WRITE;
/*!40000 ALTER TABLE `personas` DISABLE KEYS */;
INSERT INTO `personas` VALUES (1,'DNI','32945871','Marcela Isabel','Rivas','Delgado','1980-02-19','943215876','marcela.rivas@gmail.com','Av. Pardo 1180, Urb. Garatea, Nuevo Chimbote','2026-10-25 05:07:49','2026-10-25 05:07:49'),(2,'DNI','43127896','Jorge Luis','Paredes','Sanchez','1985-06-30','956402318','jorge.paredes@gmail.com','Jr. Elias Aguirre 412, Chimbote','2026-10-25 05:07:49','2026-10-25 05:07:49'),(3,'DNI','70458213','Katherine Milagros','Vega','Torres','1992-09-12','987654120','katherine.vega@gmail.com','Av. Country Club 560, Nuevo Chimbote','2026-10-25 05:07:49','2026-10-25 05:07:49'),(4,'DNI','71236845','Rosa Elvira','Campos','Huaman','1994-03-08','921874563','rosa.campos@gmail.com','Urb. Buenos Aires Mz. D Lt. 9, Nuevo Chimbote','2026-10-25 05:07:49','2026-10-25 05:07:49'),(5,'DNI','72834519','Luis Fernando','Quispe','Mendoza','1990-03-14','943218765','luis.quispe@gmail.com','Jr. Leoncio Prado 233, Chimbote','2026-10-25 05:07:49','2026-10-25 05:07:49'),(6,'DNI','45671289','Carmen Rosa','Salazar','Villanueva','1985-07-22','968340127','carmen.salazar@hotmail.com','Av. Industrial 874, Nuevo Chimbote','2026-10-25 05:07:49','2026-10-25 05:07:49'),(7,'DNI','40318765','Pedro Antonio','Ramirez','Cruz','1978-11-05','955120483','pedro.ramirez@gmail.com','Urb. Garatea Mz. B Lt. 12, Nuevo Chimbote','2026-10-25 05:07:49','2026-10-25 05:07:49'),(8,'DNI','74125896','Ana Lucia','Torres','Nique','1996-01-30','934782016','ana.torres@gmail.com','Jr. Manuel Ruiz 145, Chimbote','2026-10-25 05:07:49','2026-10-25 05:07:49'),(9,'DNI','32478105','Jose Manuel','Huaman','Flores','1969-09-18','976215430','joseM@gmail.com','AA.HH. Villa Maria Mz. K Lt. 3, Nuevo Chimbote','2026-10-25 05:07:49','2026-10-28 04:01:07'),(10,'DNI','75283914','Mariela Estefany','Caceres','Rojas','2001-05-09','912345678','mariela.caceres@gmail.com','Av. Los Pescadores 321, Chimbote','2026-10-25 05:07:49','2026-10-25 05:07:49'),(11,'DNI','78451296','Sebastian Alonso','Vega','Rivas','2012-08-17','945871230','familia.vega@gmail.com','Urb. El Trapecio Mz. F Lt. 7, Nuevo Chimbote','2026-10-25 05:07:49','2026-10-25 05:07:49'),(12,'DNI','41562387','Elena Patricia','Chavez','Linares','1982-12-03','962587413','elena.chavez@gmail.com','Av. Universitaria 1020, Nuevo Chimbote','2026-10-25 05:07:49','2026-10-25 05:07:49'),(13,'DNI','33894512','Raul Alberto','Mendoza','Paredes','1975-04-27','989123456','raul.mendoza@hotmail.com','Jr. Jose Galvez 678, Chimbote','2026-10-25 05:07:49','2026-10-25 05:07:49'),(14,'DNI','73562148','Daniela Alejandra','Rios','Castillo','1999-10-11','951478230','daniela.rios@gmail.com','Urb. Bellamar Mz. C Lt. 15, Nuevo Chimbote','2026-10-25 05:07:49','2026-10-25 05:07:49'),(16,'DNI','62161614','Juan','Flores','Silva','2006-10-10','945655453','Juan@gmail.com','Santa chimbote','2026-10-26 00:01:41','2026-10-26 00:01:41'),(17,'DNI','89545623','Juan','Quispe','Tapia','2003-10-07','912320151','Juan@gmail.com','UTP-Chimbote','2026-10-26 05:00:17','2026-10-26 05:00:17'),(18,'DNI','60610545','Rodrigo','Loyola','Juarez','2006-06-16','945516321','Rodrigo@gmail.com','Garatea','2026-10-28 15:29:50','2026-10-28 15:29:50');
/*!40000 ALTER TABLE `personas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `plan_tratamiento_detalles`
--

DROP TABLE IF EXISTS `plan_tratamiento_detalles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `plan_tratamiento_detalles` (
  `id_detalle_plan` bigint unsigned NOT NULL AUTO_INCREMENT,
  `id_plan` bigint unsigned NOT NULL,
  `id_servicio` int unsigned NOT NULL,
  `pieza_fdi` char(2) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `superficie` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `sesion_sugerida` smallint unsigned DEFAULT NULL,
  `cantidad` decimal(8,2) NOT NULL DEFAULT '1.00',
  `precio_unitario` decimal(10,2) NOT NULL,
  `observaciones` text COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`id_detalle_plan`),
  KEY `idx_detalles_plan` (`id_plan`),
  KEY `fk_detalles_plan_servicio` (`id_servicio`),
  CONSTRAINT `fk_detalles_plan_plan` FOREIGN KEY (`id_plan`) REFERENCES `planes_tratamiento` (`id_plan`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_detalles_plan_servicio` FOREIGN KEY (`id_servicio`) REFERENCES `servicios` (`id_servicio`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `chk_detalle_plan_cantidad` CHECK ((`cantidad` > 0)),
  CONSTRAINT `chk_detalle_plan_pieza_fdi` CHECK (((`pieza_fdi` is null) or (`pieza_fdi` in (_utf8mb4'11',_utf8mb4'12',_utf8mb4'13',_utf8mb4'14',_utf8mb4'15',_utf8mb4'16',_utf8mb4'17',_utf8mb4'18',_utf8mb4'21',_utf8mb4'22',_utf8mb4'23',_utf8mb4'24',_utf8mb4'25',_utf8mb4'26',_utf8mb4'27',_utf8mb4'28',_utf8mb4'31',_utf8mb4'32',_utf8mb4'33',_utf8mb4'34',_utf8mb4'35',_utf8mb4'36',_utf8mb4'37',_utf8mb4'38',_utf8mb4'41',_utf8mb4'42',_utf8mb4'43',_utf8mb4'44',_utf8mb4'45',_utf8mb4'46',_utf8mb4'47',_utf8mb4'48')))),
  CONSTRAINT `chk_detalle_plan_precio` CHECK ((`precio_unitario` >= 0)),
  CONSTRAINT `chk_detalle_plan_sesion` CHECK (((`sesion_sugerida` is null) or (`sesion_sugerida` > 0)))
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `plan_tratamiento_detalles`
--

LOCK TABLES `plan_tratamiento_detalles` WRITE;
/*!40000 ALTER TABLE `plan_tratamiento_detalles` DISABLE KEYS */;
INSERT INTO `plan_tratamiento_detalles` VALUES (1,1,7,'46','Oclusal',1,1.00,550.00,'Endodoncia de molar inferior derecho'),(2,1,5,'26','Mesial-Oclusal',2,1.00,140.00,'Restauracion de caries de dentina'),(3,1,5,'46','Oclusal',3,1.00,140.00,'Reconstruccion posterior a la endodoncia'),(4,2,10,NULL,NULL,1,1.00,900.00,'Instalacion de brackets metalicos'),(5,2,13,NULL,NULL,1,1.00,30.00,'Radiografia previa al tratamiento'),(6,1,7,'46','Oclusal',1,1.00,550.00,'Endodoncia de molar inferior derecho'),(7,1,5,'26','Mesial-Oclusal',2,1.00,140.00,'Restauracion de caries de dentina'),(8,1,5,'46','Oclusal',3,1.00,140.00,'Reconstruccion posterior a la endodoncia'),(9,2,10,NULL,NULL,1,1.00,900.00,'Instalacion de brackets metalicos'),(10,2,13,NULL,NULL,1,1.00,30.00,'Radiografia previa al tratamiento');
/*!40000 ALTER TABLE `plan_tratamiento_detalles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `planes_tratamiento`
--

DROP TABLE IF EXISTS `planes_tratamiento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `planes_tratamiento` (
  `id_plan` bigint unsigned NOT NULL AUTO_INCREMENT,
  `id_atencion` bigint unsigned NOT NULL,
  `id_odontologo_proponente` bigint unsigned NOT NULL,
  `id_usuario_respuesta` bigint unsigned DEFAULT NULL,
  `numero_sesiones_estimadas` smallint unsigned DEFAULT NULL,
  `estado` enum('PENDIENTE','ACEPTADO','RECHAZADO','EN_CURSO','COMPLETADO','CANCELADO') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'PENDIENTE',
  `propuesto_en` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `respondido_en` datetime DEFAULT NULL,
  `observaciones` text COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`id_plan`),
  KEY `idx_planes_estado_fecha` (`estado`,`propuesto_en`),
  KEY `idx_planes_atencion` (`id_atencion`),
  KEY `fk_planes_odontologo` (`id_odontologo_proponente`),
  KEY `fk_planes_usuario_respuesta` (`id_usuario_respuesta`),
  CONSTRAINT `fk_planes_atencion` FOREIGN KEY (`id_atencion`) REFERENCES `atenciones` (`id_atencion`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_planes_odontologo` FOREIGN KEY (`id_odontologo_proponente`) REFERENCES `odontologos` (`id_usuario`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_planes_usuario_respuesta` FOREIGN KEY (`id_usuario_respuesta`) REFERENCES `usuarios` (`id_usuario`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `chk_planes_respuesta` CHECK (((`respondido_en` is null) or (`respondido_en` >= `propuesto_en`))),
  CONSTRAINT `chk_planes_sesiones` CHECK (((`numero_sesiones_estimadas` is null) or (`numero_sesiones_estimadas` > 0)))
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `planes_tratamiento`
--

LOCK TABLES `planes_tratamiento` WRITE;
/*!40000 ALTER TABLE `planes_tratamiento` DISABLE KEYS */;
INSERT INTO `planes_tratamiento` VALUES (1,1,2,4,3,'EN_CURSO','2026-10-11 09:50:00','2026-10-11 10:20:00','Endodoncia del 46 y restauraciones posteriores'),(2,6,3,4,12,'ACEPTADO','2026-10-24 17:30:00','2026-10-25 21:10:46','Tratamiento de ortodoncia para correccion del apinamiento'),(3,1,2,4,3,'EN_CURSO','2026-10-11 09:50:00','2026-10-11 10:20:00','Endodoncia del 46 y restauraciones posteriores'),(4,6,3,NULL,12,'PENDIENTE','2026-10-24 17:30:00',NULL,'Tratamiento de ortodoncia para correccion del apinamiento');
/*!40000 ALTER TABLE `planes_tratamiento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `procedimientos_atencion`
--

DROP TABLE IF EXISTS `procedimientos_atencion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `procedimientos_atencion` (
  `id_procedimiento_atencion` bigint unsigned NOT NULL AUTO_INCREMENT,
  `id_atencion` bigint unsigned NOT NULL,
  `id_servicio` int unsigned NOT NULL,
  `id_diagnostico_atencion` bigint unsigned DEFAULT NULL,
  `pieza_fdi` char(2) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `superficie` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `cantidad` decimal(8,2) NOT NULL DEFAULT '1.00',
  `precio_unitario` decimal(10,2) NOT NULL,
  `creado_en` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_procedimiento_atencion`),
  KEY `idx_procedimientos_atencion` (`id_atencion`),
  KEY `idx_procedimientos_servicio` (`id_servicio`),
  KEY `fk_procedimientos_diagnostico` (`id_diagnostico_atencion`),
  CONSTRAINT `fk_procedimientos_atencion_atencion` FOREIGN KEY (`id_atencion`) REFERENCES `atenciones` (`id_atencion`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_procedimientos_atencion_servicio` FOREIGN KEY (`id_servicio`) REFERENCES `servicios` (`id_servicio`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_procedimientos_diagnostico` FOREIGN KEY (`id_diagnostico_atencion`) REFERENCES `diagnosticos_atencion` (`id_diagnostico_atencion`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `chk_procedimiento_cantidad` CHECK ((`cantidad` > 0)),
  CONSTRAINT `chk_procedimiento_pieza_fdi` CHECK (((`pieza_fdi` is null) or (`pieza_fdi` in (_utf8mb4'11',_utf8mb4'12',_utf8mb4'13',_utf8mb4'14',_utf8mb4'15',_utf8mb4'16',_utf8mb4'17',_utf8mb4'18',_utf8mb4'21',_utf8mb4'22',_utf8mb4'23',_utf8mb4'24',_utf8mb4'25',_utf8mb4'26',_utf8mb4'27',_utf8mb4'28',_utf8mb4'31',_utf8mb4'32',_utf8mb4'33',_utf8mb4'34',_utf8mb4'35',_utf8mb4'36',_utf8mb4'37',_utf8mb4'38',_utf8mb4'41',_utf8mb4'42',_utf8mb4'43',_utf8mb4'44',_utf8mb4'45',_utf8mb4'46',_utf8mb4'47',_utf8mb4'48')))),
  CONSTRAINT `chk_procedimiento_precio` CHECK ((`precio_unitario` >= 0))
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `procedimientos_atencion`
--

LOCK TABLES `procedimientos_atencion` WRITE;
/*!40000 ALTER TABLE `procedimientos_atencion` DISABLE KEYS */;
INSERT INTO `procedimientos_atencion` VALUES (1,1,1,NULL,NULL,NULL,1.00,40.00,'2026-10-25 05:07:49'),(2,2,2,4,NULL,NULL,1.00,80.00,'2026-10-25 05:07:49'),(3,3,4,5,'14','Oclusal-Distal',1.00,90.00,'2026-10-25 05:07:49'),(4,4,6,6,'21',NULL,1.00,350.00,'2026-10-25 05:07:49'),(5,5,7,7,'46','Oclusal',1.00,550.00,'2026-10-25 05:07:49'),(6,6,11,NULL,NULL,NULL,1.00,100.00,'2026-10-25 05:07:49'),(7,1,1,NULL,NULL,NULL,1.00,40.00,'2026-10-25 05:14:05'),(8,2,2,4,NULL,NULL,1.00,80.00,'2026-10-25 05:14:05'),(9,3,4,5,'14','Oclusal-Distal',1.00,90.00,'2026-10-25 05:14:05'),(10,4,6,6,'21',NULL,1.00,350.00,'2026-10-25 05:14:05'),(11,5,7,7,'46','Oclusal',1.00,550.00,'2026-10-25 05:14:05'),(12,6,11,NULL,NULL,NULL,1.00,100.00,'2026-10-25 05:14:05'),(13,9,4,NULL,'15','Distal',1.00,90.00,'2026-10-28 16:00:13'),(14,9,12,NULL,NULL,NULL,1.00,450.00,'2026-10-28 16:01:28');
/*!40000 ALTER TABLE `procedimientos_atencion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `receta_detalles`
--

DROP TABLE IF EXISTS `receta_detalles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `receta_detalles` (
  `id_detalle_receta` bigint unsigned NOT NULL AUTO_INCREMENT,
  `id_receta` bigint unsigned NOT NULL,
  `medicamento` varchar(180) COLLATE utf8mb4_unicode_ci NOT NULL,
  `dosis_frecuencia` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `duracion` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `indicaciones` text COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`id_detalle_receta`),
  KEY `idx_receta_detalles_receta` (`id_receta`),
  CONSTRAINT `fk_receta_detalles_receta` FOREIGN KEY (`id_receta`) REFERENCES `recetas` (`id_receta`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `receta_detalles`
--

LOCK TABLES `receta_detalles` WRITE;
/*!40000 ALTER TABLE `receta_detalles` DISABLE KEYS */;
INSERT INTO `receta_detalles` VALUES (1,1,'Ibuprofeno 400 mg tabletas','1 tableta cada 8 horas','3 dias','Tomar despues de los alimentos'),(2,1,'Amoxicilina 500 mg capsulas','1 capsula cada 8 horas','7 dias','Completar el tratamiento'),(3,2,'Naproxeno 550 mg tabletas','1 tableta cada 12 horas','3 dias','Tomar despues de los alimentos'),(4,2,'Paracetamol 500 mg tabletas','1 tableta cada 8 horas solo si hay dolor','3 dias',NULL),(5,1,'Ibuprofeno 400 mg tabletas','1 tableta cada 8 horas','3 dias','Tomar despues de los alimentos'),(6,1,'Amoxicilina 500 mg capsulas','1 capsula cada 8 horas','7 dias','Completar el tratamiento'),(7,2,'Naproxeno 550 mg tabletas','1 tableta cada 12 horas','3 dias','Tomar despues de los alimentos'),(8,2,'Paracetamol 500 mg tabletas','1 tableta cada 8 horas solo si hay dolor','3 dias',NULL),(9,4,'panadol','10 ml','30 dias','');
/*!40000 ALTER TABLE `receta_detalles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recetas`
--

DROP TABLE IF EXISTS `recetas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `recetas` (
  `id_receta` bigint unsigned NOT NULL AUTO_INCREMENT,
  `id_atencion` bigint unsigned NOT NULL,
  `indicaciones` text COLLATE utf8mb4_unicode_ci,
  `emitida_en` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_receta`),
  UNIQUE KEY `uq_recetas_atencion` (`id_atencion`),
  CONSTRAINT `fk_recetas_atencion` FOREIGN KEY (`id_atencion`) REFERENCES `atenciones` (`id_atencion`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recetas`
--

LOCK TABLES `recetas` WRITE;
/*!40000 ALTER TABLE `recetas` DISABLE KEYS */;
INSERT INTO `recetas` VALUES (1,4,'Evitar masticar del lado tratado por 24 horas. Mantener higiene bucal.','2026-10-25 00:07:49'),(2,5,'Dieta blanda por 48 horas. Acudir a control si el dolor persiste.','2026-10-25 00:07:49'),(4,9,'','2026-10-28 11:01:54');
/*!40000 ALTER TABLE `recetas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `id_rol` tinyint unsigned NOT NULL AUTO_INCREMENT,
  `nombre` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
  `descripcion` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id_rol`),
  UNIQUE KEY `uq_roles_nombre` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,'ADMINISTRADOR','Gestiona usuarios, servicios, consultorios, disponibilidad, configuracion y reportes de la clinica'),(2,'ODONTOLOGO','Atiende pacientes, registra odontograma, diagnosticos, procedimientos, planes de tratamiento y recetas'),(3,'RECEPCIONISTA','Registra pacientes, gestiona la agenda de citas, la caja, los presupuestos y los pagos');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `servicios`
--

DROP TABLE IF EXISTS `servicios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `servicios` (
  `id_servicio` int unsigned NOT NULL AUTO_INCREMENT,
  `id_categoria` smallint unsigned NOT NULL,
  `nombre` varchar(150) COLLATE utf8mb4_unicode_ci NOT NULL,
  `descripcion` text COLLATE utf8mb4_unicode_ci,
  `precio` decimal(10,2) NOT NULL DEFAULT '0.00',
  `duracion_min` smallint unsigned NOT NULL DEFAULT '30',
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  `creado_en` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `actualizado_en` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_servicio`),
  UNIQUE KEY `uq_servicios_categoria_nombre` (`id_categoria`,`nombre`),
  KEY `idx_servicios_activo` (`activo`),
  CONSTRAINT `fk_servicios_categoria` FOREIGN KEY (`id_categoria`) REFERENCES `categorias_servicio` (`id_categoria`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `chk_servicios_duracion` CHECK ((`duracion_min` > 0)),
  CONSTRAINT `chk_servicios_precio` CHECK ((`precio` >= 0))
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `servicios`
--

LOCK TABLES `servicios` WRITE;
/*!40000 ALTER TABLE `servicios` DISABLE KEYS */;
INSERT INTO `servicios` VALUES (1,1,'Consulta y evaluacion odontologica','Examen clinico y plan inicial',40.00,30,1,'2026-10-25 05:07:49','2026-10-25 05:07:49'),(2,1,'Profilaxis dental','Limpieza y remocion de sarro',80.00,45,1,'2026-10-25 05:07:49','2026-10-25 05:07:49'),(3,1,'Aplicacion de fluor','Fluorizacion topica',50.00,30,1,'2026-10-25 05:07:49','2026-10-25 05:07:49'),(4,2,'Resina compuesta simple','Restauracion de una o dos superficies',90.00,40,1,'2026-10-25 05:07:49','2026-10-25 05:07:49'),(5,2,'Resina compuesta compleja','Restauracion de tres o mas superficies',140.00,60,1,'2026-10-25 05:07:49','2026-10-25 05:07:49'),(6,3,'Endodoncia unirradicular','Tratamiento de conductos en pieza de una raiz',400.00,60,1,'2026-10-25 05:07:49','2026-10-25 23:55:33'),(7,3,'Endodoncia multirradicular','Tratamiento de conductos en molares',550.00,90,1,'2026-10-25 05:07:49','2026-10-25 05:07:49'),(8,4,'Exodoncia simple','Extraccion dental sin complicaciones',100.00,40,1,'2026-10-25 05:07:49','2026-10-25 05:07:49'),(9,4,'Exodoncia de tercer molar','Extraccion de muela del juicio',300.00,60,1,'2026-10-25 05:07:49','2026-10-25 05:07:49'),(10,5,'Instalacion de brackets metalicos','Colocacion de aparatología fija',900.00,90,1,'2026-10-25 05:07:49','2026-10-25 05:07:49'),(11,5,'Control mensual de ortodoncia','Ajuste de arcos y control',100.00,30,1,'2026-10-25 05:07:49','2026-10-25 05:07:49'),(12,6,'Blanqueamiento dental','Blanqueamiento en consultorio',450.00,60,1,'2026-10-25 05:07:49','2026-10-25 05:07:49'),(13,1,'Radiografia periapical','Radiografia de una pieza dental',30.00,15,1,'2026-10-25 05:07:49','2026-10-25 05:07:49'),(15,6,'aperación','estetica facial',2000.00,60,0,'2026-10-25 23:56:29','2026-10-26 05:03:59');
/*!40000 ALTER TABLE `servicios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sesiones_plan`
--

DROP TABLE IF EXISTS `sesiones_plan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sesiones_plan` (
  `id_sesion_plan` bigint unsigned NOT NULL AUTO_INCREMENT,
  `id_plan` bigint unsigned NOT NULL,
  `id_cita` bigint unsigned NOT NULL,
  `numero_sesion` smallint unsigned NOT NULL,
  PRIMARY KEY (`id_sesion_plan`),
  UNIQUE KEY `uq_sesion_plan_numero` (`id_plan`,`numero_sesion`),
  UNIQUE KEY `uq_sesion_plan_cita` (`id_cita`),
  CONSTRAINT `fk_sesiones_plan_cita` FOREIGN KEY (`id_cita`) REFERENCES `citas` (`id_cita`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_sesiones_plan_plan` FOREIGN KEY (`id_plan`) REFERENCES `planes_tratamiento` (`id_plan`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `chk_sesion_plan_numero` CHECK ((`numero_sesion` > 0))
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sesiones_plan`
--

LOCK TABLES `sesiones_plan` WRITE;
/*!40000 ALTER TABLE `sesiones_plan` DISABLE KEYS */;
INSERT INTO `sesiones_plan` VALUES (1,1,5,1),(2,1,10,2),(3,1,15,3);
/*!40000 ALTER TABLE `sesiones_plan` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario_roles`
--

DROP TABLE IF EXISTS `usuario_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario_roles` (
  `id_usuario` bigint unsigned NOT NULL,
  `id_rol` tinyint unsigned NOT NULL,
  `asignado_en` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_usuario`,`id_rol`),
  KEY `idx_usuario_roles_rol` (`id_rol`),
  CONSTRAINT `fk_usuario_roles_rol` FOREIGN KEY (`id_rol`) REFERENCES `roles` (`id_rol`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_usuario_roles_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id_usuario`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario_roles`
--

LOCK TABLES `usuario_roles` WRITE;
/*!40000 ALTER TABLE `usuario_roles` DISABLE KEYS */;
INSERT INTO `usuario_roles` VALUES (1,1,'2026-10-25 05:07:49'),(1,2,'2026-10-25 05:07:49'),(2,2,'2026-10-25 05:07:49'),(3,2,'2026-10-26 05:02:12'),(4,3,'2026-10-25 05:07:49'),(6,2,'2026-10-27 23:52:27'),(7,1,'2026-10-28 15:29:50'),(7,2,'2026-10-28 15:29:50');
/*!40000 ALTER TABLE `usuario_roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `id_usuario` bigint unsigned NOT NULL AUTO_INCREMENT,
  `id_persona` bigint unsigned NOT NULL,
  `cargo_laboral` varchar(80) COLLATE utf8mb4_unicode_ci NOT NULL,
  `fecha_ingreso` date DEFAULT NULL,
  `fecha_baja` date DEFAULT NULL,
  `estado_laboral` enum('ACTIVO','INACTIVO','BAJA') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'ACTIVO',
  `estado_acceso` enum('HABILITADO','BLOQUEADO') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'HABILITADO',
  `nombre_usuario` varchar(80) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password_hash` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `creado_en` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `actualizado_en` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_usuario`),
  UNIQUE KEY `uq_usuarios_persona` (`id_persona`),
  UNIQUE KEY `uq_usuarios_nombre` (`nombre_usuario`),
  KEY `idx_usuarios_estados` (`estado_laboral`,`estado_acceso`),
  CONSTRAINT `fk_usuarios_persona` FOREIGN KEY (`id_persona`) REFERENCES `personas` (`id_persona`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `chk_usuarios_fechas_laborales` CHECK (((`fecha_baja` is null) or (`fecha_ingreso` is null) or (`fecha_baja` >= `fecha_ingreso`)))
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (1,1,'Gerente general y odontologa','2019-03-01',NULL,'ACTIVO','HABILITADO','mrivas','$2a$10$i/iOOvC1Y9UZ6hJ6ZbIRJeBr6IijgmIE0SkeOJfMYIqc3QlU/YW1K','2026-10-25 05:07:49','2026-10-25 05:07:49'),(2,2,'Odontologo general','2021-08-16',NULL,'ACTIVO','HABILITADO','jparedes','$2a$10$i/iOOvC1Y9UZ6hJ6ZbIRJeBr6IijgmIE0SkeOJfMYIqc3QlU/YW1K','2026-10-25 05:07:49','2026-10-25 05:07:49'),(3,3,'Ortodoncista','2023-02-01',NULL,'ACTIVO','HABILITADO','kvega','$2a$10$i/iOOvC1Y9UZ6hJ6ZbIRJeBr6IijgmIE0SkeOJfMYIqc3QlU/YW1K','2026-10-25 05:07:49','2026-10-26 05:02:12'),(4,4,'Recepcionista','2024-05-20',NULL,'ACTIVO','HABILITADO','rcampos','$2a$10$i/iOOvC1Y9UZ6hJ6ZbIRJeBr6IijgmIE0SkeOJfMYIqc3QlU/YW1K','2026-10-25 05:07:49','2026-10-25 05:07:49'),(6,17,'ODONTOLOGO','2026-10-25','2026-10-26','ACTIVO','HABILITADO','JuanQ','$2a$10$v6QO/0Cro8kj/FJmCC3bkO23fWsB7otYDzXkE0NnaoJjX1gHIcYAy','2026-10-26 05:00:17','2026-10-27 23:52:27'),(7,18,'Odontólogo','2026-10-28',NULL,'ACTIVO','HABILITADO','Rfranco','$2a$10$87NYj8AY6gz0GBQcp.0h1OcguNKLf5AUcR04ktGkczsZJmhtLD/yi','2026-10-28 15:29:50','2026-10-28 15:29:50');
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;
SET @@SESSION.SQL_LOG_BIN = @MYSQLDUMP_TEMP_LOG_BIN;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-10-28 14:44:20
