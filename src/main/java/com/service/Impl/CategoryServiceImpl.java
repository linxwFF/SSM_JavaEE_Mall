package com.service.Impl;

import com.common.ServerResponse;
import com.dao.CategoryMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.pojo.Category;
import com.service.ICategoryService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * Created by LINxwFF on 2017/11/20.
 */
@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService{

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public ServerResponse addCategory(String categoryName, Integer parentId)
    {
        if(StringUtils.isBlank(categoryName) || parentId == null)
        {
            return ServerResponse.createByErrorMsg("添加分类，参数错误");
        }

        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true);

        int rowCount = categoryMapper.insert(category);
        if(rowCount > 0){
            return ServerResponse.createBySuccessMessage("添加分类成功");
        }
        return ServerResponse.createByErrorMsg("添加分类失败");
    }

    @Override
    public ServerResponse UpdateCategory(String categoryName, Integer categoryId)
    {
        if(StringUtils.isBlank(categoryName) && categoryId == null)
        {
            return ServerResponse.createByErrorMsg("更新分类，参数错误");
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setId(categoryId);

        int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
        if(rowCount > 0){
            return ServerResponse.createBySuccessMessage("更新分类成功");
        }
        return ServerResponse.createByErrorMsg("更新分类失败");
    }

    @Override
    public ServerResponse<List<Category>> getChildrenCategory(Integer categoryId)
    {
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        if(CollectionUtils.isEmpty(categoryList)){
            return ServerResponse.createBySuccessMessage("无子分类");
        }

        return ServerResponse.createBySuccess(categoryList);
    }


    //递归算法算出子节点
    private Set<Category> findChildCategory(Set<Category> categorySet, Integer categoryId)
    {
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if(category != null){
            categorySet.add(category);
        }

        //查找子节点
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        for (Category categoryItem: categoryList){
            findChildCategory(categorySet,categoryItem.getId());
        }

        return categorySet;
    }

    /**
     * 递归查询本节点id和孩子节点的Id
     * @param categoryId
     * @return
     */
    public ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId)
    {
        Set<Category> categorySet = Sets.newHashSet();
        findChildCategory(categorySet,categoryId);

        List<Integer> categoryIdList = Lists.newArrayList();
        if(categoryId != null){
            for (Category categoryItem : categorySet){
                categoryIdList.add(categoryItem.getId());
            }
        }

        return ServerResponse.createBySuccess(categoryIdList);
    }
}
