package com.cxd.rtcroom.dao;

import com.cxd.rtcroom.bean.QueryLogs;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface QueryLogsRepository extends CrudRepository<QueryLogs, Long> {
List<QueryLogs> getQueryLogsByNonceStr(String nonceStr);
}
