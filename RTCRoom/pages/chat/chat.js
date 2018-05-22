const app = getApp();
var config = require('../../config.js');
Page({
    data: {
        friends: []
    },
    onLoad: function () {
        var _this = this;
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