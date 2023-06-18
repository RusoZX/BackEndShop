package com.daniilzverev.shopserver.dao;

import com.daniilzverev.shopserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User, Long> {

}
