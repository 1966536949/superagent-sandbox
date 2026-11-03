package com.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.entity.GonggaoxinxiEntity;
import com.entity.view.GonggaoxinxiView;
import com.entity.vo.GonggaoxinxiVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 公告信息
 *
 * @author
 * @email
 * @date 2022-03-25 19:57:55
 */
public interface GonggaoxinxiDao extends BaseMapper<GonggaoxinxiEntity> {

    List<GonggaoxinxiVO> selectListVO(@Param("ew") Wrapper<GonggaoxinxiEntity> wrapper);

    GonggaoxinxiVO selectVO(@Param("ew") Wrapper<GonggaoxinxiEntity> wrapper);

    List<GonggaoxinxiView> selectListView(@Param("ew") Wrapper<GonggaoxinxiEntity> wrapper);

    List<GonggaoxinxiView> selectListView(Pagination page, @Param("ew") Wrapper<GonggaoxinxiEntity> wrapper);

    GonggaoxinxiView selectView(@Param("ew") Wrapper<GonggaoxinxiEntity> wrapper);


}
