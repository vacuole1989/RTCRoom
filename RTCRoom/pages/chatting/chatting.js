const app = getApp();
const config = require('../../config.js');
const sendAudio = wx.createInnerAudioContext()
const reciveAudio = wx.createInnerAudioContext()
sendAudio.src = '/pages/Resources/send.wav'
reciveAudio.src = '/pages/Resources/recive.wav'

Page({
    data: {
        friend: {},
        userInfo: {},
        content: '',
        msgs: [],
        pageHide: false
    },
    inputComment: function (e) {
        this.setData({
            content: e.detail.value
        })
    },
    clearComment: function () {
        this.setData({
            content: ''
        })
    },
    sendVideo:function(){
        var _this=this;
        wx.request({
            url: config.url +'/getPlayUrl?seqId='+_this.data.friend.seqId,
            success:function(res){
                app.globalData.userInfo.playUrl = res.data.data;
                app.globalData.contactUserInfo = _this.data.friend;
                app.globalData.stopTime = 60;
                app.globalData.fromChat = true;
                wx.redirectTo({
                    url: '../talk/talk',
                })
            }
        })
        
    },
    onLoad: function (options) {
        console.info('cload')
        this.setData({
            pageHide: false
        })
        var _this = this;
        _this.setData({
            userInfo: app.globalData.userInfo,
            friend: options
        })
        var _this = this;
        wx.request({
            url: config.url + '/getUserInfoById?seqId=' + _this.data.friend.seqId,
            success: function (res) {
                _this.setData({
                    friend: res.data.data
                })
                wx.request({
                    url: config.url + '/getChatMsgList',
                    method: 'POST',
                    data: {
                        fromUserSeqId: _this.data.userInfo.seqId,
                        toUserSeqId: _this.data.friend.seqId
                    },
                    success: function (res) {
                        _this.setData({
                            msgs: res.data.data
                        })
                        wx.pageScrollTo({
                            scrollTop: 100000,
                        })
                        _this.cycleContent();
                    }

                })
            }
        })
    },
    sendComment: function () {
        let _this = this;
        if ('' != _this.data.content) {
            _this.onSendMsg();
        }

    },
    alert: function (con) {
        wx.showModal({
            title: '提示',
            content: con,
        })
    },
    onSendMsg: function () {
        var _this = this;
        wx.request({
            url: config.url + '/sendChatMsg',
            method: 'POST',
            data: {
                fromUserSeqId: _this.data.userInfo.seqId,
                toUserSeqId: _this.data.friend.seqId,
                content: _this.data.content
            },
            success: function (res) {
                _this.addMsg(res.data.data);
                _this.clearComment();
                sendAudio.play();
            }

        })

    },
    addMsg: function (msg) {
        var _this = this;
        var msgs = _this.data.msgs || [];
        msgs.push(msg);
        _this.setData({
            msgs: msgs
        })
        wx.pageScrollTo({
            scrollTop: 100000,
        })
    },
    cycleContent: function () {
        console.info('cycleMsg')
        this.data.timer = setTimeout(function () {
            var _this = this;
            if (_this.data.msgs.length > 0) {
                var preSeq = _this.data.msgs[_this.data.msgs.length - 1].seqId;
                if (undefined != preSeq) {
                    wx.request({
                        url: config.url + '/getNewChatMsgList',
                        method: 'POST',
                        data: {
                            fromUserSeqId: _this.data.userInfo.seqId,
                            toUserSeqId: _this.data.friend.seqId,
                            seqId: preSeq
                        },
                        success: function (res) {
                            if (res.data.success) {
                                for (var i = 0; i < res.data.data.length; i++) {
                                    _this.addMsg(res.data.data[i]);
                                    reciveAudio.play();
                                }
                            }
                        },
                        complete: function (res) {
                            _this.cycleContent();
                        }
                    })
                } else {
                    _this.cycleContent();
                }
            } else {
                _this.cycleContent();
            }

        }.bind(this), 1000);
    },
    onShow: function () {
        console.info('cshow')
        this.setData({
            pageHide: false
        })

    },
    onUnload: function () {
        console.info('cunload')
        clearTimeout(this.data.timer);
        this.setData({
            pageHide: true
        })
    },
    onHide: function () {
        console.info('chide')
        this.setData({
            pageHide: true
        })
    }

})