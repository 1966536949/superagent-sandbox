package com.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dao.ToupiaofenleiDao;
import com.entity.ToupiaofenleiEntity;
import com.entity.view.ToupiaofenleiView;
import com.entity.vo.ToupiaofenleiVO;
import com.service.ToupiaofenleiService;
import com.utils.PageUtils;
import com.utils.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("toupiaofenleiService")
public class ToupiaofenleiServiceImpl extends ServiceImpl<ToupiaofenleiDao, ToupiaofenleiEntity> implements ToupiaofenleiService {


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<ToupiaofenleiEntity> page = this.selectPage(
                new Query<ToupiaofenleiEntity>(params).getPage(),
                new EntityWrapper<ToupiaofenleiEntity>()
        );
        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Wrapper<ToupiaofenleiEntity> wrapper) {
        Page<ToupiaofenleiView> page = new Query<ToupiaofenleiView>(params).getPage();
        page.setRecords(baseMapper.selectListView(page, wrapper));
        PageUtils pageUtil = new PageUtils(page);
        return pageUtil;
    }

    @Override
    public List<ToupiaofenleiVO> selectListVO(Wrapper<ToupiaofenleiEntity> wrapper) {
        return baseMapper.selectListVO(wrapper);
    }

    @Override
    public ToupiaofenleiVO selectVO(Wrapper<ToupiaofenleiEntity> wrapper) {
        return baseMapper.selectVO(wrapper);
    }

    @Override
    public List<ToupiaofenleiView> selectListView(Wrapper<ToupiaofenleiEntity> wrapper) {
        return baseMapper.selectListView(wrapper);
    }

    @Override
    public ToupiaofenleiView selectView(Wrapper<ToupiaofenleiEntity> wrapper) {
        return baseMapper.selectView(wrapper);
    }


}
