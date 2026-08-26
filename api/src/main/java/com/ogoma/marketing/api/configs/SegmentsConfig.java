package com.ogoma.marketing.api.configs;

import com.ogoma.marketing.core.application.segments.CreateSegmentCommandHandler;
import com.ogoma.marketing.core.domain.segments.SegmentRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class SegmentsConfig {

    @Bean
    CreateSegmentCommandHandler createSegmentCommandHandler(SegmentRepository segmentRepository, Clock clock) {
        return new CreateSegmentCommandHandler(segmentRepository, clock);
    }
}
