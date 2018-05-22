const app = getApp();
var config = require('../../config.js');
Page({
    data: {
        userInfo: {},
        banners: [],
        AvatorText: [],
        newList: []
    },
    onLoad: function () {
        var _this = this;
        //调用应用实例的方法获取全局数据
        _this.setData({
            userInfo: app.globalData.userInfo
        })
        wx.request({
            url: config.url +"/getVideoList",
            success:function(res){
                var map=res.data.data;
                _this.setData({
                    banners:map.swiper,
                    AvatorText:map.category,
                    newList:map.index
                })

                

            }
        })
    }
})