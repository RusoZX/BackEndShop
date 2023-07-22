package com.daniilzverev.shopserver.dao;

import com.daniilzverev.shopserver.entity.Order;
import com.daniilzverev.shopserver.wrapper.FullOrderForClientWrapper;
import com.daniilzverev.shopserver.wrapper.OrderForClientWrapper;
import com.daniilzverev.shopserver.wrapper.OrderForEmployeeWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderDao extends JpaRepository<Order, Long> {

    @Query("select new com.daniilzverev.shopserver.wrapper.OrderForClientWrapper(o.id, o.paymentStatus, o.orderStatus, o.createdDate)" +
            " from Order o where o.user.id = :userId")
    List<OrderForClientWrapper> findAllByUserId(@Param("userId") Long userId);
    @Query("select new com.daniilzverev.shopserver.wrapper.FullOrderForClientWrapper( o.paymentStatus, o.orderStatus,o.paymentMethod, o.deliveryMethod, o.createdDate, o.address.id)" +
            " from Order o where o.id = :orderId")
    FullOrderForClientWrapper findFullByOrderId(@Param("orderId") Long orderId);

    @Query("select new com.daniilzverev.shopserver.wrapper.OrderForEmployeeWrapper(o.id, o.user.email, o.paymentStatus," +
            " o.orderStatus,(SELECT SUM(p.price * g.quantity) FROM Goods g  JOIN g.order ordr JOIN g.product p WHERE ordr.id = o.id))" +
            " from Order o order by createdDate Desc")
    List<OrderForEmployeeWrapper> findAllNone();

    @Query("select new com.daniilzverev.shopserver.wrapper.OrderForEmployeeWrapper(o.id, o.user.email, o.paymentStatus," +
            " o.orderStatus,(SELECT SUM(p.price * g.quantity) FROM Goods g  JOIN g.order ordr JOIN g.product p WHERE ordr.id = o.id))" +
            " from Order o where o.createdDate BETWEEN :start And :end order by createdDate Desc")
    List<OrderForEmployeeWrapper> findAllTimeInterval(@Param("start") LocalDate start, @Param("end") LocalDate end );


}
