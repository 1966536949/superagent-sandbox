package com.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.entity.ToupiaojiluEntity;
import com.entity.view.ToupiaojiluView;
import com.entity.vo.ToupiaojiluVO;
import com.utils.PageUtils;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * 投票记录
 *
 * @author
 * @email
 * @date 2022-03-25 19:57:55
 */
public interface ToupiaojiluService extends IService<ToupiaojiluEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<ToupiaojiluVO> selectListVO(Wrapper<ToupiaojiluEntity> wrapper);

    ToupiaojiluVO selectVO(@Param("ew") Wrapper<ToupiaojiluEntity> wrapper);

    List<ToupiaojiluView> selectListView(Wrapper<ToupiaojiluEntity> wrapper);

    ToupiaojiluView selectView(@Param("ew") Wrapper<ToupiaojiluEntity> wrapper);

    PageUtils queryPage(Map<String, Object> params, Wrapper<ToupiaojiluEntity> wrapper);


    List<Map<String, Object>> selectValue(Map<String, Object> params, Wrapper<ToupiaojiluEntity> wrapper);

    List<Map<String, Object>> selectTimeStatValue(Map<String, Object> params, Wrapper<ToupiaojiluEntity> wrapper);

    List<Map<String, Object>> selectGroup(Map<String, Object> params, Wrapper<ToupiaojiluEntity> wrapper);
}

