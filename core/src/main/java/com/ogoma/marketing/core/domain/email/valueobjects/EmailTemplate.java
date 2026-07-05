package com.ogoma.marketing.core.domain.email.valueobjects;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Setter
public class EmailTemplate implements Serializable {
    @JsonProperty("metadata")
    private EmailTemplateMetaData metaData;
    @JsonProperty("settings")
    private EmailSetting emailSetting;
    private List<BaseEmailBlock> blocks;

    public EmailTemplate() {
        this.blocks = new ArrayList<>();
    }

    public String renderHtml() {
        String blocksHtml = this.blocks.stream().map(BaseEmailBlock::renderHtml).collect(Collectors.joining("\n      "));
        return """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <meta http-equiv="X-UA-Compatible" content="IE=edge">
                    <title>%s</title>
                    <style type="text/css">
                        body {
                            font-family: Arial, Helvetica, sans-serif;
                            font-size: 14px;
                            line-height: 1.6;
                            color: #333333;
                            margin: 0;
                            padding: 0;
                            background-color: #f4f4f4;
                        }
                        .email-wrapper {
                            background-color: #f4f4f4;
                            padding: 20px 0;
                        }
                        .email-container {
                            max-width: 600px;
                            margin: 0 auto;
                            background-color: #ffffff;
                            padding: 20px;
                            border-radius: 8px;
                            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
                        }
                        @media only screen and (max-width: 600px) {
                            .email-container {
                                width: 100%% !important; /* double %% to escape */
                                padding: 10px !important;
                            }
                        }
                    </style>
                </head>
                <body>
                    <div class="email-wrapper">
                        <div class="email-container">
                            %s
                        </div>
                    </div>
                </body>
                </html>
                """.formatted(Optional.ofNullable(emailSetting).map(EmailSetting::getSubject).orElse(""), blocksHtml);

    }
}
