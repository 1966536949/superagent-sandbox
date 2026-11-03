package com.controller;

import com.annotation.IgnoreAuth;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.entity.ToupiaofenleiEntity;
import com.entity.view.ToupiaofenleiView;
import com.service.ToupiaofenleiService;
import com.utils.MPUtil;
import com.utils.PageUtils;
import com.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * 投票分类
 * 后端接口
 *
 * @author
 * @email
 * @date 2022-03-25 19:57:55
 */
@RestController
@RequestMapping("/toupiaofenlei")
public class ToupiaofenleiController {
    @Autowired
    private ToupiaofenleiService toupiaofenleiService;


    /**
     * 后端列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, ToupiaofenleiEntity toupiaofenlei,
                  HttpServletRequest request) {
        EntityWrapper<ToupiaofenleiEntity> ew = new EntityWrapper<ToupiaofenleiEntity>();
        PageUtils page = toupiaofenleiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, toupiaofenlei), params), params));

        return R.ok().put("data", page);
    }

    /**
     * 前端列表
     */
    @IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, ToupiaofenleiEntity toupiaofenlei,
                  HttpServletRequest request) {
        EntityWrapper<ToupiaofenleiEntity> ew = new EntityWrapper<ToupiaofenleiEntity>();
        PageUtils page = toupiaofenleiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, toupiaofenlei), params), params));
        return R.ok().put("data", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/lists")
    public R list(ToupiaofenleiEntity toupiaofenlei) {
        EntityWrapper<ToupiaofenleiEntity> ew = new EntityWrapper<ToupiaofenleiEntity>();
        ew.allEq(MPUtil.allEQMapPre(toupiaofenlei, "toupiaofenlei"));
        return R.ok().put("data", toupiaofenleiService.selectListView(ew));
    }

    /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(ToupiaofenleiEntity toupiaofenlei) {
        EntityWrapper<ToupiaofenleiEntity> ew = new EntityWrapper<ToupiaofenleiEntity>();
        ew.allEq(MPUtil.allEQMapPre(toupiaofenlei, "toupiaofenlei"));
        ToupiaofenleiView toupiaofenleiView = toupiaofenleiService.selectView(ew);
        return R.ok("查询投票分类成功").put("data", toupiaofenleiView);
    }

    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        ToupiaofenleiEntity toupiaofenlei = toupiaofenleiService.selectById(id);
        return R.ok().put("data", toupiaofenlei);
    }

    /**
     * 前端详情
     */
    @IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id) {
        ToupiaofenleiEntity toupiaofenlei = toupiaofenleiService.selectById(id);
        return R.ok().put("data", toupiaofenlei);
    }


    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody ToupiaofenleiEntity toupiaofenlei, HttpServletRequest request) {
        toupiaofenlei.setId(new Date().getTime() + new Double(Math.floor(Math.random() * 1000)).longValue());
        //ValidatorUtils.validateEntity(toupiaofenlei);
        toupiaofenleiService.insert(toupiaofenlei);
        return R.ok();
    }

    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody ToupiaofenleiEntity toupiaofenlei, HttpServletRequest request) {
        toupiaofenlei.setId(new Date().getTime() + new Double(Math.floor(Math.random() * 1000)).longValue());
        //ValidatorUtils.validateEntity(toupiaofenlei);
        toupiaofenleiService.insert(toupiaofenlei);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody ToupiaofenleiEntity toupiaofenlei, HttpServletRequest request) {
        //ValidatorUtils.validateEntity(toupiaofenlei);
        toupiaofenleiService.updateById(toupiaofenlei);//全部更新
        return R.ok();
    }


    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        toupiaofenleiService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }

    /**
     * 提醒接口
     */
    @RequestMapping("/remind/{columnName}/{type}")
    public R remindCount(@PathVariable("columnName") String columnName, HttpServletRequest request,
                         @PathVariable("type") String type, @RequestParam Map<String, Object> map) {
        map.put("column", columnName);
        map.put("type", type);

        if (type.equals("2")) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            Date remindStartDate = null;
            Date remindEndDate = null;
            if (map.get("remindstart") != null) {
                Integer remindStart = Integer.parseInt(map.get("remindstart").toString());
                c.setTime(new Date());
                c.add(Calendar.DAY_OF_MONTH, remindStart);
                remindStartDate = c.getTime();
                map.put("remindstart", sdf.format(remindStartDate));
            }
            if (map.get("remindend") != null) {
                Integer remindEnd = Integer.parseInt(map.get("remindend").toString());
                c.setTime(new Date());
                c.add(Calendar.DAY_OF_MONTH, remindEnd);
                remindEndDate = c.getTime();
                map.put("remindend", sdf.format(remindEndDate));
            }
        }

        Wrapper<ToupiaofenleiEntity> wrapper = new EntityWrapper<ToupiaofenleiEntity>();
        if (map.get("remindstart") != null) {
            wrapper.ge(columnName, map.get("remindstart"));
        }
        if (map.get("remindend") != null) {
            wrapper.le(columnName, map.get("remindend"));
        }


        int count = toupiaofenleiService.selectCount(wrapper);
        return R.ok().put("count", count);
    }


}
