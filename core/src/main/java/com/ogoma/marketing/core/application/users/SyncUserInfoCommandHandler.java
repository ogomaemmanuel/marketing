package com.ogoma.marketing.core.application.users;

import com.ogoma.marketing.core.abstractions.CommandHandler;
import com.ogoma.marketing.core.domain.users.UserEntity;
import com.ogoma.marketing.core.domain.users.UserID;
import com.ogoma.marketing.core.domain.users.UsersRepository;

public class SyncUserInfoCommandHandler implements CommandHandler<SyncUserInfoCommand, UserID> {
    private final UsersRepository usersRepository;

    public SyncUserInfoCommandHandler(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public Class<SyncUserInfoCommand> supports() {
        return SyncUserInfoCommand.class;
    }

    @Override
    public UserID handle(SyncUserInfoCommand command) {
        var user = this.usersRepository.findByExternalId(command.externalId()).map(existingUserEntity -> {
            existingUserEntity.update(command.firstName(), command.lastName(), command.email());
            return existingUserEntity;
        }).orElseGet(() -> UserEntity.createNew(command.firstName(), command.lastName(), command.email(), command.externalId()));
        return usersRepository.save(user).getId();
    }
}
