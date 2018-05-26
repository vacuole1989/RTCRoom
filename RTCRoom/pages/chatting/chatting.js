const app = getApp();
const config = require('../../config.js');
var webim = require('../utils/webim_wx.js');


//帐号模式，0-表示独立模式，1-表示托管模式
var accountMode = 0;

//官方 demo appid,需要开发者自己修改（托管模式）
var sdkAppID = 1400085319;
var accountType = 27184;

//当前用户身份
var loginInfo = {
    'sdkAppID': sdkAppID, //用户所属应用id,必填
    'identifier': null, //当前用户ID,必须是否字符串类型，必填
    'accountType': accountType, //用户所属应用帐号类型，必填
    'userSig': null, //当前用户身份凭证，必须是字符串类型，必填
    'identifierNick': null, //当前用户昵称，不用填写，登录接口会返回用户的昵称，如果没有设置，则返回用户的id
    'headurl': 'img/me.jpg' //当前用户默认头像，选填，如果设置过头像，则可以通过拉取个人资料接口来得到头像信息
};

var AdminAcount = 'admin';
var selType = webim.SESSION_TYPE.C2C; //当前聊天类型
var selToID = null; //当前选中聊天id（当聊天类型为私聊时，该值为好友帐号，否则为群号）
var selSess = null; //当前聊天会话对象
var recentSessMap = {}; //保存最近会话列表
var reqRecentSessCount = 50; //每次请求的最近会话条数，业务可以自定义

var isPeerRead = 1;//是否需要支持APP端已读回执的功能,默认为0。是：1，否：0。

//默认好友头像
var friendHeadUrl = 'img/friend.jpg'; //仅demo使用，用于没有设置过头像的好友
//默认群头像
var groupHeadUrl = 'img/group.jpg'; //仅demo使用，用于没有设置过群头像的情况


//存放c2c或者群信息（c2c用户：c2c用户id，昵称，头像；群：群id，群名称，群头像）
var infoMap = {}; //初始化时，可以先拉取我的好友和我的群组信息


var maxNameLen = 12; //我的好友或群组列表中名称显示最大长度，仅demo用得到
var reqMsgCount = 15; //每次请求的历史消息(c2c获取群)条数，仅demo用得到

var pageSize = 15; //表格的每页条数，bootstrap table 分页时用到
var totalCount = 200; //每次接口请求的条数，bootstrap table 分页时用到

var emotionFlag = false; //是否打开过表情选择框

var curPlayAudio = null; //当前正在播放的audio对象

var getPrePageC2CHistroyMsgInfoMap = {}; //保留下一次拉取好友历史消息的信息
var getPrePageGroupHistroyMsgInfoMap = {}; //保留下一次拉取群历史消息的信息

var defaultSelGroupId = null; //登录默认选中的群id，选填，仅demo用得到


//监听连接状态回调变化事件
var onConnNotify = function (resp) {
    console.warn(resp);
    var info;
    switch (resp.ErrorCode) {
        case webim.CONNECTION_STATUS.ON:
            webim.Log.warn('建立连接成功: ' + resp.ErrorInfo);
            break;
        case webim.CONNECTION_STATUS.OFF:
            info = '连接已断开，无法收到新消息，请检查下你的网络是否正常: ' + resp.ErrorInfo;
            // alert(info);
            webim.Log.warn(info);
            break;
        case webim.CONNECTION_STATUS.RECONNECT:
            info = '连接状态恢复正常: ' + resp.ErrorInfo;
            // alert(info);
            webim.Log.warn(info);
            break;
        default:
            webim.Log.error('未知连接状态: =' + resp.ErrorInfo);
            break;
    }
};

//IE9(含)以下浏览器用到的jsonp回调函数
function jsonpCallback(rspData) {
    webim.setJsonpLastRspData(rspData);
}


var isAccessFormalEnv = true; //是否访问正式环境


var isLogOn = false; //是否开启sdk在控制台打印日志

//初始化时，其他对象，选填
var options = {
    'isAccessFormalEnv': isAccessFormalEnv, //是否访问正式环境，默认访问正式，选填
    'isLogOn': isLogOn //是否开启控制台打印日志,默认开启，选填
}

Date.prototype.format = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1,                 //月份
        "d+": this.getDate(),                    //日
        "h+": this.getHours(),                   //小时
        "m+": this.getMinutes(),                 //分
        "s+": this.getSeconds(),                 //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds()             //毫秒
    };
    if (/(y+)/.test(fmt)) {
        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    }
    for (var k in o) {
        if (new RegExp("(" + k + ")").test(fmt)) {
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        }
    }
    return fmt;
}
Page({
    data: {
        friend: {},
        userInfo: {},
        content: '',
        userSig: '',
        identifier: '',
        msgs: []

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
        selToID = "wjsd_user_" + options.seqId;
        let _this = this;
        _this.setData({
            userInfo: app.globalData.userInfo
        })
        wx.request({
            url: config.url + "/getFriendById?seqId=" + options.seqId,
            success: function (res) {
                _this.setData({
                    friend: res.data.data.friend
                })
                wx.setNavigationBarTitle({
                    title: res.data.data.friend.nickName
                })
            }
        })
        wx.request({
            url: config.url + "/IMLogin?mySeqId=" + _this.data.userInfo.seqId,
            success: function (res) {
                loginInfo.identifier = 'wjsd_user_' + _this.data.userInfo.seqId;
                loginInfo.userSig = res.data.data.userSig;
                _this.webimLogin();
            }
        })
    },
    webimLogin: function () {
        var _this = this;

        //监听事件
        var listeners = {
            "onConnNotify": onConnNotify //监听连接状态回调变化事件,必填
            ,
            "jsonpCallback": jsonpCallback //IE9(含)以下浏览器用到的jsonp回调函数，
            ,
            "onMsgNotify": function (newMsgList) {
                console.info(newMsgList);
                var sess, newMsg;
                //获取所有聊天会话
                var sessMap = webim.MsgStore.sessMap();

                for (var j in newMsgList) {//遍历新消息
                    newMsg = newMsgList[j];

                    if (newMsg.getSession().id() == selToID) {//为当前聊天对象的消息
                        // selSess = newMsg.getSession();
                        //在聊天窗体中新增一条消息
                        //console.warn(newMsg);
                        _this.addMsg(newMsg);
                    }
                }
                //消息已读上报，以及设置会话自动已读标记
                // webim.setAutoRead(selSess, true, true);

                
            }
            ,
            "onKickedEventCall": function (opt) {
                console.error(opt)
            } //被其他登录实例踢下线
        };


        webim.login(
            loginInfo, listeners, options,
            function (resp) {
                console.info(resp);
                _this.getLastC2CHistoryMsgs(function (msg) {
                    var msgs = _this.data.msgs || [];
                    for (var i = 0; i < msg.length; i++) {
                        if (msg[i].isSend) {
                            msgs.push({
                                'isSend': msg[i].isSend,
                                'time': new Date(parseInt(msg[i].time + '000')).format('yyyy-MM-dd hh:mm:ss'),
                                'content': msg[i].elems[0].content.text,
                                'headUrl': _this.data.userInfo.avatarUrl,
                                'nickName': _this.data.userInfo.nickName
                            })
                        } else {
                            msgs.push({
                                'isSend': msg[i].isSend,
                                'time': new Date(parseInt(msg[i].time + '000')).format('yyyy-MM-dd hh:mm:ss'),
                                'content': msg[i].elems[0].content.text,
                                'headUrl': _this.data.friend.avatarUrl,
                                'nickName': _this.data.friend.nickName
                            })

                        }
                    }
                    _this.setData({
                        msgs: msgs
                    })
                    wx.pageScrollTo({
                        scrollTop: 10000
                    })
                })
                loginInfo.identifierNick = resp.identifierNick;//设置当前用户昵称
                loginInfo.headurl = resp.headurl;//设置当前用户头像
            },
            function (err) {
                console.error(err);
                wx.showModal({
                    title: '提示',
                    content: err.ErrorInfo,
                })
            }
        );
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
    pageScrollToBottom: function () {
        wx.createSelectorQuery().select('#chatId').boundingClientRect(function (rect) {
            // 使页面滚动到底部
            wx.pageScrollTo({
                scrollTop: rect.bottom
            })
        }).exec()
    },
    getLastC2CHistoryMsgs: function (cbOk, cbError) {
        if (selType == webim.SESSION_TYPE.GROUP) {
            alert('当前的聊天类型为群聊天，不能进行拉取好友历史消息操作');
            return;
        }
        var lastMsgTime = 0;//第一次拉取好友历史消息时，必须传 0
        var msgKey = '';
        var options = {
            'Peer_Account': selToID, //好友帐号
            'MaxCnt': reqMsgCount, //拉取消息条数
            'LastMsgTime': lastMsgTime, //最近的消息时间，即从这个时间点向前拉取历史消息
            'MsgKey': msgKey
        };
        webim.getC2CHistoryMsgs(
            options,
            function (resp) {
                var complete = resp.Complete;//是否还有历史消息可以拉取，1-表示没有，0-表示有
                var retMsgCount = resp.MsgCount;//返回的消息条数，小于或等于请求的消息条数，小于的时候，说明没有历史消息可拉取了
                if (resp.MsgList.length == 0) {
                    webim.Log.error("没有历史消息了:data=" + JSON.stringify(options));
                    return;
                }
                getPrePageC2CHistroyMsgInfoMap[selToID] = {//保留服务器返回的最近消息时间和消息Key,用于下次向前拉取历史消息
                    'LastMsgTime': resp.LastMsgTime,
                    'MsgKey': resp.MsgKey
                };
                if (cbOk)
                    cbOk(resp.MsgList);
            },
            cbError
        );
    },
    alert: function (con) {
        wx.showModal({
            title: '提示',
            content: con,
        })
    },
    onSendMsg: function () {
        var _this = this;
        if (!selToID) {
            _this.alert("你还没有选中好友或者群组，暂不能聊天");
            _this.clearComment();
            return;
        }
        //获取消息内容
        var msgtosend = _this.data.content;
        var msgLen = webim.Tool.getStrBytes(msgtosend);
        if (msgtosend.length < 1) {
            _this.alert("发送的消息不能为空!");
            _this.clearComment();
            return;
        }
        var maxLen, errInfo;
        if (selType == webim.SESSION_TYPE.C2C) {
            maxLen = webim.MSG_MAX_LENGTH.C2C;
            errInfo = "消息长度超出限制(最多" + Math.round(maxLen / 3) + "汉字)";
        } else {
            maxLen = webim.MSG_MAX_LENGTH.GROUP;
            errInfo = "消息长度超出限制(最多" + Math.round(maxLen / 3) + "汉字)";
        }
        if (msgLen > maxLen) {
            _this.alert(errInfo);
            return;
        }
        if (!selSess) {
            var selSess = new webim.Session(selType, selToID, selToID, friendHeadUrl, Math.round(new Date().getTime() / 1000));
        }
        var isSend = true;//是否为自己发送
        var seq = -1;//消息序列，-1表示 SDK 自动生成，用于去重
        var random = Math.round(Math.random() * 4294967296);//消息随机数，用于去重
        var msgTime = Math.round(new Date().getTime() / 1000);//消息时间戳
        var subType;//消息子类型
        if (selType == webim.SESSION_TYPE.C2C) {
            subType = webim.C2C_MSG_SUB_TYPE.COMMON;
        } else {
            //webim.GROUP_MSG_SUB_TYPE.COMMON-普通消息,
            //webim.GROUP_MSG_SUB_TYPE.LOVEMSG-点赞消息，优先级最低
            //webim.GROUP_MSG_SUB_TYPE.TIP-提示消息(不支持发送，用于区分群消息子类型)，
            //webim.GROUP_MSG_SUB_TYPE.REDPACKET-红包消息，优先级最高
            subType = webim.GROUP_MSG_SUB_TYPE.COMMON;
        }
        var msg = new webim.Msg(selSess, isSend, seq, random, msgTime, loginInfo.identifier, subType, loginInfo.identifierNick);
        var text_obj, face_obj, tmsg, emotionIndex, emotion, restMsgIndex;
        //解析文本和表情
        var expr = /\[[^[\]]{1,3}\]/mg;
        var emotions = msgtosend.match(expr);
        if (!emotions || emotions.length < 1) {
            text_obj = new webim.Msg.Elem.Text(msgtosend);
            msg.addText(text_obj);
        } else {
            for (var i = 0; i < emotions.length; i++) {
                tmsg = msgtosend.substring(0, msgtosend.indexOf(emotions[i]));
                if (tmsg) {
                    text_obj = new webim.Msg.Elem.Text(tmsg);
                    msg.addText(text_obj);
                }
                emotionIndex = webim.EmotionDataIndexs[emotions[i]];
                emotion = webim.Emotions[emotionIndex];
                if (emotion) {
                    face_obj = new webim.Msg.Elem.Face(emotionIndex, emotions[i]);
                    msg.addFace(face_obj);
                } else {
                    text_obj = new webim.Msg.Elem.Text(emotions[i]);
                    msg.addText(text_obj);
                }
                restMsgIndex = msgtosend.indexOf(emotions[i]) + emotions[i].length;
                msgtosend = msgtosend.substring(restMsgIndex);
            }
            if (msgtosend) {
                text_obj = new webim.Msg.Elem.Text(msgtosend);
                msg.addText(text_obj);
            }
        }
        webim.sendMsg(msg, function (resp) {
            if (selType == webim.SESSION_TYPE.C2C) {//私聊时，在聊天窗口手动添加一条发的消息，群聊时，长轮询接口会返回自己发的消息
                _this.addMsg(msg);
            }
            // webim.Tool.setCookie("tmpmsg_" + selToID, '', 0);
            _this.clearComment();
        }, function (err) {
            console.error(err);
            _this.alert(err.ErrorInfo);
            _this.clearComment();
        });
    },
    addMsg: function (msg) {
        var _this = this;
        var msgs = _this.data.msgs || [];
        msgs.push({
            'isSend': msg.isSend,
            'time': new Date(parseInt(msg.time + '000')).format('yyyy-MM-dd hh:mm:ss'),
            'content': msg.elems[0].content.text,
            'headUrl': _this.data.userInfo.avatarUrl,
            'nickName': _this.data.userInfo.nickName
        })

        _this.setData({
            msgs: msgs
        })
        wx.pageScrollTo({
            scrollTop: 10000
        })
    },
    onUnload: function () {
        selToID = null;
        webim.logout(function (rep) {
            console.info("退出成功");
            console.info(rep);
        }, function (err) {
            console.error("退出失败");
            console.error(err);
        });
    },

})