package com.imooc.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.imooc.project.entity.Resource;
import com.imooc.project.dao.ResourceMapper;
import com.imooc.project.service.ResourceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imooc.project.vo.ResourceVO;
import com.imooc.project.vo.TreeVO;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * <p>
 * 资源表 服务实现类
 * </p>
 *
 * @author 梁海俊
 * @since 2021-03-18
 */
@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements ResourceService {
    /**
     * 根据id查询，该角色所具有的资源
     * @param roleId
     * @return
     */
    @Override
    public List<ResourceVO> listResourceByRoleId(Long roleId) {
        QueryWrapper<Resource> query = Wrappers.<Resource>query();
        query.eq("rr.role_id",roleId).isNull("re.parent_id").orderByAsc("re.sort");
        List<ResourceVO> resourceVOS=baseMapper.listResource(query);
        resourceVOS.forEach(r->{
            Long resourceId=r.getResourceId();
            QueryWrapper<Resource> subWrapper=Wrappers.<Resource>query();
            subWrapper.eq("rr.role_id",roleId).eq("re.parent_id",resourceId)
                    .orderByAsc("re.sort");
            List<ResourceVO> subResourceVOS=baseMapper.listResource(subWrapper);
            if(CollectionUtils.isNotEmpty(subResourceVOS)){
                r.setSubs(subResourceVOS);
            }
        });
        return resourceVOS;
    }

    @Override
    public List<TreeVO> listResource(Long roleId,Integer flag) {
        if (roleId==null){
            LambdaQueryWrapper<Resource> wrapper = Wrappers.<Resource>lambdaQuery()
                    .isNull(Resource::getParentId).orderByAsc(Resource::getSort);
            List<Resource> resources = list(wrapper);

            List<TreeVO> treeVOS = resources.stream().map(r -> {
                TreeVO treeVO = new TreeVO();
                treeVO.setId(r.getResourceId());
                treeVO.setTitle(r.getResourceName());

                LambdaQueryWrapper<Resource> subwrapper = Wrappers.<Resource>lambdaQuery()
                        .eq(Resource::getParentId, r.getResourceId()).orderByAsc(Resource::getSort);
                List<Resource> subRescoures = list(subwrapper);
                if (CollectionUtils.isNotEmpty(subRescoures)) {
                    List<TreeVO> children = subRescoures.stream().map(sub -> {
                        TreeVO subtreeVO = new TreeVO();
                        subtreeVO.setId(sub.getResourceId());
                        subtreeVO.setTitle(sub.getResourceName());
                        return subtreeVO;
                    }).collect(Collectors.toList());
                    treeVO.setChildren(children);
                }
                return treeVO;
            }).collect(Collectors.toList());
            return treeVOS;
        }else {
            QueryWrapper<Resource> query = Wrappers.<Resource>query();
            query.eq(flag==1,"rr.role_id",roleId)
                    .isNull("re.parent_id").orderByAsc("re.sort");
            List<TreeVO> treeVOS = baseMapper.listResourceByRoleId(query, roleId);
            treeVOS.forEach(t->{
                t.setChecked(false);
                Long id = t.getId();
                QueryWrapper<Resource> subWrapper=Wrappers.<Resource>query();
                subWrapper.eq(flag==1,"rr.role_id",roleId)
                        .eq("re.parent_id",id)
                        .orderByAsc("re.sort");
                List<TreeVO> children = baseMapper.listResourceByRoleId(subWrapper, roleId);
                if (CollectionUtils.isNotEmpty(children)){
                    t.setChildren(children);
                }

            });

            return treeVOS;
        }
    }
    @Override
    public HashSet<String> convert(List<ResourceVO> resourceVOS) {
        HashSet<String> module=new HashSet<>();
        resourceVOS.forEach(r->{
            String url = r.getUrl();
            if(StringUtils.isNotBlank(url)){
                module.add(url.substring(0,url.indexOf("/")));
            }
            List<ResourceVO> subResourceVOs = r.getSubs();
            if(CollectionUtils.isNotEmpty(subResourceVOs)){
                subResourceVOs.forEach(sub->{
                    String subUrl=sub.getUrl();
                    if(StringUtils.isNotBlank(subUrl)){
                        module.add(subUrl.substring(0,subUrl.indexOf("/")));
                    }
                });
            }
        });
        return module;
    }
}
