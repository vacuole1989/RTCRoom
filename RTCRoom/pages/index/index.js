// pages/push/push.js
const app = getApp();
var config = require('../../config.js');
Page({

    /**
     * 页面的初始数据
     */
    data: {},
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
            url: "../game/game",
        });
    },
    onChatClick: function () {
        wx.navigateTo({
            url: "../chat/chat",
        });
    },
    onTalkClick: function () {
        wx.navigateTo({
            url: "../loading/loading",
        });
    },
    onMoreClick: function () {
        wx.navigateTo({
            url: "../more/more",
        });
    },
    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {

    },
    /**
     * 生命周期函数--监听页面初次渲染完成
     */
    onReady: function () {
        wx.setKeepScreenOn({
            keepScreenOn: true,
        })
    },

    /**
     * 生命周期函数--监听页面显示
     */
    onShow: function () {
        // 保持屏幕常亮
        wx.setKeepScreenOn({
            keepScreenOn: true
        })

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
        this.stop();
        wx.setKeepScreenOn({
            keepScreenOn: false,
        })
    }

})