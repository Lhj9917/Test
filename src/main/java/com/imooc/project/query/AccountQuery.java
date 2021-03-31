package com.imooc.project.query;

import lombok.Data;

@Data
public class AccountQuery {
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 邮箱
     */
    private String email;

    private String createTimeRange;

    private Long page;

    private Long limit;
}
