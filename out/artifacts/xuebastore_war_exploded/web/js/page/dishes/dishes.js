/**
 * Created by Administrator on 2015/11/24.
 */
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
                url:'../../api/'+url+'/'+id
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
/*总店菜品管理*/
app.controller('dishes',['$scope','$http','globalService','mService',function($scope,$http,globalService,mService){
    var vm=this;
    vm.getClass=getClass;
    vm.addClass=addClass;
    vm.addDish=addDish;
    vm.searchDishes=searchDishes;
    vm.setDish=setDish;
    vm.deleteDish=deleteDish;
    vm.setting=setting;
    vm.getDishImg=getDishImg;
    $scope.uploadImg=uploadImg;
    vm.classList={};
    vm.dishesList={};
    vm.dishData={};
    vm.dishImg='';
    vm.classId=null;
    vm.dishesTotalRow=0;

    getClass();

    /*分类列表*/
    function getClass(){
        globalService.doGet('headdishescate/query',null,function(response){
            vm.classTotalRow=response.totalRow;
            vm.classList=response.body;
        });
    }

    /*添加分类*/
    function addClass(className){
        if(className){
            var data={
                name:className,
                status:1,
                sort:1
            };
            globalService.doPost('headdishescate',data,function(response){
                if(response.status==0){
                    getClass();
                }
                alert(response.message);
            })
        }else{
            alert('分类名称出错啦');
        }

    }

    /*添加菜品*/
    function addDish(id,dishData){
        var judge=false;
        if(dishData.price&&dishData.unit&&dishData.stock&&dishData.description&&dishData.name){
            judge=true;
        }
        if(judge){
            var data={
                headDishesCatagoryId:id, //对应总店菜品分类id
                price:dishData.price,
                unit:dishData.unit,
                picUrl: vm.dishImg,
                stock:dishData.stock,
                description:dishData.description,
                status:1,         //默认都是下架状态
                name:dishData.name
            };
            globalService.doPost('headdishes',data,function(response){
                if(response.status==6){
                    alert(response.message+',该菜品名已用！');
                }else if(response.status==0){
                    alert(response.message);
                    vm.searchDishes(id);
                    vm.dishData.isAdd=false;
                    vm.dishData=[];
                    vm.dishImg='';
                }
            });
        }else{
            alert('请完善信息');
        }

    }

    /*查询分类下的菜品*/
    function searchDishes(catagoryId){
        var data={
            "parameters['catagoryId']":catagoryId
        };
        globalService.doGet('headdishes/query',data,function(response){
            vm.dishesList=response.body;
            vm.dishesTotalRow=response.totalRow;
            vm.classId=catagoryId;
            var str=('dishesPag'+catagoryId).toString();
            /*分页*/
            page(catagoryId,$('.'+str),vm.dishesTotalRow);
        });
    }

    /*分页*/
    function page(catagoryId,selector,totalRow){
        globalService.paginationInit(selector,totalRow,function(currentPage){
            var data={
                "parameters['catagoryId']":catagoryId,
                currentPage:currentPage
            };   
            globalService.doGet('headdishes/query',data,function(response){
                vm.dishesTotalRow=response.totalRow;
                vm.dishesList=response.body;
            });
        });
    }

    /*修改菜品*/
    function setDish(id,dishItem){
        console.log(vm.dishImg);
        var data={
            id:dishItem.id,
            price:dishItem.price,
            unit:dishItem.unit,
            picUrl:vm.dishImg,
            stock:dishItem.stock,
            description:dishItem.description,
            name:dishItem.name,
            status:dishItem.status
        };
        mService.doPatch('headdishes',data,function(response){
            searchDishes(id);
            dishItem.isSet=false;
            vm.dishImg='';
        });
    }

    function deleteDish(id,dishItem){
        mService.doDelete('headdishes',dishItem.id,function(response){
            searchDishes(id);
        });
    }

    /*上传图片*/
    function uploadImg(file){
        console.log(file);
        mService.uploadImg(file,function(response){
            vm.dishImg='../..'+response.body;
        });
    }

    function setting(dishItem){
        console.log(dishItem.picUrl);
        dishItem.isSet=true;
        vm.dishImg=dishItem.picUrl;
    }

    function getDishImg(dishItem){
        console.log(dishItem.picUrl);
        vm.dishImg=dishItem.picUrl;
        console.log(vm.dishImg);
    }
}]);

/*分店菜品管理*/
app.controller('dishesBranch',['$scope','$http','globalService','mService',function($scope,$http,globalService,mService){
    var vm=this;
    vm.getClass=getClass;
    vm.addDish=addDish;
    vm.searchDishes=searchDishes;
    vm.setDish=setDish;
    vm.getHeadDish=getHeadDish;
    vm.selectDish=selectDish;
    vm.deleteDish=deleteDish;
    vm.setting=setting;
    vm.getDishImg=getDishImg;
    $scope.uploadImg=uploadImg;
    vm.classList=[];
    vm.dishesList=[];
    vm.dishData={};
    vm.dishImg='';   //菜品图像路径，一次只有一个
    vm.selectData=[];
    vm.classId=null;  //删选菜品时的分类ID
    vm.shopId=loginInfo.storeId; //分店id
    vm.classTotalRow=0;
    vm.dishesTotalRow=0;

    getClass();

    /*分类列表*/
    function getClass(){
        var data={
            "parameters['shopId']":vm.shopId
        };
        globalService.doGet('shopdishescate/query',data,function(response){
            vm.classTotalRow=response.totalRow;
            vm.classList=response.body;
            console.log(vm.classList);
        });
    }

    function getDishImg(dishItem){
        console.log(dishItem.picUrl);
        vm.dishImg=dishItem.picUrl;
        console.log(vm.dishImg);
    }

    /*添加菜品*/
    function addDish(id,dishData){
        var judge=true;
        var alertStr='';
        if(!dishData.name){
            alertStr='菜品名字';
            judge=false;
        }
        if(!dishData.price){
            alertStr='价格';
            judge=false;
        }
        if(!dishData.unit){
            alertStr='价格';
            judge=false;
        }
        if(!dishData.description){
            alertStr='描述';
            judge=false;
        }
        if(!dishData.stock){
            alertStr='库存';
            judge=false;
        }
        if(judge){
            var data={
                shopId:vm.shopId,                    //分店id
                "shopDishesCatagoryId":id,  //对应分店菜品分类id
                "headDishesId":0, //对应总店菜品id,分店自创菜品该值填0 ?
                "isOriginal":1, //是否自创菜品， 1--是，0--不是  ?
                price:dishData.price,
                unit:dishData.unit,
                picUrl:vm.dishImg,
                stock:dishData.stock,
                description:dishData.description,
                status:1,         //默认都是下架状态
                name:'★'+dishData.name
            };
            globalService.doPost('shopdishes', data, function(response) {
                if (response.status == 6) {
                    alert(response.message + ',该菜品名已用！');
                } else if (response.status == 0) {
                    alert(response.message);
                    vm.searchDishes(id);
                    vm.dishData=[];
                    vm.dishImg='';
                }
            })
        }else{
            alert(alertStr+'有误！');
        }
    }

    /*查询分类下的菜品*/
    function searchDishes(catagoryId){
        var data={
            "parameters['catagoryId']":catagoryId,
            "parameters['shopId']":vm.shopId
        };
        globalService.doGet('shopdishes/query',data,function(response){
            vm.dishesList=response.body;
            vm.dishesTotalRow=response.totalRow;
            vm.classId=catagoryId;
            var str=('dishesPag'+catagoryId).toString();
            page(catagoryId,$('.'+str),vm.dishesTotalRow);
        });
    }

    /*分页*/
    function page(catagoryId,selector,totalRow){
        console.log(selector);
        globalService.paginationInit(selector,totalRow,function(currentPage){
            var data={
                "parameters['catagoryId']":catagoryId,
                currentPage:currentPage,
                "parameters['shopId']":vm.shopId
            };   
            globalService.doGet('shopdishes/query',data,function(response){
                vm.dishesTotalRow=response.totalRow;
                vm.dishesList=response.body;
            });
        });
    }

    /*修改菜品*/
    function setDish(id,dishItem){
        console.log(id,dishItem);
        dishItem.isSet=false;
        var data={
            id:dishItem.id,
            "headDishesId":dishItem.headDishesId||0, //对应总店菜品id,分店自创菜品该值填0  ?
            "shopId":vm.shopId,	 //分店id
            "shopDishesCatagoryId":id,  //对应分店菜品分类id
            price:dishItem.price, 	//菜品单价
            stock:dishItem.stock,	//菜品库存
            description:dishItem.description,//菜品描述
            name:dishItem.name,  //菜品名称
            "isOriginal":"1", //是否自创菜品， 1--是，0--不是
            picUrl:vm.dishImg,
            unit:dishItem.unit,  //菜品名称
            "status":dishItem.status //菜品状态
        };
        mService.doPatch('shopdishes',data,function(response){
            searchDishes(id);
            vm.dishImg='';
        });
    }
    function deleteDish(id,CategoryId){
        mService.doDelete('shopdishes',id,function(response){
            searchDishes(CategoryId);
        });
    }

    /*选择菜品*/
    function getHeadDish(catagoryId){
        var data={
            "parameters['catagoryId']":catagoryId
        };
        globalService.doGet('headdishes/query',data,function(response){
            vm.headDishesList=response.body;
        });
    }

    function selectDish(data){
        console.log(data);
        vm.dishes=[];
        for(var i=0;i<data.length;i++){
            if(data[i].isSelect){
                console.log(data[i].isSelect);
                delete data[i].isSelect;
                data[i].shopDishesCatagoryId=vm.catagoryId;
                delete data[i].headDishesCatagoryId;
                data[i].headDishesId=data[i].id;
                delete data[i].id;
                data[i].shopId=vm.shopId;
                vm.dishes.push(data[i]);
            }
        }
        globalService.doPost('shopdishesBatch',vm.dishes,function(response){
            vm.dishes=[];
            alert('筛选成功');
            console.log(vm.catagoryId);
            searchDishes(vm.catagoryId);
        });
    }

    /*上传图片*/
    function uploadImg(file){
        mService.uploadImg(file,function(response){
            vm.dishImg='../..'+response.body;
        });
    }

    function setting(dishItem){
        console.log(dishItem.picUrl);
        dishItem.isSet=true;
        vm.dishImg=dishItem.picUrl;
    }

    function getDishImg(dishItem){
        console.log(dishItem.picUrl);
        vm.dishImg=dishItem.picUrl;
        console.log(vm.dishImg);
    }
}]);


/*总店菜品分类管理*/
app.controller('dishesType',['$scope','$http','globalService','mService',function($scope,$http,globalService,mService){
    var vm=this;
    vm.getClass=getClass;
    vm.addClass=addClass;
    vm.setClass=setClass;
    vm.deleteClass=deleteClass;
    vm.classList={};
    vm.classTotalRow=0;

    getClass();

    /*分类列表*/
    function getClass(){
        globalService.doGet('headdishescate/query',null,function(response){
            vm.classTotalRow=response.totalRow;
            vm.classList=response.body;
        });
    }

    /*添加分类*/
    function addClass(className){
        if(className){
            var data={
                name:className,
                status:1,
                sort:1
            };
            globalService.doPost('headdishescate',data,function(response){
                if(response.status==0){
                    getClass();
                }
                alert(response.message);
            });
        }else{
            alert('添加分类失败！');
        }
    }

    /*修改分类*/
    function setClass(classItem){
        var data={
            "id":classItem.id, 	//菜品分类id
            "name":classItem.name,  //菜品分类名称
            "status":classItem.status,  //菜品分类状态
            "sort":classItem.sort	//菜品分类排序码
        };
        mService.doPatch('headdishescate',data,function(response){
            classItem.isSet=false;
        });
    }

    function deleteClass(id){
        mService.doDelete('headdishescate',id,function(response){
            getClass();
        });
    }


}]);



/*分店菜品分类管理*/
app.controller('dishesTypeBranch',['$scope','$http','globalService','mService',function($scope,$http,globalService,mService){
    var vm=this;
    vm.getClass=getClass;
    vm.setClass=setClass;
    vm.getHeadClass=getHeadClass;
    vm.selectClass=selectClass;
    vm.deleteClass=deleteClass;
    vm.classList={};
    vm.headClassList=[];
    vm.selectData=[];
    vm.shopId=loginInfo.storeId;
    vm.classTotalRow=0;

    getClass();

    /*查询分类*/
    function getClass(){
        var data={
            "parameters['shopId']":vm.shopId
        };
        globalService.doGet('shopdishescate/query',data,function(response){
            vm.classTotalRow=response.totalRow;
            vm.classList=response.body;
        });
    }

    /*修改分类*/
    function setClass(classData){
        var data={
            "headDishesCategoryId":classData.headDishesCategoryId||0,  //总店分类ID ？
            "id":classData.id, 	//菜品分类id
            "name":classData.name,  //菜品分类名称
            "status":classData.status,  //菜品分类状态
            "sort":classData.sort	//菜品分类排序码
        };
        mService.doPatch('shopdishescate',data,function(response){
            classData.isSet=false;
            getClass();
        });
    }

    /*获取总店分类*/
    function getHeadClass(){
        globalService.doGet('headdishescate/query',null,function(response){
            vm.headClassList=response.body;
        });
    }

    /*筛选分类*/

    function selectClass(target){
        for(var i=0;i<target.length;i++){
            if(target[i].isSelect){   //已选中
                delete target[i].isSelect;
                target[i].headDishesCategoryId=target[i].id;
                delete target[i].id;
                target[i].shopId=vm.shopId;
                vm.selectData.push(target[i]);
            }
        }
        globalService.doPost('shopdishescatesBatch',vm.selectData,function(response){
            vm.selectData=[];
            getClass();
            alert('筛选成功');
        });
    }

    function deleteClass(id){
        mService.doDelete('shopdishescate',id,function(response){
            getClass();
        });
    }
}]);