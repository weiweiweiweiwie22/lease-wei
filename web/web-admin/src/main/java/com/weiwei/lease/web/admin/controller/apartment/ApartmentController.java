package com.weiwei.lease.web.admin.controller.apartment;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.weiwei.lease.common.result.Result;
import com.weiwei.lease.model.entity.ApartmentInfo;
import com.weiwei.lease.model.enums.ReleaseStatus;
import com.weiwei.lease.web.admin.service.ApartmentInfoService;
import com.weiwei.lease.web.admin.vo.apartment.ApartmentDetailVo;
import com.weiwei.lease.web.admin.vo.apartment.ApartmentItemVo;
import com.weiwei.lease.web.admin.vo.apartment.ApartmentQueryVo;
import com.weiwei.lease.web.admin.vo.apartment.ApartmentSubmitVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//0

@Tag(name = "公寓信息管理")
@RestController
@RequestMapping("/admin/apartment")
public class ApartmentController {

    @Resource
    private ApartmentInfoService apartmentService;
    @Operation(summary = "保存或更新公寓信息")
    @PostMapping("saveOrUpdate")
    public Result saveOrUpdate(@RequestBody ApartmentSubmitVo apartmentSubmitVo) {
        apartmentService.saveOrUpdateApartment(apartmentSubmitVo);
        return Result.ok();
    }

    @Operation(summary = "根据条件分页查询公寓列表")
    @GetMapping("pageItem")
    public Result<IPage<ApartmentItemVo>> pageItem(@RequestParam long current, @RequestParam long size, ApartmentQueryVo queryVo) {
        Page<ApartmentItemVo> page = new Page<>(current,size);
        IPage<ApartmentItemVo> list = apartmentService.pageItem(page, queryVo);
        return Result.ok(list);
    }

    @Operation(summary = "根据ID获取公寓详细信息")
    @GetMapping("getDetailById")
    public Result<ApartmentDetailVo> getDetailById(@RequestParam Long id) {
        ApartmentDetailVo result = apartmentService.getDetailById(id);
        return Result.ok(result);
    }

    @Operation(summary = "根据id删除公寓信息")
    @DeleteMapping("removeById")
    public Result removeById(@RequestParam Long id) {
        apartmentService.removeApartmentById(id);
        return Result.ok();
    }

    @Operation(summary = "根据id修改公寓发布状态")
    @PostMapping("updateReleaseStatusById")
    public Result updateReleaseStatusById(@RequestParam Long id, @RequestParam ReleaseStatus status) {
        LambdaUpdateWrapper<ApartmentInfo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ApartmentInfo::getId, id);
        updateWrapper.set(ApartmentInfo::getIsRelease, status);
        apartmentService.update(updateWrapper);
        return Result.ok();
    }

    @Operation(summary = "根据区县id查询公寓信息列表")
    @GetMapping("listInfoByDistrictId")
    public Result<List<ApartmentInfo>> listInfoByDistrictId(@RequestParam Long id) {
        List<ApartmentInfo> list = apartmentService.list(new LambdaQueryWrapper<ApartmentInfo>().eq(ApartmentInfo::getDistrictId, id));
        return Result.ok(list);

    }
}














