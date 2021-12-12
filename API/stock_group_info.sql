-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Nov 09, 2021 at 12:10 PM
-- Server version: 5.7.36-0ubuntu0.18.04.1
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
-- Table structure for table `stock_group_info`
--

CREATE TABLE `stock_group_info` (
  `pk` bigint(20) NOT NULL COMMENT '流水號',
  `group_num` bigint(20) NOT NULL COMMENT '股票組合代號',
  `group_num_order` int(10) NOT NULL COMMENT 'group num 裡的順序',
  `company_name` varchar(10) COLLATE utf8_bin NOT NULL COMMENT '公司名稱(10字元)',
  `stock_symbol` varchar(10) COLLATE utf8_bin NOT NULL COMMENT '股票代號(4碼)',
  `short_profit` varchar(3) COLLATE utf8_bin NOT NULL COMMENT '短線',
  `long_profit` varchar(3) COLLATE utf8_bin NOT NULL COMMENT '長線',
  `date_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '建立資料的時間點'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- Dumping data for table `stock_group_info`
--

INSERT INTO `stock_group_info` (`pk`, `group_num`, `group_num_order`, `company_name`, `stock_symbol`, `short_profit`, `long_profit`, `date_time`) VALUES
(1, 3, 1, '台積電', '2330', '無影響', '下跌', '2021-11-05 19:02:38'),
(2, 1, 1, '鴻海', '2317', '上漲', '上漲', '2021-11-05 19:02:38'),
(3, 3, 2, '聯發科', '2454', '下跌', '下跌', '2021-11-05 19:02:38'),
(4, 3, 3, '台塑化', '6505', '上漲', '下跌', '2021-11-05 19:02:38'),
(5, 4, 1, '富邦金', '2881', '下跌', '無影響', '2021-11-05 19:02:38'),
(6, 4, 6, '中華電', '2412', '下跌', '上漲', '2021-11-05 19:02:38'),
(7, 6, 1, '聯電', '2303', '下跌', '下跌', '2021-11-05 19:02:38'),
(8, 1, 2, '國泰金', '2882', '上漲', '下跌', '2021-11-05 19:02:38'),
(9, 1, 3, '南亞', '1303', '上漲', '上漲', '2021-11-05 19:02:38'),
(10, 6, 2, '台苯', '1301', '無影響', '下跌', '2021-11-05 19:02:38'),
(11, 1, 4, '台達電', '2308', '上漲', '上漲', '2021-11-05 19:02:38'),
(12, 2, 1, '中鋼', '2002', '上漲', '下跌', '2021-11-05 19:02:38'),
(13, 4, 7, '台化', '1326', '上漲', '下跌', '2021-11-05 19:02:38'),
(14, 4, 2, '日月光投控', '3711', '上漲', '上漲', '2021-11-05 19:02:38'),
(15, 3, 4, '中信金', '2891', '下跌', '無影響', '2021-11-05 19:02:38'),
(16, 6, 4, '永豐金', '2890', '上漲', '無影響', '2021-11-05 19:02:38'),
(17, 5, 1, '統一', '1216', '下跌', '下跌', '2021-11-05 19:02:38'),
(18, 5, 2, '中租', '-KY5871', '上漲', '無影響', '2021-11-05 19:02:38'),
(19, 3, 5, '陽明', '2609', '下跌', '下跌', '2021-11-05 19:02:38'),
(20, 2, 6, '玉山金', '2884', '上漲', '上漲', '2021-11-05 19:02:38'),
(21, 4, 3, '台灣大', '3045', '無影響', '下跌', '2021-11-05 19:02:38'),
(22, 4, 4, '和泰車', '2207', '上漲', '上漲', '2021-11-05 19:02:38'),
(23, 2, 2, '台泥', '1101', '下跌', '上漲', '2021-11-05 19:02:38'),
(24, 3, 6, '合庫金', '5880', '上漲', '下跌', '2021-11-05 19:02:38'),
(25, 2, 3, '元大金', '2885', '下跌', '上漲', '2021-11-05 19:02:38'),
(26, 5, 3, '廣達', '2382', '上漲', '上漲', '2021-11-05 19:02:38'),
(27, 4, 5, '大立光', '3008', '上漲', '下跌', '2021-11-05 19:02:38'),
(28, 2, 4, '第一金', '2892', '上漲', '上漲', '2021-11-05 19:02:38'),
(29, 6, 3, '統一超', '2912', '下跌', '下跌', '2021-11-05 19:02:38'),
(30, 2, 5, '研華', '2395', '下跌', '無影響', '2021-11-05 19:02:38');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `stock_group_info`
--
ALTER TABLE `stock_group_info`
  ADD PRIMARY KEY (`pk`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `stock_group_info`
--
ALTER TABLE `stock_group_info`
  MODIFY `pk` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '流水號', AUTO_INCREMENT=31;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
