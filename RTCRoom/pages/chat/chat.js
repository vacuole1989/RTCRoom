const app = getApp();
const config = require('../../config.js');
Page({
    data: {
        friends: [],
        msgCount: {},
        pageHide: false
    },
    cycleMsg: function () {
        setTimeout(function () {
            var _this = this;
            _this.setData({
                msgCount: app.globalData.msgCount
            })

            // if (!_this.data.pageHide) {
                _this.cycleMsg();
            // }
        }.bind(this), 1000);
    },
    onLoad: function () {
        this.cycleMsg();
        this.setData({
            pageHide: false
        })
        var _this = this;
        _this.setData({
            msgCount: app.globalData.msgCount
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
    },
    onUnload: function () {
        this.setData({
            pageHide: true
        })
    },
    onShow: function () {
        this.setData({
            pageHide: false
        })
        
    },
    onHide: function () {
        this.setData({
            pageHide: true
        })
    }
})