package com.lilangel.teamplay.repository;

import com.lilangel.teamplay.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
    boolean existsByTgId(Long tgId);

    User getUserByTgId(Long tgId);

}
