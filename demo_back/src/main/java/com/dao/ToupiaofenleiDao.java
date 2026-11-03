package com.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.entity.ToupiaofenleiEntity;
import com.entity.view.ToupiaofenleiView;
import com.entity.vo.ToupiaofenleiVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 投票分类
 *
 * @author
 * @email
 * @date 2022-03-25 19:57:55
 */
public interface ToupiaofenleiDao extends BaseMapper<ToupiaofenleiEntity> {

    List<ToupiaofenleiVO> selectListVO(@Param("ew") Wrapper<ToupiaofenleiEntity> wrapper);

    ToupiaofenleiVO selectVO(@Param("ew") Wrapper<ToupiaofenleiEntity> wrapper);

    List<ToupiaofenleiView> selectListView(@Param("ew") Wrapper<ToupiaofenleiEntity> wrapper);

    List<ToupiaofenleiView> selectListView(Pagination page, @Param("ew") Wrapper<ToupiaofenleiEntity> wrapper);

    ToupiaofenleiView selectView(@Param("ew") Wrapper<ToupiaofenleiEntity> wrapper);


}
