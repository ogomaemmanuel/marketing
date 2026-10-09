
package com.ogoma.marketing.api.contacts;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.ogoma.marketing.core.application.contacts.commands.ImportContactsFromCsvCommand;
import com.ogoma.marketing.core.application.contacts.commands.ImportContactsFromCsvCommand.ContactDetails;
import com.ogoma.marketing.core.domain.audience.AudienceId;
import jakarta.validation.constraints.NotNull;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.InputStream;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record ImportContactsFromCsvRequest(
        FilePart file,
        List<@NotNull UUID> targetAudienceIds
) {

    private static final CsvMapper CSV_MAPPER = new CsvMapper();

    private static final CsvSchema CSV_SCHEMA =
            CSV_MAPPER.schemaFor(ContactDetails.class).withoutHeader();

    public ImportContactsFromCsvRequest {
        targetAudienceIds = targetAudienceIds == null
                ? List.of()
                : List.copyOf(targetAudienceIds);
    }

    public Mono<ImportContactsFromCsvCommand> toCommand(String userId) {
        if (file == null) {
            return Mono.error(
                    new IllegalArgumentException(
                            "Import file must not be null or empty."
                    )
            );
        }

        Set<AudienceId> audienceIds = targetAudienceIds.stream()
                .map(AudienceId::new)
                .collect(Collectors.toUnmodifiableSet());

        return parseCsv(file)
                .collectList()
                .map(contacts ->
                        new ImportContactsFromCsvCommand(
                                contacts,
                                audienceIds,
                                userId
                        )
                );
    }

    public Flux<ContactDetails> parseCsv(FilePart filePart) {
        if (filePart == null) {
            return Flux.error(
                    new IllegalArgumentException("Import file must not be null.")
            );
        }

        return DataBufferUtils.join(filePart.content())
                .publishOn(Schedulers.boundedElastic())
                .flatMapMany(this::parseBuffer);
    }

    private Flux<ContactDetails> parseBuffer(DataBuffer dataBuffer) {
        try {
            return parseBufferContents(dataBuffer);
        } finally {
            DataBufferUtils.release(dataBuffer);
        }
    }

    private Flux<ContactDetails> parseBufferContents(DataBuffer dataBuffer) {
        try (InputStream inputStream = dataBuffer.asInputStream(false);
             MappingIterator<ContactDetails> iterator = CSV_MAPPER
                     .readerFor(ContactDetails.class)
                     .with(CSV_SCHEMA)
                     .readValues(inputStream)) {

            // Parsing occurs on boundedElastic, not the Netty event loop.
            List<ContactDetails> contacts = iterator.readAll();

            return Flux.fromIterable(contacts);

        } catch (Exception exception) {
            return Flux.error(
                    new IllegalArgumentException(
                            "Failed to parse CSV file. Check the file format and column values.",
                            exception
                    )
            );
        }
    }
}