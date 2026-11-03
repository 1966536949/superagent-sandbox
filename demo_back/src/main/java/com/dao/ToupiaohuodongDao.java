package com.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.entity.ToupiaohuodongEntity;
import com.entity.view.ToupiaohuodongView;
import com.entity.vo.ToupiaohuodongVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * 投票活动
 *
 * @author
 * @email
 * @date 2022-03-25 19:57:55
 */
public interface ToupiaohuodongDao extends BaseMapper<ToupiaohuodongEntity> {

    List<ToupiaohuodongVO> selectListVO(@Param("ew") Wrapper<ToupiaohuodongEntity> wrapper);

    ToupiaohuodongVO selectVO(@Param("ew") Wrapper<ToupiaohuodongEntity> wrapper);

    List<ToupiaohuodongView> selectListView(@Param("ew") Wrapper<ToupiaohuodongEntity> wrapper);

    List<ToupiaohuodongView> selectListView(Pagination page, @Param("ew") Wrapper<ToupiaohuodongEntity> wrapper);

    ToupiaohuodongView selectView(@Param("ew") Wrapper<ToupiaohuodongEntity> wrapper);


    List<Map<String, Object>> selectValue(@Param("params") Map<String, Object> params, @Param("ew") Wrapper<ToupiaohuodongEntity> wrapper);

    List<Map<String, Object>> selectTimeStatValue(@Param("params") Map<String, Object> params, @Param("ew") Wrapper<ToupiaohuodongEntity> wrapper);

    List<Map<String, Object>> selectGroup(@Param("params") Map<String, Object> params, @Param("ew") Wrapper<ToupiaohuodongEntity> wrapper);
}
