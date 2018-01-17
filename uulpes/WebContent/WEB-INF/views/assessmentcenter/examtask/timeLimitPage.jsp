<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<style type="text/css">
.time-item{
	display:inline-block;
	vertical-align:middle
}
.time-item strong {
	float:left;
	margin-top:8px;
	background:#C71C60;
	color:#fff;
	line-height:30px;
	font-size:25px;
	font-family:Arial;
	padding:0 8px;
	margin-right:10px;
	border-radius:5px;
	box-shadow:1px 1px 3px rgba(0,0,0,0.2);
}
</style>

<script type="text/javascript">
var timeID;

//var intDiff = parseInt('${limittime}')*60;//倒计时总秒数量
var intDiff;


function timer(t){
	intDiff =t;
	clearInterval(timeID);
	if(intDiff)
	timeID = window.setInterval(function(){
	if(intDiff==0) {
		clearInterval(timeID);
		timeout();
	}
	var day=0,

		//hour=0,

		minute=0,

		second=0;//时间默认值		

	if(intDiff > 0){

		//day = Math.floor(intDiff / (60 * 60 * 24));

		//hour = Math.floor(intDiff / (60 * 60));

		//minute = Math.floor(intDiff / 60)  - (hour * 60);

		//second = Math.floor(intDiff)  - (hour * 60 * 60) - (minute * 60);
		
		minute = Math.floor(intDiff / 60);

		second = Math.floor(intDiff)  -  (minute * 60);

	}

	if (minute <= 9) minute = '0' + minute;

	if (second <= 9) second = '0' + second;

	//$('#day_show').html(day+"天");

	//$('#hour_show').html('<s id="h"></s>'+hour+'时');

	$('#minute_show').html('<s></s>'+minute+'分');

	$('#second_show').html('<s></s>'+second+'秒');

	intDiff--;

	}, 1000);

} 

//$(function(){

	//timer(intDiff);

//});	

</script>
<div style="float:left;line-height:45px;padding-left: 50px;"><h2>注意,此部分题目有时间限制超时将直接提交,倒计时:</h2></div>

<div class="time-item">

	<strong id="hour_show">0时</strong>

	<strong id="minute_show">0分</strong>

	<strong id="second_show">0秒</strong>

</div>

