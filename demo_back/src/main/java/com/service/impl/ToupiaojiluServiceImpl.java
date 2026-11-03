package com.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dao.ToupiaojiluDao;
import com.entity.ToupiaojiluEntity;
import com.entity.view.ToupiaojiluView;
import com.entity.vo.ToupiaojiluVO;
import com.service.ToupiaojiluService;
import com.utils.PageUtils;
import com.utils.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("toupiaojiluService")
public class ToupiaojiluServiceImpl extends ServiceImpl<ToupiaojiluDao, ToupiaojiluEntity> implements ToupiaojiluService {


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<ToupiaojiluEntity> page = this.selectPage(
                new Query<ToupiaojiluEntity>(params).getPage(),
                new EntityWrapper<ToupiaojiluEntity>()
        );
        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Wrapper<ToupiaojiluEntity> wrapper) {
        Page<ToupiaojiluView> page = new Query<ToupiaojiluView>(params).getPage();
        page.setRecords(baseMapper.selectListView(page, wrapper));
        PageUtils pageUtil = new PageUtils(page);
        return pageUtil;
    }

    @Override
    public List<ToupiaojiluVO> selectListVO(Wrapper<ToupiaojiluEntity> wrapper) {
        return baseMapper.selectListVO(wrapper);
    }

    @Override
    public ToupiaojiluVO selectVO(Wrapper<ToupiaojiluEntity> wrapper) {
        return baseMapper.selectVO(wrapper);
    }

    @Override
    public List<ToupiaojiluView> selectListView(Wrapper<ToupiaojiluEntity> wrapper) {
        return baseMapper.selectListView(wrapper);
    }

    @Override
    public ToupiaojiluView selectView(Wrapper<ToupiaojiluEntity> wrapper) {
        return baseMapper.selectView(wrapper);
    }

    @Override
    public List<Map<String, Object>> selectValue(Map<String, Object> params, Wrapper<ToupiaojiluEntity> wrapper) {
        return baseMapper.selectValue(params, wrapper);
    }

    @Override
    public List<Map<String, Object>> selectTimeStatValue(Map<String, Object> params, Wrapper<ToupiaojiluEntity> wrapper) {
        return baseMapper.selectTimeStatValue(params, wrapper);
    }

    @Override
    public List<Map<String, Object>> selectGroup(Map<String, Object> params, Wrapper<ToupiaojiluEntity> wrapper) {
        return baseMapper.selectGroup(params, wrapper);
    }

}
