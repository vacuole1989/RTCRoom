package com.cxd.rtcroom.dao;


import com.cxd.rtcroom.bean.Category;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CategoryRepository extends CrudRepository<Category, Long> {

    List<Category> findByTypeAndVideo(int type, boolean video);
}
