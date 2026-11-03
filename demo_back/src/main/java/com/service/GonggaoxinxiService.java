package com.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.entity.GonggaoxinxiEntity;
import com.entity.view.GonggaoxinxiView;
import com.entity.vo.GonggaoxinxiVO;
import com.utils.PageUtils;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * 公告信息
 *
 * @author
 * @email
 * @date 2022-03-25 19:57:55
 */
public interface GonggaoxinxiService extends IService<GonggaoxinxiEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<GonggaoxinxiVO> selectListVO(Wrapper<GonggaoxinxiEntity> wrapper);

    GonggaoxinxiVO selectVO(@Param("ew") Wrapper<GonggaoxinxiEntity> wrapper);

    List<GonggaoxinxiView> selectListView(Wrapper<GonggaoxinxiEntity> wrapper);

    GonggaoxinxiView selectView(@Param("ew") Wrapper<GonggaoxinxiEntity> wrapper);

    PageUtils queryPage(Map<String, Object> params, Wrapper<GonggaoxinxiEntity> wrapper);


}

