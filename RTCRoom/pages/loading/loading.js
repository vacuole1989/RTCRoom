const app = getApp();
const config = require('../../config.js');
Page({

    /**
     * 页面的初始数据
     */
    data: {
        animationData: {},
        time: 20,
        userInfo: {},
        showText: '',
        color: [],
        timer:null
    },
    munTime: function () {
        this.timer = setTimeout(function () {
            if (this.data.time > 0) {
                var tt = this.data.time - 1;
                this.setData({
                    time: tt
                });
                this.munTime();
            }
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
                        });
                        app.globalData.userInfo.playUrl = res.data.data.playUrl;
                        app.globalData.contactUserInfo = res.data.data.contactUserInfo;
                        app.globalData.stopTime = res.data.data.stopTime;
                        wx.redirectTo({
                            url: '../talk/talk',
                        })
                    } else {
                        _this.onOffOnline();
                    }
                } else {
                    if (_this.data.time > 0) {
                        setTimeout(function () {
                            this.getOnlinePerson();
                        }.bind(_this), 1000);
                    } else {
                        _this.onOffOnline();
                    }
                }
            }
        })
    },
    onOffOnline: function () {
        var _this = this;
        wx.request({
            url: config.url + '/offline?seqId=' + app.globalData.userInfo.seqId,
            success: function (res) {
                if(_this.data.time>-2){
                    wx.navigateBack()
                }
               
            }
        });
    },
    finishSearch: function () {
        var _this = this;
        _this.setData({
            time: -2
        });
    },
    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        var _this = this;
        _this.setData({
            userInfo: app.globalData.userInfo
        })
        var animation = wx.createAnimation({
            duration: 1000,
            timingFunction: 'ease',
        })

        this.animation = animation
        var colors = ['#FF3636', '#FFCC33', '#71E3FD']
        var n = 0;
        //连续动画需要添加定时器,所传参数每次+1就行
        setInterval(function () {
            var co = [];
            for (var i = 0; i < 5; i++) {

                if (i == n % 5) {
                    co[i] = true;
                } else {
                    co[i] = false;
                }
            }
            n = n + 1;
            var dot = '';
            for (var i = 0; i < n % 4; i++) {
                dot += '▪';
            }
            

            this.animation.backgroundColor(colors[n % 3], colors[(n + 1) % 3]).step()
            this.setData({
                animationData: this.animation.export(),
                showText: dot,
                color:co
            })
        }.bind(this), 1000)
    },
    onShow: function () {
        var _this = this;
        wx.request({
            url: config.url + '/online?seqId=' + app.globalData.userInfo.seqId,
            success: function (res) {
                _this.setData({
                    time: 20
                });
                _this.munTime();
                _this.getOnlinePerson();
            }
        });
    },
    /**
     * 生命周期函数--监听页面卸载
     */
    onUnload: function () {
        this.finishSearch();
    }

})