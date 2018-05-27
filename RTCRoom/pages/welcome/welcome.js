const app = getApp();
var config = require('../../config.js');
Page({

    /**
     * 页面的初始数据
     */
    data: { jumpTime: 0, hasLogin: false },
    munTime: function () {
        this.timer = setTimeout(function () {
            if (this.data.jumpTime > 0) {
                var tt = this.data.jumpTime - 1;
                this.setData({
                    jumpTime: tt
                });
                this.munTime();
            } else {
                if (this.data.hasLogin) {
                    this.onJump();
                } else {
                    this.onLogin(true);
                }
            }
        }.bind(this), 1000);
    },
    onJump: function () {

        if (app.globalData.userInfo.inited) {
            wx.redirectTo({
                url: '../index/index',
            });
        } else {
            wx.redirectTo({
                url: '../initpage/initpage',
            });
        }
    },
    onLogin: function (show) {
        var _this = this;
        if (show) {
            wx.showLoading({
                title: '登录中',
            });
        }
        wx.getUserInfo({
            success: res => {
                app.globalData.userInfo = res.userInfo
                wx.login({
                    success: function (res) {
                        if (res.code) {
                            //发起网络请求
                            wx.request({
                                method: 'POST',
                                url: config.url + '/onLogin?code=' + res.code,
                                data: app.globalData.userInfo,
                                success: function (retdata) {
                                    app.globalData.userInfo = retdata.data.data;
                                    _this.setData({
                                        hasLogin: true
                                    });
                                    if (show) {
                                        _this.onJump();
                                        wx.hideLoading();
                                    }

                                }
                            })
                        } else {
                            console.log('登录失败！' + res.errMsg)
                        }
                    }
                });
            }
        });
    },
    onLoad: function (options) {
        var _this = this;
        _this.munTime();
        _this.onLogin(false);


    }
})