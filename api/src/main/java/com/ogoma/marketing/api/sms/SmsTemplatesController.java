package com.ogoma.marketing.api.sms;

import com.ogoma.marketing.api.annotations.CurrentUser;
import com.ogoma.marketing.api.sms.requests.CreateSmsTemplateRequest;
import com.ogoma.marketing.api.sms.requests.DuplicateSmsTemplateRequest;
import com.ogoma.marketing.api.sms.requests.UpdateSmsTemplateRequest;
import com.ogoma.marketing.core.abstractions.CommandDispatcher;
import com.ogoma.marketing.core.abstractions.QueryDispatcher;
import com.ogoma.marketing.core.application.sms.DuplicateSmsTemplateCommand;
import com.ogoma.marketing.core.application.sms.queries.GetSmsTemplatesQuery;
import com.ogoma.marketing.core.application.sms.queries.GetSmsTemplatesView;
import com.ogoma.marketing.core.domain.sms.SmsTemplateEntity;
import com.ogoma.marketing.core.domain.sms.SmsTemplateID;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/sms-templates")
@PreAuthorize("isFullyAuthenticated()")
public record SmsTemplatesController(
        CommandDispatcher commandDispatcher,
        QueryDispatcher queryDispatcher) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SmsTemplateID createSmsTemplate(
            @RequestBody @Valid CreateSmsTemplateRequest createSmsTemplateRequest,
            @CurrentUser String username) {
        return this.commandDispatcher.dispatch(createSmsTemplateRequest.toCommandWith(username)).getId();
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PagedModel<GetSmsTemplatesView> getSmsTemplates(String searchTerm, Pageable pageable) {
        return new PagedModel<>(this.queryDispatcher.dispatch(new GetSmsTemplatesQuery(searchTerm, pageable)));
    }

    @PutMapping("/{id}")
    public void updateSmsTemplate(
            @PathVariable @Parameter(schema = @Schema(type = "string", format = "uuid")) SmsTemplateID id,
            @RequestBody UpdateSmsTemplateRequest updateSmsTemplateRequest, @CurrentUser String userId) {
        this.commandDispatcher.dispatch(updateSmsTemplateRequest.toCommand(id, userId));
    }

    @PostMapping("/{id}/duplicate")
    @ResponseStatus(HttpStatus.OK)
    public Optional<SmsTemplateEntity> duplicateSmsTemplate(
            @PathVariable UUID id,
            @RequestBody @Valid DuplicateSmsTemplateRequest request,
            @CurrentUser String userId
    ) {
        return this.commandDispatcher.dispatch(new DuplicateSmsTemplateCommand(
                new SmsTemplateID(id),
                request.suggestedName(),
                userId));
    }


}
