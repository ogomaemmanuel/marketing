package com.ogoma.marketing.infrastructure.sms;

import com.ogoma.marketing.core.domain.sms.SmsTemplateEntity;
import com.ogoma.marketing.core.domain.sms.SmsTemplateID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SmsTemplateJDBCRepository extends CrudRepository<SmsTemplateEntity, SmsTemplateID>, ListPagingAndSortingRepository<SmsTemplateEntity,SmsTemplateID> {
}
