-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Nov 07, 2021 at 06:44 AM
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
-- Table structure for table `stock_long_profit`
--

CREATE TABLE `stock_long_profit` (
  `pk` bigint(20) NOT NULL,
  `company_name` varchar(10) COLLATE utf8_bin NOT NULL COMMENT '公司名稱',
  `stock_symbol` varchar(4) COLLATE utf8_bin NOT NULL COMMENT '股票名稱',
  `name` varchar(10) COLLATE utf8_bin NOT NULL COMMENT '不知什麼名稱',
  `result` varchar(3) COLLATE utf8_bin NOT NULL,
  `date_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '建立資料的時間點'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- Dumping data for table `stock_long_profit`
--

INSERT INTO `stock_long_profit` (`pk`, `company_name`, `stock_symbol`, `name`, `result`, `date_time`) VALUES
(1, '台積電', '2330', '綜合評估', '上漲', '2021-11-07 03:33:01'),
(2, '台積電', '2330', 'MA90', '下跌', '2021-11-07 03:33:01'),
(3, '台積電', '2330', 'MA200', '上漲', '2021-11-07 03:33:01'),
(4, '台積電', '2330', 'MA365', '無影響', '2021-11-07 03:33:01');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `stock_long_profit`
--
ALTER TABLE `stock_long_profit`
  ADD PRIMARY KEY (`pk`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `stock_long_profit`
--
ALTER TABLE `stock_long_profit`
  MODIFY `pk` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
