package com.ogoma.marketing.core.application.email.queries;

import com.ogoma.marketing.core.abstractions.Query;
import com.ogoma.marketing.core.domain.email.EmailTemplateID;

import java.util.Optional;

public record GetEmailTemplateByIDQuery(EmailTemplateID id) implements Query<Optional<GetEmailTemplateByIDView>> {

}
