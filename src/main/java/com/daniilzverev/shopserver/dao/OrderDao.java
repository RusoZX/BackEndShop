package com.daniilzverev.shopserver.dao;

import com.daniilzverev.shopserver.entity.Order;
import com.daniilzverev.shopserver.wrapper.OrderForClientWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderDao extends JpaRepository<Order, Long> {
    //fix
    @Query("select new com.daniilzverev.shopserver.wrapper.OrderForClientWrapper(o.id, o.paymentStatus, o.orderStatus)" +
            " from Order o where o.user.id = :userId")
    List<OrderForClientWrapper> findAllByUserId(@Param("userId") Long userId);
}
