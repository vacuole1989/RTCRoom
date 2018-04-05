//index.js
//获取应用实例
const app = getApp()

Page({
  data: {
    motto: 'Hello World',
    userInfo: {},
    hasUserInfo: false,
    canIUse: wx.canIUse('button.open-type.getUserInfo'),
    pushUrl: '',
    playUrl: ''
  },
  statechange(e) {
    console.log('live-pusher code:', e.detail.code)
  },
  //事件处理函数
  bindViewTap: function () {
    wx.navigateTo({
      url: '../logs/logs'
    })
  },
  onLoad: function () {
  
    if (app.globalData.userInfo) {
      this.setData({
        userInfo: app.globalData.userInfo,
        hasUserInfo: true
      })
    } else if (this.data.canIUse) {
      // 由于 getUserInfo 是网络请求，可能会在 Page.onLoad 之后才返回
      // 所以此处加入 callback 以防止这种情况
      app.userInfoReadyCallback = res => {
        this.setData({
          userInfo: res.userInfo,
          hasUserInfo: true
        })
      }
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

console.info(this.data.userInfo);


    var pushUrl1 = 'rtmp://22043.livepush.myqcloud.com/live/22043_vacuole?bizid=22043&txSecret=ff67c2f209f1e0653e7218b2c2835cf8&txTime=5AE5EBFF';
    var playUrl2 = 'rtmp://22043.liveplay.myqcloud.com/live/22043_vacuole';

    var pushUrl2 = 'rtmp://22043.livepush.myqcloud.com/live/22043_39f272c41c?bizid=22043&txSecret=2ab9adca7fcac316f1fca22b00a78b5b&txTime=5AE5EBFF';
    var playUrl1 = 'rtmp://22043.liveplay.myqcloud.com/live/22043_39f272c41c';

    if (this.nickName == '东哥') {
      this.setData({
        pushUrl: pushUrl1,
        playUrl: playUrl1
      })
    } else {
      this.setData({
        pushUrl: pushUrl2,
        playUrl: playUrl2
      })
    }

  },
  getUserInfo: function (e) {
    console.log(e)
    app.globalData.userInfo = e.detail.userInfo
    this.setData({
      userInfo: e.detail.userInfo,
      hasUserInfo: true
    })
  }
})
