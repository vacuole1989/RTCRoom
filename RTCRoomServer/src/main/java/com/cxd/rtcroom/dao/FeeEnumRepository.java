package com.cxd.rtcroom.dao;

import com.cxd.rtcroom.bean.FeeEnum;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FeeEnumRepository extends CrudRepository<FeeEnum, Long> {

    List<FeeEnum> findFeeEnumsByAppId(String appId);

}
