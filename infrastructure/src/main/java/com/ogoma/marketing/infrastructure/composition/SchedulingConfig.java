package com.ogoma.marketing.infrastructure.composition;

import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@Configuration
@EnableSchedulerLock(defaultLockAtMostFor = "10m") // Global fallback max lock time
public class SchedulingConfig {
}
