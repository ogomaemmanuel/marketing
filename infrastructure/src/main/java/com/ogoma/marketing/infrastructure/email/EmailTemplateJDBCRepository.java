package com.ogoma.marketing.infrastructure.email;

import com.ogoma.marketing.core.domain.email.EmailTemplateEntity;
import com.ogoma.marketing.core.domain.email.EmailTemplateEntityID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailTemplateJDBCRepository extends CrudRepository<EmailTemplateEntity, EmailTemplateEntityID>, ListPagingAndSortingRepository<EmailTemplateEntity, EmailTemplateEntityID> {
}
