package com.weiwei.lease.web.app.mapper;

import com.weiwei.lease.model.entity.ViewAppointment;
import com.weiwei.lease.web.app.vo.appointment.AppointmentItemVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author liubo
* @description 针对表【view_appointment(预约看房信息表)】的数据库操作Mapper
* @createDate 2023-07-26 11:12:39
* @Entity com.weiwei.lease.model.entity.ViewAppointment
*/
public interface ViewAppointmentMapper extends BaseMapper<ViewAppointment> {

    List<AppointmentItemVo> getAppointmentItemVo(Long userId);
}




