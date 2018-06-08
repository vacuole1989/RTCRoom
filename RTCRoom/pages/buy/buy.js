const app = getApp();
const config = require('../../config.js');
Page({
    data: {
        diamondNum: 0,
        userInfo: {},
        selected: {
            diamond: 100,
            cny: 0.01
        },
        btns: [
            {
                diamond: 100,
                cny: 0.01
            },
            {
                diamond: 200,
                cny: 0.01
            },
            {
                diamond: 500,
                cny: 0.01
            },
            {
                diamond: 1500,
                cny: 0.01
            },
            {
                diamond: 2000,
                cny: 0.01
            },
            {
                diamond: 3000,
                cny: 0.01
            }
        ]
    },
    onBuyBtn: function () {
        var _this = this;
        _this.getUserCode(_this.data.selected.cny);
    },
    onChooseBtn: function (e) {
        this.setData({
            selected: this.data.btns[e.currentTarget.dataset.idx]
        })
    },
    getUserCode: function (cny) {
        var _this = this;
        wx.login({
            success: res => {
                _this.undefierPay(res.code, cny);
            }
        });
    },
    undefierPay: function (code, cny) {
        var _this = this;
        wx.request({
            url: config.url + '/unifiedorder',
            data: {
                cny: cny,
                code: code
            },
            success: function (res) {
                if (res.data.success) {
                    var retData = res.data.message;
                    _this.payMoney(retData);
                } else {
                    _this.showAlert('接口调用失败', res.data.message.return_msg);
                }

            }
        });
    },
    payMoney: function (retData, itype) {
        var _this = this;
        wx.requestPayment({
            'timeStamp': retData.timeStamp,
            'nonceStr': retData.nonceStr,
            'package': retData.package,
            'signType': retData.signType,
            'paySign': retData.sign,
            'success': function (res) {
                wx.request({
                    url: config.url + '/paySuccess?nonceStr=' + retData.nonceStr,
                    success: function (res) {
                        if (res.data.success) {
                            _this.getDiamondNum();
                            _this.showAlert('提示', '充值成功，请稍后查看钻石数量。');
                        } else {
                            _this.showAlert('提示', '充值失败。');
                        }
                    }
                })
            },
            'fail': function (res) {
                _this.showAlert('提示', '支付失败，请重试。');
            }
        });
    },
    showAlert: function (title, text) {
        wx.showModal({
            title: title,
            content: text,
            showCancel: false,
            confirmText: '知道了',
            success: function (res) {

            }
        });
    },
    getDiamondNum: function () {
        var _this = this;
        wx.request({
            url: config.url + '/getDiamondNum?seqId=' + _this.data.userInfo.seqId,
            success: function (res) {
                if (res.data.success) {
                    _this.setData({
                        diamondNum: res.data.data.totalDiamond
                    })
                }
            }
        })
    },
    onLoad: function (options) {
        this.setData({
            userInfo: app.globalData.userInfo
        })
        this.getDiamondNum();
    }
})