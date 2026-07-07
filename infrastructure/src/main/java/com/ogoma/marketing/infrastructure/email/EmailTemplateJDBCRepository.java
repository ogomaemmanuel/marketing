package com.ogoma.marketing.infrastructure.email;

import com.ogoma.marketing.core.domain.email.EmailTemplateEntity;
import com.ogoma.marketing.core.domain.email.EmailTemplateID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailTemplateJDBCRepository extends CrudRepository<EmailTemplateEntity, EmailTemplateID>, ListPagingAndSortingRepository<EmailTemplateEntity, EmailTemplateID> {
}
