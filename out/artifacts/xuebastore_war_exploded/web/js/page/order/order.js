app.factory('orderService', ['globalService', 
	function(globalService){
		return {
			query: function() {
				
			}
		};
	}]);

app.controller('allOrder', ['$scope', 'globalService', 
	function($scope, globalService) {
		$scope.orderList = [];
		$scope.storeList = [];
		$scope.statusList = [];
		$scope.queryData = {
			source: 'all',
			storeId: 'all',
			status: 'all',
			orderTimeStart: '',
			orderTimeEnd: ''
		};

		var queryOrderUrl = '../api/order/query';
		var getStoreListUrl = '../api/store/list';

		function dealQueryData(res) {
			$scope.orderList = res.body;
		}

		function dealStoreListData(res) {
			$scope.storeList = res.body;
		}

		$scope.queryOrder = function() {
			getOrderReq = {
				currentPage: 1,
				'parameters["source"]': $scope.queryData['source'],
				'parameters["storeId"]': $scope.queryData['storeId'],
				'parameters["status"]': $scope.queryData['status'],
				'parameters["orderTimeStart"]': $scope.queryData['orderTimeStart'],
				'parameters["orderTimeEnd"]': $scope.queryData['orderTimeEnd']
			};
			globalService.doGet(queryOrderUrl, getOrderReq, dealQueryData);
		};

		globalService.datetimepickerInit('.form_datetime', $scope);
		globalService.doGet(getStoreListUrl, {}, dealStoreListData);
		globalService.doGet(queryOrderUrl, {
			currentPage: 1
		}, dealQueryData);
}]);

app.controller('historyOrder', ['$scope', 'globalService', 
	function($scope, globalService) {
		$scope.orderList = [];
		$scope.storeList = [];
		$scope.statusList = [];
		$scope.queryData = {
			source: 'all',
			orderTimeStart: '',
			orderTimeEnd: ''
		};

		var queryOrderUrl = '../api/order/query';
		var getStoreListUrl = '../api/store/list';

		function dealQueryData(res) {
			$scope.orderList = res.body;
		}

		function dealStoreListData(res) {
			$scope.storeList = res.body;
		}

		$scope.queryOrder = function() {
			getOrderReq = {
				currentPage: 1,
				'parameters["source"]': $scope.queryData['source'],
				'parameters["orderTimeStart"]': $scope.queryData['orderTimeStart'],
				'parameters["orderTimeEnd"]': $scope.queryData['orderTimeEnd']
			};
			globalService.doGet(queryOrderUrl, getOrderReq, dealQueryData);
		};

		globalService.datetimepickerInit('.form_datetime', $scope);
		globalService.doGet(queryOrderUrl, {
			currentPage: 1
		}, dealQueryData);
}]);

app.controller('orderReciving', ['$scope', 'globalService', 
	function($scope, globalService) {
		var queryOrderUrl = '../api/order/query';
		$scope.queryData = 1;
		$scope.orderList = [];

		function dealOrderData(res) {
			$scope.orderList = res.body;
		}

		globalService.doGet(queryOrderUrl, {'parameters["status"]': 1}, dealOrderData);

		$scope.$watch('queryData', function() {
			globalService.doGet(queryOrderUrl, {'parameters["status"]': $scope.queryData}, dealOrderData);
		});
	}]);

app.controller('reserveOrder', ['$scope', 'globalService', 
	function($scope, globalService) {
		var queryOrderUrl = '../api/order/query';
		$scope.queryData = 1;
		$scope.orderList = [];

		function dealOrderData(res) {
			$scope.orderList = res.body;
		}

		globalService.doGet(queryOrderUrl, {'parameters["status"]': 1}, dealOrderData);

		$scope.$watch('queryData', function() {
			globalService.doGet(queryOrderUrl, {'parameters["status"]': $scope.queryData}, dealOrderData);
		});
	}]);

app.controller('cancelOrder', ['$scope', 'globalService', 
	function($scope, globalService) {
		var queryOrderUrl = '../api/order/query';
		$scope.queryData = -1;
		$scope.orderList = [];

		function dealOrderData(res) {
			$scope.orderList = res.body;
		}

		globalService.doGet(queryOrderUrl, {'parameters["status"]': -1}, dealOrderData);

		$scope.$watch('queryData', function() {
			globalService.doGet(queryOrderUrl, {'parameters["status"]': $scope.queryData}, dealOrderData);
		});
	}]);

app.controller('addOrder', ['$scope', 'globalService',
	function($scope, globalService) {
		$scope.dishList = [];

		var getDishReq = {
			currentPage: 1
		};

		var getDishUrl = '../api/headdishes/query';

		function dealDishData(res) {
			$scope.dishList = res.body;
		}

		globalService.doGet(getDishUrl, getDishReq, dealDishData);
}]);