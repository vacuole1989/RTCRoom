//app.js
var config = require('./config.js');


App({
  onLaunch: function (options) {
    console.log(options);
  },
  onShow: function (options) {
    console.log(options);
  },
  onHide: function () {
    console.log(msg);
  },
  onError: function (msg) {
    console.log(msg);
  },
  globalData: {
    userInfo: null
  }
})