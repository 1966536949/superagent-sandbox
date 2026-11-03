package com.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;


/**
 * 公告信息
 * 数据库通用操作实体类（普通增删改查）
 *
 * @author
 * @email
 * @date 2022-03-25 19:57:55
 */
@TableName("gonggaoxinxi")
public class GonggaoxinxiEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键id
     */
    @TableId
    private Long id;
    /**
     * 公告标题
     */

    private String gonggaobiaoti;
    /**
     * 公告类型
     */

    private String gonggaoleixing;
    /**
     * 公告图片
     */

    private String gonggaotupian;
    /**
     * 公告内容
     */

    private String gonggaoneirong;
    /**
     * 发布日期
     */

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat
    private Date faburiqi;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat
    private Date addtime;

    public GonggaoxinxiEntity() {

    }


    public GonggaoxinxiEntity(T t) {
        try {
            BeanUtils.copyProperties(this, t);
        } catch (IllegalAccessException | InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取：公告标题
     */
    public String getGonggaobiaoti() {
        return gonggaobiaoti;
    }

    /**
     * 设置：公告标题
     */
    public void setGonggaobiaoti(String gonggaobiaoti) {
        this.gonggaobiaoti = gonggaobiaoti;
    }

    /**
     * 获取：公告类型
     */
    public String getGonggaoleixing() {
        return gonggaoleixing;
    }

    /**
     * 设置：公告类型
     */
    public void setGonggaoleixing(String gonggaoleixing) {
        this.gonggaoleixing = gonggaoleixing;
    }

    /**
     * 获取：公告图片
     */
    public String getGonggaotupian() {
        return gonggaotupian;
    }

    /**
     * 设置：公告图片
     */
    public void setGonggaotupian(String gonggaotupian) {
        this.gonggaotupian = gonggaotupian;
    }

    /**
     * 获取：公告内容
     */
    public String getGonggaoneirong() {
        return gonggaoneirong;
    }

    /**
     * 设置：公告内容
     */
    public void setGonggaoneirong(String gonggaoneirong) {
        this.gonggaoneirong = gonggaoneirong;
    }

    /**
     * 获取：发布日期
     */
    public Date getFaburiqi() {
        return faburiqi;
    }

    /**
     * 设置：发布日期
     */
    public void setFaburiqi(Date faburiqi) {
        this.faburiqi = faburiqi;
    }

}
