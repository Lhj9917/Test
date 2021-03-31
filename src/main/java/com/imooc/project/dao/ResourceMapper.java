package com.imooc.project.dao;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.imooc.project.entity.Resource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.imooc.project.vo.ResourceVO;
import com.imooc.project.vo.TreeVO;
import org.apache.ibatis.annotations.Param;


import java.util.List;

/**
 * <p>
 * 资源表 Mapper 接口
 * </p>
 *
 * @author 梁海俊
 * @since 2021-03-18
 */
public interface ResourceMapper extends BaseMapper<Resource> {
    /**
     * 查询当前登录人的资源
     * @param wrapper
     * @return
     */
    List<ResourceVO> listResource(@Param(Constants.WRAPPER) Wrapper<Resource> wrapper);

    List<TreeVO> listResourceByRoleId(@Param(Constants.WRAPPER) Wrapper<Resource> wrapper
            ,@Param("roleId") Long roleId);
}
