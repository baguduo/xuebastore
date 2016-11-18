app.controller('orderInfo',['$scope','$http','globalService','$state',function($scope,$http,globalService,$state){
	$scope.orderInfo = {
		orderInfoAbout : []
	};
	$scope.tel = "";
	$scope.changeOrderInfo = {
		"id"         : "",
		"name"       : "",
		"tel"        : "",
		"guestsamount": "",
		"ordertime"  : "",
		"arrivetime" : "",
		"remarks"    : ""
	};
	
	globalService.doGet("appointmentAdministration/query?currentPage=1",null,function(response){
		$scope.orderInfo.orderInfoAbout = response.body

		globalService.paginationInit('.pagination',response.totalRow,function(newPage){
			globalService.doGet("appointmentAdministration/query?currentPage=" + newPage,null,function(response){
				$scope.orderInfo.orderInfoAbout = response.body
			})
		})
	})

    globalService.datetimepickerInit('.form_datetime',$scope);
    
    $scope.searchOrderInfo = function(tel){
    	var setData = {
    		"tel" : tel
    	}

		globalService.doGet("appointmentAdministration/getByTel/" + tel,setData,function(response){
			$scope.orderInfo.orderInfoAbout.splice(0,$scope.orderInfo.orderInfoAbout.length-1);
			$scope.orderInfo.orderInfoAbout[0] = response.body
		})
    }
	$scope.changeOrder = function(orderInfoAbout,$index){
		if(orderInfoAbout.arrivetime != null){
			var date = new Date(orderInfoAbout.arrivetime);
		} else {
			var date = new Date();
		}
		
		var Y = date.getFullYear() + '-';
		var M = (date.getMonth()+1 < 10 ? '0' + (date.getMonth()+1) : date.getMonth()+1) + '-';
		var D = (date.getDate()    < 10 ? '0' + (date.getDate())    : date.getDate())    + ' ';
		var h = (date.getHours()   < 10 ? '0' + (date.getHours())   : date.getHours())   + ':';
		var m = (date.getMinutes() < 10 ? '0' + (date.getMinutes()) : date.getMinutes());

		$scope.changeOrderInfo.id         = orderInfoAbout.id;
		$scope.changeOrderInfo.name       = orderInfoAbout.name;
		$scope.changeOrderInfo.tel        = orderInfoAbout.tel;
		$scope.changeOrderInfo.guestsamount = orderInfoAbout.guestsamount;
		$scope.changeOrderInfo.ordertime  = orderInfoAbout.ordertime;
		$scope.changeOrderInfo.arrivetime = Y+M+D+h+m;
		$scope.changeOrderInfo.remarks    = orderInfoAbout.remarks;
		
		$scope.orderInfoSure = function(changeOrderInfo){
			var setData = {
				"id"           : changeOrderInfo.id,
				"name"         : changeOrderInfo.name,
				"tel"          : changeOrderInfo.tel,
				"guestsamount" : changeOrderInfo.guestsamount,
				"ordertime"    : changeOrderInfo.ordertime,
				"arrivetime"   : changeOrderInfo.arrivetime.replace(/(\s)/,"T") + ":00.000+0800",
				"remarks"      : changeOrderInfo.remarks
			}

			globalService.doPatch("appointmentAdministration/",setData,function(response){
				alert(response.message);
				orderInfoAbout.id         = changeOrderInfo.id ;
				orderInfoAbout.name       = changeOrderInfo.name;
				orderInfoAbout.tel        = changeOrderInfo.tel;
				orderInfoAbout.guestsamount = changeOrderInfo.guestsamount;
				orderInfoAbout.ordertime  = changeOrderInfo.ordertime;
				orderInfoAbout.arrivetime = changeOrderInfo.arrivetime ;
				orderInfoAbout.remarks    = changeOrderInfo.remarks;
			})
		};
	}

	

	$scope.orderInfoDel = function(orderInfoAbout){
		globalService.doDelete("appointmentAdministration",orderInfoAbout.id,function(response){
			alert(response.message);
			$state.reload();
		})
	}
}])					

