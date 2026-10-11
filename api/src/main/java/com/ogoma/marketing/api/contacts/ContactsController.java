package com.ogoma.marketing.api.contacts;


import com.ogoma.marketing.api.annotations.CurrentUser;
import com.ogoma.marketing.core.abstractions.CommandDispatcher;
import com.ogoma.marketing.core.abstractions.QueryDispatcher;
import com.ogoma.marketing.core.application.contacts.queries.GetContactByIDQuery;
import com.ogoma.marketing.core.application.contacts.queries.GetContactByIDView;
import com.ogoma.marketing.core.application.contacts.queries.GetContactsQuery;
import com.ogoma.marketing.core.application.contacts.queries.GetContactsView;
import com.ogoma.marketing.core.domain.contacts.ContactEntity;
import com.ogoma.marketing.core.domain.contacts.ContactID;
import com.opencsv.CSVWriter;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

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

    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity<String>> importContacts(ImportContactsFromCsvRequest importContactsFromCsvRequest, @CurrentUser String userId) {
        return importContactsFromCsvRequest.toCommand(userId).flatMap(this.commandDispatcher::dispatch)
                .then(Mono.fromCallable(() -> ResponseEntity.ok("Import completed")));

    }

    @PostMapping("/export")
    public ResponseEntity<Flux<DataBuffer>> exportContacts(
            ExportContactsRequest exportContactsRequest, @CurrentUser String userId) {
        var body = Flux.using(() ->
                        this.commandDispatcher.dispatch(exportContactsRequest.toCommand(userId)),
                contactStream -> Flux.fromStream(contactStream)
                        .map(contact ->
                                encodeRow(new String[]{
                                        Objects.toString(contact.id(), ""),
                                        contact.firstName(),
                                        contact.lastName(),
                                        contact.email(),
                                        contact.phoneNumber(),
                                })
                        ),
                Stream::close
        ).subscribeOn(Schedulers.boundedElastic());
        return ResponseEntity.ok()
                .contentType(new MediaType(
                        "text", "csv", StandardCharsets.UTF_8))
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"report.csv\""
                )
                .body(body);

    }

    private DataBuffer encodeRow(String[] values) {
        DataBufferFactory bufferFactory =
                new DefaultDataBufferFactory();
        StringWriter stringWriter = new StringWriter();
        try (CSVWriter csvWriter = new CSVWriter(stringWriter)) {
            csvWriter.writeNext(values);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        return bufferFactory.wrap(
                stringWriter.toString()
                        .getBytes(StandardCharsets.UTF_8)
        );
    }

    @GetMapping("/{id}")
    public GetContactByIDView getContactByID(@PathVariable UUID id) {
        return this.queryDispatcher.dispatch(new GetContactByIDQuery(new ContactID(id)));
    }

    @GetMapping
    public PagedModel<GetContactsView> getContacts(@RequestParam(required = false) String searchTerm, Pageable pageable) {
        return new PagedModel<>(this.queryDispatcher.dispatch(new GetContactsQuery(searchTerm, pageable)));
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
