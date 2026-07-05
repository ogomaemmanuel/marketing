package com.ogoma.marketing.api.email;


import com.ogoma.marketing.api.annotations.CurrentUser;
import com.ogoma.marketing.core.abstractions.CommandDispatcher;
import com.ogoma.marketing.core.abstractions.QueryDispatcher;
import com.ogoma.marketing.core.application.email.commands.CloneEmailTemplateCommand;
import com.ogoma.marketing.core.application.email.queries.GetEmailTemplateByIDQuery;
import com.ogoma.marketing.core.application.email.queries.GetEmailTemplateByIDView;
import com.ogoma.marketing.core.domain.email.EmailTemplateEntityID;
import com.ogoma.marketing.core.domain.email.valueobjects.EmailTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/email-templates")
public class EmailTemplatesController {

    private final CommandDispatcher commandDispatcher;

    private final QueryDispatcher queryDispatcher;

    public EmailTemplatesController(CommandDispatcher commandDispatcher, QueryDispatcher queryDispatcher) {
        this.commandDispatcher = commandDispatcher;
        this.queryDispatcher = queryDispatcher;
    }


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
        return this.queryDispatcher.dispatch(new GetEmailTemplateByIDQuery(new EmailTemplateEntityID(id)));
    }

    @GetMapping(value = "/{id}/preview", produces = MediaType.TEXT_HTML_VALUE)
    public String preview(@PathVariable UUID id) {
        return this.queryDispatcher.dispatch(new GetEmailTemplateByIDQuery(new EmailTemplateEntityID(id)))
                .map(GetEmailTemplateByIDView::emailTemplate).map(EmailTemplate::renderHtml).orElse("");
    }

    @PostMapping(value = "/{id}/clone")
    public Void clone(@PathVariable UUID id, CloneEmailTemplateRequest request, String userID) {
        return this.commandDispatcher.dispatch(new CloneEmailTemplateCommand(new EmailTemplateEntityID(id), request.suggestedName(), userID));
    }
}
