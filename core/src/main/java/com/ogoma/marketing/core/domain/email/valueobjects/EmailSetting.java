package com.ogoma.marketing.core.domain.email.valueobjects;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class EmailSetting implements Serializable {
    private String subject;
    private String senderName;
    private String replyTo;
}
