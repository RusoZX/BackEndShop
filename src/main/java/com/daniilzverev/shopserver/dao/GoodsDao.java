package com.daniilzverev.shopserver.dao;

import com.daniilzverev.shopserver.entity.Goods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GoodsDao extends JpaRepository<Goods, Long> {

    List<Goods> findAllByOrderId(@Param("orderId") Long orderID);
}
