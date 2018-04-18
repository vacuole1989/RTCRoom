const app = getApp();
var config = require('../../config.js');
Page({

  /**
   * 页面的初始数据
   */
  data: {
    timer: null,
    time: 20,
    flag: false
  },
  munTime: function () {
    this.timer = setTimeout(function () {
      var tt = this.data.time - 1;
      this.setData({
        time: tt
      });
      if (this.data.time > 0) {
        this.munTime();
      }
      console.info(this.data.time);
    }.bind(this), 1000);
  },
  getOnlinePerson: function () {
    var _this = this;
    wx.request({
      method: 'POST',
      url: config.url + '/onlineUser?seqId=' + app.globalData.userInfo.seqId,
      success: function (res) {
        if (res.data.success) {
          if (_this.data.time > 0) {
            _this.setData({
              time: 0
            })
            app.globalData.userInfo.playUrl = res.data.data.playUrl;
            _this.setData({
              flag: true
            })
            wx.redirectTo({
              url: '../talk/talk',
            })
          }

        } else {
          _this.setData({
            flag: false
          })
          console.log(res);
          if (_this.data.time > 0) {
            setTimeout(function () {
              this.getOnlinePerson();
            }.bind(_this), 1000)
          } else {
            wx.navigateBack({

            })
          }
        }
      }
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var _this = this;
    wx.request({
      method: 'POST',
      url: config.url + '/online?seqId=' + app.globalData.userInfo.seqId,
      success: function (retdata) {
        _this.munTime();
        _this.getOnlinePerson();
      }
    })

  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {
    var _this = this;
    if (!_this.data.flag) {

      _this.setData({
        time: 0
      })
      console.info('loading页面关闭');
      wx.request({
        method: 'POST',
        url: config.url + '/offline?seqId=' + app.globalData.userInfo.seqId,
        success: function (retdata) {

        }
      })
    }
  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  }
});
