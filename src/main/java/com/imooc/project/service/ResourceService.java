package com.imooc.project.service;

import com.imooc.project.entity.Resource;
import com.baomidou.mybatisplus.extension.service.IService;
import com.imooc.project.vo.ResourceVO;
import com.imooc.project.vo.TreeVO;

import java.util.HashSet;
import java.util.List;

/**
 * <p>
 * 资源表 服务类
 * </p>
 *
 * @author 梁海俊
 * @since 2021-03-18
 */
public interface ResourceService extends IService<Resource> {
    /**
     * 根据id查询，该角色所具有的资源
     * @param roleId
     * @return
     */
    List<ResourceVO> listResourceByRoleId(Long roleId);

    /**
     * 查询系统资源，供前端组件渲染
     * @return
     */
    List<TreeVO> listResource(Long roleId,Integer flag);

    HashSet<String> convert(List<ResourceVO> resourceVOS);
}
