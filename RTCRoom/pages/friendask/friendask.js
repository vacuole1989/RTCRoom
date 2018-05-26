const app = getApp();
const config = require('../../config.js');
Page({

    /**
     * 页面的初始数据
     */
    data: {
        userInfo: {},
        askFriends: []
    },
    agree: function (e) {
        var _this=this;
        wx.request({
            url: config.url + "/AgreeFriend?seqId=" + e.currentTarget.dataset.seqid + '&agree=true',
            success: function (res) {
                if (res.data.success) {

                    wx.showModal({
                        title: '提示',
                        content: '操作成功',
                    })
                }else{
                    wx.showModal({
                        title: '提示',
                        content: res.data.message,
                    })
                }
                _this.flushFriend();
            }
        })
    },
    notagree: function (e) {
        var _this = this;
        wx.request({
            url: config.url + "/AgreeFriend?seqId=" + e.currentTarget.dataset.seqid + '&agree=false',
            success: function (res) {
                if (res.data.success) {
                    wx.showModal({
                        title: '提示',
                        content: '操作成功',
                    })
                    
                }
                _this.flushFriend();
            }
        })
    },
    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        wx.setNavigationBarTitle({
            title: '好友申请',
        })
        var _this = this;
        _this.setData({
            userInfo: app.globalData.userInfo
        })
        _this.flushFriend();

    },
    flushFriend:function(){
        var _this = this;
        wx.request({
            url: config.url + "/getFriendTip?seqId=" + _this.data.userInfo.seqId,
            success: function (res) {
                _this.setData({
                    askFriends: res.data.data
                })
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

    }
})