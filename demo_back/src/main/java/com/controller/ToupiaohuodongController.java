package com.controller;

import com.annotation.IgnoreAuth;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.entity.ToupiaohuodongEntity;
import com.entity.view.ToupiaohuodongView;
import com.service.ToupiaohuodongService;
import com.utils.MPUtil;
import com.utils.PageUtils;
import com.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 投票活动
 * 后端接口
 *
 * @author
 * @email
 * @date 2022-03-25 19:57:55
 */
@RestController
@RequestMapping("/toupiaohuodong")
public class ToupiaohuodongController {
    @Autowired
    private ToupiaohuodongService toupiaohuodongService;


    /**
     * 后端列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, ToupiaohuodongEntity toupiaohuodong,
                  @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date riqistart,
                  @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date riqiend,
                  HttpServletRequest request) {
        EntityWrapper<ToupiaohuodongEntity> ew = new EntityWrapper<ToupiaohuodongEntity>();
        if (riqistart != null) ew.ge("riqi", riqistart);
        if (riqiend != null) ew.le("riqi", riqiend);
        PageUtils page = toupiaohuodongService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, toupiaohuodong), params), params));

        return R.ok().put("data", page);
    }

    /**
     * 前端列表
     */
    @IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, ToupiaohuodongEntity toupiaohuodong,
                  @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date riqistart,
                  @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date riqiend,
                  HttpServletRequest request) {
        EntityWrapper<ToupiaohuodongEntity> ew = new EntityWrapper<ToupiaohuodongEntity>();
        if (riqistart != null) ew.ge("riqi", riqistart);
        if (riqiend != null) ew.le("riqi", riqiend);
        PageUtils page = toupiaohuodongService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, toupiaohuodong), params), params));
        return R.ok().put("data", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/lists")
    public R list(ToupiaohuodongEntity toupiaohuodong) {
        EntityWrapper<ToupiaohuodongEntity> ew = new EntityWrapper<ToupiaohuodongEntity>();
        ew.allEq(MPUtil.allEQMapPre(toupiaohuodong, "toupiaohuodong"));
        return R.ok().put("data", toupiaohuodongService.selectListView(ew));
    }

    /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(ToupiaohuodongEntity toupiaohuodong) {
        EntityWrapper<ToupiaohuodongEntity> ew = new EntityWrapper<ToupiaohuodongEntity>();
        ew.allEq(MPUtil.allEQMapPre(toupiaohuodong, "toupiaohuodong"));
        ToupiaohuodongView toupiaohuodongView = toupiaohuodongService.selectView(ew);
        return R.ok("查询投票活动成功").put("data", toupiaohuodongView);
    }

    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        ToupiaohuodongEntity toupiaohuodong = toupiaohuodongService.selectById(id);
        return R.ok().put("data", toupiaohuodong);
    }

    /**
     * 前端详情
     */
    @IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id) {
        ToupiaohuodongEntity toupiaohuodong = toupiaohuodongService.selectById(id);
        return R.ok().put("data", toupiaohuodong);
    }


    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody ToupiaohuodongEntity toupiaohuodong, HttpServletRequest request) {
        toupiaohuodong.setId(new Date().getTime() + new Double(Math.floor(Math.random() * 1000)).longValue());
        //ValidatorUtils.validateEntity(toupiaohuodong);
        toupiaohuodongService.insert(toupiaohuodong);
        return R.ok();
    }

    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody ToupiaohuodongEntity toupiaohuodong, HttpServletRequest request) {
        toupiaohuodong.setId(new Date().getTime() + new Double(Math.floor(Math.random() * 1000)).longValue());
        //ValidatorUtils.validateEntity(toupiaohuodong);
        toupiaohuodongService.insert(toupiaohuodong);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody ToupiaohuodongEntity toupiaohuodong, HttpServletRequest request) {
        //ValidatorUtils.validateEntity(toupiaohuodong);
        toupiaohuodongService.updateById(toupiaohuodong);//全部更新
        return R.ok();
    }


    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        toupiaohuodongService.deleteBatchIds(Arrays.asList(ids));
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

        Wrapper<ToupiaohuodongEntity> wrapper = new EntityWrapper<ToupiaohuodongEntity>();
        if (map.get("remindstart") != null) {
            wrapper.ge(columnName, map.get("remindstart"));
        }
        if (map.get("remindend") != null) {
            wrapper.le(columnName, map.get("remindend"));
        }


        int count = toupiaohuodongService.selectCount(wrapper);
        return R.ok().put("count", count);
    }


    /**
     * （按值统计）
     */
    @RequestMapping("/value/{xColumnName}/{yColumnName}")
    public R value(@PathVariable("yColumnName") String yColumnName, @PathVariable("xColumnName") String xColumnName, HttpServletRequest request) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("xColumn", xColumnName);
        params.put("yColumn", yColumnName);
        EntityWrapper<ToupiaohuodongEntity> ew = new EntityWrapper<ToupiaohuodongEntity>();
        List<Map<String, Object>> result = toupiaohuodongService.selectValue(params, ew);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (Map<String, Object> m : result) {
            for (String k : m.keySet()) {
                if (m.get(k) instanceof Date) {
                    m.put(k, sdf.format((Date) m.get(k)));
                }
            }
        }
        return R.ok().put("data", result);
    }

    /**
     * （按值统计）时间统计类型
     */
    @RequestMapping("/value/{xColumnName}/{yColumnName}/{timeStatType}")
    public R valueDay(@PathVariable("yColumnName") String yColumnName, @PathVariable("xColumnName") String xColumnName, @PathVariable("timeStatType") String timeStatType, HttpServletRequest request) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("xColumn", xColumnName);
        params.put("yColumn", yColumnName);
        params.put("timeStatType", timeStatType);
        EntityWrapper<ToupiaohuodongEntity> ew = new EntityWrapper<ToupiaohuodongEntity>();
        List<Map<String, Object>> result = toupiaohuodongService.selectTimeStatValue(params, ew);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (Map<String, Object> m : result) {
            for (String k : m.keySet()) {
                if (m.get(k) instanceof Date) {
                    m.put(k, sdf.format((Date) m.get(k)));
                }
            }
        }
        return R.ok().put("data", result);
    }

    /**
     * 分组统计
     */
    @RequestMapping("/group/{columnName}")
    public R group(@PathVariable("columnName") String columnName, HttpServletRequest request) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("column", columnName);
        EntityWrapper<ToupiaohuodongEntity> ew = new EntityWrapper<ToupiaohuodongEntity>();
        List<Map<String, Object>> result = toupiaohuodongService.selectGroup(params, ew);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (Map<String, Object> m : result) {
            for (String k : m.keySet()) {
                if (m.get(k) instanceof Date) {
                    m.put(k, sdf.format((Date) m.get(k)));
                }
            }
        }
        return R.ok().put("data", result);
    }

}
