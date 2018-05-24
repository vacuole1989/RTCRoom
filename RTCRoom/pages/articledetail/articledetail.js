const app = getApp();
const config = require('../../config.js');
Page({

    /**
     * 页面的初始数据
     */
    data: {
        article: {},
        comments: [],
        content: ''
    },
    inputComment: function (e) {
        this.setData({
            content: e.detail.value
        })
    },
    encodeUnicode: function (str) {
        var res = [];
        for (var i = 0; i < str.length; i++) {
            res[i] = ("00" + str.charCodeAt(i).toString(16)).slice(-4);
        }
        return "\\u" + res.join("\\u");
    },
    sendComment: function () {

        var _this = this;
        console.info(_this.data.content);
        if ('' == _this.data.comment) {
            wx.showModal({
                title: '提示',
                content: '请输入评论内容！',
            })
        }

        wx.login({
            success: function (resl) {
                var con = _this.data.content;
                _this.setData({
                    content: ''
                })
                wx.request({
                    url: config.url + '/sendComment',
                    data: {
                        code: resl.code,
                        articleSeqId: _this.data.article.seqId,
                        content: con
                    },
                    method: 'POST',
                    success: function (res) {


                        _this.getComment(_this.data.article.seqId);

                        wx.showToast({
                            title: '评论成功',
                            success: function () {

                            }
                        })

                    }
                })

            }
        })


    },
    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        var _this = this;
        wx.request({
            url: config.url + '/getArticleById?seqId=' + options.seqId,
            success: function (res) {
                console.info(res.data.data);
                _this.setData({
                    article: res.data.data
                });
            }
        })
        _this.getComment(options.seqId);
    },
    getComment: function (seqId) {
        var _this = this;
        wx.request({
            url: config.url + '/getComment?seqId=' + seqId,
            success: function (res) {
                _this.setData({
                    comments: res.data.data
                });
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

    },

    /**
     * 页面相关事件处理函数--监听用户下拉动作
     */
    onPullDownRefresh: function () {
        var _this = this;
        wx.showNavigationBarLoading()
        _this.getComment(_this.data.article.seqId);
        setTimeout(() => {
            wx.hideNavigationBarLoading()
            wx.stopPullDownRefresh()
        }, 2000);
        
    },

    /**
     * 页面上拉触底事件的处理函数
     */
    onReachBottom: function () {

    },

    /**
     * 用户点击右上角分享
     */
    onShareAppMessage: function () {

    }
})