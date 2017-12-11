package com.jhsports.user.mapper;

import com.jhsports.user.entity.App;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by TQ on 2017/11/22.
 */
@Repository
public interface AppMapper {
    List<App> selectAll();
}
