package com.cxd.rtcroom.dao;


import com.cxd.rtcroom.bean.QueryPay;
import org.springframework.data.repository.CrudRepository;

public interface QueryPayRepository extends CrudRepository<QueryPay, Long> {

    QueryPay findQueryPayByNonceStr(String nonceStr);

}
