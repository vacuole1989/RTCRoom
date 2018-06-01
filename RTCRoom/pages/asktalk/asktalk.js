const app = getApp();
var config = require('../../config.js');

Page({

    /**
     * 页面的初始数据
     */
    data: {
        time: 60,
        userInfo: {},
        timer: null,
        pageHide: false,
        pageFinish: false
    },
    onCloseClick: function () {
        wx.navigateBack();
    },
    munTime: function () {
        this.timer = setTimeout(function () {
            if (this.data.time > 0) {
                var tt = this.data.time - 1;
                this.setData({
                    time: tt
                });
                this.munTime();
            }
        }.bind(this), 1000);
    },
    onLoad: function (options) {
        console.info('load')
        var _this = this;
        _this.setData({
            userInfo: app.globalData.userInfo
        })
        _this.setData({
            pageHide: false
        })
        _this.cycleAsked(options);
    },
    cycleAsked: function (options) {
        var _this = this;
        wx.request({
            url: config.url + '/onCharVideoCycle?seqId=' + options.seqId,
            success: function (res) {
                if (res.data.success) {
                    app.globalData.userInfo.playUrl = res.data.data.playUrl;
                    app.globalData.contactUserInfo = res.data.data.contactUserInfo;
                    app.globalData.askUser = res.data.data.askUser;
                    app.globalData.stopTime = 60;
                    app.globalData.fromChat = true;
                    wx.redirectTo({
                        url: '../talk/talk',
                    })
                }
            },
            complete: function () {
                if (!_this.data.pageFinish) {
                    _this.cycleAsked(options);
                }
            }
        })
    },
    onReady: function () {
        console.info('ready')
        wx.setKeepScreenOn({
            keepScreenOn: true,
        })
    },
    onShow: function () {
        // this.cycleMsg();

        wx.setKeepScreenOn({
            keepScreenOn: true
        })

        this.setData({
            pageHide: false
        })

    },
    onHide: function () {
        console.info('hide')
        clearTimeout(this.data.timer);
        this.setData({
            pageHide: true
        })
    },
    onUnload: function () {
        console.info('unload')
        this.setData({
            pageFinish: true
        })
        wx.setKeepScreenOn({
            keepScreenOn: false,
        })
    },
    onShareAppMessage: function () {

    }
})