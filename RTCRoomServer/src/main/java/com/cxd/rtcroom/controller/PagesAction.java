package com.cxd.rtcroom.controller;

import com.cxd.rtcroom.bean.AppTag;
import com.cxd.rtcroom.bean.FeeEnum;
import com.cxd.rtcroom.bean.InsImg;
import com.cxd.rtcroom.bean.SwitchTag;
import com.cxd.rtcroom.dao.AppTagRepository;
import com.cxd.rtcroom.dao.FeeEnumRepository;
import com.cxd.rtcroom.dao.InsImgRepository;
import com.cxd.rtcroom.dto.JSONResult;
import com.cxd.rtcroom.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author ws03089
 */
@RestController
@RequestMapping("/page")
public class PagesAction {

    @Autowired
    private AppTagRepository appTagRepository;
    @Autowired
    private FeeEnumRepository feeEnumRepository;
    @Autowired
    private InsImgRepository insImgRepository;


    @RequestMapping("/initData")
    public Object index(HttpServletRequest request, HttpSession session) {
        AppTag appTag = (AppTag) session.getAttribute("loginUser");
        if (!StringUtils.isEmpty(appTag)) {
            FeeEnum feeEnum1 = new FeeEnum(appTag.getAppId(), feeEnumRepository);
            appTag = appTagRepository.findOne(appTag.getAppId());
            List<FeeEnum> feeEnums = feeEnum1.validateSwitch(appTag);
            InsImg one= insImgRepository.findOne(appTag.getAppId());
            return new JSONResult(true, feeEnums,one);
        } else {
            return new JSONResult(false, "");

        }
    }


    @RequestMapping("/login")
    public Object login(HttpServletRequest request, HttpSession session, @RequestBody AppTag appTag) {
        List<AppTag> appTagsByLoginName = appTagRepository.findAppTagsByLoginName(appTag.getLoginName());
        if (appTagsByLoginName.size() > 0) {
            AppTag appTag1 = appTagsByLoginName.get(0);
            if (MD5Util.MD5(appTag.getUserPass()).equalsIgnoreCase(appTag1.getUserPass())) {
                session.setAttribute("loginUser", appTag1);
                return new JSONResult(true, appTag1);
            }
        }
        return new JSONResult(false, "登陆失败");
    }
    @RequestMapping("/editPwd")
    public Object editPwd(HttpServletRequest request, HttpSession session, @RequestBody AppTag appTag) {

        if(StringUtils.isEmpty(appTag.getUserPass())){
            return new JSONResult(false, "密码不能为空");
        }

        AppTag appTag1 = (AppTag) session.getAttribute("loginUser");
        if (!StringUtils.isEmpty(appTag1)) {

             appTag1.setUserPass(MD5Util.MD5(appTag.getUserPass()));
             appTagRepository.save(appTag1);

            return new JSONResult(true, "密码修改成功，请重新登陆");
        } else {
            return new JSONResult(false, "");

        }
    }



    @RequestMapping("/saveFeeDetail")
    public Object saveFeeDetail(HttpServletRequest request, HttpSession session, @RequestBody FeeEnum feeEnum) {

        FeeEnum one = feeEnumRepository.findOne(feeEnum.getSeqId());
        one.setFee(feeEnum.getFee());
        one.setName(feeEnum.getName());
        one.setShowTitle(feeEnum.getShowTitle());
        feeEnumRepository.save(one);

        AppTag appTag = (AppTag) session.getAttribute("loginUser");
        if (!StringUtils.isEmpty(appTag)) {
            FeeEnum feeEnum1 = new FeeEnum(appTag.getAppId(), feeEnumRepository);
            appTag = appTagRepository.findOne(appTag.getAppId());
            List<FeeEnum> feeEnums = feeEnum1.validateSwitch(appTag);
            return new JSONResult(true, feeEnums);
        }

        return new JSONResult(false, "修改失败");
    }

    @RequestMapping("/changeSwitch")
    public void changeSwitch(HttpServletRequest request, HttpSession session, @RequestBody SwitchTag switchTag) {
        AppTag appTag = (AppTag) session.getAttribute("loginUser");

        if (!StringUtils.isEmpty(appTag)) {
            appTag = appTagRepository.findOne(appTag.getAppId());
            appTag.setSearchSwitch(null == appTag.getSearchSwitch() ? "" : appTag.getSearchSwitch());
            if (switchTag.isIcheck()) {
                appTag.setSearchSwitch(appTag.getSearchSwitch().toLowerCase() + "," + switchTag.getTag());
            } else {
                appTag.setSearchSwitch(appTag.getSearchSwitch().toLowerCase().replaceAll(switchTag.getTag() + ",", "").replaceAll("," + switchTag.getTag(), ""));
            }
            appTagRepository.save(appTag);
        }
    }

    @RequestMapping("/loginOut")
    public Object loginout(HttpServletRequest request, HttpSession session) {
        session.removeAttribute("loginUser");
        return new JSONResult(true, "");
    }

    @RequestMapping("/uploadFile")
    public Object upload(HttpServletRequest request, HttpSession session, @RequestBody InsImg insImg) {
        AppTag appTag = (AppTag) session.getAttribute("loginUser");
        if (!StringUtils.isEmpty(appTag)) {
            appTag = appTagRepository.findOne(appTag.getAppId());
            insImg.setAppId(appTag.getAppId());
            insImgRepository.save(insImg);
            return new JSONResult(true, "上传成功",insImg);
        }

        return new JSONResult(false, "上传失败");


    }

}
