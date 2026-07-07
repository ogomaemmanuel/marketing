package com.ogoma.marketing.core.domain.users;

import java.util.Optional;

public interface UsersRepository {
    UserEntity save(UserEntity userEntity);

    Optional<UserEntity> findById(UserID id);

    Optional<UserEntity> findByExternalId(String externalId);
}
