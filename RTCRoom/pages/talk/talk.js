const app = getApp();
var config = require('../../config.js');
Page({

    /**
     * 页面的初始数据
     */
    data: {
        livePushContext: {},
        livePlayContext: {},
        pushUrl: "",
        playUrl: "",
        timer: null
    },
    onHeartBeat: function () {
        setTimeout(function () {
            var _this = this;
            wx.request({
                url: config.url + '/heartbeat?seqId=' + app.globalData.userInfo.seqId,
                success: function (res) {
                    _this.onHeartBeat();
                }
            });
        }.bind(this), 1000);
    },
    onPushInit: function () {
        var _this = this;
        _this.setData({
            pushUrl: app.globalData.userInfo.pushUrl,
            playUrl: app.globalData.userInfo.playUrl
        });
        _this.data.livePushContext.start();
        _this.data.livePlayContext.play();
    },
    onPushEvent: function (e) {
        if (e.detail.code == -1307) {
            this.data.livePushContext.stop();
            this.data.livePlayContext.stop();
            wx.showToast({
                title: '推流多次失败',
            })
        }
        if (e.detail.code == 5000) {
            this.data.livePushContext.stop();
            this.data.livePlayContext.stop()
            wx.showToast({
                title: '用户关闭小程序',
            })
        }

    },
    createContext: function () {
        this.setData({
            livePushContext: wx.createLivePusherContext('camera-push')
        })
        this.setData({
            livePlayContext: wx.createLivePlayerContext('video-player')
        })
    },
    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {

    },
    /**
     * 生命周期函数--监听页面初次渲染完成
     */
    onReady: function () {
        this.createContext();
        this.onPushInit();
        this.onHeartBeat();
        wx.setKeepScreenOn({
            keepScreenOn: true,
        })
    },
    /**
     * 生命周期函数--监听页面显示
     */
    onShow: function () {
        if (this.data.livePushContext) {
            this.data.livePushContext.play();
        }
        // 保持屏幕常亮
        wx.setKeepScreenOn({
            keepScreenOn: true
        })
    },
    /**
     * 生命周期函数--监听页面隐藏
     */
    onHide: function () {
        wx.setKeepScreenOn({
            keepScreenOn: false,
        });
    },

    /**
     * 生命周期函数--监听页面卸载
     */
    onUnload: function () {
        if (this.data.livePushContext.stop) {
            this.data.livePushContext.stop();
        }
        if (this.data.livePlayContext.stop) {
            this.data.livePlayContext.stop();
        }
        wx.setKeepScreenOn({
            keepScreenOn: false,
        });
    }
})