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
        stopTime: 60,
        userInfo: {},
        contactUserInfo: {},
        addFriends:"请求加好友",
        hasFriend:false
    },
    askFriend:function(){
        var _this = this;
        wx.request({
            url: config.url + '/askFriend?userSeqId=' + app.globalData.userInfo.seqId + '&friendSeqId=' + _this.data.contactUserInfo.seqId,
            success: function (res) {
                console.info(res);
                if(res.data.success){
                    wx.showModal({
                        title: '提示',
                        content: '申请好友成功！',
                        success: function (res) {

                        }
                    });
                }
                
            }
        });
    },
    onCloseTalk: function () {
        var _this = this;
        wx.showModal({
            title: '提示',
            content: '您确定要退出聊天吗？',
            success: function (res) {
                if (res.confirm) {
                    wx.request({
                        url: config.url + '/closeTalk?seqId=' + app.globalData.userInfo.seqId,
                        success: function (res) {
                            _this.setData({
                                stopTime: 0
                            });
                            wx.reLaunch({
                                url: '../index/index',
                            });
                        }
                    });
                } else if (res.cancel) {

                }
            }
        });
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
            if (this.data.stopTime > 0) {
                var tt = this.data.stopTime - 1;
                this.setData({
                    stopTime: tt
                });
                this.munTime();
            } else {
                wx.reLaunch({
                    url: '../index/index',
                });
            }
        }.bind(this), 1000);
    },
    onHeartBeat: function () {
        var _this = this;
        wx.request({
            url: config.url + '/heartBeat?seqId=' + app.globalData.userInfo.seqId,
            success: function (res) {
                if (res.data.data) {
                    if (_this.data.stopTime > 0) {
                        setTimeout(function () {
                            this.onHeartBeat();
                        }.bind(_this), 1000);
                    }
                } else {
                    wx.reLaunch({
                        url: '../index/index',
                    });
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
            // this.onStopAll();
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
            stopTime: app.globalData.stopTime,
            userInfo: app.globalData.userInfo,
            contactUserInfo: app.globalData.contactUserInfo
        });
        if (this.data.contactUserInfo.gender==1){
          this.setData({
            contactGender:'男'
          });
        } else if (this.data.contactUserInfo.gender == 2){
          this.setData({
            contactGender: '女'
          });
        }else{
          this.setData({
            contactGender: '未知'
          });
        }



        var _this = this;
        wx.request({
            url: config.url + '/getIfFriend?userSeqId=' + app.globalData.userInfo.seqId + '&friendSeqId=' + _this.data.contactUserInfo.seqId,
            success: function (res) {
                if(res.data.success){
                    _this.setData({
                        hasFriend:true,
                        addFriends:'已经是好友'
                    })
                }else{
                    _this.setData({
                        hasFriend: false,
                        addFriends: '请求加好友'
                    })
                }
            }
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
        console.info("3聊天页面隐藏了");
        clearTimeout(this.timer);
        this.onStopAll();
        wx.request({
            url: config.url + '/closeTalk?seqId=' + app.globalData.userInfo.seqId,
            success: function (res) {

            }
        });   
        wx.setKeepScreenOn({
            keepScreenOn: false,
        });
    },

    /**
     * 生命周期函数--监听页面卸载
     */
    onUnload: function () {
        console.info("4聊天页面卸载了");
        
        clearTimeout(this.timer);
        this.onStopAll();
        wx.request({
            url: config.url + '/closeTalk?seqId=' + app.globalData.userInfo.seqId,
            success: function (res) {
            }
        });
        wx.setKeepScreenOn({
            keepScreenOn: false,
        });
    }
})