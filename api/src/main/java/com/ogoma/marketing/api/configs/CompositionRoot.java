package com.ogoma.marketing.api.configs;

import com.ogoma.marketing.core.abstractions.*;
import com.ogoma.marketing.core.application.audience.commands.AddAudienceCommandHandler;
import com.ogoma.marketing.core.application.audience.commands.UpdateAudienceCommandHandler;
import com.ogoma.marketing.core.application.audience.queries.GetAudienceByIDQueryHandler;
import com.ogoma.marketing.core.application.audience.queries.GetAudiencesQueryHandler;
import com.ogoma.marketing.core.application.campaign.commands.CreateCampaignCommandHandler;
import com.ogoma.marketing.core.application.campaign.queries.GetCampaignByIDQueryHandler;
import com.ogoma.marketing.core.application.campaign.queries.GetCampaignsQueryHandler;
import com.ogoma.marketing.core.application.contacts.commands.AddContactCommandHandler;
import com.ogoma.marketing.core.application.contacts.commands.AudienceMembershipValidator;
import com.ogoma.marketing.core.application.contacts.commands.UpdateContactCommandHandler;
import com.ogoma.marketing.core.application.contacts.queries.GetContactByIDQueryHandler;
import com.ogoma.marketing.core.application.contacts.queries.GetContactsQueryHandler;
import com.ogoma.marketing.core.application.dashboard.queries.DashboardService;
import com.ogoma.marketing.core.application.dashboard.queries.GetStatsQueryHandler;
import com.ogoma.marketing.core.application.email.commands.CloneEmailTemplateCommandHandler;
import com.ogoma.marketing.core.application.email.commands.CreateEmailTemplateCommandHandler;
import com.ogoma.marketing.core.application.email.commands.UpdateEmailTemplateCommandHandler;
import com.ogoma.marketing.core.application.email.queries.GetEmailTemplateByIDQueryHandler;
import com.ogoma.marketing.core.application.email.queries.GetEmailTemplatesQueryHandler;
import com.ogoma.marketing.core.application.segments.CreateSegmentCommandHandler;
import com.ogoma.marketing.core.application.sms.commands.CreateSmsTemplateCommandHandler;
import com.ogoma.marketing.core.application.sms.commands.DuplicateSmsTemplateCommandHandler;
import com.ogoma.marketing.core.application.sms.commands.UpdateSmsTemplateCommandHandler;
import com.ogoma.marketing.core.application.sms.queries.GetSmsTemplateByIDQueryHandler;
import com.ogoma.marketing.core.application.sms.queries.GetSmsTemplatesQueryHandler;
import com.ogoma.marketing.core.application.transactionalmessages.SendSmsTransactionalCommandHandler;
import com.ogoma.marketing.core.application.transactionalmessages.SendTransactionalEmailCommandHandler;
import com.ogoma.marketing.core.application.users.SyncUserInfoCommandHandler;
import com.ogoma.marketing.core.domain.audience.AudienceRepository;
import com.ogoma.marketing.core.domain.campaigns.CampaignRepository;
import com.ogoma.marketing.core.domain.contacts.AudienceMembershipRepository;
import com.ogoma.marketing.core.domain.contacts.ContactRepository;
import com.ogoma.marketing.core.domain.email.EmailTemplateRepository;
import com.ogoma.marketing.core.domain.sms.SmsTemplateRepository;
import com.ogoma.marketing.core.domain.users.UsersRepository;
import com.ogoma.marketing.core.implementations.CommandDispatcherImpl;
import com.ogoma.marketing.core.implementations.MessageRouterImpl;
import com.ogoma.marketing.core.implementations.QueryDispatcherImpl;
import com.ogoma.marketing.infrastructure.templaterendering.PeppleTemplateRenderer;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

import java.util.List;

@Configuration
@EnableJdbcRepositories(basePackages = "com.ogoma.marketing")
@EntityScan(basePackages = "com.ogoma.marketing")
@ComponentScan(basePackages = "com.ogoma.marketing")
public class CompositionRoot {
    @Bean
    CommandDispatcher commandDispatcher(List<CommandHandler<?, ?>> handlers) {
        return new CommandDispatcherImpl(handlers);
    }

    @Bean
    QueryDispatcher queryDispatcher(List<QueryHandler<?, ?>> handlers) {
        return new QueryDispatcherImpl(handlers);
    }

    @Bean
    CreateEmailTemplateCommandHandler createEmailTemplateCommandHandler(EmailTemplateRepository emailTemplateRepository) {
        return new CreateEmailTemplateCommandHandler(emailTemplateRepository);
    }

    @Bean
    UpdateEmailTemplateCommandHandler updateEmailTemplateCommandHandler(EmailTemplateRepository emailTemplateRepository) {
        return new UpdateEmailTemplateCommandHandler(emailTemplateRepository);
    }

    @Bean
    GetEmailTemplateByIDQueryHandler getEmailTemplateByIDQueryHandler(EmailTemplateRepository emailTemplateRepository) {
        return new GetEmailTemplateByIDQueryHandler(emailTemplateRepository);
    }

    @Bean
    CloneEmailTemplateCommandHandler cloneEmailTemplateCommandHandler(EmailTemplateRepository emailTemplateRepository) {
        return new CloneEmailTemplateCommandHandler(emailTemplateRepository);
    }

    @Bean
    GetEmailTemplatesQueryHandler getTemplatesQueryHandler(EmailTemplateRepository emailTemplateRepository) {
        return new GetEmailTemplatesQueryHandler(emailTemplateRepository);
    }

    @Bean
    CreateSmsTemplateCommandHandler createSmsTemplateCommandHandler(SmsTemplateRepository smsTemplateRepository) {
        return new CreateSmsTemplateCommandHandler(smsTemplateRepository);
    }

    @Bean
    GetSmsTemplatesQueryHandler getSmsTemplatesQueryHandler(SmsTemplateRepository smsTemplateRepository) {
        return new GetSmsTemplatesQueryHandler(smsTemplateRepository);
    }

    @Bean
    DuplicateSmsTemplateCommandHandler duplicateSmsTemplateCommandHandler(SmsTemplateRepository smsTemplateRepository) {
        return new DuplicateSmsTemplateCommandHandler(smsTemplateRepository);
    }

    @Bean
    MessageRouter messageRouter(List<MessageSenderService<? extends Message>> messageSenderServices) {
        return new MessageRouterImpl(messageSenderServices);
    }

    @Bean
    SendSmsTransactionalCommandHandler sendSmsTransactionalCommandHandler(NotificationWorkflowStarterService notificationWorkflowStarterService) {
        return new SendSmsTransactionalCommandHandler(notificationWorkflowStarterService);
    }

    @Bean
    SendTransactionalEmailCommandHandler sendTransactionalEmailCommandHandler(
            NotificationWorkflowStarterService notificationWorkflowStarterService
    ) {
        return new SendTransactionalEmailCommandHandler(notificationWorkflowStarterService);
    }

    @Bean
    AddAudienceCommandHandler addAudienceCommandHandler(
            AudienceRepository audienceRepository
    ) {
        return new AddAudienceCommandHandler(audienceRepository);
    }

    @Bean
    UpdateAudienceCommandHandler updateAudienceCommandHandler(AudienceRepository audienceRepository) {
        return new UpdateAudienceCommandHandler(audienceRepository);
    }

    @Bean
    GetAudienceByIDQueryHandler getAudienceByIDQueryHandler(AudienceRepository audienceRepository) {
        return new GetAudienceByIDQueryHandler(audienceRepository);
    }

    @Bean
    GetAudiencesQueryHandler getAudiencesQueryHandler(AudienceRepository audienceRepository) {
        return new GetAudiencesQueryHandler(audienceRepository);
    }

    @Bean
    UpdateSmsTemplateCommandHandler updateSmsTemplateCommandHandler(SmsTemplateRepository smsTemplateRepository) {
        return new UpdateSmsTemplateCommandHandler(smsTemplateRepository);
    }

    @Bean
    GetSmsTemplateByIDQueryHandler getSmsTemplateByIDQueryHandler(SmsTemplateRepository smsTemplateRepository) {
        return new GetSmsTemplateByIDQueryHandler(smsTemplateRepository);
    }

    @Bean
    GetStatsQueryHandler getStatsQueryHandler(DashboardService dashboardService) {
        return new GetStatsQueryHandler(dashboardService);
    }

    @Bean
    AddContactCommandHandler addContactCommandHandler(
            ContactRepository contactRepository,
            AudienceRepository audienceRepository,
            AudienceMembershipRepository audienceMembershipRepository,
            UnitOfWork unitOfWork
    ) {
        return new AddContactCommandHandler(contactRepository, audienceRepository, audienceMembershipRepository, unitOfWork);
    }


    @Bean
    GetContactByIDQueryHandler getContactByIDQueryHandler(ContactRepository contactRepository) {
        return new GetContactByIDQueryHandler(contactRepository);
    }

    @Bean
    UpdateContactCommandHandler updateContactCommandHandler(
            ContactRepository contactRepository,
            AudienceMembershipValidator audienceMembershipValidator,
            AudienceMembershipRepository audienceMembershipRepository,
            UnitOfWork unitOfWork
    ) {
        return new UpdateContactCommandHandler(
                contactRepository,
                audienceMembershipValidator,
                audienceMembershipRepository, unitOfWork);
    }

    @Bean
    AudienceMembershipValidator audienceMembershipValidator(AudienceRepository audienceRepository) {
        return new AudienceMembershipValidator(audienceRepository);
    }

    @Bean
    CreateSegmentCommandHandler createSegmentCommandHandler(AudienceRepository audienceRepository) {
        return new CreateSegmentCommandHandler(audienceRepository);
    }

    @Bean
    GetContactsQueryHandler getContactsQueryHandler(ContactRepository contactRepository) {
        return new GetContactsQueryHandler(contactRepository);
    }

    @Bean
    CreateCampaignCommandHandler createCampaignCommandHandler(CampaignRepository campaignRepository, UnitOfWork unitOfWork) {
        return new CreateCampaignCommandHandler(campaignRepository, unitOfWork);
    }

    @Bean
    GetCampaignByIDQueryHandler getCampaignByIDQueryHandler(CampaignRepository campaignRepository) {
        return new GetCampaignByIDQueryHandler(campaignRepository);
    }

    @Bean
    GetCampaignsQueryHandler getCampaignsQueryHandler(CampaignRepository campaignRepository) {
        return new GetCampaignsQueryHandler(campaignRepository);
    }


    @Bean
    SyncUserInfoCommandHandler syncUserInfoCommandHandler(UsersRepository usersRepository) {
        return new SyncUserInfoCommandHandler(usersRepository);
    }

    @Bean
    TemplateRenderer templateRenderer() {
        return new PeppleTemplateRenderer();
    }

}
