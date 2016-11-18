app.controller('founding', ['$scope', '$state', '$stateParams', 'globalService',
	function($scope, $state, $stateParams, globalService){
		$scope.dishesClassList = []; //菜品类别
		$scope.dishesList = {};
		$scope.dishesOrder = {  //点菜单数据及标识，标识为属性	
			total: 0,
			dishesLength: 0,
			dishesList: {}
		};

		$scope.foundingData = $stateParams; //开台相关信息
		
		// 点菜单初始化，回调提交点菜单菜品详情
		$scope.submitOrder = function() {
			if ($scope.dishesOrder.dishesLength !== 0 ) {
				globalService.doPost('repastMenu', {
					storeId: loginInfo.storeId,
					repastFormId: $scope.foundingData.repastFormId,
					status: 1,
					isClearing: 0
				}, function(res) {
					var requestData = [];
					var dishesList = $scope.dishesOrder.dishesList;
					for (var prop in dishesList) {
						var obj = {
							storeId: loginInfo.storeId,
							repastFormId: $scope.foundingData.repastFormId,
							repastMenuId: res.body,
							shopDishesId: dishesList[prop].id,
							name: dishesList[prop].name,
							dishesNum: dishesList[prop].number,
							dishesPrice: dishesList[prop].price
						};
						requestData.push(obj);
					}
					globalService.doPost('repastDishesBatch', JSON.stringify(requestData),
						function(res) {
							alert(res.message);
							$state.go('^.detail', {
								id: $scope.foundingData.deskNum
							});
						});
				});
			}
		};

		//点菜时菜品数量处理
		$scope.dishesNumber = function(method, which) {
			var select = $scope.dishesOrder.dishesList[which];
			if (method === 0) {
				select.number--;
				if (select.number === 0) {
					var _id = 'dishes' + which;
					document.getElementById(_id).checked = false;
					delete $scope.dishesOrder.dishesList[which];
					$scope.dishesOrder.dishesLength--;
				}
			} else {
				select.number++;
				$scope.dishesOrder.dishesLength++;
			}
			totalPrice();
		};

		//添加或删除点菜单中的菜品
		$scope.dealDishes = function(obj, evt) {
			var checked = evt.target.checked;
			var list = $scope.dishesOrder.dishesList;
			if(checked) {
				if (obj.id in list) 
					return;
				var item = {
					id: obj.id,
					name: obj.name,
					price: obj.price,
					number: 1
				};
				list[obj.id] = item;
				$scope.dishesOrder.dishesLength++;
			} else {
				if (obj.id in list) {
					delete list[obj.id];
					$scope.dishesOrder.dishesLength--;
				}
			}
			totalPrice();
		};

		//计算点菜单总额
		function totalPrice() {
			var dishesList = $scope.dishesOrder.dishesList;
			$scope.dishesOrder.total = 0;
			for (var prop in dishesList) {
				var price = dishesList[prop].number * dishesList[prop].price;
				$scope.dishesOrder.total += price;
			}
		}

		// 查询具体类别的菜品列表事件
		$scope.queryDishes = function(id) {
			globalService.doGet('shopdishes/query', {
				'parameters["catagoryId"]': id,
				'parameters["shopId"]': loginInfo.storeId,
				maxRow: 100
			}, function(res) {
				$scope.dishesList[id] = res.body;
			});
		};

		// 查询菜品类别
		globalService.doGet('shopdishescate/query', {
			'parameters["shopId"]': loginInfo.storeId
		}, function(res) {
			$scope.dishesClassList = res.body;
			for (var i = 0, len = res.body.length; i < len; i++) {
				var name = res.body[i].id;
				$scope.dishesList[name] = [];
			}
		});
	}]);

app.controller('accountOrder', ['$scope', '$state', 'globalService',
	function($scope, $state, globalService) {
		$scope.foundingData = {}; //开台请求数据
		$scope.foundingData.isParlor = 0;

		$scope.deskList = []; //已使用桌子列表
		$scope.deskNum = ''; //将要查询的桌号
		$scope.deskInfo = {}; //桌子使用详情
		$scope.foundingTrigger = false;

		//申请开台触发
		$scope.foundingEvent = function() {
			// $scope.foundingTrigger = true;
			var form = $scope.founding;
			if (!(form.deskNum.$valid && form.repastNum.$valid && form.isParlor.$valid)) {
				return;
			}
			if ($scope.foundingData.isParlor == 1) {
				if (form.parlorAmount.$invalid) {
					return;
				}
			}
			$scope.foundingTrigger = true;
			$('#founding').modal('hide');
		};

		//开台请求
		$("#founding").on('hidden.bs.modal', function() {
			if ($scope.foundingTrigger) {
				$scope.foundingData.status = 1;
				$scope.foundingData.storeId = loginInfo.storeId;
				if ($scope.foundingData.isParlor != 1) {
					delete $scope.foundingData.parlorAmount;
				}
				globalService.doPost('repastForm', $scope.foundingData, function(res) {
					$state.go('^.founding', {
						repastFormId: res.body,
						deskNum: $scope.foundingData.deskNum,
						repastNum: $scope.foundingData.repastNum,
						isParlor: $scope.foundingData.isParlor
					});
				});
				$scope.foundingTrigger = false;
			}
		});

		// 查询已使用桌子
		globalService.doGet('repastForm/query', {
				'parameters["storeId"]': loginInfo.storeId
			},
			function(res) {
				$scope.deskList = res.body;
				//分页初始化
				globalService.paginationInit('.pagination' ,res.totalRow, function(newPage) {
					globalService.doGet('repastForm/query', {
						currentPage: newPage
					},function(res) {
						$scope.deskList = res.body;
					});
				});
			});

		//查询桌子使用情况
		globalService.doGet('storeDeskInfo', {
				storeId: loginInfo.storeId
			}, function(res) {
				$scope.deskInfo = res.body;
			});

		//查询对应桌号详情
		$scope.orderDetailEvent = function() {
			var deskNumLabel = $scope.queryByDeskNum.deskNum; 
			if (deskNumLabel.$valid && deskNumLabel.$modelValue) {
				$state.go('^.detail', {
					id: $scope.deskNum
				});
			}
		};
	}]);

app.controller('orderAccount', ['$scope', '$stateParams', '$state', 'globalService',
	function($scope, $stateParams, $state, globalService){
		$scope.deskNum = ''; //查询桌号
		$scope.hasOrderDetail = false; //用于标识是否已有数据
		$scope.orderDetail = {}; //订单详情
		$scope.finalAmount = 0;
		$scope.orderDishesList = [];	//订单里点菜单详情
		$scope.deskNumInit = false; //是否已查询
		$scope.accountLabel = { //是否选择折扣标志
			self: false,
			discountBranch: false,
			discount: false,	//折扣标志
			coupon: false //代金券标志
		};
		$scope.discountList = []; //折扣列表
		$scope.discountBranchList = []; //折扣列表分店
		$scope.discountData = {}; //折扣信息
		$scope.payData = {};	//付款信息
		$scope.payLabel = false;
		$scope.paySuccess = false;

		if ($stateParams.id) {
			$scope.deskNumInit = true;
			$scope.deskNum = parseInt($stateParams.id);
			globalService.doGet('repastFormDetailByNum', {
					deskNum: $stateParams.id,
					storeId: loginInfo.storeId
				}, function(res){
					if (res.body.parlorAmount) {
						
						$scope.finalAmount = res.body.totalAmount;
					}
					$scope.orderDetail = res.body;
					$scope.finalAmount = res.body.totalAmount;
					$scope.orderDishesList = res.body.repastMenuList;
					$scope.hasOrderDetail = true;
				});
		}

		$scope.accountLabelChange = function(str) {
			$scope.discountData = {};
			for (var prop in $scope.accountLabel) {
				if (prop == str || prop == 'self') {
					continue;
				}
				$scope.accountLabel[prop] = false;
			}
		};

		$scope.canPay = function() {
			if ($scope.accountLabel.self) {
				$scope.payLabel = true;
			} else {
				$scope.payLabel = false;
			}
		};

		// 查询桌号对应点菜单
		$scope.orderDetailEvent = function() {
			if ($scope.queryByDeskNum.deskNum.$valid) {
				globalService.doGet('repastFormDetailByNum', {
						deskNum: $scope.deskNum,
						storeId: loginInfo.storeId
					}, function(res){
						if (res.body.parlorAmount) {
							
							$scope.finalAmount = res.body.totalAmount;
						}
						$scope.orderDetail = res.body;
						$scope.finalAmount = res.body.totalAmount;
						$scope.orderDishesList = res.body.repastMenuList;
						$scope.hasOrderDetail = true;
					});
			}
		};

		// 请求总店折扣信息
		globalService.doGet('discount/query', {
				'parameters["shopId"]': 0
			},
			function(res) {
				$scope.discountList = res.body;
			});

		// 请求分店折扣信息
		globalService.doGet('discount/query', {
				'parameters["shopId"]': loginInfo.storeId
			}, function(res) {
				$scope.discountBranchList = res.body;
			});

		// 发送折扣信息
		$scope.discountEvent = function() {
			var req = {
				formId: $scope.orderDetail.id
			};
			var hasDiscout = false;
			for (var prop in $scope.discountData) {
				if ($scope.accountLabel[prop]) {
					hasDiscout = true;
					if ($scope.discountData[prop] == 0) {
						alert('请选择打折方案！');
						return;
					}
					req.discountId = $scope.discountData[prop].id;
					globalService.doGet('repastForm/discountAmount', req,
						function(res) {
							$scope.accountLabel.self = false;
							$scope.payLabel = false;
							$scope.finalAmount = res.body;
						});
				}
			}
			if (!hasDiscout) {
				alert('请选择打折方案！');
			}
		};

		// 发送付款请求
		$scope.payEvent = function(evt) {
			$scope.payData['repast_form_id'] = $scope.orderDetail.id;
			$scope.payData['total_fee'] = $scope.finalAmount;
			$scope.payData['title'] = '芭夯兔支付';
			var btn = $('button[name=payBtn]');
			btn.html('付款中...');
			globalService.doPost('pay', $.param($scope.payData),
				function(res) {
					alert(res.message);
					$('#account').modal('hide');
					btn.html('付款');
					$scope.payData = {};
					$scope.paySuccess = true;
				}, function(res) {
					alert(res.message);
					$('#account').modal('hide');
					btn.html('付款');
					$scope.payData = {};
				}, {
					headers: {'Content-Type': 'application/x-www-form-urlencoded'}
				});
		};

		$("#account").on('hidden.bs.modal', function() {
			if ($scope.paySuccess) {
				$scope.paySuccess = false;
				$state.go('^.order');
			}
		});

		// 现金付款确认
		$scope.payCash = function() {
			var paid = confirm('现金付款确认！');
			if (paid) {
				var data = $.param({
					repast_form_id: $scope.orderDetail.id
				});
				globalService.doPost('payCash', data,
					function(res) {
						alert(res.message);
						$state.go('^.order');
					}, null, {
						headers: {'Content-Type': 'application/x-www-form-urlencoded'}
					});
			}
		};

		$scope.focus = function() {
			$('input[name="auth_code"]').focus();
		};
	}]);

app.controller('accountDetail', ['$scope', '$state', '$stateParams', 'globalService',
	function($scope, $state, $stateParams, globalService) {
		$scope.detail = {}; //该桌号点菜单详情
		$scope.orderDishesList = []; //该桌号点菜单菜品详情
		$scope.id = $stateParams.id; //桌号
		$scope.hasOrderDetail = false;

		// 获取该桌号点菜单详情
		globalService.doGet('repastFormDetailByNum', {
				deskNum: $stateParams.id,
				storeId: loginInfo.storeId
			}, function(res) {
				if (res.body.parlorAmount) {
					
				}
				$scope.detail = res.body;
				$scope.orderDishesList = res.body.repastMenuList;
				$scope.hasOrderDetail = true;
			}, function(res) {
				alert(res.message);
				$state.go('^.order');
			});

		$scope.orderDishes = function() {
			$state.go('^.founding', {
				repastFormId: $scope.detail.id,
				deskNum: $scope.detail.deskNum,
				repastNum: $scope.detail.repastNum,
				isParlor: $scope.detail.isParlor
			});
		};
	}]);