package com.ogoma.marketing.api.campaigns;

import com.ogoma.marketing.core.application.campaign.commands.CreateCampaignCommand;
import com.ogoma.marketing.core.domain.audience.AudienceId;
import com.ogoma.marketing.core.domain.campaigns.Channel;
import com.ogoma.marketing.core.domain.email.EmailTemplateID;
import com.ogoma.marketing.core.domain.segments.SegmentID;
import com.ogoma.marketing.core.domain.sms.SmsTemplateID;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record CreateCampaignRequest(
        @NotBlank
        String name,
        String description,
        @NotNull
        @NotEmpty
        Set<Channel> channels,
        @NotNull
        @NotEmpty
        Set<UUID> targetAudienceIds,
        Set<UUID> targetSegmentIds,
        @Schema(
                description = "Sms Template id, required when channels contain SMS "
        )
        UUID smsTemplateID,


        @Schema(
                description = "Email Template id, required when channels contain Email "
        )
        UUID emailTemplateID
) {
    public CreateCampaignRequest {
        targetAudienceIds = Set.copyOf(Objects.requireNonNullElse(targetAudienceIds,Set.of()));
        targetSegmentIds= Set.copyOf(Objects.requireNonNullElse(targetSegmentIds,Set.of()));
        channels = Set.copyOf(Objects.requireNonNullElse(channels,Set.of()));
        if (channels.contains(Channel.SMS) && smsTemplateID == null) {
            throw new IllegalArgumentException("Sms template is required");
        }
        if (channels.contains(Channel.EMAIL) && emailTemplateID == null) {
            throw new IllegalArgumentException("Email template is required");
        }
    }

    public CreateCampaignCommand toCommand(String userId) {
        var audienceIds = targetAudienceIds.stream().map(AudienceId::new).collect(Collectors.toUnmodifiableSet());
        var segmentIds = targetSegmentIds.stream().map(SegmentID::new).collect(Collectors.toUnmodifiableSet());

        return new CreateCampaignCommand(
                name,
                description,
                channels,
                audienceIds,
                segmentIds,
                emailTemplateID == null ? null : new EmailTemplateID(emailTemplateID),
                smsTemplateID == null ? null : new SmsTemplateID(smsTemplateID),
                userId);
    }
}
