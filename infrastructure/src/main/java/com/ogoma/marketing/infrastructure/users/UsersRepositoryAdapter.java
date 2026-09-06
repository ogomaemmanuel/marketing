package com.ogoma.marketing.infrastructure.users;

import com.ogoma.marketing.core.domain.users.UserEntity;
import com.ogoma.marketing.core.domain.users.UserID;
import com.ogoma.marketing.core.domain.users.UsersRepository;
import org.springframework.data.core.PropertyPath;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public record UsersRepositoryAdapter(JdbcAggregateTemplate jdbcAggregateTemplate) implements UsersRepository {

    @Override
    public UserEntity save(UserEntity userEntity) {
        return jdbcAggregateTemplate.save(userEntity);
    }
    @Override
    public Optional<UserEntity> findById(UserID id) {
        return Optional.ofNullable(jdbcAggregateTemplate.findById(id, UserEntity.class));
    }
    @Override
    public Optional<UserEntity> findByExternalId(String externalId) {
        var criteria = Criteria.where(PropertyPath.of(UserEntity::getExternalId)).is(externalId);
        var query = Query.query(criteria);
        return jdbcAggregateTemplate.findOne(query, UserEntity.class);
    }
}
