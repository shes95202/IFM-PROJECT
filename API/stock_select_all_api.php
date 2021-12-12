<?php
/*
空回傳:{"data":[{"company_name":["null"],"stock_symbol":["null"],"short_profit":["null"],"long_profit":["null"]}]}
有值:{"data":[{"company_name":"鴻海","stock_symbol":"2317","short_profit":"無影響","long_profit":"下跌"},{"company_name":"國泰金","stock_symbol":"2882","short_profit":"上漲","long_profit":"上漲"},{"company_name":"南亞","stock_symbol":"1303","short_profit":"下跌","long_profit":"下跌"},{"company_name":"台達電","stock_symbol":"2308","short_profit":"上漲","long_profit":"下跌"},{"company_name":"中鋼","stock_symbol":"2002","short_profit":"下跌","long_profit":"無影響"},{"company_name":"台泥","stock_symbol":"1101","short_profit":"下跌","long_profit":"上漲"},{"company_name":"元大金","stock_symbol":"2885","short_profit":"下跌","long_profit":"下跌"},{"company_name":"第一金","stock_symbol":"2892","short_profit":"上漲","long_profit":"下跌"},{"company_name":"研華","stock_symbol":"2395","short_profit":"上漲","long_profit":"上漲"},{"company_name":"玉山金","stock_symbol":"2884","short_profit":"無影響","long_profit":"下跌"},{"company_name":"台積電","stock_symbol":"2330","short_profit":"上漲","long_profit":"上漲"},{"company_name":"聯發科","stock_symbol":"2454","short_profit":"上漲","long_profit":"下跌"},{"company_name":"台塑化","stock_symbol":"6505","short_profit":"上漲","long_profit":"下跌"},{"company_name":"中信金","stock_symbol":"2891","short_profit":"上漲","long_profit":"上漲"},{"company_name":"陽明","stock_symbol":"2609","short_profit":"下跌","long_profit":"無影響"},{"company_name":"合庫金","stock_symbol":"5880","short_profit":"上漲","long_profit":"無影響"},{"company_name":"富邦金","stock_symbol":"2881","short_profit":"下跌","long_profit":"下跌"},{"company_name":"日月光投控","stock_symbol":"3711","short_profit":"上漲","long_profit":"無影響"},{"company_name":"台灣大","stock_symbol":"3045","short_profit":"下跌","long_profit":"下跌"},{"company_name":"和泰車","stock_symbol":"2207","short_profit":"上漲","long_profit":"上漲"},{"company_name":"大立光","stock_symbol":"3008","short_profit":"無影響","long_profit":"下跌"},{"company_name":"中華電","stock_symbol":"2412","short_profit":"上漲","long_profit":"上漲"},{"company_name":"台化","stock_symbol":"1326","short_profit":"下跌","long_profit":"上漲"},{"company_name":"統一","stock_symbol":"1216","short_profit":"上漲","long_profit":"下跌"},{"company_name":"中租-KY","stock_symbol":"5871","short_profit":"下跌","long_profit":"上漲"},{"company_name":"廣達","stock_symbol":"2382","short_profit":"上漲","long_profit":"上漲"},{"company_name":"聯電","stock_symbol":"2303","short_profit":"上漲","long_profit":"下跌"},{"company_name":"台苯","stock_symbol":"1301","short_profit":"上漲","long_profit":"上漲"},{"company_name":"統一超","stock_symbol":"2912","short_profit":"下跌","long_profit":"下跌"},{"company_name":"永豐金","stock_symbol":"2890","short_profit":"下跌","long_profit":"無影響"}]}
*/
$data_arr = array();

$conn = new PDO("mysql:host=localhost;dbname=APP","root","Domain-0042");//測試
//$conn = new PDO("mysql:host=localhost;dbname=android","admin","aefdu6359783");
$conn -> exec("SET CHARACTER SET utf8");
$conn -> setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

$sql_ = $conn -> prepare("SELECT company_name,stock_symbol,short_profit,long_profit FROM stock_group_info where 1 ORDER BY `stock_group_info`.`pk` ASC");
$sql_ -> execute();
$total_row = $sql_ -> rowCount();

if($total_row == 0)
{
	$null_data = ["company_name" => "null", "stock_symbol" => "null", "short_profit" => "null", "long_profit" => "null"];
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
	$json_display = ["company_name" => $rs[0], "stock_symbol" => $rs[1], "short_profit" => $rs[2], "long_profit" => $rs[3]];
	array_push($data_arr,$json_display);
}
$json_display = ["data" => $data_arr];
$display = json_encode($json_display, JSON_UNESCAPED_UNICODE);
echo $display;

unset($conn);

?>
