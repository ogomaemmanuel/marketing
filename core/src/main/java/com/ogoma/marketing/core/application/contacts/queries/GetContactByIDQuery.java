package com.ogoma.marketing.core.application.contacts.queries;

import com.ogoma.marketing.core.abstractions.Query;
import com.ogoma.marketing.core.domain.contacts.ContactID;

public record GetContactByIDQuery(ContactID contactID) implements Query<GetContactByIDView> {


}
