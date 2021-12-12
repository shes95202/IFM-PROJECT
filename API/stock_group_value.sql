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
-- Table structure for table `stock_group_value`
--

CREATE TABLE `stock_group_value` (
  `pk` bigint(20) NOT NULL,
  `group_num` bigint(20) NOT NULL,
  `VF` float NOT NULL,
  `Units` int(5) NOT NULL,
  `date_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '建立資料時間點'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- Dumping data for table `stock_group_value`
--

INSERT INTO `stock_group_value` (`pk`, `group_num`, `VF`, `Units`, `date_time`) VALUES
(1, 1, 0.65, 6, '2021-11-06 17:52:25'),
(2, 2, 0.89, 33, '2021-11-06 17:52:25'),
(3, 3, 0.71, 28, '2021-11-06 17:52:25'),
(4, 4, 0.33, 14, '2021-11-06 17:52:25'),
(5, 5, 0.84, 24, '2021-11-06 17:52:25'),
(6, 6, 0.02, 36, '2021-11-06 17:52:25');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `stock_group_value`
--
ALTER TABLE `stock_group_value`
  ADD PRIMARY KEY (`pk`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `stock_group_value`
--
ALTER TABLE `stock_group_value`
  MODIFY `pk` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
