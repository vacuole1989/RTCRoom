//app.js

var qcloud = require('./lib/index');
var config = require('./config.js');


App({
  onLaunch: function () {
    var _this = this;
    wx.login({
      success: function (res) {
        if (res.code) {
          // 获取用户信息
          wx.getUserInfo({
            withCredentials: false,
            success: function (ret) {
              wx.request({
                method: 'POST',
                url: config.url + '/onLogin?code=' + res.code,
                data: ret.userInfo,
                success: function (retdata) {
                  console.info(retdata);
                  _this.globalData.pushUrl = retdata.data.data;
                }
              });
            },
            fail: function () {
              console.log('获取信息用户登录态失败！');
            }
          });
        } else {
          console.log('获取用户登录态失败！' + res.errMsg);
         
        }
      },
      fail: function () {
        console.log('获取用户登录态失败！' + res.errMsg);
        if (ret.errMsg == 'request:fail timeout') {
          var errCode = -1;
          var errMsg = '网络请求超时，请检查网络状态';
        }
       
      }
    });
  },
  globalData: {
    userInfo: null,
    pushUrl: null
  }
})