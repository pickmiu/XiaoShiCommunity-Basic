package com.sicnu.community.json;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Tangliyi (2238192070@qq.com)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperationDetail {
    private String name;
    private String subject;
    private String templateId;
}