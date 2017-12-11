package com.jhsports.user.mapper;

import com.jhsports.user.entity.AppToken;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * Created by TQ on 2017/12/4.
 */
@Repository
public interface AppTokenMapper {
    int insert(AppToken appToken);
    int delete(Map map);
    int update(AppToken appToken);
    int conflictWithNewBindOldBind(Map map);
    int updateByAppId_UnionUserId(Map map);
    // 续期
    int renewal(Map map);
    // 过期
    int expire(Map map);
    AppToken selectByAppId_UnionUserId(Map map);
    AppToken selectByTokenValue(String tokenValue);
}
