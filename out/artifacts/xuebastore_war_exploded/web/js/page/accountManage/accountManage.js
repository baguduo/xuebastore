// 点击获取订单信息
app.factory('accountDetail',['globalService',
function(globalService){
	return {
		detail : function(id,callback) {
			globalService.doGet("repastFormDetail/" + id,null,callback);
		},
		getTime : function() {
			var date = new Date();
			var Y = date.getFullYear() + '-';
			var M = (date.getMonth()+1 < 10 ? '0' + (date.getMonth()+1) : date.getMonth()+1) + '-';
			var D = (date.getDate()    < 10 ? '0' + (date.getDate())    : date.getDate());
			var time = Y + M + D;
				
			return time;
		}
	}
}])

// 分店

// 今日
app.controller('partToday',['$scope','globalService','accountDetail',
function($scope,globalService,accountDetail){
	var timeNow = accountDetail.getTime();
	$scope.orderList = [];
	$scope.todayPartDetail;

	// 初始化加载
	$scope.getOrder = function(){
		globalService.doGet("shopRepastStatistics/query?currentPage=1&parameters['storeId']=" + loginInfo.storeId + "&parameters['ordertimeNowtart']="+ timeNow +"&parameters['ordertimeNownd']=" + timeNow + "&parameters['isClearing']=1",null,function(response){
			$scope.orderList = response.body;

			globalService.paginationInit('.pagination',response.totalRow,function(newPage){
				globalService.doGet("shopRepastStatistics/query?currentPage=" + newPage + "&parameters['storeId']=" + loginInfo.storeId + "&parameters['ordertimeNowtart']="+ timeNow +"&parameters['ordertimeNownd']=" + timeNow + "&parameters['isClearing']=1",null,function(response){
					$scope.orderList = response.body;
				});
			});		
		})
	}

	$scope.getOrder();
	// 点击查询订单信息
	$scope.getAboutOrder = function(order) {
		accountDetail.detail(order.id,function(response){
			$scope.todayPartDetail = response.body;
			$scope.todayPartDetail.source = order.source;
		});
	}
}])

//历史 
app.controller('partHistory',['$scope','globalService','accountDetail',
function($scope,globalService,accountDetail){
	$scope.orderList = [];
	$scope.startTime = "";
	$scope.endTime = "";
	// 日历
	globalService.datetimepickerInit(".form_datetime",$scope,{
		format: 'yyyy-mm-dd',
		minView: 2,
		startView: 2
	});

	// 按时间搜索
	$scope.getOrder = function(startTime,endTime){
		globalService.doGet("shopRepastStatistics/query?currentPage=1&parameters['storeId']=" + loginInfo.storeId + "&parameters['orderTimeStart']=" + startTime + "&parameters['orderTimeEnd']=" + endTime + "&parameters['isClearing']=1",null,function(response){
			$scope.orderList = response.body;

			globalService.paginationInit('.pagination',response.totalRow,function(newPage){
				globalService.doGet("shopRepastStatistics/query?currentPage=" + newPage + "&parameters['storeId']=" + loginInfo.storeId + "&parameters['orderTimeStart']=" + startTime + "&parameters['orderTimeEnd']=" + endTime + "&parameters['isClearing']=1",null,function(response){
					$scope.orderList = response.body;
				});
			});		
		})
	}
	// 点击查询订单信息
	$scope.getAboutOrder = function(order) {
		accountDetail.detail(order.id,function(response){
			$scope.todayPartDetail = response.body;
			$scope.todayPartDetail.source = order.source;
		});
	}

	// 初始化加载
	$scope.getOrder("2000-01-01","3000-12-31");
}])


// 总后台

//今日
app.controller('todaySales',['$scope','$http','globalService','accountDetail',
function($scope,$http,globalService,accountDetail){
	$scope.partStore = false;
	$scope.orderListTd = [];
	$scope.orderListTdTemp = [];
	$scope.orderListPart = [];
	$scope.select = null;

	// 初始化加载
	$scope.getOrder = function(){
		var timeNow = accountDetail.getTime();

		globalService.doGet("headRepastStatistics/query?parameters['orderTimeStart']=" + timeNow + "&parameters['orderTimeEnd']=" + timeNow,null,function(response){
			$scope.orderListTd = response.body;
			$scope.orderListTdTemp = response.body;
		})
	}

	$scope.getOrder();
	// 下拉框进行搜索
	$scope.searchByStoreTd = function(){
		var timeNow = accountDetail.getTime();
		var url;

		if($scope.select == null){
			url = "headRepastStatistics/query?currentPage=1&parameters['orderTimeStart']=" + timeNow + "&parameters['orderTimeEnd']" + timeNow;
			globalService.doGet(url,null,function(response){
				$scope.orderListTd = response.body;
				$scope.partStore = false;
			})
		} else {
			url = "shopRepastStatistics/query?currentPage=1&parameters['storeId']=" + $scope.select + "&parameters['ordertimeNowtart']="+ timeNow +"&parameters['ordertimeNownd']=" + timeNow + "&parameters['isClearing']=1";
			globalService.doGet(url,null,function(response){
				$scope.orderListPart = response.body;
				$scope.partStore = true;

				globalService.paginationInit(".pagination",response.totalRow,function(newPage){
					globalService.doGet("shopRepastStatistics/query?currentPage=" + newPage + "&parameters['storeId']=" + $scope.select + "&parameters['ordertimeNowtart']="+ timeNow +"&parameters['ordertimeNownd']=" + timeNow + "&parameters['isClearing']=1",null,function(response){
						$scope.orderListPart = response.body;
					})
				})
				$scope.getAboutOrder = function(order) {
					accountDetail.detail(order.id,function(response){
						$scope.todayPartDetail = response.body;
						$scope.todayPartDetail.source = order.source;
					});
				}
			})
		}
	}
}])

// 历史
app.controller('historySales',['$scope','$http','globalService','accountDetail',
function($scope,$http,globalService,accountDetail){
	$scope.order = false;
	$scope.store = true;
	$scope.orderList = [];
	$scope.orderListHi = [];
	$scope.orderListHiTemp = [];
	$scope.select = null;
	$scope.startTime = "";
	$scope.endTime = "";
	$scope.partStore = false;
	// 分页
	globalService.datetimepickerInit(".form_datetime",$scope,{
		format: 'yyyy-mm-dd',
		minView: 2,
		startView: 2
	});
	// 初始化加载
	$scope.getOrder = function(){
		globalService.doGet("headRepastStatistics/query?parameters['orderTimeStart']=2000-01-01&parameters['orderTimeEnd']=3000-12-31",null,function(response){
			$scope.orderListHi = response.body;
			$scope.orderListHiTemp = response.body;
		})
	}

	$scope.getOrder();
	// 下拉框进行搜索
	$scope.searchByStoreHi = function(){
		var url;
		$scope.startTime = "";
		$scope.endTime = "";

		if($scope.select == null){
			url = "headRepastStatistics/query?parameters['orderTimeStart']=2000-01-01&parameters['orderTimeEnd']=3000-12-31";
			globalService.doGet(url,null,function(response){
				$scope.orderListHi = response.body;
				$scope.partStore = false;
			})
		} else {
			url = "shopRepastStatistics/query?currentPage=1&parameters['storeId']=" + $scope.select + "&parameters['orderTimeStart']=2000-01-01&parameters['orderTimeEnd']=3000-12-31&parameters['isClearing']=1";
			globalService.doGet(url,null,function(response){
				$scope.orderListPart = response.body;
				$scope.partStore = true;

				globalService.paginationInit(".pagination",response.totalRow,function(newPage){
					globalService.doGet("shopRepastStatistics/query?currentPage=" + newPage + "&parameters['storeId']=" + $scope.select + "&parameters['orderTimeStart']=2000-01-01&parameters['orderTimeEnd']=3000-12-31&parameters['isClearing']=1",null,function(response){
						$scope.orderListPart = response.body;
					})
				})
				$scope.getAboutOrder = function(order) {
					accountDetail.detail(order.id,function(response){
						$scope.todayPartDetail = response.body;
						$scope.todayPartDetail.source = order.source;
					});
				}
			})
		}
	}
	// 按时间搜索
	$scope.searchByTime = function(startTime,endTime){
		if($scope.select == null){
			globalService.doGet("headRepastStatistics/query?parameters['orderTimeStart']="+ startTime +"&parameters['orderTimeEnd']=" + endTime,null,function(response){
				$scope.orderListHi = response.body;
			})
		} else {
			globalService.doGet("shopRepastStatistics/query?currentPage=1&parameters['storeId']="+ $scope.select +"&parameters['orderTimeStart']=" + startTime + "&parameters['orderTimeEnd']=" + endTime + "&parameters['isClearing']=1",null,function(response){
				$scope.orderListPart = response.body;

				globalService.paginationInit(".pagination",response.totalRow,function(newPage){
					globalService.doGet("shopRepastStatistics/query?currentPage=" + newPage + "&parameters['storeId']=" + $scope.select + "&parameters['orderTimeStart']=2000-01-01&parameters['orderTimeEnd']=3000-12-31&parameters['isClearing']=1",null,function(response){
						$scope.orderListPart = response.body;
					})
				})
			})
		}
		
	}
}])


// 过滤名称
app.filter('sourceFilt',function(){
	return function(item){
		if(item == 0) return "芭夯直达";
		else if(item == 4) return "店内点餐";
		else if(item == 1) return "来一火";
		else if(item == 2) return "美团";
		else if(item == 3) return "电话订单";
		else return null;
	}
})

// 支付方式过滤
app.filter('pay',function(){
	return function(item){
		if(item == 0) return "支付宝";
		else if (item == 1) return "微信支付";
	}
})