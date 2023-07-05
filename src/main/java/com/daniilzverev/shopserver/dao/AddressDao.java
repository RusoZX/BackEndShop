package com.daniilzverev.shopserver.dao;

import com.daniilzverev.shopserver.entity.Address;
import com.daniilzverev.shopserver.wrapper.AddressWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AddressDao extends JpaRepository<Address, Long> {
    @Query("select new com.daniilzverev.shopserver.wrapper.AddressWrapper(a.id, a.country, a.street) from Address a where" +
            " a.user.id = :userId")
    List<AddressWrapper> findAllWrapper(@Param("userId") Long userId);
    

}
