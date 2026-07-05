package com.ogoma.marketing.api.sms;

import com.ogoma.marketing.api.annotations.CurrentUser;
import com.ogoma.marketing.core.abstractions.CommandDispatcher;
import com.ogoma.marketing.core.abstractions.QueryDispatcher;
import com.ogoma.marketing.core.application.sms.DuplicateSmsTemplateCommand;
import com.ogoma.marketing.core.application.sms.queries.GetSmsTemplatesQuery;
import com.ogoma.marketing.core.application.sms.queries.GetSmsTemplatesView;
import com.ogoma.marketing.core.domain.sms.SmsTemplateEntity;
import com.ogoma.marketing.core.domain.sms.SmsTemplateID;
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
public class SmsTemplatesController {
    private final CommandDispatcher commandDispatcher;
    private final QueryDispatcher queryDispatcher;

    public SmsTemplatesController(
            CommandDispatcher commandDispatcher,
            QueryDispatcher queryDispatcher) {
        this.commandDispatcher = commandDispatcher;
        this.queryDispatcher = queryDispatcher;
    }

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

    @PostMapping("/{id}/duplicate")
    @ResponseStatus(HttpStatus.OK)
    public Optional<SmsTemplateEntity> duplicateSmsTemplate(
            @PathVariable UUID id,
            @RequestBody @Valid DuplicateSmsTemplateRequest request,
            @CurrentUser String userId
    ) {
        return this.commandDispatcher.dispatch(new DuplicateSmsTemplateCommand(new SmsTemplateID(id),
                request.suggestedName(), userId));
    }


}
