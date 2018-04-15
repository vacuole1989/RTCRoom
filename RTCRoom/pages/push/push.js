// pages/push/push.js
const app = getApp();
var config = require('../../config.js');
Page({

  /**
   * 页面的初始数据
   */
  data: {
    focus: false,
    playing: false,
    frontCamera: true,
    cameraContext: {},
    videoContext: {},
    pushUrl: "",
    pushUrl2: "",
    showHDTips: false, //显示清晰度弹窗
    mode: "RTC",
    muted: false,
    enableCamera: true,
    orientation: "vertical",
    beauty: 6.3,
    whiteness: 3.0,
    backgroundMute: false,
    hide: false,
    debug: false,
    playUrl: "",
    userInfo: {},
    hasUserInfo: false
  },
  onGameClick: function () {
    wx.navigateTo({
      url: "../game/game",
    });
  },
  onPersonClick: function () {
    wx.navigateTo({
      url: "../person/person",
    });
  },
  onChatClick: function () {
    wx.navigateTo({
      url: "../chat/chat",
    });
  },
  onPushClick: function () {
    var _this = this;
    console.info(_this.data.pushUrl2)
    _this.setData({
      pushUrl: _this.data.pushUrl2,
      playUrl: ""
    })
    this.setData({
      playing: true,
    })
    if (this.data.playing) {
      this.data.cameraContext.start();
      console.log("camera start");
    } else {
      this.data.cameraContext.stop();
      console.log("camera stop");
    }
  },
  onPushEvent: function (e) {
    console.log(e.detail.code);

    if (e.detail.code == -1307) {
      this.stop();
      wx.showToast({
        title: '推流多次失败',
      })
    }
  },
  createContext: function () {
    this.setData({
      cameraContext: wx.createLivePusherContext('camera-push')
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var _this = this;
    if (app.globalData.userInfo) {
      this.setData({
        userInfo: app.globalData.userInfo,
        hasUserInfo: true
      })
    } else {
      // 在没有 open-type=getUserInfo 版本的兼容处理
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




    // if (!app.globalData.inited) {
    //   wx.redirectTo({
    //     url: '../main/main',
    //   })
    // }
  },
  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
    var _this = this;
    wx.login({
      success: function (res) {
        if (res.code) {
          //发起网络请求
          wx.request({
            method: 'POST',
            url: config.url + '/onLogin?code=' + res.code,
            data: _this.data.userInfo,
            success: function (retdata) {
              console.info(retdata);
              _this.data.pushUrl2 = retdata.data.data.pushUrl;
                _this.createContext();
                _this.onPushClick();
            }
          })
        } else {
          console.log('登录失败！' + res.errMsg)
        }
      }
    });
    
    wx.setKeepScreenOn({
      keepScreenOn: true,
    })
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    console.log("onLoad onShow");
    // 保持屏幕常亮
    wx.setKeepScreenOn({
      keepScreenOn: true
    })

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {
    console.log("onLoad onHide");

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {

    console.log("onLoad onUnload");
    this.stop();

    wx.setKeepScreenOn({
      keepScreenOn: false,
    })
  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {
    console.log("onLoad onPullDownRefresh");

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {
    console.log("onLoad onReachBottom");

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {
    console.log("onLoad onShareAppMessage");
  }
})