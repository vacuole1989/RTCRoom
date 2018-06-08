
const app = getApp();
var config = require('../../config.js');
Page({
    data: {
        userInfo: {},
        hasUserInfo: false
    },
    onSettingTap: function () {
        wx.navigateTo({
            url: '/pages/setting/setting',
        })
    },
    onBuyTap:function(){
        wx.navigateTo({
            url: '/pages/buy/buy'
        })
    },
    onLoad: function (options) {
        var _this = this;
        if (app.globalData.userInfo) {
            this.setData({
                userInfo: app.globalData.userInfo,
                hasUserInfo: true
            })
        } else {
            wx.getUserInfo({
                success: res => {
                    app.globalData.userInfo = res.userInfo
                    this.setData({
                        userInfo: res.userInfo,
                        hasUserInfo: true
                    })
                }
            })
        }
    }
})