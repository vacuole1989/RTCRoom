var myApp = new Framework7({
    animateNavBackIcon: true,
    precompileTemplates: true,
    template7Pages: true,
    modalTitle: '提示',
    modalButtonOk: '是',
    modalButtonCancel: '否',
    modalPreloaderTitle: '加载中……',
    template7Data: {},
    modalPasswordPlaceholder:''
});
var $$ = Dom7;
var view1 = myApp.addView('#view-1', {
    dynamicNavbar: true
});
var view2 = myApp.addView('#view-2', {
    dynamicNavbar: true
});
var view3 = myApp.addView('#view-3');
var view4 = myApp.addView('#view-4');

var changeSwitch = function (event) {
    var icheck = event.target.checked;
    var tag = $$(event.target).data('cc');
    var param = {'icheck': icheck, 'tag': tag};
    $$.ajax({
        url: 'page/changeSwitch',
        type: 'post',
        dataType: 'json',
        data: JSON.stringify(param),
        contentType: "application/json",
        success: function (data) {
            if (data.success) {
                location.reload();
            } else {
                myApp.alert(data.message);
            }
        }
    });
};
var signIn = function () {
    var formData = myApp.formToJSON('#login-form');
    $$.ajax({
        url: 'page/login',
        type: 'post',
        dataType: 'json',
        data: JSON.stringify(formData),
        contentType: "application/json",
        success: function (data) {
            if (data.success) {
                localStorage.loginUser = data.message.loginName;
                myApp.closeModal($$('.login-screen'));
                location.reload();
            } else {
                myApp.alert(data.message);
            }
        }
    });
};
var loginOut = function () {
    myApp.confirm('是否退出？', '提示', function () {
        $$.ajax({
            url: 'page/loginOut',
            type: 'post',
            dataType: 'json',
            contentType: "application/json",
            success: function (data) {
                localStorage.removeItem('loginUser');
                myApp.loginScreen();
            }
        });
    })
};
$$('.login-btn').on('click', signIn);

var saveFeeDetail = function () {
    var formData = myApp.formToJSON('#fee-detail-form');
    console.info(formData);
    $$.ajax({
        url: 'page/saveFeeDetail',
        type: 'post',
        dataType: 'json',
        data: JSON.stringify(formData),
        contentType: "application/json",
        success: function (data) {
            if (data.success) {
                myApp.template7Data.fees = data.message;
                view2.router.back();
            } else {
                myApp.alert(data.message);
            }
        }
    });
};


if (undefined === localStorage.loginUser) {
    myApp.loginScreen();
}

function initData() {

    $$.ajax({
        url: 'page/initData',
        type: 'post',
        dataType: 'json',
        contentType: "application/json",
        success: function (data) {
            if (data.success) {
                myApp.template7Data.fees = data.message;
                //获取模板
                var template = $$('#view1fees').html();
                // 编译模板
                var compiledTemplate = Template7.compile(template);
                // 使用模板加载数据
                var htmlStr = compiledTemplate(myApp.template7Data);
                //将得到的结果输出到指定区域
                $$("#view-1").html(htmlStr);

                $$('#userTitle').html('当前登录：' + localStorage.loginUser);
                $$('#insShow').attr('src', data.data.baseStr);
            } else {
                localStorage.removeItem('loginUser');
                myApp.loginScreen();
            }
        }
    });
}


function readFile(file) {
    var reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = function (e) {
        var param = {'baseStr': e.target.result};
        $$.ajax({
            url: 'page/uploadFile',
            type: 'post',
            dataType: 'json',
            data: JSON.stringify(param),
            contentType: "application/json",
            success: function (data) {
                if (data.success) {
                    $$('#insfile').val('');
                    $$('#insShow').attr('src', data.data.baseStr);
                } else {
                    myApp.alert(data.message);
                }
            }
        });
    }

}

function uploadimg() {
    var file1 = $$('#insfile').val();
    if (file1 == null || file1 == '') {
        myApp.alert('请先选择一张图片');
    } else {
        var file = document.getElementById('insfile');
        if (file.files && file.files[0] && file.files[0].size > 2 * 1024 * 1024) {
            myApp.alert('最大只能上传2M的文件');
            return false;
        }

        readFile(file.files[0]);

    }
}

function editPwd() {
    myApp.modalPassword('请输入要修改的密码', '密码修改', function (password) {
        var param = {'userPass': password};
        $$.ajax({
            url: 'page/editPwd',
            type: 'post',
            dataType: 'json',
            data: JSON.stringify(param),
            contentType: "application/json",
            success: function (data) {
                if (data.success) {
                    myApp.alert(data.message, function () {
                        localStorage.removeItem('loginUser');
                        myApp.loginScreen();
                    });

                } else {
                    myApp.alert(data.message);
                }
            }
        });
    });
}

initData();

document.onkeydown = function (e) {
    if ($$('.login-screen').css('display') == 'block') {
        if (e.keyCode == 13) {
            signIn();
        }
    }
}


