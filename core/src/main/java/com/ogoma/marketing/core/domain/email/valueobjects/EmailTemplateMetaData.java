package com.ogoma.marketing.core.domain.email.valueobjects;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class EmailTemplateMetaData implements Serializable {
    private String title;
    private String version;
    private String blockCount;
    private String attachmentCount;
}
