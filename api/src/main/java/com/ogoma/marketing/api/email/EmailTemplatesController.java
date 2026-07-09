package com.ogoma.marketing.api.email;


import com.ogoma.marketing.api.annotations.CurrentUser;
import com.ogoma.marketing.core.abstractions.CommandDispatcher;
import com.ogoma.marketing.core.abstractions.QueryDispatcher;
import com.ogoma.marketing.core.application.email.commands.CloneEmailTemplateCommand;
import com.ogoma.marketing.core.application.email.queries.GetEmailTemplateByIDQuery;
import com.ogoma.marketing.core.application.email.queries.GetEmailTemplateByIDView;
import com.ogoma.marketing.core.domain.email.EmailTemplateID;
import com.ogoma.marketing.core.domain.email.valueobjects.EmailTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/email-templates")
@PreAuthorize("isFullyAuthenticated()")
public record EmailTemplatesController(
        CommandDispatcher commandDispatcher,
        QueryDispatcher queryDispatcher
) {

    @PostMapping
    public Void createEmailTemplate(@RequestBody CreateEmailTemplateRequest createEmailTemplateRequest, @CurrentUser String username) {
        return this.commandDispatcher.dispatch(createEmailTemplateRequest.toCommandWith(username));
    }

    @PutMapping("/{id}")
    public Void updateEmailTemplate(@PathVariable UUID id, @RequestBody CreateEmailTemplateRequest createEmailTemplateRequest, @CurrentUser String username) {
        return this.commandDispatcher.dispatch(createEmailTemplateRequest.toCommandWith(username));
    }

    @GetMapping("/{id}")
    public Optional<GetEmailTemplateByIDView> getEmailTemplateByIDView(@PathVariable UUID id) {
        return this.queryDispatcher.dispatch(new GetEmailTemplateByIDQuery(new EmailTemplateID(id)));
    }

    @GetMapping(value = "/{id}/preview", produces = MediaType.TEXT_HTML_VALUE)
    public String preview(@PathVariable UUID id) {
        return this.queryDispatcher.dispatch(new GetEmailTemplateByIDQuery(new EmailTemplateID(id)))
                .map(GetEmailTemplateByIDView::emailTemplate).map(EmailTemplate::renderHtml).orElse("");
    }

    @PostMapping(value = "/{id}/clone")
    public Void clone(@PathVariable UUID id, CloneEmailTemplateRequest request, String userID) {
        return this.commandDispatcher.dispatch(new CloneEmailTemplateCommand(new EmailTemplateID(id), request.suggestedName(), userID));
    }
}
