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
        timer: null,
        stopTime: 60
    },
    onPlayAll: function () {
        if (this.data.livePushContext.start) {
            this.data.livePushContext.start();
        }
        if (this.data.livePlayContext.play) {
            this.data.livePlayContext.play();
        }
    },
    onStopAll: function () {
        if (this.data.livePushContext.stop) {
            this.data.livePushContext.stop();
        }
        if (this.data.livePlayContext.stop) {
            this.data.livePlayContext.stop();
        }
    },
    munTime: function () {
      this.timer = setTimeout(function () {
        console.info(this.data.stopTime)
        if (this.data.stopTime > 0) {
          var tt = this.data.stopTime - 1;
          this.setData({
            stopTime: tt
          });
          this.munTime();
        }else{
          wx.navigateBack();
        }
      }.bind(this), 1000);
    },
    onHeartBeat: function () {
        var _this = this;
       
        wx.request({
          url: config.url + '/heartbeat?seqId=' + app.globalData.userInfo.seqId,
          success: function (res) {
            if(_this.data.stopTime>0){
              setTimeout(function () {
                this.onHeartBeat();
              }.bind(_this), 1000);
            }
          }
        });
    },
    onPushInit: function () {
        var _this = this;
        _this.setData({
            pushUrl: app.globalData.userInfo.pushUrl,
            playUrl: app.globalData.userInfo.playUrl
        });
        _this.onPlayAll();
        _this.onHeartBeat();
        _this.munTime();
    },
    onPushEvent: function (e) {
        console.info("推流状态：" + e.detail.code + "-" + e.detail.message);
        if (e.detail.code && (e.detail.code < 0 || e.detail.code > 3000)) {
            this.onStopAll();
        }

    },
    onPlayEvent: function (e) {
        console.info("播放器网络状态：" + e.detail.code + "-" + e.detail.message);
        if (e.detail.code && (e.detail.code > 3000 || e.detail.code < 0)) {

        }
    },
    createContext: function () {
        this.setData({
            livePushContext: wx.createLivePusherContext('camera-push')
        });
        this.setData({
            livePlayContext: wx.createLivePlayerContext('video-player')
        });
    },
    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        this.setData({
            stopTime:options.time
        });
    },

    /**
     * 生命周期函数--监听页面显示
     */
    onShow: function () {
        console.info("1聊天页面显示了");
        // this.onPlayAll();
        // 保持屏幕常亮
        wx.setKeepScreenOn({
            keepScreenOn: true
        })
    },
    /**
 * 生命周期函数--监听页面初次渲染完成
 */
    onReady: function () {
      console.info("2聊天页面准备好了");
      this.createContext();
      this.onPushInit();
      wx.setKeepScreenOn({
        keepScreenOn: true,
      })
    },
    /**
     * 生命周期函数--监听页面隐藏
     */
    onHide: function () {
        console.info("聊天页面隐藏了");
        // clearTimeout(this.timer);
        // this.onStopAll();
        wx.setKeepScreenOn({
            keepScreenOn: false,
        });
    },

    /**
     * 生命周期函数--监听页面卸载
     */
    onUnload: function () {
        console.info("聊天页面卸载了");
        clearTimeout(this.timer);
        this.onStopAll();
        wx.setKeepScreenOn({
            keepScreenOn: false,
        });
    }
})