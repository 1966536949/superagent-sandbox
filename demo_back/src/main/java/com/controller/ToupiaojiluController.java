package com.controller;

import com.annotation.IgnoreAuth;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.entity.ToupiaohuodongEntity;
import com.entity.ToupiaojiluEntity;
import com.entity.view.ToupiaojiluView;
import com.service.ToupiaohuodongService;
import com.service.ToupiaojiluService;
import com.utils.MPUtil;
import com.utils.PageUtils;
import com.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 投票记录
 * 后端接口
 *
 * @author
 * @email
 * @date 2022-03-25 19:57:55
 */
@RestController
@RequestMapping("/toupiaojilu")
public class ToupiaojiluController {
    @Autowired
    private ToupiaojiluService toupiaojiluService;

    @Autowired
    private ToupiaohuodongService toupiaohuodongService;


    /**
     * 后端列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, ToupiaojiluEntity toupiaojilu,
                  HttpServletRequest request) {
        String tableName = request.getSession().getAttribute("tableName").toString();
        if (tableName.equals("yonghu")) {
            toupiaojilu.setYonghuming((String) request.getSession().getAttribute("username"));
        }
        EntityWrapper<ToupiaojiluEntity> ew = new EntityWrapper<ToupiaojiluEntity>();
        PageUtils page = toupiaojiluService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, toupiaojilu), params), params));

        return R.ok().put("data", page);
    }

    /**
     * 前端列表
     */
    @IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, ToupiaojiluEntity toupiaojilu,
                  HttpServletRequest request) {
        EntityWrapper<ToupiaojiluEntity> ew = new EntityWrapper<ToupiaojiluEntity>();
        PageUtils page = toupiaojiluService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, toupiaojilu), params), params));
        return R.ok().put("data", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/lists")
    public R list(ToupiaojiluEntity toupiaojilu) {
        EntityWrapper<ToupiaojiluEntity> ew = new EntityWrapper<ToupiaojiluEntity>();
        ew.allEq(MPUtil.allEQMapPre(toupiaojilu, "toupiaojilu"));
        return R.ok().put("data", toupiaojiluService.selectListView(ew));
    }

    /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(ToupiaojiluEntity toupiaojilu) {
        EntityWrapper<ToupiaojiluEntity> ew = new EntityWrapper<ToupiaojiluEntity>();
        ew.allEq(MPUtil.allEQMapPre(toupiaojilu, "toupiaojilu"));
        ToupiaojiluView toupiaojiluView = toupiaojiluService.selectView(ew);
        return R.ok("查询投票记录成功").put("data", toupiaojiluView);
    }

    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        ToupiaojiluEntity toupiaojilu = toupiaojiluService.selectById(id);
        return R.ok().put("data", toupiaojilu);
    }

    /**
     * 前端详情
     */
    @IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id) {
        ToupiaojiluEntity toupiaojilu = toupiaojiluService.selectById(id);
        return R.ok().put("data", toupiaojilu);
    }


    /**
     * 后端保存
     */
    @RequestMapping("/save")
    @Transactional(rollbackFor = Exception.class)
    public R save(@RequestBody ToupiaojiluEntity toupiaojilu, HttpServletRequest request) {
        return saveVote(toupiaojilu, request);
    }

    /**
     * 前端保存
     */
    @RequestMapping("/add")
    @Transactional(rollbackFor = Exception.class)
    public R add(@RequestBody ToupiaojiluEntity toupiaojilu, HttpServletRequest request) {
        return saveVote(toupiaojilu, request);
    }

    /**
     * 保存投票记录。同一用户对同一活动只能投一票。
     * 先按 crossuserid、crossrefid 查重，已有记录时直接拒绝；
     * 没有记录时写入一条投票记录，并把对应活动的 piaoshu 固定加 1。
     * 查重、加票和写入记录都在同一个事务中，加票或写记录失败会一起回滚。
     */
    private R saveVote(ToupiaojiluEntity toupiaojilu, HttpServletRequest request) {
        Long crossuserid = toupiaojilu.getCrossuserid();
        Long crossrefid = toupiaojilu.getCrossrefid();
        ToupiaohuodongEntity toupiaohuodong = null;
        if (crossrefid != null) {
            // 锁定活动行，串行化同一活动的投票，避免并发重复投票和票数丢失
            toupiaohuodong = toupiaohuodongService.selectByIdForUpdate(crossrefid);
            if (toupiaohuodong == null) {
                return R.error("投票活动不存在");
            }
        }
        // 绕过页面直接调用接口时，用当前登录用户补齐 crossuserid
        if (crossuserid == null && crossrefid != null && request != null) {
            Object currentUserId = request.getSession() == null ? null : request.getSession().getAttribute("userId");
            if (currentUserId != null) {
                crossuserid = Long.valueOf(currentUserId.toString());
                toupiaojilu.setCrossuserid(crossuserid);
            }
        }
        // 判定依据：同一 crossuserid + crossrefid 已存在投票记录
        if (crossuserid != null && crossrefid != null) {
            EntityWrapper<ToupiaojiluEntity> voteWrapper = new EntityWrapper<ToupiaojiluEntity>();
            voteWrapper.eq("crossuserid", crossuserid);
            voteWrapper.eq("crossrefid", crossrefid);
            if (toupiaojiluService.selectCount(voteWrapper) > 0) {
                // 已经投过票：不写记录，也不修改活动票数
                return R.error("您已参与过投票！");
            }
        }
        // 没有重复记录时，先把活动票数固定加 1；加票失败则不写入投票记录
        if (crossrefid != null) {
            ToupiaohuodongEntity update = new ToupiaohuodongEntity();
            update.setId(crossrefid);
            update.setPiaoshu((toupiaohuodong.getPiaoshu() == null ? 0 : toupiaohuodong.getPiaoshu()) + 1);
            if (!toupiaohuodongService.updateById(update)) {
                return R.error("投票失败");
            }
        }
        toupiaojilu.setId(new Date().getTime() + new Double(Math.floor(Math.random() * 1000)).longValue());
        if (!toupiaojiluService.insert(toupiaojilu)) {
            // 活动票数已经加过，记录写入失败时抛出异常回滚
            throw new RuntimeException("投票记录保存失败");
        }
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody ToupiaojiluEntity toupiaojilu, HttpServletRequest request) {
        //ValidatorUtils.validateEntity(toupiaojilu);
        toupiaojiluService.updateById(toupiaojilu);//全部更新
        return R.ok();
    }


    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        toupiaojiluService.deleteBatchIds(Arrays.asList(ids));
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

        Wrapper<ToupiaojiluEntity> wrapper = new EntityWrapper<ToupiaojiluEntity>();
        if (map.get("remindstart") != null) {
            wrapper.ge(columnName, map.get("remindstart"));
        }
        if (map.get("remindend") != null) {
            wrapper.le(columnName, map.get("remindend"));
        }

        String tableName = request.getSession().getAttribute("tableName").toString();
        if (tableName.equals("yonghu")) {
            wrapper.eq("yonghuming", (String) request.getSession().getAttribute("username"));
        }

        int count = toupiaojiluService.selectCount(wrapper);
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
        EntityWrapper<ToupiaojiluEntity> ew = new EntityWrapper<ToupiaojiluEntity>();
        String tableName = request.getSession().getAttribute("tableName").toString();
        if (tableName.equals("yonghu")) {
            ew.eq("yonghuming", (String) request.getSession().getAttribute("username"));
        }
        List<Map<String, Object>> result = toupiaojiluService.selectValue(params, ew);
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
        EntityWrapper<ToupiaojiluEntity> ew = new EntityWrapper<ToupiaojiluEntity>();
        String tableName = request.getSession().getAttribute("tableName").toString();
        if (tableName.equals("yonghu")) {
            ew.eq("yonghuming", (String) request.getSession().getAttribute("username"));
        }
        List<Map<String, Object>> result = toupiaojiluService.selectTimeStatValue(params, ew);
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
        EntityWrapper<ToupiaojiluEntity> ew = new EntityWrapper<ToupiaojiluEntity>();
        String tableName = request.getSession().getAttribute("tableName").toString();
        if (tableName.equals("yonghu")) {
            ew.eq("yonghuming", (String) request.getSession().getAttribute("username"));
        }
        List<Map<String, Object>> result = toupiaojiluService.selectGroup(params, ew);
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
