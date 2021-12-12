-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Nov 08, 2021 at 09:41 PM
-- Server version: 5.7.35-0ubuntu0.18.04.1
-- PHP Version: 7.4.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `YCT_test`
--

-- --------------------------------------------------------

--
-- Table structure for table `stock_popular`
--

CREATE TABLE `stock_popular` (
  `pk` int(1) NOT NULL,
  `company_name` varchar(10) COLLATE utf8_bin NOT NULL,
  `stock_symbol` varchar(4) COLLATE utf8_bin NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- Dumping data for table `stock_popular`
--

INSERT INTO `stock_popular` (`pk`, `company_name`, `stock_symbol`) VALUES
(1, '聯電', '2303'),
(2, '台積電', '2330'),
(3, '陽明', '2609'),
(4, '鴻海', '2317'),
(5, '中鋼', '2002');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `stock_popular`
--
ALTER TABLE `stock_popular`
  ADD PRIMARY KEY (`pk`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `stock_popular`
--
ALTER TABLE `stock_popular`
  MODIFY `pk` int(1) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
