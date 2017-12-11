package com.jhsports.user.mapper;

import com.jhsports.user.entity.LoginLog;
import org.springframework.stereotype.Repository;

/**
 * Created by TQ on 2017/12/4.
 */
@Repository
public interface LoginLogMapper {
    int insert(LoginLog loginLog);
}
