<?php
ini_set('display_errors','1');
error_reporting(E_ALL);
/*
接收:userId=>Nokia1638336535234
空回傳:{"data":[{"userId":"null","company_name":"null","stock_symbol":"null","stock_ratio":"null","oneStockCost":"null","total":"null"}]}
有值:{"data":[{"userId":"Nokia1638336535234","company_name":"鴻海","stock_symbol":"2317","stock_ratio":"6.6","oneStockCost":"106.5","total":"100000"},{"userId":"Nokia1638336535234","company_name":"中鋼","stock_symbol":"2002","stock_ratio":"36.3","oneStockCost":"32.6","total":"100000"}]}
*/
$data_arr = array();

$userId = @$_GET['userId'];
$conn = new PDO("mysql:host=localhost;dbname=APP","root","Domain-0042");//測試
$conn -> exec("SET CHARACTER SET utf8");
$conn -> setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

$sql_ = $conn -> prepare("SELECT * FROM user_info where userId LIKE '$userId'");
$sql_ -> execute();
$total_row = $sql_ -> rowCount();

if($total_row == 0)
{
	$null_data = ["userId" => "null", "company_name" => "null", "stock_symbol" => "null", "stock_cost" => "null", "stock_count" => "null", "total" => "null"];
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
	$json_display = ["userId" => $rs[1], "company_name" => $rs[2], "stock_symbol" => $rs[3], "stock_cost" => $rs[4], "stock_count" => $rs[5], "total" => $rs[6]];
	array_push($data_arr,$json_display);
}
$json_display = ["data" => $data_arr];
$display = json_encode($json_display, JSON_UNESCAPED_UNICODE);
echo $display;

unset($conn);

?>
