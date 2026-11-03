package com.entity.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;


/**
 * 投票活动
 * 接收传参的实体类
 * （实际开发中配合移动端接口开发手动去掉些没用的字段， 后端一般用entity就够用了）
 * 取自ModelAndView 的model名称
 *
 * @author
 * @email
 * @date 2022-03-25 19:57:55
 */
public class ToupiaohuodongModel implements Serializable {
    private static final long serialVersionUID = 1L;


    /**
     * 投票图片
     */

    private String toupiaotupian;

    /**
     * 投票分类
     */

    private String toupiaofenlei;

    /**
     * 内容
     */

    private String neirong;

    /**
     * 票数
     */

    private Integer piaoshu;

    /**
     * 日期
     */

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat
    private Date riqi;

    /**
     * 获取：投票图片
     */
    public String getToupiaotupian() {
        return toupiaotupian;
    }

    /**
     * 设置：投票图片
     */

    public void setToupiaotupian(String toupiaotupian) {
        this.toupiaotupian = toupiaotupian;
    }

    /**
     * 获取：投票分类
     */
    public String getToupiaofenlei() {
        return toupiaofenlei;
    }

    /**
     * 设置：投票分类
     */

    public void setToupiaofenlei(String toupiaofenlei) {
        this.toupiaofenlei = toupiaofenlei;
    }

    /**
     * 获取：内容
     */
    public String getNeirong() {
        return neirong;
    }

    /**
     * 设置：内容
     */

    public void setNeirong(String neirong) {
        this.neirong = neirong;
    }

    /**
     * 获取：票数
     */
    public Integer getPiaoshu() {
        return piaoshu;
    }

    /**
     * 设置：票数
     */

    public void setPiaoshu(Integer piaoshu) {
        this.piaoshu = piaoshu;
    }

    /**
     * 获取：日期
     */
    public Date getRiqi() {
        return riqi;
    }

    /**
     * 设置：日期
     */

    public void setRiqi(Date riqi) {
        this.riqi = riqi;
    }

}
