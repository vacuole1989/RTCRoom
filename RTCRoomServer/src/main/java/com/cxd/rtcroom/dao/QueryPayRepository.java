package com.cxd.rtcroom.dao;

import com.cxd.rtcroom.bean.QueryPay;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface QueryPayRepository extends CrudRepository<QueryPay, Long> {

    List<QueryPay> getQueryPayByNonceStr(String nonceStr);

}
