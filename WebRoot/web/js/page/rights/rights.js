app.controller('rights',['$scope','globalService','$state',function($scope,globalService,$state) {
	$scope.changeRight = false;//隐藏修改用户信息
	$scope.changePass  = true;//隐藏修改密码
	$scope.add         = false;//隐藏添加
	$scope.roles       = [
		{"name" : null},
		{"name" : 0},
		{"name" : 2}
	];//用户角色下拉菜单

	$scope.admin       = [];//用户列表
	$scope.rightStore  = [];//店铺列表
	$scope.addStore    = [];//添加管理员店铺列表

	$scope.isAbled     = false;//总管理员区别

	$scope.check       = {
		"isCheck"  : [],
		"checkId"  : ""
	}//选中的店铺相关信息

	$scope.adminAdd = {
		"id"       : "",
		"username" : "",
		"password" : "",
		"role"     : null,
		"storeId"  : ""
	};//新添加的角色

	globalService.doGet("store/query?maxRow=100",null,function(response){
		$scope.rightStore = response.body;
		$scope.addStore   = response.body;

		for(var i = 0;i < response.body.length;i++) {
			$scope.check.isCheck[i] = false;
		}
	});
	//获取用户列表
	globalService.doGet("admin/query?currentPage=1",null,function(response){
		$scope.admin = response.body;

		globalService.paginationInit('.pagination',response.totalRow,function(newPage){
			globalService.doGet("admin/query?currentPage=" + newPage,null,function(response){
				$scope.admin = response.body;
			});
		});			
	});

	// 判断是否是总店管理员
	$scope.isSuperAdmin = function(role) {
		this.isAbled = false;

		if(role == 0) {
			this.isAbled = true;
		}
	}

	//修改用户信息
	$scope.rightChange = function(admin) {
		this.changeRight = true;
		this.isAbled     = false;
		
		if(admin.role == 0) {
			this.isAbled = true;
		}
		// 保存旧的信息
		var oldData = {};

		for(var data in admin) {
			oldData[data] = admin[data];
		}
		// 取消修改
		$scope.rightCancel = function() {
			this.changeRight = false;

			for(var data in oldData) {
				admin[data] = oldData[data];
			}
		}	

		$scope.checkStore = function(store,$index) {
			for(var i = 0; i < $scope.check.isCheck.length;i++) {
				$scope.check.isCheck[i] = false;
			}
			$scope.check.isCheck[$index] = true;
			admin.storeId          = store.id;
			admin.storeName        = store.name;
		}
		// 保存修改
		$scope.rightSave = function(admin) {
			if(admin.username == "") {
				alert("请完善管理员名字");
				return false;
			} else if (admin.role == null) {
				alert("请完善管理员角色");
				return false;
			} else if(admin.role == 0) {
				admin.storeId = null;
			} else if(admin.role != 0) {
				if(admin.storeId == null) {
					alert("请完善管理员权限");
					return false;
				}
			}
			var thisAdmin = this;
			var setData = {
				"id"       : admin.id,
				"username" : admin.username,
				"role"     : admin.role,
				"storeId"  : admin.storeId
			};
			/*
			*@change admin
			*/ 
			globalService.doPatch("admin",setData,function(response) {
				thisAdmin.changeRight = false;
				alert(response.message);
				if(admin.role == 0) {
					admin.storeName = "";
				}
			})
		}
	}
	// 修改密码
	$scope.changePassWord = function(admin){
		var thisAdmin = this;
		var pass = admin.password;

		thisAdmin.changePass = false;
		admin.password = "";

		$scope.passCancel = function() {
			thisAdmin.changePass = true;
			admin.password = pass;
		}
		$scope.passSave = function(admin){
			if(admin.password == ""){
				alert("请完善管理员密码");
				return false;
			} else {
				var setData = {
					"id"       : admin.id,
					"password" : admin.password,
				};
				/*
				*@change admin
				*/ 
				globalService.doPatch("admin",setData,function(response){
					thisAdmin.changePass = true;
					alert(response.message);
				})
			}
		};
	}
	// 点击编辑进行数据绑定到模态板
	$scope.resetCheck = function(admin) {
		for(var i = 0;i < $scope.rightStore.length;i++) {
			var targetId = $scope.rightStore[i].id;
			if(admin.storeId == targetId || $scope.check.checkId == targetId) {
				$scope.check.isCheck[i] = true;
			} else {
				$scope.check.isCheck[i] = false;
			}
		}
	}
	// 删除管理员
	$scope.rightDel = function(admin){
		var conf = confirm("确认删除？");
		if(conf == true){
			globalService.doDelete("admin",admin.id,function(response){
				alert(response.message);
				$state.reload();
			})
		}
	}
	// 添加管理员
	$scope.rightsAdd = function(){
		this.add = true;

		$scope.checkStore = function(store,$index) {
			for(var i = 0; i < $scope.check.isCheck.length;i++) {
				$scope.check.isCheck[i] = false;
			}
			$scope.check.isCheck[$index]= true;
			$scope.check.checkId        = store.id;
		}
	};
	$scope.rightsAddSave = function(adminAdd){
		if(adminAdd.username == "") {
			alert("请完善管理员名字");
			return false;
		} else if (adminAdd.password == "") {
			alert ("请完善管理员密码");
			return false;
		} else if (adminAdd.role == null) {
			alert("请完善管理员角色");
			return false;
		} 

		if(adminAdd.role != 0) {
			if ($scope.check.checkId == "") {
				alert("请完善管理员权限");
				return false;
			}
		}

		var setData = {
			"username" : adminAdd.username,
			"password" : adminAdd.password,
			"role"     : adminAdd.role,
			"storeId"  : $scope.check.checkId
		}

		globalService.doPost("admin",setData,function(response){
			alert(response.message)
			$state.reload();
		})
	};
}])
// 过滤数字为管理员角色
app.filter('rightsFilt',function(){
	return function(item){
		if(item == 0) return "总店管理员";
		else if (item == 2) return "分店管理员";
		else return "角色选择";
	}
})
