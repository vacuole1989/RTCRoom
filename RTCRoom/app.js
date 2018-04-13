//app.js

var qcloud = require('./lib/index');
var config = require('./config.js');

App({
  onLaunch: function () {

    wx.login({
      success: function (res) {
        if (res.code) {
          //发起网络请求
          wx.request({
            url: config.url + '/onLogin',
            data: {
              code: res.code
            }
          })
        } else {
          console.log('登录失败！' + res.errMsg)
        }
      }
    });




  
  },
  globalData: {
    userInfo: null
  }
})