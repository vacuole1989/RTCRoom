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
        pageFinish: false,
        isAsked: false,
        askSeqId:null
    },
    onCloseClick: function () {
        wx.navigateBack();
    },
    onAgreeClick: function (e) {
        var _this=this;
        wx.request({
            url: config.url + '/agreeAsk?seqId=' + _this.data.askSeqId+'&agree='+e.currentTarget.dataset.agree,
            success:function(res){
                if(res.data.data.agree==1){
                    app.globalData.userInfo.playUrl = res.data.data.playUrl;
                    app.globalData.contactUserInfo = res.data.data.contactUserInfo;
                    app.globalData.askUser = res.data.data.askUser;
                    app.globalData.stopTime = 60;
                    app.globalData.fromChat = true;
                    wx.redirectTo({
                        url: '../talk/talk',
                    })
                }
            }
        })       
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
        _this.setData({
            askSeqId:options.seqId
        })
    },
    onReady: function () {
        console.info('ready')
        wx.setKeepScreenOn({
            keepScreenOn: true,
        })
    },
    onShow: function () {
        // this.cycleMsg();
        console.info('show')
        wx.setKeepScreenOn({
            keepScreenOn: true
        })

        this.setData({
            pageHide: false
        })

    },
    onHide: function () {
        console.info('hide')
        clearTimeout(this.data.timer);
        this.setData({
            pageHide: true
        })
    },
    onUnload: function () {
        console.info('unload')
        this.setData({
            pageFinish: true
        })
        wx.setKeepScreenOn({
            keepScreenOn: false,
        })
    },
    onShareAppMessage: function () {

    }
})