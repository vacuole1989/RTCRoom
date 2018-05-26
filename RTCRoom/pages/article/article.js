const app = getApp();
const config = require('../../config.js');
Page({
    data: {
        feed: [],
        feed_length: 0,
        searchText: ''
    },
    onLoad: function () {
        console.log('onLoad')
        var that = this
        //调用应用实例的方法获取全局数据
        that.getData();
    },
    sendArticle:function(){
        wx.navigateTo({
            url: '/pages/newarticle/newarticle',
        })
    },
    onDetailTap: function (e) {
        wx.navigateTo({
            url: '/pages/articledetail/articledetail?seqId=' + e.currentTarget.dataset.seqid,
        })
    },
    searchBtn: function (e) {
        var _this = this;
        if (null == _this.data.searchText || '' == _this.data.searchText) {
            // wx.showModal({
            //     title: '提示',
            //     content: '查询内容不能为空',
            // })
            // return;
        }
        wx.request({
            data: { title: _this.data.searchText },
            method: 'POST',
            url: config.url + '/searchArticleList',
            success: function (res) {
                console.info(res.data.data)
                _this.setData({
                    feed: res.data.data,
                    feed_length: res.data.data.length
                });
            }
        })
    },
    inputText: function (e) {
        this.data.searchText = e.detail.value;
    },
    getData: function () {
        var _this = this;
        wx.request({
            method: 'POST',
            url: config.url + '/getLastArticleList',
            success: function (res) {
                console.info(res.data.data);
                _this.setData({
                    feed: res.data.data,
                    feed_length: res.data.data.length
                });
            }
        })
    },
    refresh: function () {
        var _this = this;
        var fd = _this.data.feed;
        wx.request({
            method: 'POST',
            data: { seqId: fd[0].seqId },
            url: config.url + '/getNewArticleList',
            success: function (res) {
                var fd = res.data.data;
                var length = res.data.data.length;
                for (var i = 0; i < _this.data.feed.length; i++) {
                    fd[length + i] = _this.data.feed[i];
                }
                _this.setData({
                    feed: fd,
                    feed_length: fd.length
                });
            }
        });
    },
    nextLoad: function () {
        var _this = this;
        var fd = _this.data.feed;
        wx.request({
            method: 'POST',
            data: { seqId: fd[fd.length - 1].seqId},
            url: config.url + '/getPreArticleList',
            success: function (res) {
                console.info(res.data.data);
                var length = fd.length;
                for (var i = 0; i < res.data.data.length; i++) {
                    fd[length + i] = res.data.data[i];
                }
                _this.setData({
                    feed: fd,
                    feed_length: fd.length
                });
            }
        })
    },
    onPullDownRefresh: function () {
        var _this = this;
        wx.showNavigationBarLoading()
        _this.refresh();
        setTimeout(() => {
            wx.hideNavigationBarLoading()
            wx.stopPullDownRefresh()
        }, 2000);
    },
    onReachBottom: function () {
        wx.showNavigationBarLoading();
        var _this = this;
        setTimeout(() => {
            wx.hideNavigationBarLoading();
            _this.nextLoad();
        }, 1000);
    }

})
