package com.weiwei.lease.web.admin.service;

import com.weiwei.lease.model.entity.ApartmentInfo;
import com.weiwei.lease.web.admin.vo.apartment.ApartmentDetailVo;
import com.weiwei.lease.web.admin.vo.apartment.ApartmentItemVo;
import com.weiwei.lease.web.admin.vo.apartment.ApartmentQueryVo;
import com.weiwei.lease.web.admin.vo.apartment.ApartmentSubmitVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author liubo
* @description 针对表【apartment_info(公寓信息表)】的数据库操作Service
* @createDate 2023-07-24 15:48:00
*/
public interface ApartmentInfoService extends IService<ApartmentInfo> {


    void saveOrUpdateApartment(ApartmentSubmitVo apartmentSubmitVo);

    IPage<ApartmentItemVo> pageItem(Page<ApartmentItemVo> page, ApartmentQueryVo queryVo);


    ApartmentDetailVo getDetailById(Long id);

    void removeApartmentById(Long id);
}
