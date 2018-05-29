const app = getApp();
var config = require('../../config.js');

Page({

    /**
     * 页面的初始数据
     */
    data: {
        time: 20,
        userInfo: {},
        msgCount: {},
        timer: null,
        pageHide: false,
        isAsked: false
    },
    onPersonClick: function () {
        wx.navigateTo({
            url: "../person/person",
        });
    },
    onSearchClick: function () {
        wx.navigateTo({
            url: "../search/search",
        });
    },
    onGameClick: function () {
        wx.navigateTo({
            url: "../article/article",
        });
    },
    onChatClick: function () {
        wx.navigateTo({
            url: "../chat/chat",
        });
    },
    onMoreClick: function () {
        wx.navigateTo({
            url: "../video/video",
        });
    },
    onTalkClick: function () {
        wx.navigateTo({
            url: "../loading/loading",
        });
    },
    onTalkClick2: function () {
        var _this = this;
        wx.request({
            url: config.url + '/online?seqId=' + app.globalData.userInfo.seqId,
            success: function (res) {
                _this.setData({
                    time: 20
                });
                wx.showLoading({
                    title: '匹配中…' + _this.data.time,
                    mask: true
                });
                _this.munTime();
                _this.getOnlinePerson();
            }
        });
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
                wx.showLoading({
                    title: '匹配中…' + _this.data.time,
                    mask: true
                });
                if (res.data.success) {
                    wx.hideLoading();
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
                        wx.hideLoading();
                        _this.onOffOnline();
                    }
                }
            }
        })
    },
    onOffOnline: function () {
        wx.request({
            url: config.url + '/offline?seqId=' + app.globalData.userInfo.seqId,
            success: function (res) {

            }
        });
    },
    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        console.info('load')
        var _this = this;
        _this.setData({
            userInfo: app.globalData.userInfo
        })
        this.setData({
            pageHide: false
        })
    },
    /**
     * 生命周期函数--监听页面初次渲染完成
     */
    onReady: function () {
        console.info('ready')
        wx.setKeepScreenOn({
            keepScreenOn: true,
        })
    },
    cycleMsg: function () {
        console.info('cycle')
        this.data.timer = setTimeout(function () {
            var _this = this;
            wx.request({
                url: config.url + '/getFriendTipCount?seqId=' + _this.data.userInfo.seqId,
                success: function (res) {
                    _this.setData({
                        msgCount: res.data.data
                    })
                    if (_this.data.msgCount.asked) {
                        if (!_this.data.isAsked) {
                            _this.setData({
                                isAsked: true
                            })
                            wx.showModal({
                                title: '提示',
                                content: '【'+_this.data.msgCount.asked.nickName+'】邀请你视频！',
                                cancelText: '拒绝',
                                cancelColor: '#E93027',
                                confirmText: '同意',
                                confirmColor: '#00B26A',
                                success:function(resm){
                                    if(resm.confirm){
                                        app.globalData.userInfo.playUrl = _this.data.msgCount.asked.playUrl;
                                        app.globalData.contactUserInfo = _this.data.friend;
                                        app.globalData.stopTime = 60;
                                        app.globalData.fromChat = true;
                                        wx.navigateTo({
                                            url: '../talk/talk',
                                        })
                                    }else{

                                    }
                                },
                                complete:function(res){
                                    
                                }
                            }) 
                        }
                    }
                },
                complete: function () {
                    if (!_this.data.pageHide) {
                        _this.cycleMsg();
                    }
                }
            })
        }.bind(this), 1000);
    },
    /**
     * 生命周期函数--监听页面显示
     */
    onShow: function () {
        this.cycleMsg();
        console.info('show')
        // 保持屏幕常亮
        wx.setKeepScreenOn({
            keepScreenOn: true
        })

        this.setData({
            pageHide: false
        })

    },

    /**
     * 生命周期函数--监听页面隐藏
     */
    onHide: function () {
        console.info('hide')
        clearTimeout(this.data.timer);
        this.setData({
            pageHide: true
        })
    },

    /**
     * 生命周期函数--监听页面卸载
     */
    onUnload: function () {
        console.info('unload')
        this.setData({
            pageHide: true
        })
        wx.setKeepScreenOn({
            keepScreenOn: false,
        })
    },
    onShareAppMessage: function () {

    }
})