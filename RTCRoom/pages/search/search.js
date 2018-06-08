const app = getApp();
const config = require('../../config.js');
Page({
    data: {
        userInfo: {}
    },
    onAllSearch: function (e) {
        wx.redirectTo({
            url: '/pages/loading/loading?type=all',
        })

    },
    onBoySearch: function (e) {
        var _this = this;
        wx.showModal({
            title: '提示',
            content: '是否花费2钻石搜索男生',
            success: function (res) {
                if (res.confirm) {
                    _this.payDiamond('boy');
                }
            }
        });


    },
    onGirlSearch: function (e) {
        var _this = this;
        wx.showModal({
            title: '提示',
            content: '是否花费2钻石搜索女生',
            success: function (res) {
                if (res.confirm) {
                    _this.payDiamond('girl');
                }
            }
        });


    },
    onCitySearch: function (e) {
        wx.redirectTo({
            url: '/pages/loading/loading?type=city',
        })
    },
    payDiamond: function (itype) {
        var _this=this;
        wx.request({
            url: config.url + '/payDiamond?itype=' + itype+'&seqId='+_this.data.userInfo.seqId,
            success: function (res) {
                if (res.data.success) {
                    wx.redirectTo({
                        url: '/pages/loading/loading?type=' + itype,
                    })
                } else {
                    _this.showAlert('提示', res.data.message);
                }
            }
        })
    },
    showAlert: function (title, text) {
        wx.showModal({
            title: title,
            content: text,
            showCancel: false,
            confirmText: '知道了',
            success: function (res) {

            }
        });
    },
    onLoad: function (options) {
        var _this = this;
        _this.setData({
            userInfo: app.globalData.userInfo
        })
    }
})