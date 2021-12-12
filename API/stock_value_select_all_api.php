<?php
/*
空回傳:{"data":[{"company_name":["null"],"stock_symbol":["null"],"short_profit":["null"],"long_profit":["null"]}]}
有值:{"data":[{"company_name":"台積電","stock_symbol":"2330","stock_ratio":"1.24","now_value":"615"},{"company_name":"鴻海","stock_symbol":"2317","stock_ratio":"1.28","now_value":"106.5"},{"company_name":"聯發科","stock_symbol":"2454","stock_ratio":"1.45","now_value":"1080"},{"company_name":"台塑化","stock_symbol":"6505","stock_ratio":"1.02","now_value":"97"},{"company_name":"富邦金","stock_symbol":"2881","stock_ratio":"1.64","now_value":"74.4"},{"company_name":"中華電","stock_symbol":"2412","stock_ratio":"1.02","now_value":"112.5"},{"company_name":"聯電","stock_symbol":"2303","stock_ratio":"1.78","now_value":"64.7"},{"company_name":"國泰金","stock_symbol":"2882","stock_ratio":"1.47","now_value":"61.1"},{"company_name":"南亞","stock_symbol":"1303","stock_ratio":"1.29","now_value":"85.5"},{"company_name":"台塑","stock_symbol":"1301","stock_ratio":"1.18","now_value":"104.5"},{"company_name":"台達電","stock_symbol":"2308","stock_ratio":"1.22","now_value":"259"},{"company_name":"中鋼","stock_symbol":"2002","stock_ratio":"1.44","now_value":"32.6"},{"company_name":"台化","stock_symbol":"1326","stock_ratio":"1.00","now_value":"80.2"},{"company_name":"日月光投控","stock_symbol":"3711","stock_ratio":"1.44","now_value":"106.5"},{"company_name":"中信金","stock_symbol":"2891","stock_ratio":"1.24","now_value":"24.4"},{"company_name":"永豐金","stock_symbol":"2890","stock_ratio":"1.35","now_value":"15.2"},{"company_name":"統一","stock_symbol":"1216","stock_ratio":"0.99","now_value":"67.5"},{"company_name":"中租-KY","stock_symbol":"5871","stock_ratio":"1.48","now_value":"246"},{"company_name":"陽明","stock_symbol":"2609","stock_ratio":"8.50","now_value":"110.5"},{"company_name":"玉山金","stock_symbol":"2884","stock_ratio":"1.07","now_value":"27.5"},{"company_name":"台灣大","stock_symbol":"3045","stock_ratio":"1.00","now_value":"99.1"},{"company_name":"和泰車","stock_symbol":"2207","stock_ratio":"0.96","now_value":"665"},{"company_name":"台泥","stock_symbol":"1101","stock_ratio":"1.11","now_value":"47.9"},{"company_name":"合庫金","stock_symbol":"5880","stock_ratio":"1.16","now_value":"23.7"},{"company_name":"元大金","stock_symbol":"2885","stock_ratio":"1.29","now_value":"25"},{"company_name":"廣達","stock_symbol":"2382","stock_ratio":"1.12","now_value":"86.5"},{"company_name":"大立光","stock_symbol":"3008","stock_ratio":"0.62","now_value":"2100"},{"company_name":"第一金","stock_symbol":"2892","stock_ratio":"1.10","now_value":"23.7"},{"company_name":"統一超","stock_symbol":"2912","stock_ratio":"1.06","now_value":"288.5"},{"company_name":"研華","stock_symbol":"2395","stock_ratio":"1.22","now_value":"389"}]}
*/
$data_arr = array();

// $conn = new PDO("mysql:host=localhost;dbname=YCT_test","ma430104","75147514");//測試
$conn = new PDO("mysql:host=localhost;dbname=APP","root","Domain-0042");
$conn -> exec("SET CHARACTER SET utf8");
$conn -> setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

$sql_ = $conn -> prepare("SELECT company_name,stock_symbol,ROUND(now_value/before_value,2) as stock_ratio,now_value FROM stock_value where 1 ORDER BY `stock_value`.`pk` ASC");
$sql_ -> execute();
$total_row = $sql_ -> rowCount();

if($total_row == 0)
{
	$null_data = ["company_name" => "null", "stock_symbol" => "null", "stock_ratio" => "null", "now_value" => "null"];
	array_push($data_arr,$null_data);
	$json_display = ["data" => $data_arr];
	$display = json_encode($json_display, JSON_UNESCAPED_UNICODE);
	echo $display;

	unset($conn);
	
	exit;
}

$information_1 = $sql_ -> fetchAll(PDO::FETCH_NUM);

foreach($information_1 as $rs)
{
	$json_display = ["company_name" => $rs[0], "stock_symbol" => $rs[1], "stock_ratio" => $rs[2], "now_value" => $rs[3]];
	array_push($data_arr,$json_display);
}
$json_display = ["data" => $data_arr];
$display = json_encode($json_display, JSON_UNESCAPED_UNICODE);
echo $display;

unset($conn);

?>
