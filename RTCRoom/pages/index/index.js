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
    onOffOnline: function () {
        wx.request({
            url: config.url + '/offline?seqId=' + app.globalData.userInfo.seqId,
            success: function (res) {

            }
        });
    },
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

                            wx.navigateTo({
                                url: '/pages/talkin/talkin?seqId=' + _this.data.msgCount.asked.seqId
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
        }.bind(this), 2000);
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