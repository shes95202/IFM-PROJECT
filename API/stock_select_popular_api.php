<?php
/*
空回傳:{"data":{"company_name":["null"],"stock_symbol":["null"],"short_profit":["null"],"long_profit":["null"]}}
有值:{"data":[{"company_name":"鴻海","stock_symbol":"2317","short_profit":"無影響","long_profit":"下跌"},{"company_name":"國泰金","stock_symbol":"2882","short_profit":"上漲","long_profit":"上漲"},{"company_name":"南亞","stock_symbol":"1303","short_profit":"下跌","long_profit":"下跌"},{"company_name":"台達電","stock_symbol":"2308","short_profit":"上漲","long_profit":"下跌"},{"company_name":"中鋼","stock_symbol":"2002","short_profit":"下跌","long_profit":"無影響"}]}
*/

$conn = new PDO("mysql:host=localhost;dbname=APP","root","Domain-0042");//測試
//$conn = new PDO("mysql:host=localhost;dbname=android","admin","aefdu6359783");
$conn -> exec("SET CHARACTER SET utf8");
$conn -> setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

$sql_ = $conn -> prepare("SELECT a.company_name,a.stock_symbol,b.short_profit,b.long_profit FROM stock_popular as a join stock_group_info as b on a.company_name = b.company_name ORDER BY a.pk ASC");
$sql_ -> execute();
$total_row = $sql_ -> rowCount();

if($total_row == 0)
{
	$data_arr = ["company_name" => "null", "stock_symbol" => "null", "short_profit" => "null", "long_profit" => "null"];
	$json_display = ["data" => $data_arr];
	$display = json_encode($json_display, JSON_UNESCAPED_UNICODE);
	echo $display;

	unset($conn);
	
	exit;
}

$information_1 = $sql_ -> fetchAll(PDO::FETCH_NUM);

$data_arr = array();

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
