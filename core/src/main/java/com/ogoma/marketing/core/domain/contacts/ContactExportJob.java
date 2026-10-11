package com.ogoma.marketing.core.domain.contacts;

import com.ogoma.marketing.core.sharedkernel.ddd.AggregateRoot;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Clock;
import java.time.Instant;


@Table(name = "export_jobs")
@Getter
public class ContactExportJob extends AggregateRoot<ContactExportID> {
    @Id
    private String fileUrl;
    private String requestedBy;
    private Instant requestedAt;
    private ExportFormat exportFormat;
    private ExportStatus status;
    private Instant lastUpdatedAt;

    private ContactExportJob(
            String fileUrl,
            String requestedBy,
            ExportFormat exportFormat,
            Instant instant
    ) {
        super(new ContactExportID());
        this.exportFormat = exportFormat;
        this.fileUrl = fileUrl;
        this.requestedBy = requestedBy;
        this.status = ExportStatus.PROCESSING;
        this.requestedAt = instant;
        this.lastUpdatedAt = instant;

    }

    public static ContactExportJob createNew(
            String fileUrl,
            String requestedBy,
            ExportFormat exportFormat,
            Clock clock) {
        var contactExportJob = new ContactExportJob(fileUrl, requestedBy, exportFormat, clock.instant());
        contactExportJob.raiseEvent(new ContactExportRequestedEvent(contactExportJob.id.id()));
        return contactExportJob;
    }

}
