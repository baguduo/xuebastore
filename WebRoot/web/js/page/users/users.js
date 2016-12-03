/**
 * Created by Administrator on 2015/11/23.
 */

//分店门户信息控制器
//app.controller('storeBranch',['$scope','$http','globalService','mService',function($scope,$http,globalService,mService){
//    var vm=this;
//    vm.isSet=false;
//    vm.storeData={};
//    vm.shopDaysData=[];
//    vm.getStore=getStore;
//    vm.getOtherInfo=getOtherInfo;
//    vm.setting=setting;
//    vm.checkDate=checkDate;
//    vm.shopId=loginInfo.storeId;
//
//    getStore(vm.shopId);
//
//    /*搜索门店*/
//    function getStore(id){
//        globalService.doGet('store/'+id,null,function(response){
//            vm.storeData=response.body;
//            var shopHourStr=response.body.shopHours;//营业时间
//            var shopDayStr=response.body.shopDays; //营业日
//            if(shopDayStr){
//                vm.shopDaysData=shopDayStr.split(',');
//            }
//            if(shopHourStr){
//                var shopArray=shopHourStr.split(',');
//                vm.storeData.shopType=shopArray[0];
//                vm.storeData.shopTime=shopArray[1];
//            }else{
//                vm.storeData.shopType='a';
//            }
//        });
//    }
//
//
//    /*基本设置*/
//    function getOtherInfo(data){
//        var data={
//            id:data.id,
//            remark:data.remark,
//            phone:data.phone
//        }
//        mService.doPatch('store',data,function(response){
//            vm.isSet=false;
//        });
//    }
//
//    /*营业设置*/
//    function setting(data){
//        var shopHours=data.shopType;
//        if(data.shopTime){
//            shopHours+=','+data.shopTime;
//        }
//        var shopDays=vm.shopDaysData.join(',');
//        var data={
//            id:data.id,
//            shopHours:shopHours,
//            shopDays:shopDays
//        };
//        mService.doPatch('store',data,function(response){
//            vm.isSet=false;
//        });
//    }
//
//    //多选日期
//    function checkDate(day){
//        var n=vm.shopDaysData.indexOf(day);
//        if(n==-1){   //营业时间中没有
//            vm.shopDaysData.push(day);
//            vm.shopDaysData.sort();
//        }else{
//            vm.shopDaysData.splice(n,1);
//        }
//        console.log(vm.shopDaysData);
//    }
//
//
//}]);

//用户信息控制器
app.controller('users',['$scope','$http','$state','globalService','mService',function($scope,$http,$state,globalService,mService){
    var vm=this;
    vm.data={};
    //vm.searchUser=searchUser;    //按店名搜索门店
    //vm.userSelect=userSelect;    //获取下拉框下的所有门店
    //vm.selectUser=selectUser;   //下拉选择门店
    vm.getUser=getUser;
    //vm.setStore=setStore;
    vm.deleteUser=deleteUser;
    //vm.getActivity=getActivity;
    vm.totalRow=0;

    getUser();
    //storeSelect();

    //下拉菜单
    //function userSelect(){
    //    globalService.doGet('user/list',null,function(response){
    //        vm.data.userSelect=response.body;
    //    });
    //}

    /*获取用户信息*/
    function getUser(data){
        globalService.doGet('user/query',data,function(response){
            vm.data.userList=response.body;
            vm.totalRow=response.totalRow;
            /*分页*/
            globalService.paginationInit($('.pagination'),vm.totalRow,function(currentPage){
                var data={
                    currentPage:currentPage
                };
                globalService.doGet('user/query',data,function(response){
                    vm.totalRow=response.totalRow;
                    vm.data.userList=response.body;
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
    ///*修改门店*/
    //function setUser(userItem,status){
    //        var data={
    //            id:userItem.id,
    //            "status":status
    //        };
    //        mService.doPatch('user',data,function(response){
    //            userItem.status=status;
    //            getUser(vm.userId);
    //        });
    //}
    //
    function deleteUser(id){
        mService.doDelete('user',id,function(response){
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

/*用户注册*/
app.controller('addUser',['$scope','$state','globalService','mService',function($scope,$state,globalService,mService){
    var vm=this;
    vm.addUser=addUser;
    //vm.openPage=openPage;
    vm.userData={};
    //$scope.uploadImg=uploadImg;


    /*上传图片*/
    //function uploadImg(file){
    //    mService.uploadImg(file,function(response){
    //        vm.storeData.imgUrl='../..'+response.body;
    //    });
    //}

    function openPage(){
        window.open('http://api.map.soso.com/doc_v2/tooles/picker.html');
    }

    /*新建门店*/
    function addUser(userData,boolean){
        if(!boolean){
            var data={
                "username":userData.username,
                "password": userData.password,
                "phone": userData.phone,
                "address": userData.address,
                "remark":userData.remark
            };
            globalService.doPost('user/add',data,function(response){
                alert('注册成功');
                $state.go('users');
            });
        }else{
            var alertStr='';
            if(!userData.username){
                alertStr='用户名';
            }
            if(!userData.password){
                alertStr='密码';
            }
            if(!userData.phone){
                alertStr='联系电话';
            }
            if(!userData.address){
                alertStr='住址';
            }
            if(!userData.remark){
                alertStr='飞友会';
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

