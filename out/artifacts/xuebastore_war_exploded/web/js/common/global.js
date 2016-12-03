var app = angular.module('app', ['ui.router']);
var loginInfo = {
	storeId: sessionStorage.getItem('storeId'),
	storeName: sessionStorage.getItem('storeName'),
	username: sessionStorage.getItem('username'),
	userId: sessionStorage.getItem('userId')
};
app.factory('mService',function($http){
	return{
		uploadImg:uploadImg,
		doPatch:doPatch,            //patch请求方式
		doDelete:doDelete          //delete请求方式
	};

	function loadScript() {
		var script = document.createElement("script");
		script.type = "text/javascript";
		script.src = "../../libs/TXmap.js";
		document.body.appendChild(script);
		console.log(script);
	}

	function uploadImg(file,callback){
		var ImgType=file[0].type;
		if(/(gif|jpg|jpeg|png|GIF|JPG|PNG)/.test(ImgType)){
			var Img=new FormData();
			Img.append('file',file[0]);
			$http.post('../../api/upload',Img,{
				withCredentials: true,
				headers: {'Content-Type': undefined },
				transformRequest: angular.identity
			}).success(function(response){
				if(response.status==0){
					callback(response);
				}
				alert(response.message);
			}).error(function(data,status){
				alert(status+':'+data);
			});
		}else{
			alert('传送格式有误！');
		}
	}

	function map(){
		var center=new qq.maps.LatLng(30.66667,104.06667);
		var map = new qq.maps.Map(document.getElementById("qqMap"),{
			center: center,
			zoom: 4
		});
		qq.maps.event.addListener(map, 'click', function(event) {
			sessionStorage.lat=event.latLng.lat;
			sessionStorage.lng=event.latLng.lng;
			if(sessionStorage.lat&&sessionStorage.lng){
				alert('已抓取经纬度!');
			}
		});
	}

	function doPatch(url,data,callback){
		$http({
			method:'patch',
			url:'../../api/'+url,
			data:data
		}).success(function(response){
			if(response.status==0){
				callback(response);
			}
			if(response.message==null){
				alert('修改成功！');
			}else{
				alert(response.message);
			}
		}).error(function(data,status){
			alert(status+':'+data);
		});
	}

	function doDelete(url,id,callback){
		var con=confirm('是否确认删除？');
		if(con){
			$http({
				method:'delete',
				url:'../../api/'+url+'/delete/'+id
			}).success(function(response){
				if(response.status==0){
					callback(response);
				}
				alert(response.message);
			}).error(function(data,status){
				alert(status+':'+data);
			});
		}
	}
});
app.factory('globalService', ['$location', '$http', function($location, $http) {
	var apiPath = '/api/';

	function resCallback(res, callback, err) {
		if (res.status === 0) {
			callback(res);
		} else if (res.status === 13) {
			location.href = '../../html/page/login.html';
		} else {
			if (err) {
				err(res);
			} else {
				alert(res.message);
			}
		}
	}

	return {
		getHtmlName: function() {
			var url = $location.absUrl(),
				html = '';
			url = url.split('#');
			url = url[0].split('/');
			url = url[url.length - 1].split('.');
			htmlName = url[0];
			return htmlName;
		},

		getPathName: function() {
			var pathName = $location.path();
			return pathName;
		},

		datetimepickerInit: function(selector, scope, config) {
			$.fn.datetimepicker.dates['chinese'] = {
				days: ["周日", "周一", "周二", "周三", "周四", "周五", "周六", "周日"],
				daysShort: ["周日", "周一", "周二", "周三", "周四", "周五", "周六", "周日"],
				daysMin: ["周日", "周一", "周二", "周三", "周四", "周五", "周六", "周日"],
				months: ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"],
				monthsShort: ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"],
				meridiem: ['am', 'pm'],
				today: "今天"
			};
			var options = {
					format: 'yyyy-mm-dd hh:ii',
					startView: 1,
					language: 'chinese',
					autoclose: true,
					pickerPosition: "bottom-left"
				};
			if (config) {
				$.extend(options, config);
			}
			$(selector).datetimepicker(options)
				.on('changeDate', function(ev) {
					var target = ev.currentTarget.firstElementChild;
					var modelPath = $(target).attr('ng-model');
					var value = target.value;
					if (modelPath) {
						scope[modelPath] = value;
						scope.$apply();
					}
				});
		},

		doPost: function(requestPath, requestData, callback, err, config) { //发送请求
			requestPath = apiPath + requestPath;
			if (config === null) {
				config = {};
			}
			return $http.post(requestPath, requestData, config)
				.success(function(response) {
					resCallback(response, callback, err);
				});
				// .error(function(data, status) {
				// 	alert('status::::::' + status);
				// });
		},

		doPatch: function(requestPath, requestData, callback) { //发送请求
			requestPath = apiPath + requestPath;
			return $http.patch(requestPath, requestData)
				.success(function(response) {
					resCallback(response, callback);
				});
				// .error(function(data, status) {
				// 	alert('status::::::' + status);
				// });
		},

		doDelete: function(requestPath, requestData, callback) { //发送请求
			requestPath = apiPath + requestPath;
			var config = {
				method: 'DELETE',
				url: requestPath + '/' + requestData
			};
			return $http(config)
				.success(function(response) {
					resCallback(response, callback);
				});
				// .error(function(data, status) {
				// 	alert('status::::::' + status);
				// });
		},

		doGet: function(requestPath, requestData, callback, err) {
			var params = '';
			requestPath = apiPath + requestPath;
			if (requestData != null) {
				params = '?';
				for (var prop in requestData) {
					if (requestData[prop] == null || requestData[prop] == 'all')
						continue;
					params += (prop + '=' + requestData[prop] + '&');
				}
			}
			params = params.slice(0, -1);
			var config = {
				method: 'GET',
				url: requestPath + params
			};
			return $http(config)
				.success(function(response) {
					resCallback(response, callback, err);
				});
				 //.error(function(data, status) {
				 //	alert('Status::::::' + status);
				 //});
		},

		doHeadRequest: function(requestPath, requestData, callback) { //发送带头部token的请求
			requestPath = apiPath + requestPath;
			var config = {
				method: 'POST',
				url: requestPath,
				headers: {
					'token': window.token
				},
				data: requestData
			};
			return $http(config)
				.success(function(response) {
					resCallback(response, callback);
				});
				// .error(function(data, status) {
				// 	alert('Status::::::' + status);
				// });
		},

		paginationInit: function(selector, totalRow, callback) {
			var init = true;
			$(selector).pagination(totalRow, {
				items_per_page: 5,
				num_display_entries: 7,
				current_page: 0,
				next_text: '»',
				prev_text: '«',
				callback: function(newPage, container) {
					// event.preventDefault();
					newPage++;
					if (!init) {
						callback(newPage);
					} else {
						init = false;
					}
					return false;
				}
			});
		}
	};

}]);



app.controller('aside', ['$scope', 'globalService',
	function($scope, globalService) {
		$scope.nav = {};
		$scope.nav['total'] = [
		// {
		// 	state: 'order',
		// 	content: '订单管理'
		// }, 
		{
			state: 'users',
			content: '用户管理'
		}, 
		{
			state: 'flights',
			content: '航班信息管理'
		}, {
			state: 'activity',
			content: '飞友会长信息管理'
		}, {
			state: 'accountManage',
			content: '账目管理'
		}, {
			state: 'rights',
			content: '权限管理'
		}];

		$scope.nav['part'] = [{
			state: 'store',
			content: '门店管理',
		}, {
			state: 'dishes',
			content: '菜品管理'
		}, {
			state: 'account',
			content: '点餐结算'
		}, {
			state: 'orderInfo',
			content: '预定'
		}, {
			state: 'activity',
			content: '活动管理'
		}, {
			state: 'accountManage',
			content: '账目管理'
		}];

		$scope.htmlName = globalService.getHtmlName();
	}
]);

app.controller('asideUser', ['$scope', 'globalService',
	function($scope, globalService) {
		$scope.nav = {};
		$scope.nav['total'] = [
			{
				state: 'flights',
				content: '查看航班信息'
			},
			{
				state: 'flightsRecommend',
				content: '航班推荐'
			},
			{
				state: 'allPlanes',
				content: '查看机型信息'
			},
			//{
			//	state: 'activity',
			//	content: '飞友会长信息管理'
			//}, {
			//	state: 'accountManage',
			//	content: '账目管理'
			//}, {
			//	state: 'rights',
			//	content: '权限管理'
			//}
             ];
		$scope.htmlName = globalService.getHtmlName();
	}
]);

app.controller('header', ['$scope', 'globalService',
	function($scope, globalService){
		$scope.loginInfo = loginInfo;

		$scope.logout = function(evt) {
			evt.preventDefault();
			globalService.doGet('admin/loginOut', {}, function(res) {
				alert(res.message);
				sessionStorage.clear();
				location.href = '/web/html/page/login.html';
			});
		};
	}]);

app.controller('userHeader', ['$scope', 'globalService',
	function($scope, globalService){
		$scope.loginInfo = loginInfo;

		$scope.logout = function(evt) {
			evt.preventDefault();
			globalService.doGet('user/loginOut', {}, function(res) {
				alert(res.message);
				sessionStorage.clear();
				location.href = '/web/html/page/userLogin.html';
			});
		};
	}]);