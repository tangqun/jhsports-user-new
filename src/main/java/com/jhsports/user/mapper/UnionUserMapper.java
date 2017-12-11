package com.jhsports.user.mapper;

import com.jhsports.user.entity.UnionUser;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * Created by TQ on 2017/12/1.
 */
@Repository
public interface UnionUserMapper {
    int insert(UnionUser unionUser);
    int delete(String id);
    int bindUser(Map map);
    int oldToNew(Map map);
    int update(Map map);
    int resetMobileNum(Map map);
    int resetPassword(Map map);
    UnionUser selectByMobileNum(String mobileNum);
    UnionUser selectById(String id);
    UnionUser selectByUnionId(String unionId);
    UnionUser selectByAuthorizedUserId(String authorizedUserId);
}
