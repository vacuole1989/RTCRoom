package com.cxd.rtcroom.dao;

import com.cxd.rtcroom.bean.AppTag;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AppTagRepository extends CrudRepository<AppTag, String> {
    List<AppTag> findAppTagsByLoginName(String loginName);
}
