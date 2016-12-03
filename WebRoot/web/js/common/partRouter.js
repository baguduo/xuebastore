app.config(function($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.otherwise('/flights');
    $urlRouterProvider.when('/flights', 'flights/allflights');
    $urlRouterProvider.when('/order', 'order/allOrder');
    $urlRouterProvider.when('/accountManage', 'accountManage/todaySales');
    $stateProvider

        //航班信息路由
        .state('flights', {
            url: '/flights',
            templateUrl:'flights/index.html'
        })
        .state('flights.allflights', {
            url: '/allflights',
            views: {
                'child@flights': {
                    templateUrl: 'flights/allFlights.html'
                }
            }
        })
        .state('flights.takeoffnear', {
            url: '/takeoffnear',
            views: {
                'child@flights': {
                    templateUrl: 'flights/takeoffNearFlights.html'
                }
            }
        })
        .state('flights.landingnear', {
            url: '/landingnear',
            views: {
                'child@flights': {
                    templateUrl: 'flights/landingNearFlights.html'
                }
            }
        })
        .state('flightsRecommend', {
            url: '/flightsRecommend',
            templateUrl: 'flights/flightsRecommend.html'

        })
        .state('allPlanes', {
            url: '/allPlanes',
            templateUrl: 'planes/allPlanes.html'

        })

        //管理员路由
        //.state('users', {
        //    url: '/users',
        //    templateUrl: 'page/users/users.html'
        //})
        //.state('flights', {
        //    url: '/flights',
        //    templateUrl: 'page/flights/flights.html'
        //})
        //.state('addFlight', {
        //    url: '/addFlight',
        //    templateUrl: 'page/flights/addFlight.html'
        //})
        //.state('userInfo', {
        //	url: '/storeInfo/:id',
        //	templateUrl: 'users/storeInfo.html'
        //})







    //订单管理
    //.state('order', {
    //		url: '/order',
    //		views: {
    //			'': {
    //				templateUrl: 'page/order/index.html'
    //			},
    //			// 'child@order': {
    //			// 	controller: 'allOrder',
    //			// 	templateUrl: 'page/order/allOrder.html'
    //			// }
    //		}
    //	})
    //	.state('order.allOrder', {
    //		url: '/allOrder',
    //		views: {
    //			'child@order': {
    //				controller: 'allOrder',
    //				templateUrl: 'page/order/allOrder.html'
    //			}
    //		}
    //	})
    //	.state('order.addOrder', {
    //		url: '/addOrder',
    //		views: {
    //			'child@order': {
    //				controller: 'addOrder',
    //				templateUrl: 'page/order/addOrder.html'
    //			}
    //		}
    //	})
    //	.state('order.orderReciving', {
    //		url: '/orderReciving',
    //		views: {
    //			'child@order': {
    //				controller: 'orderReciving',
    //				templateUrl: 'page/order/orderReciving.html'
    //			}
    //		}
    //	})
    //	.state('order.reserveOrder', {
    //		url: '/reserveOrder',
    //		views: {
    //			'child@order': {
    //				controller: 'reserveOrder',
    //				templateUrl: 'page/order/reserveOrder.html'
    //			}
    //		}
    //	})
    //	.state('order.cancelOrder', {
    //		url: '/cancelOrder',
    //		views: {
    //			'child@order': {
    //				controller: 'cancelOrder',
    //				templateUrl: 'page/order/cancelOrder.html'
    //			}
    //		}
    //	})
    //	.state('order.historyOrder', {
    //		url: '/historyOrder',
    //		views: {
    //			'child@order': {
    //				controller: 'historyOrder',
    //				templateUrl: 'page/order/historyOrder.html'
    //			}
    //		}
    //	})
    //	.state('order.modifyOrder', {
    //		url: '/modifyOrder',
    //		views: {
    //			'child@order': {
    //				templateUrl: 'page/order/modifyOrder.html'
    //			}
    //		}
    //	})
    //	// 权限管理
    //	.state('rights', {
    //		url: '/rights',
    //		templateUrl: 'page/rights/rights.html'
    //	})
    //
    ////活动管理
    //.state('activity', {
    //	url: '/activity',
    //	templateUrl: 'page/activity/activity.html'
    //})
    //
    //.state('accountManage', {
    //		url: '/accountManage',
    //		templateUrl: 'page/accountManage/index.html'
    //	})
    //	.state('accountManage.todaySales', {
    //		url: "/todaySales",
    //		views: {
    //			'child@accountManage': {
    //				templateUrl: 'page/accountManage/todaySales.html'
    //			}
    //		}
    //	})
    //	.state('accountManage.historySales', {
    //		url: "/historySales",
    //		views: {
    //			'child@accountManage': {
    //				templateUrl: 'page/accountManage/historySales.html'
    //			}
    //		}
    //	});
    //总店路由结束


});