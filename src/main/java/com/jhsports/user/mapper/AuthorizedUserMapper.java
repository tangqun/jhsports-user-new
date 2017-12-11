package com.jhsports.user.mapper;

import com.jhsports.user.entity.AuthorizedUser;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * Created by TQ on 2017/12/5.
 */
@Repository
public interface AuthorizedUserMapper {
    int insert(AuthorizedUser authorizedUser);
    int update(Map map);
    AuthorizedUser selectByAppId_OpenId(Map map);
}
