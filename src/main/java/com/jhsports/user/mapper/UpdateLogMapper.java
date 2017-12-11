package com.jhsports.user.mapper;

import com.jhsports.user.entity.UpdateLog;
import org.springframework.stereotype.Repository;

/**
 * Created by TQ on 2017/12/6.
 */
@Repository
public interface UpdateLogMapper {
    int insert(UpdateLog updateLog);
}
