const app = getApp();
const config = require('../../config.js');
Page({
    data: {
        friends: [],
        msgCount:0
    },
    cycleMsg: function () {
        setTimeout(function () {
            var _this = this;
            _this.setData({
                msgCount: app.globalData.msgCount
            })
            _this.cycleMsg();
        }.bind(this), 1000);
    },
    onLoad: function () {
        var _this = this;
        _this.setData({
            msgCount: app.globalData.msgCount
        })
        _this.cycleMsg();
        wx.login({
            success: function (res) {
                wx.request({
                    url: config.url + '/getFriends?code=' + res.code,
                    success: function (ress) {
                        _this.setData({
                            friends: ress.data.data
                        })
                    }
                })
            }
        })
    }
})