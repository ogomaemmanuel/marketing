package com.ogoma.marketing.api.contacts;


import com.ogoma.marketing.api.annotations.CurrentUser;
import com.ogoma.marketing.core.abstractions.CommandDispatcher;
import com.ogoma.marketing.core.abstractions.QueryDispatcher;
import com.ogoma.marketing.core.application.contacts.queries.GetContactByIDQuery;
import com.ogoma.marketing.core.application.contacts.queries.GetContactByIDView;
import com.ogoma.marketing.core.domain.contacts.ContactEntity;
import com.ogoma.marketing.core.domain.contacts.ContactID;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/contacts")
public record ContactsController(
        CommandDispatcher commandDispatcher,
        QueryDispatcher queryDispatcher
) {


    @PostMapping
    public ResponseEntity<UUID> addContact(
            @RequestBody @Valid AddContactRequest addContactRequest,
            @CurrentUser String userId,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        ContactEntity contactEntity = this.commandDispatcher.dispatch(addContactRequest.toCommand(userId));
        URI uri = uriComponentsBuilder.path("/api/v1/contacts/{id}").buildAndExpand(contactEntity.getId().id()).toUri();
        return ResponseEntity.created(uri).body(contactEntity.getId().id());
    }

    @GetMapping("/{id}")
    public GetContactByIDView getContactByID(@PathVariable UUID id) {
        return this.queryDispatcher.dispatch(new GetContactByIDQuery(new ContactID(id)));
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateContact(
            @Parameter(
                    description = "Contact ID",
                    schema = @Schema(type = "string", format = "uuid")
            )
            @PathVariable ContactID id,
            @RequestBody @Valid UpdateContactRequest updateContactRequest,
            @CurrentUser String userId
    ) {
        this.commandDispatcher.dispatch(updateContactRequest.toCommand(id, userId));
    }

}
