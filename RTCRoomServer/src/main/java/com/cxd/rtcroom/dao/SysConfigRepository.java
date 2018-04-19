package com.cxd.rtcroom.dao;

import com.cxd.rtcroom.bean.SysConfig;
import com.cxd.rtcroom.bean.UserInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface SysConfigRepository extends CrudRepository<SysConfig, Long> {



}
