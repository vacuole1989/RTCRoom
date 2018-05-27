const app = getApp();
const config = require('../../config.js');

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
    onLoad: function (options) {
        this.setData({
            pageHide: false
        })
        var _this = this;
        _this.setData({
            userInfo: app.globalData.userInfo
        })
        wx.request({
            url: config.url + '/getUserInfoById?seqId=' + options.seqId,
            success: function (res) {
                _this.setData({
                    friend: res.data.data
                })
                wx.request({
                    url: config.url + '/getChatMsgList',
                    method: 'POST',
                    data: {
                        fromUserSeqId: _this.data.userInfo.seqId,
                        toUserSeqId: options.seqId
                    },
                    success: function (res) {
                        _this.setData({
                            msgs: res.data.data
                        })
                        _this.cycleContent();
                    }

                })
            }
        })


    },
    sendComment: function () {
        let _this = this;
        if ('' == _this.data.content) {
            wx.showModal({
                title: '提示',
                content: '请输入评论内容！',
            })
        }
        _this.onSendMsg();

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

    },
    cycleContent: function () {
        setTimeout(function () {
            var _this = this;
            if (undefined != _this.data.msgs[_this.data.msgs.length - 1].seqId) {
                wx.request({
                    url: config.url + '/getNewChatMsgList',
                    method: 'POST',
                    data: {
                        fromUserSeqId: _this.data.userInfo.seqId,
                        toUserSeqId: _this.data.friend.seqId,
                        seqId: _this.data.msgs[_this.data.msgs.length - 1].seqId
                    },
                    success: function (res) {
                        if (res.data.success) {
                            for (var i = 0; i < res.data.data.length; i++) {
                                _this.addMsg(res.data.data[i]);
                            }
                        }
                    },
                    complete: function (res) {
                        if (!_this.data.pageHide) {
                            _this.cycleContent();
                        }
                    }
                })
            }
        }.bind(this), 1000);
    },
    onUnload: function () {
        this.setData({
            pageHide: true
        })
    },
    onShow: function () {
        this.setData({
            pageHide: false
        })
    },
    onHide: function () {
        this.setData({
            pageHide: true
        })
    }

})