package com.sky.controller.admin;


import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


/**
 * 分类管理
 */
@RestController
@RequestMapping("/admin/category")
@Api(tags = "分类接口文档")
@Slf4j
public class CategoryController {

    @Resource
    private CategoryService categoryService;


    /**
     * 新增分类
     * @return
     */
    @PostMapping
    @ApiOperation("新增分类")

    public Result<CategoryDTO> save(@RequestBody CategoryDTO categoryDTO){
        log.info("新增分类:{}",categoryDTO);
        categoryService.save(categoryDTO);
        return Result.success();
    }

    /**
     * 分类分页查询
     *
     * @param categoryPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("分类分页查询")
    public Result<PageResult> page(CategoryPageQueryDTO categoryPageQueryDTO){
        log.info("分类分页查询：{}",categoryPageQueryDTO);
        PageResult pageResult=categoryService.page(categoryPageQueryDTO);
        return Result.success(pageResult);

    }

    /**
     * 根据id删除分类
     * @return
     */
    @DeleteMapping
    @ApiOperation("根据id删除分类")
    public Result<String> delete(Long id){
        log.info("根据id删除分类:{}",id);
        categoryService.delete(id);
        return Result.success();
    }

    /**
     * 修改分类
     * @param categoryDTO
     * @return
     */
    @PutMapping
    @ApiOperation("修改分类")
    public Result update(CategoryDTO categoryDTO){
        log.info("修改分类:{}",categoryDTO);
        return Result.success();
    }

    /**
     * 启用、禁用分类
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("启用、禁用分类")
    public Result startOrStop(@PathVariable("status") Integer status,Long id){
        log.info("状态和id:{},{}",status,id);
        categoryService.startOrStop(status,id);
        return  Result.success();
    }

    /**
     * 根据类型查询分类
     * @param type
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据类型查询分类")
    public Result<List<Category>> list(Integer type){
        List<Category> list = categoryService.list(type);
        return Result.success(list);
    }
}
