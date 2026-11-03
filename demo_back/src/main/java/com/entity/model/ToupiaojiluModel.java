package com.entity.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;


/**
 * 投票记录
 * 接收传参的实体类
 * （实际开发中配合移动端接口开发手动去掉些没用的字段， 后端一般用entity就够用了）
 * 取自ModelAndView 的model名称
 *
 * @author
 * @email
 * @date 2022-03-25 19:57:55
 */
public class ToupiaojiluModel implements Serializable {
    private static final long serialVersionUID = 1L;


    /**
     * 投票分类
     */

    private String toupiaofenlei;

    /**
     * 票数
     */

    private Integer piaoshu;

    /**
     * 投票时间
     */

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat
    private Date toupiaoshijian;

    /**
     * 用户名
     */

    private String yonghuming;

    /**
     * 手机
     */

    private String shouji;

    /**
     * 跨表用户id
     */

    private Long crossuserid;

    /**
     * 跨表主键id
     */

    private Long crossrefid;

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
     * 获取：投票时间
     */
    public Date getToupiaoshijian() {
        return toupiaoshijian;
    }

    /**
     * 设置：投票时间
     */

    public void setToupiaoshijian(Date toupiaoshijian) {
        this.toupiaoshijian = toupiaoshijian;
    }

    /**
     * 获取：用户名
     */
    public String getYonghuming() {
        return yonghuming;
    }

    /**
     * 设置：用户名
     */

    public void setYonghuming(String yonghuming) {
        this.yonghuming = yonghuming;
    }

    /**
     * 获取：手机
     */
    public String getShouji() {
        return shouji;
    }

    /**
     * 设置：手机
     */

    public void setShouji(String shouji) {
        this.shouji = shouji;
    }

    /**
     * 获取：跨表用户id
     */
    public Long getCrossuserid() {
        return crossuserid;
    }

    /**
     * 设置：跨表用户id
     */

    public void setCrossuserid(Long crossuserid) {
        this.crossuserid = crossuserid;
    }

    /**
     * 获取：跨表主键id
     */
    public Long getCrossrefid() {
        return crossrefid;
    }

    /**
     * 设置：跨表主键id
     */

    public void setCrossrefid(Long crossrefid) {
        this.crossrefid = crossrefid;
    }

}
