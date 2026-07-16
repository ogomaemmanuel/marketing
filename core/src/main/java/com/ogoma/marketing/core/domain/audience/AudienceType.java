package com.ogoma.marketing.core.domain.audience;

/**
 * STATIC contacts are reacted via sign forms or imports etc
 * DYNAMIC contacts are for segmentation, must contain Ruleset
 * SNAPSHOT once member are added, they cannot be modified
 */
public enum AudienceType {


    STATIC, DYNAMIC, SNAPSHOT
}
