package com.daniilzverev.shopserver.dao;

import com.daniilzverev.shopserver.entity.Goods;
import com.daniilzverev.shopserver.wrapper.GoodsWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GoodsDao extends JpaRepository<Goods, Long> {

    List<Goods> findAllByOrderId(@Param("orderId") Long orderID);
    @Query("select new com.daniilzverev.shopserver.wrapper.GoodsWrapper(i.product.id,i.product.title,i.product.price, i.quantity) " +
            "from Goods i where i.order.id = :orderId")
    List<GoodsWrapper> findAllGoods(@Param("orderId") Long orderID);
}
