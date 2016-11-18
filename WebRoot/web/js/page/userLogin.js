/**
 * Created by Administrator on 2015/11/23.
 */
var storeInfo = {};
$(function() {
    var oldValue = '';
    $('.submit').click(loginListener);
    $('.form-control')
        .keydown(function(evt) {
            if (evt.which === 13) {
                oldValue = $(this).val();
            }
        })
        .keyup(function(evt) {
            var newValue = $(this).val();
            //判断是否是从历史中读取值，是则不触发登陆事件
            if (evt.which === 13 && newValue === oldValue) {
                loginListener();
            }
        });

    function loginListener() {

        var formData = {
            username: $('input[name=username]').val(),
            password: $('input[name=password]').val()
        };
        $.ajax({
            url: '../../../api/user/login',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(formData),
            dataType: 'json',
            success: function(res) {

                if (res.status === 0) {
                    res = res.body;
                    sessionStorage.setItem('username', res.username);
                    sessionStorage.setItem('userId', res.id);
                    location.href = '/web/html/page/userIndex.html';
                } else {
                    alert("登陆失败");
                }
            }
        });
    }
});