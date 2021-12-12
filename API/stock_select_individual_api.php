<?php
ini_set('display_errors','1');
error_reporting(E_ALL);
/*
get的key是select_text(完整的股票代號或公司名稱)
空回傳:{"data":[{"短線":{"上漲":["null"],"下跌":["null"],"無影響":["null"]}},{"長線":{"上漲":["null"],"下跌":["null"],"無影響":["null"]}}]}
有值:{"data":[{"短線":{"上漲":["綜合評估","MA5","MA200","布林","九轉"],"下跌":["MA35","RSI","MACD"],"無影響":["新聞"]}},{"長線":{"上漲":["綜合評估","MA200"],"下跌":["MA90"],"無影響":["MA365"]}}]}
*/
/*$select_text = file_get_contents('php://input');
echo $select_text;*/
//echo $select_text["select_text"];
$select_text = @$_GET['select_text'];
$conn = new PDO("mysql:host=localhost;dbname=APP","root","Domain-0042");//測試
//$conn = new PDO("mysql:host=localhost;dbname=android","admin","aefdu6359783");
$conn -> exec("SET CHARACTER SET utf8");
$conn -> setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

$sql_ = $conn -> prepare("SELECT name,result FROM stock_short_profit where company_name = '$select_text' or stock_symbol = '$select_text' order by pk asc");
$sql_ -> execute();

$information_1 = $sql_ -> fetchAll(PDO::FETCH_NUM);

$stock_short_up_name_arr = array();
$stock_short_down_name_arr = array();
$stock_short_no_effect_name_arr = array();

foreach($information_1 as $rs)
{
	if($rs[1] == '上漲')
	{
		array_push($stock_short_up_name_arr,$rs[0]);
	}
	else if($rs[1] == '下跌')
	{
		array_push($stock_short_down_name_arr,$rs[0]);
	}
	else if($rs[1] == '無影響')
	{
		array_push($stock_short_no_effect_name_arr,$rs[0]);
	}
}

if($stock_short_up_name_arr == array())
{
	$stock_short_up_name_arr = array("null");
}
if($stock_short_down_name_arr == array())
{
	$stock_short_down_name_arr = array("null");
}
if($stock_short_no_effect_name_arr == array())
{
	$stock_short_no_effect_name_arr = array("null");
}

$stock_short_json = ["上漲" => $stock_short_up_name_arr, "下跌" => $stock_short_down_name_arr, "無影響" => $stock_short_no_effect_name_arr];
$stock_short_final_json = ["短線" => $stock_short_json];

$sql_ = $conn -> prepare("SELECT name,result FROM stock_long_profit where company_name = '$select_text' or stock_symbol = '$select_text' order by pk asc");
$sql_ -> execute();

$information_1 = $sql_ -> fetchAll(PDO::FETCH_NUM);

$stock_long_up_name_arr = array();
$stock_long_down_name_arr = array();
$stock_long_no_effect_name_arr = array();

foreach($information_1 as $rs)
{
	if($rs[1] == '上漲')
	{
		array_push($stock_long_up_name_arr,$rs[0]);
	}
	else if($rs[1] == '下跌')
	{
		array_push($stock_long_down_name_arr,$rs[0]);
	}
	else if($rs[1] == '無影響')
	{
		array_push($stock_long_no_effect_name_arr,$rs[0]);
	}
	
}

if($stock_long_up_name_arr == array())
{
	$stock_long_up_name_arr = array("null");
}
if($stock_long_down_name_arr == array())
{
	$stock_long_down_name_arr = array("null");
}
if($stock_long_no_effect_name_arr == array())
{
	$stock_long_no_effect_name_arr = array("null");
}

$stock_long_json = ["上漲" => $stock_long_up_name_arr, "下跌" => $stock_long_down_name_arr, "無影響" => $stock_long_no_effect_name_arr];
$stock_long_final_json = ["長線" => $stock_long_json];

$data_arr = array();
array_push($data_arr, $stock_short_final_json, $stock_long_final_json);

$json_display = ["data" => $data_arr];
$display = json_encode($json_display, JSON_UNESCAPED_UNICODE);
echo $display;

unset($conn);

?>
