package com.service;

import com.common.ServerResponse;
import com.pojo.Category;

import java.util.List;

/**
 * Created by LINxwFF on 2017/11/20.
 */
public interface ICategoryService {
    ServerResponse addCategory(String categoryName, Integer parentId);
    ServerResponse UpdateCategory(String categoryName, Integer categoryId);
    ServerResponse<List<Category>> getChildrenCategory (Integer categoryId);
    ServerResponse selectCategoryAndChildrenById(Integer categoryId);
}
