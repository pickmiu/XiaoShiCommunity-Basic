package com.sicnu.community.pojo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

/**
 * @author Tangliyi (2238192070@qq.com)
 */
@Data
public class User implements Serializable {

    /**
     * 所有实现 Serializable 的 实体类都必须生成
     * 目的: 实现 新老版本 User 序列化的兼容
     */
    private static final long serialVersionUID = -7920611876388872639L;

    private int id;

    private String email;

    private String nickname;

    private String phone;

    private String schoolEmail;

    @JsonIgnore
    private String password;

    /**
     * 最近一次密码修改时间
     */
    @JsonIgnore
    private Date passwordChangeTime;

    private String createTime;

    private String updateTime;
}
