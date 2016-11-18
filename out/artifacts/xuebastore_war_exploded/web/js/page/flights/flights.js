/**
 * Created by Administrator on 2016/11/13.
 */

//总店门户信息控制器
app.controller('flights',['$scope','$http','$state','globalService','mService',function($scope,$http,$state,globalService,mService){
    var vm=this;
    vm.data={};
    //vm.searchUser=searchUser;    //按店名搜索门店
    //vm.userSelect=userSelect;    //获取下拉框下的所有门店
    //vm.selectUser=selectUser;   //下拉选择门店
    vm.getFlight=getFlight;
    vm.setFlight=setFlight;
    vm.deleteFlight=deleteFlight;
    //vm.getActivity=getActivity;
    vm.totalRow=0;

    getFlight();
    //storeSelect();

    //下拉菜单
    //function userSelect(){
    //    globalService.doGet('user/list',null,function(response){
    //        vm.data.userSelect=response.body;
    //    });
    //}

    /*获取门店信息*/
    function getFlight(data){
        globalService.doGet('flight/query',data,function(response){
            vm.data.flightList=response.body;
            vm.totalRow=response.totalRow;
            /*分页*/
            globalService.paginationInit($('.pagination'),vm.totalRow,function(currentPage){
                var data={
                    currentPage:currentPage
                };
                globalService.doGet('flight/query',data,function(response){
                    vm.totalRow=response.totalRow;
                    vm.data.flightList=response.body;
                });
            });
        });
    }

    //function selectUser(id){
    //    var data={
    //        "parameters['id']":id
    //    };
    //    getUser(data);
    //}
    //
    //function searchUser(name){
    //    var data={
    //        name:name
    //    };
    //    globalService.doGet('queryLikeName',data,function(response){
    //        var arry=[];
    //        arry.push(response.body);
    //        vm.data.userList=arry;
    //        vm.totalRow=response.totalRow;
    //        /*分页*/
    //        globalService.paginationInit($('.pagination'),vm.totalRow,function(currentPage){
    //            var data={
    //                currentPage:currentPage
    //            };
    //            globalService.doGet('user/query',data,function(response){
    //                vm.totalRow=response.totalRow;
    //                vm.data.userList=response.body;
    //            });
    //        });
    //    });
    //}
    //
    /*修改航班信息*/
    function setFlight(flightItem){
            var data={
                "id":flightItem.id,
                "fltno":flightItem.fltno,
                "airline":flightItem.airline,
                "type":flightItem.type,
                "takeoff":flightItem.takeoff,
                "landing":flightItem.landing,
                "date":flightItem.date,
                "takeofftime":flightItem.takeofftime,
                "landingtime":flightItem.landingtime,
                "planeid":flightItem.planeid
            };
            mService.doPatch('flight/update',data,function(response){
                flightItem.isSet=false;
                //flightItem.status=status;
                //getUser(vm.userId);
            });
    }

    function deleteFlight(id){
        mService.doDelete('flight',id,function(response){
            $state.reload();
        });
    }
    //
    //function getActivity(id){
    //    vm.activityList=[];
    //    vm.isLoaded=false;
    //    var data={
    //        "parameters['userId']":id
    //    };
    //    globalService.doGet('discount/query',data,function(response){
    //        vm.activityList=response.body;
    //        vm.isLoaded=true;
    //    });
    //}

}]);

/*新建门店*/
app.controller('addFlight',['$scope','$state','globalService','mService',function($scope,$state,globalService,mService){
    var vm=this;
    vm.addFlight=addFlight;
    //vm.openPage=openPage;
    vm.flightData={};
    $scope.uploadImg=uploadImg;


    /*上传图片*/
    function uploadImg(file){
        mService.uploadImg(file,function(response){
            vm.storeData.imgUrl='../..'+response.body;
        });
    }

    function openPage(){
        window.open('http://api.map.soso.com/doc_v2/tooles/picker.html');
    }

    /*新建航班信息*/
    function addFlight(flightData,boolean){
        if(!boolean){
            var data={
                "fltno":flightData.fltNo,
                "date":flightData.date,
                "airline":flightData.airline,
                "takeofftime":flightData.takeoffTime,
                "landingtime":flightData.landingTime,
                "type":flightData.type,
                "planeid":flightData.planeId,
                "takeoff":flightData.takeoff,
                "landing":flightData.landing,
                "punctuality":flightData.punctuality

                //"name":storeData.storeName,
                //"address": storeData.address,
                //"phone": storeData.phone,
                //"managerName":storeData.username ,
                //"status":0 ,     //默认是正在营业
                //"pictureUrl": storeData.imgUrl,
                //"gpsLongitude": storeData.gpsLongitude, //经度
                //"gpsLatitude": storeData.gpsLatitude,    //纬度
                //managerNamePhone:storeData.managerNamePhone
            };
            globalService.doPost('flight/add',data,function(response){
                alert('保存成功');
                $state.go('flights');
            });
        }else{
            var alertStr='';
            if(!flightData.fltNo){
                alertStr='航班号';
            }
            if(!flightData.date){
                alertStr='日期';
            }
            if(!flightData.airline){
                alertStr='航空公司';
            }
            if(!flightData.takeoffTime){
                alertStr='起飞时间';
            }
            if(!flightData.landingTime){
                alertStr='降落时间';
            }
            if(!flightData.type){
                alertStr='机型';
            }
            if(!flightData.planeId){
                alertStr='飞机编号';
            }
            if(!flightData.takeoff){
                alertStr='起飞';
            }
            if(!flightData.landing){
                alertStr='降落';
            }
            if(!flightData.punctuality){
                alertStr='准点率';
            }
            alert(alertStr+'出错！');
        }
    }
}]);

/*门店详情*/
//app.controller('storeInfo',['$scope','$http','$state','$location','globalService','mService',function($scope,$http,$state,$location,globalService,mService){
//    var vm=this;
//    vm.storeId=($location.url().split('/').slice(-1)).toString();
//    vm.isSet=false;
//    vm.storeData={};
//    vm.getStore=getStore;
//    vm.setStore=setStore;
//    $scope.uploadImg=uploadImg;
//
//    //loadScript();
//    getStore(vm.storeId);
//
//    /*获取门店信息*/
//    function getStore(id){
//        globalService.doGet('store/'+id,null,function(response){
//            vm.storeData=response.body;
//        });
//    }
//
//    /*上传图片*/
//    function uploadImg(file){
//        mService.uploadImg(file,function(response){
//            vm.storeData.pictureUrl='../..'+response.body;
//        });
//    }
//
//    /*修改门店*/
//    function setStore(storeData,boolean){
//        console.log(boolean);
//        if(!boolean){
//            var data={
//                "id":vm.storeId,
//                "name": storeData.name,
//                "address": storeData.address,
//                "phone": storeData.phone,
//                "managerName":storeData.managerName,
//                pictureUrl:storeData.pictureUrl,
//                //"status": 0,  //默认是正在营业
//                //"gpsLongitude":storeData.gpsLongitude,
//                //"gpsLatitude":storeData.gpsLatitude,
//                managerNamePhone:storeData.managerNamePhone
//            };
//            mService.doPatch('store',data,function(response){
//                vm.isSet=false;
//                //getStore(vm.storeId);
//                $state.reload();
//            });
//        }else{
//            alert('表单格式有错误！');
//        }
//    }
//}]);

//app.controller('tableSetting',['$scope','$http','globalService','mService',function($scope,$http,globalService,mService){
//    var vm=this;
//    vm.setCount=setCount;
//    vm.getStore=getStore;
//    vm.shopId=loginInfo.storeId;
//
//    getStore(vm.shopId);
//
//    function setCount(storeData){
//        var data={
//            id:vm.shopId,
//            parlorCount:storeData.parlorCount,
//            deskCount:storeData.deskCount
//        };
//        mService.doPatch('store',data,function(response){
//            vm.isSet=false;
//        });
//    }
//
//    function getStore(id){
//        globalService.doGet('store/'+id,null,function(response){
//            vm.storeData=response.body;
//            console.log(vm.storeData.deskCount);
//        });
//    }
//}]);

