package com.ogoma.marketing.campaignsbatchprocessor;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class CampaignsBatchProcessorApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(CampaignsBatchProcessorApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);

    }

}
