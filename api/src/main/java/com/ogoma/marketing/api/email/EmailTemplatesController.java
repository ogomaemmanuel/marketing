package com.ogoma.marketing.api.email;


import com.ogoma.marketing.api.annotations.CurrentUser;
import com.ogoma.marketing.core.abstractions.CommandDispatcher;
import com.ogoma.marketing.core.abstractions.QueryDispatcher;
import com.ogoma.marketing.core.application.email.commands.CloneEmailTemplateCommand;
import com.ogoma.marketing.core.application.email.queries.GetEmailTemplateByIDQuery;
import com.ogoma.marketing.core.application.email.queries.GetEmailTemplateByIDView;
import com.ogoma.marketing.core.application.email.queries.GetEmailTemplatesListItemView;
import com.ogoma.marketing.core.application.email.queries.GetEmailTemplatesQuery;
import com.ogoma.marketing.core.domain.email.EmailTemplateID;
import com.ogoma.marketing.core.domain.email.valueobjects.EmailTemplate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
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


    @GetMapping
    public PagedModel<GetEmailTemplatesListItemView> getEmailTemplates(Pageable pageable, @RequestParam(required = false) String searchTerm) {
        return new PagedModel<>(this.queryDispatcher.dispatch(new GetEmailTemplatesQuery(pageable, searchTerm)));
    }

    @PutMapping("/{id}")
    public Void updateEmailTemplate(@PathVariable UUID id, @RequestBody UpdateEmailTemplateRequest updateEmailTemplateRequest, @CurrentUser String username) {
        return this.commandDispatcher.dispatch(updateEmailTemplateRequest.toCommandWith(new EmailTemplateID(id), username));
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
    public Void clone(@PathVariable UUID id, CloneEmailTemplateRequest request,@CurrentUser final String userID) {
        return this.commandDispatcher.dispatch(new CloneEmailTemplateCommand(new EmailTemplateID(id), request.suggestedName(), userID));
    }
}
