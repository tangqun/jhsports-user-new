package com.jhsports.user.mapper;

import com.jhsports.user.entity.SMSCode;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * Created by TQ on 2017/11/22.
 */
@Repository
public interface SMSCodeMapper {
    int insert(SMSCode smsCode);
    SMSCode selectByMobileNum(Map map);
}
