const app = getApp();
const config = require('../../config.js');
Page({
    data: {
        friends: [],
        msgCount: {},
        pageHide: false,
        delBtnWidth: 180
    },
    onLoad: function () {
        this.initEleWidth();
        this.setData({
            pageHide: false
        })
        this.initData();
    },
    onUnload: function () {
        this.setData({
            pageHide: true
        })
    },
    onChatJump:function(e){
        wx.navigateTo({
            url: '/pages/chatting/chatting?seqId='+e.currentTarget.dataset.seqid
        })
    },
    touchS: function (e) {
        if (e.touches.length == 1) {
            this.setData({
                //设置触摸起始点水平方向位置
                startX: e.touches[0].clientX
            });
        }
    },

    touchM: function (e) {
        if (e.touches.length == 1) {
            //手指移动时水平方向位置
            var moveX = e.touches[0].clientX;
            //手指起始点位置与移动期间的差值
            var disX = this.data.startX - moveX;
            var delBtnWidth = this.data.delBtnWidth;
            var txtStyle = "";
            if (disX == 0 || disX < 0) {//如果移动距离小于等于0，文本层位置不变
                txtStyle = "left:0px";
            } else if (disX > 0) {//移动距离大于0，文本层left值等于手指移动距离
                txtStyle = "left:-" + disX + "px";
                if (disX >= delBtnWidth) {
                    //控制手指移动距离最大值为删除按钮的宽度
                    txtStyle = "left:-" + delBtnWidth + "px";
                }
            }
            //获取手指触摸的是哪一项
            var index = e.currentTarget.dataset.index;
            var list = this.data.friends;
            list[index].txtStyle = txtStyle;
            //更新列表的状态
            this.setData({
                friends: list
            });
        }
    },
    touchE: function (e) {
        if (e.changedTouches.length == 1) {
            //手指移动结束后水平位置
            var endX = e.changedTouches[0].clientX;
            //触摸开始与结束，手指移动的距离
            var disX = this.data.startX - endX;
            var delBtnWidth = this.data.delBtnWidth;
            //如果距离小于删除按钮的1/2，不显示删除按钮
            var txtStyle = disX > delBtnWidth / 2 ? "left:-" + delBtnWidth + "px" : "left:0px";
            //获取手指触摸的是哪一项
            var index = e.currentTarget.dataset.index;
            var list = this.data.friends;
            list[index].txtStyle = txtStyle;
            //更新列表的状态
            this.setData({
                friends: list
            });
        }
    },
    //获取元素自适应后的实际宽度
    getEleWidth: function (w) {
        var real = 0;
        try {
            var res = wx.getSystemInfoSync().windowWidth;
            var scale = (750 / 2) / (w / 2);//以宽度750px设计稿做宽度的自适应
            real = Math.floor(res / scale);
            return real;
        } catch (e) {
            return false;
            // Do something when catch error
        }
    },

    initEleWidth: function () {
        var delBtnWidth = this.getEleWidth(this.data.delBtnWidth);
        this.setData({
            delBtnWidth: delBtnWidth
        });
    },
    //点击删除按钮事件
    delItem: function (e) {
        var _this = this;
        //获取列表中要删除项的下标
        var index = e.currentTarget.dataset.index;
        var list = this.data.friends;
        wx.showModal({
            title: '提示',
            content: '是否确定删除该好友？',
            success: function (res) {
                if (res.confirm) {
                    wx.request({
                        url: config.url + '/delFriend?seqId=' + list[index].seqId + '&userSeqId=' + app.globalData.userInfo.seqId,
                        success: function (ress) {
                            _this.setData({
                                friends: ress.data.data
                            })
                        }
                    })
                }
            }
        })

    },
    initData:function(){
        var _this = this;
        wx.login({
            success: function (res) {
                wx.request({
                    url: config.url + '/getFriends?seqId=' + app.globalData.userInfo.seqId,
                    success: function (ress) {
                        _this.setData({
                            friends: ress.data.data
                        })
                    }
                })
            }
        })
        wx.request({
            url: config.url + '/getFriendTipCount?seqId=' + app.globalData.userInfo.seqId,
            success: function (res) {
                _this.setData({
                    msgCount: res.data.data
                })
            }
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
    },
    onPullDownRefresh:function(){
        var _this = this;
        wx.showNavigationBarLoading()
        _this.initData();
        setTimeout(() => {
            wx.hideNavigationBarLoading()
            wx.stopPullDownRefresh()
        }, 2000);
    }
})