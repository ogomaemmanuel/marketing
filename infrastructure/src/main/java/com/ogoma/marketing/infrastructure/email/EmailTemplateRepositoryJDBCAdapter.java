package com.ogoma.marketing.infrastructure.email;

import com.ogoma.marketing.core.domain.email.EmailTemplateEntity;
import com.ogoma.marketing.core.domain.email.EmailTemplateEntityID;
import com.ogoma.marketing.core.domain.email.EmailTemplateRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
public class EmailTemplateRepositoryJDBCAdapter implements EmailTemplateRepository {
    private final EmailTemplateJDBCRepository jdbcRepository;
    public EmailTemplateRepositoryJDBCAdapter(EmailTemplateJDBCRepository jdbcRepository) {
        this.jdbcRepository = jdbcRepository;
    }


    @Override
    public Page<EmailTemplateEntity> getEmailTemplates(Pageable pageable) {
        return this.jdbcRepository.findAll(pageable);
    }

    @Override
    public EmailTemplateEntity saveTemplate(EmailTemplateEntity emailTemplateEntity) {
        return this.jdbcRepository.save(emailTemplateEntity);
    }

    @Override
    public Optional<EmailTemplateEntity> getTemplateByID(EmailTemplateEntityID id) {
        return this.jdbcRepository.findById(id);
    }
}
