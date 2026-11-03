package com.ogoma.marketing.core.domain.forms;

import com.ogoma.marketing.core.sharedkernel.ddd.TypedID;

import java.util.UUID;

public record SignupFormID(UUID id) implements TypedID<UUID> {
}
