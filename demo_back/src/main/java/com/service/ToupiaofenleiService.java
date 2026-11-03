package com.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.entity.ToupiaofenleiEntity;
import com.entity.view.ToupiaofenleiView;
import com.entity.vo.ToupiaofenleiVO;
import com.utils.PageUtils;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * 投票分类
 *
 * @author
 * @email
 * @date 2022-03-25 19:57:55
 */
public interface ToupiaofenleiService extends IService<ToupiaofenleiEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<ToupiaofenleiVO> selectListVO(Wrapper<ToupiaofenleiEntity> wrapper);

    ToupiaofenleiVO selectVO(@Param("ew") Wrapper<ToupiaofenleiEntity> wrapper);

    List<ToupiaofenleiView> selectListView(Wrapper<ToupiaofenleiEntity> wrapper);

    ToupiaofenleiView selectView(@Param("ew") Wrapper<ToupiaofenleiEntity> wrapper);

    PageUtils queryPage(Map<String, Object> params, Wrapper<ToupiaofenleiEntity> wrapper);


}

