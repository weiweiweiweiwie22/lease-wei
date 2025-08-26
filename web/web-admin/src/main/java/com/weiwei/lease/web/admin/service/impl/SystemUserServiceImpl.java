package com.weiwei.lease.web.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.weiwei.lease.model.entity.SystemPost;
import com.weiwei.lease.model.entity.SystemUser;
import com.weiwei.lease.web.admin.mapper.SystemPostMapper;
import com.weiwei.lease.web.admin.mapper.SystemUserMapper;
import com.weiwei.lease.web.admin.service.SystemUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weiwei.lease.web.admin.vo.system.user.SystemUserItemVo;
import com.weiwei.lease.web.admin.vo.system.user.SystemUserQueryVo;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * @author liubo
 * @description 针对表【system_user(员工信息表)】的数据库操作Service实现
 * @createDate 2023-07-24 15:48:00
 */
@Service
public class SystemUserServiceImpl extends ServiceImpl<SystemUserMapper, SystemUser>
        implements SystemUserService {

    @Resource
    private SystemUserMapper systemUserMapper;

    @Resource
    private SystemPostMapper systemPostMapper;
    @Override
    public IPage<SystemUserItemVo> pageSystemUserItem(Page<SystemUserItemVo> page, SystemUserQueryVo queryVo) {

        return systemUserMapper.pageSystemUserItem(page, queryVo);
    }

    @Override
    public SystemUserItemVo getSystemUserById(Long id) {
        SystemUser systemUser = systemUserMapper.selectById(id);
        SystemPost systemPost = systemPostMapper.selectById(systemUser.getPostId());
        SystemUserItemVo systemUserItemVo = new SystemUserItemVo();
        BeanUtils.copyProperties(systemUser, systemUserItemVo);
        systemUserItemVo.setPostName(systemPost.getName());
        return systemUserItemVo;
    }
}




