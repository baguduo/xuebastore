app.controller('active',['$scope','$state','globalService',function($scope,$state,globalService){
	$scope.changeActive = false; //修改框显示标志
	$scope.addActive = true;	//添加框显示标志

	$scope.activityData = {};	//活动列表

	$scope.dishesClass = {}; //菜品列表

	$scope.newActivity = {};	//添加或修改activity

	var reqDishesClass = {},
		reqDishesClassUrl = '';

	if (loginInfo.storeId == 0) {
		reqDishesClassUrl = 'headdishescate/query';
	} else {
		reqDishesClass = {
			'parameters[shopId]': loginInfo.storeId
		};
		reqDishesClassUrl = 'shopdishescate/query';
	}

	//获取折扣活动列表
	globalService.doGet('discount/query', {
			'parameters[shopId]': loginInfo.storeId
		}, function(res) {
			var data = res.body;
			if (data.length === 0) {
				globalService.datetimepickerInit('.form_datetime', $scope);
			}
			for (var i = 0, len = data.length; i < len; i++) {
				$scope.activityData[data[i].id] = data[i];
			}
			globalService.paginationInit('.pagination', res.totalRow, function(newPage) {
				globalService.doGet('discount/query', {
					'parameters[shopId]': loginInfo.storeId,
					currentPage: newPage
				},
				function(res) {
					var data = res.body;
					$scope.activityData = {};
					for (var i = 0, len = data.length; i < len; i++) {
						$scope.activityData[data[i].id] = data[i];
					}
				});
			});
		});

	//获取菜品分类
	globalService.doGet(reqDishesClassUrl, reqDishesClass,
		 function(res) {
			var data = res.body;
			for (var i = 0, len = data.length; i < len; i++) {
				$scope.dishesClass[data[i].id] = data[i];
			}
		});

	//修改或添加dishesClass处理
	$scope.dishesClassChange = function() {
		var newActivityDishes = $scope.newActivity.discountDishes;
		var show = '';
		for(var prop in newActivityDishes) {
			if (newActivityDishes[prop]) {
				var value = $scope.dishesClass[prop].name;
				show += (value + '; ');
			}
		}
		$scope.newActivity.discountDishesValue = show;
	};

	// 点击修改时，对原数据的处理
	$scope.activityChange = function(activityInfo){
		if ($scope.changing) {
			return;
		}
		$scope.changing = true;
		this.changeActive = true;
		$scope.addActive = false;
		for (var prop in activityInfo) {
			if (prop === 'discountDishes') {
				var arr = activityInfo[prop].split(';');
				var obj = {};
				for (var i = 0, len = arr.length - 1; i < len; i++) {
					obj[arr[i]] = parseInt(arr[i]);
				}
				$scope.newActivity[prop] = obj;
			} else {
				$scope.newActivity[prop] = activityInfo[prop];
			}
		}
		delete $scope.newActivity.modified;
	};

	//保存活动
	$scope.activitySave = function(operate, id) {
		var check = [
			{
				name: 'name',
				value: '名字'
			},{
				name: 'startTime',
				value: '开始时间'
			},{
				name: 'endTime',
				value: '结束时间'
			},{
				name: 'discountDetail',
				value: '详情'
			},{
				name: 'discountDishes',
				value: '菜品类别'
			},{
				name: 'discountValue',
				value: '折扣'
			}];
		var newActivity = $scope.newActivity;
		for (var i = 0, len = check.length; i < len; i++) {
			var name = check[i].name;
			var has = newActivity.hasOwnProperty(name);
			if (!has || newActivity[name].length === 0) {
				alert('请完善活动' + check[i].value + '!');
				return;
			}
			newActivity.discountValue = parseFloat(newActivity.discountValue);
			if (isNaN(newActivity.discountValue) || newActivity.discountValue <= 0 || newActivity.discountValue >= 10) {
				alert('折扣必须为0-10之间的数字！');
				return;
			}
		}
		var discountDishes = '';
		for (var prop in newActivity.discountDishes) {
			var value = newActivity.discountDishes[prop];
			if (value) {
				discountDishes += (value + ';');
			}
		}
		var _self = this;
		newActivity.discountDishes = discountDishes;
		if (operate === 0) { //修改
			globalService.doPatch('discount', JSON.stringify(newActivity),
				function(res) {
					alert(res.message);
					var thisActicity = $scope.activityData[id];
					for (var prop in newActivity) {
						thisActicity[prop] = newActivity[prop];
					}
					$scope.newActivity = {};
					_self.changeActive = false;
					$scope.addActive = true;
					$scope.changing = false;
				});
		}
		if (operate === 1) { //添加
			newActivity.shopId = loginInfo.storeId;
			globalService.doPost('discount', JSON.stringify(newActivity),
				function(res) {
					alert(res.message);
					$state.reload();
					// newActivity.id = res.body;
					// $scope.activityData[newActivity.id] = newActivity;
					// $scope.newActivity = {};
					// _self.changeActive = false;
					// $scope.addActive = true;
					// $scope.changing = false;
				});
		}
	};

	$scope.activityCancel = function() {
		$scope.newActivity = {};
		this.changeActive = false;
		$scope.addActive = true;
		$scope.changing = false;
	};

	//删除该条活动
	$scope.activeDel = function(id){
		var isDelete = confirm('是否删除？');
		if (isDelete) {
			globalService.doDelete('discount', id,
				function(res) {
					alert(res.message);
					$state.reload();
					// delete $scope.activityData[id];
				});
		}
	};

	$scope.$on('finish',function(){
		globalService.datetimepickerInit('.form_datetime', $scope);
	});
}]);


app.directive('repeat',function(){
	return {
		restrict : "A",
		link : function(scope){
			if(scope.$last){
				scope.$emit('finish');
			}
		}
	};
});

app.directive('tree',function(){
	return {
		restrict : "E",
		link : function(scope){

		}
	};
});