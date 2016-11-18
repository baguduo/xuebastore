app.controller('userInfo',['$scope','$http','globalService','$state',function($scope,$http,globalService,$state){
	$scope.formData = "";
	$scope.userData = {
		user : []
	};


	globalService.doGet("user/query?currentPage=1",null,function(response){
		$scope.userData.user = response.body;

		globalService.paginationInit(".pagination",response.totalRows,function(newpage){
			globalService.doGet("user/query?currentPage=" + newpage,null,function(response){
				$scope.userData.user = response.body;
			})
		})
	})

	$scope.findUser = function(formData) {
		var setData = {
			parameters : formData
		};

		globalService.doGet("user/query?currentPage=1&&parameters['xxx']=" + setData,setData,function(response){
			
		})
	}

	$scope.userDel = function(user) {
		globalService.doDelete("",user.id,function(response){
			alert(response.message);
			$state.reload();
		})
	}

	$scope.userChange = function(user) {
		
	}
}])

