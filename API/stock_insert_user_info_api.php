<?php
ini_set('display_errors','1');
error_reporting(E_ALL);

$data_arr = array();

$userId = $_POST['userId'];
$All = $_POST['All'];
$act_arr = json_decode($All);

//$event_id = $act_arr -> event_id;
$conn = new PDO("mysql:host=localhost;dbname=APP","root","Domain-0042");//測試
$conn -> exec("SET CHARACTER SET utf8");
$conn -> setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

$sql_ = $conn -> prepare("SELECT pk FROM user_info where userId = '$userId'");
$sql_ -> execute();
$total_row = $sql_ -> rowCount();
if($total_row != 0)
{
	$sth = $conn -> prepare("DELETE from user_info where userId = '$userId'");
	$sth -> execute();
}

try
{
	foreach($act_arr as $act)
	{
		$stockName = $act -> stockName;
		$stockNum = $act -> stockNum;
        $stock_cost = $act -> stock_cost;
        $now_cost = $act -> now_cost;
        $stock_count = $act -> stock_count;
		$total = $act -> total;
		$sth_2 = $conn -> prepare("INSERT INTO user_info (userId,company_name,stock_symbol,stock_cost,now_cost,stock_count,total) VALUES (?,?,?,?,?,?,?)");
		$sth_2 -> execute(array($userId,$stockName,$stockNum,$stock_cost,$now_cost,$stock_count,$total));
	}
}
catch(PDOException $e)
{
	$json_display = ["result" => "fail"];
	$display = json_encode($json_display, JSON_UNESCAPED_UNICODE);
	echo $display;

	unset($conn);
	exit;
}

$json_display = ["result" => "success"];
$display = json_encode($json_display, JSON_UNESCAPED_UNICODE);
echo $display;

unset($conn);
exit;

?>
