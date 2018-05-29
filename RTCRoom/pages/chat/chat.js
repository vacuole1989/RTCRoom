const app = getApp();
const config = require('../../config.js');
Page({
    data: {
        friends: [],
        msgCount: {},
        pageHide: false
    },
    onLoad: function () {
        this.setData({
            pageHide: false
        })
    },
    onUnload: function () {
        this.setData({
            pageHide: true
        })
    },
    onShow: function () {
        var _this = this;
        this.setData({
            pageHide: false
        })
        wx.login({
            success: function (res) {
                wx.request({
                    url: config.url + '/getFriends?seqId=' + app.globalData.userInfo.seqId,
                    success: function (ress) {
                        _this.setData({
                            friends: ress.data.data
                        })
                    }
                })
            }
        })
        wx.request({
            url: config.url + '/getFriendTipCount?seqId=' + app.globalData.userInfo.seqId,
            success: function (res) {
                _this.setData({
                    msgCount: res.data.data
                })
            }
        })
    },
    onHide: function () {
        this.setData({
            pageHide: true
        })
    }
})