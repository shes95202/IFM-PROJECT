<?php
/*
空回傳:{"data":[{"group_num":["null"],"company_name":["null"],"stock_symbol":["null"],"VF":["null"],"Units":["null"]}],"total":["null"]}
有值:{"data":[{"group_num":"1","company_name":["鴻海","國泰金","南亞","台達電"],"stock_symbol":["2317","2882","1303","2308"],"VF":"0.65","Units":"6"},{"group_num":"2","company_name":["中鋼","台泥","元大金","第一金","研華","玉山金"],"stock_symbol":["2002","1101","2885","2892","2395","2884"],"VF":"0.89","Units":"33"},{"group_num":"3","company_name":["台積電","聯發科","台塑化","中信金","陽明","合庫金"],"stock_symbol":["2330","2454","6505","2891","2609","5880"],"VF":"0.71","Units":"28"},{"group_num":"5","company_name":["統一","中租-KY","廣達"],"stock_symbol":["1216","5871","2382"],"VF":"0.84","Units":"24"}],"total":91}
*/
$data_arr = array();

$conn = new PDO("mysql:host=localhost;dbname=APP","root","Domain-0042");//測試
//$conn = new PDO("mysql:host=localhost;dbname=android","admin","aefdu6359783");
$conn -> exec("SET CHARACTER SET utf8");
$conn -> setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

$sql_ = $conn -> prepare("SELECT a.group_num,a.company_name,a.stock_symbol,b.VF,b.Units FROM stock_group_info as a join stock_group_value as b on a.group_num = b.group_num where b.VF > 0.5 order by b.group_num,a.group_num_order ASC");
$sql_ -> execute();
$total_row = $sql_ -> rowCount();

if($total_row == 0)
{
	$null_data = ["group_num" => "null","company_name" => "null", "stock_symbol" => "null", "VF" => "null", "Units" => "null"];
	array_push($data_arr,$null_data);
	$json_display = ["data" => $data_arr, "total" => "null"];
	$display = json_encode($json_display, JSON_UNESCAPED_UNICODE);
	echo $display;

	unset($conn);
	
	exit;
}

$information_1 = $sql_ -> fetchAll(PDO::FETCH_NUM);

$now_group_num = null;
$last_group_num = null;
$VF = null;
$Units = null;
$total = 0;
$row_num = 0;

$company_name_arr = array();
$stock_symbol_arr = array();

foreach($information_1 as $rs)
{
	$row_num++;
	$now_group_num = $rs[0];
	
	if($total_row == $row_num)
	{
		$last_group_num = $now_group_num;
		array_push($company_name_arr,$rs[1]);
		array_push($stock_symbol_arr,$rs[2]);
		$VF = $rs[3];
		$Units = $rs[4];
		$json_display = ["group_num" => $last_group_num,"company_name" => $company_name_arr, "stock_symbol" => $stock_symbol_arr, "VF" => $VF, "Units" => $Units];
		array_push($data_arr,$json_display);
	}
	else
	{
		if($last_group_num == null)//第一筆資料
		{
			$last_group_num = $now_group_num;
			array_push($company_name_arr,$rs[1]);
			array_push($stock_symbol_arr,$rs[2]);
			$VF = $rs[3];
			$Units = $rs[4];
			$total = $rs[4];
		}
		else if($last_group_num != null && $now_group_num != $last_group_num && $total_row != $row_num)//換下一個group了
		{
			$json_display = ["group_num" => $last_group_num,"company_name" => $company_name_arr, "stock_symbol" => $stock_symbol_arr, "VF" => $VF, "Units" => $Units];
			array_push($data_arr,$json_display);
			$company_name_arr = array();
			$stock_symbol_arr = array();
			$last_group_num = $now_group_num;
			array_push($company_name_arr,$rs[1]);
			array_push($stock_symbol_arr,$rs[2]);
			$VF = $rs[3];
			$Units = $rs[4];
			$total = $total + $rs[4];
		}
		else if($last_group_num != null && $now_group_num == $last_group_num)//同一個group
		{
			array_push($company_name_arr,$rs[1]);
			array_push($stock_symbol_arr,$rs[2]);
			$VF = $rs[3];
			$Units = $rs[4];
		}
	}
	
	
}
$json_display = ["data" => $data_arr, "total" => $total];
//$json_display = ["group_num" => $group_num_arr,"company_name" => $company_name_arr, "stock_symbol" => $stock_symbol_arr, "VF" => $VF_arr, "Units" => $Units_arr];
$display = json_encode($json_display, JSON_UNESCAPED_UNICODE);
echo $display;

unset($conn);

?>
