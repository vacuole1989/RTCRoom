// pages/push/push.js
const app = getApp()
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
    userInfo: {}
  },
  onGameClick: function () {
    wx.navigateTo({
      url: "../game/game",
    });
  },
  onPushClick: function () {
    var _this = this;
    console.log("onPushClick", this.data);
    this.setData({
      pushUrl: app.globalData.pushUrl,
      playUrl: ""
    })
    this.setData({
      playing: true,
    })
    if (this.data.playing) {
      this.data.cameraContext.start();
      this.data.videoContext.play();
      console.log("camera start");
    } else {
      this.data.cameraContext.stop();
      this.data.videoContext.stop();
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

  },
  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {



    
    console.log("onLoad onReady");
    this.createContext();
    this.onPushClick();
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