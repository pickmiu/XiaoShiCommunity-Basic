package com.sicnu.community.json;

import java.io.Serializable;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Tangliyi (2238192070@qq.com)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MailMessage implements Serializable {
    private static final long serialVersionUID = -6537417792857516330L;
    private String[] to;
    private String templateId;
    private String subject;
    private Map<String, String> params;
}