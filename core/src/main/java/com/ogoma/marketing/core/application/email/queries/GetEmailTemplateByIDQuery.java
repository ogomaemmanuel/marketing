package com.ogoma.marketing.core.application.email.queries;

import com.ogoma.marketing.core.abstractions.Query;
import com.ogoma.marketing.core.domain.email.EmailTemplateEntityID;

import java.util.Optional;

public record GetEmailTemplateByIDQuery(EmailTemplateEntityID  id) implements Query<Optional<GetEmailTemplateByIDView>> {

}
