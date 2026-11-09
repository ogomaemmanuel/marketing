package com.ogoma.marketing.api.polls;

import java.util.UUID;

public record SumbitVoteRequest(
        UUID pollId,
        String trackingId,
        String feedback
) {
}
