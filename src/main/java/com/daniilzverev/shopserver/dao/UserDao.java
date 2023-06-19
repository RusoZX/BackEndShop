package com.daniilzverev.shopserver.dao;

import com.daniilzverev.shopserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface UserDao extends JpaRepository<User, Long> {
    User findByEmail(@Param("email") String email);
}
