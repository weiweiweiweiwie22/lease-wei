package com.weiwei.lease.web.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.weiwei.lease.common.constant.RedisConstant;
import com.weiwei.lease.common.exception.BusinessException;
import com.weiwei.lease.common.result.ResultCodeEnum;
import com.weiwei.lease.common.utils.CodeUtil;
import com.weiwei.lease.common.utils.JwtUtil;
import com.weiwei.lease.model.entity.UserInfo;
import com.weiwei.lease.model.enums.BaseStatus;
import com.weiwei.lease.web.app.mapper.UserInfoMapper;
import com.weiwei.lease.web.app.service.LoginService;
import com.weiwei.lease.web.app.service.SmsService;
import com.weiwei.lease.web.app.vo.user.LoginVo;
import com.weiwei.lease.web.app.vo.user.UserInfoVo;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl implements LoginService {

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private SmsService smsService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public void getCode(String phone) {
        String code = CodeUtil.getCode(6);
        String key = RedisConstant.APP_LOGIN_PREFIX + phone;

        Boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey != null && hasKey){
            Long ttl = redisTemplate.getExpire(key, TimeUnit.SECONDS);
            if (RedisConstant.APP_LOGIN_CODE_TTL_SEC - ttl < RedisConstant.APP_LOGIN_CODE_RESEND_TIME_SEC){
                throw new BusinessException(ResultCodeEnum.APP_SEND_SMS_TOO_OFTEN);
            }
        }
        smsService.sendCode(phone, code);
        redisTemplate.opsForValue().set(key, code,RedisConstant.APP_LOGIN_CODE_TTL_SEC, TimeUnit.SECONDS);

    }

    @Override
    public String login(LoginVo loginVo) {

        if (loginVo.getPhone() == null){
            throw new BusinessException(ResultCodeEnum.APP_LOGIN_PHONE_EMPTY);
        }

        if (loginVo.getCode() == null){
            throw new BusinessException(ResultCodeEnum.APP_LOGIN_CODE_EMPTY);
        }

        String key = RedisConstant.APP_LOGIN_PREFIX + loginVo.getPhone();
        String code = redisTemplate.opsForValue().get(key);

        if (code == null){
            throw new BusinessException(ResultCodeEnum.APP_LOGIN_CODE_EXPIRED);
        }

        if (!code.equals(loginVo.getCode())){
            throw new BusinessException(ResultCodeEnum.APP_LOGIN_CODE_ERROR);
        }

        LambdaQueryWrapper<UserInfo> userInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userInfoLambdaQueryWrapper.eq(UserInfo::getPhone, loginVo.getPhone());
        UserInfo userInfo = userInfoMapper.selectOne(userInfoLambdaQueryWrapper);

        if (userInfo == null){
            userInfo = new UserInfo();
            userInfo.setPhone(loginVo.getPhone());
            userInfo.setNickname("用户-" + loginVo.getPhone().substring(7));
            userInfo.setStatus(BaseStatus.ENABLE);
            userInfoMapper.insert(userInfo);
        }else if (userInfo.getStatus() == BaseStatus.DISABLE){
            throw new BusinessException(ResultCodeEnum.APP_ACCOUNT_DISABLED_ERROR);
        }


        return JwtUtil.createToken(userInfo.getId(), userInfo.getPhone());
    }

    @Override
    public UserInfoVo selectOneByUserId(Long userId) {
        UserInfo userInfo = userInfoMapper.selectById(userId);
        return new UserInfoVo(userInfo.getNickname(), userInfo.getAvatarUrl());
    }
}
