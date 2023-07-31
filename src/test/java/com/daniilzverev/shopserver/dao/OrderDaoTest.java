package com.daniilzverev.shopserver.dao;

import com.daniilzverev.shopserver.constants.Constants;
import com.daniilzverev.shopserver.entity.Address;
import com.daniilzverev.shopserver.entity.Order;
import com.daniilzverev.shopserver.entity.User;
import com.daniilzverev.shopserver.wrapper.OrderForClientWrapper;
import com.daniilzverev.shopserver.wrapper.OrderForEmployeeWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Sql(scripts = {"/insert_test_data_order.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"/delete_test_data_order.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class OrderDaoTest {
    @Autowired
    OrderDao orderDao;

    @Test
    void findAllByUserId() {
        List<OrderForClientWrapper> expected= new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.TIME_FORMAT);
        expected.add( new OrderForClientWrapper(-1L,false, "pending", LocalDate.parse("2023-07-06",formatter)));

        assertEquals(expected, orderDao.findAllByUserId(-1L));
    }
    @Test
    void findAllNone() {
        List<OrderForEmployeeWrapper> expected= new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.TIME_FORMAT);
        expected.add( new OrderForEmployeeWrapper(-1L,"example1@example.com",false, "pending",LocalDate.parse("2023-07-06",formatter),20D));
        expected.add( new OrderForEmployeeWrapper(-2L,"example@example.com",true, "paid",LocalDate.parse("2023-06-11",formatter),10D));


        assertEquals(expected, orderDao.findAllNone());
    }
    @Test
    void findAllWeek() {
        List<OrderForEmployeeWrapper> expected= new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.TIME_FORMAT);
        expected.add( new OrderForEmployeeWrapper(-1L,"example1@example.com",false, "pending",LocalDate.parse("2023-07-06",formatter),20D));



        assertEquals(expected, orderDao.findAllTimeInterval(LocalDate.parse("2023-07-06",formatter),
                LocalDate.parse("2023-07-07",formatter)));
    }
    @Test
    void findAllMonth() {
        List<OrderForEmployeeWrapper> expected= new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.TIME_FORMAT);
        expected.add( new OrderForEmployeeWrapper(-1L,"example1@example.com",false, "pending",LocalDate.parse("2023-07-06",formatter),20D));


        assertEquals(expected, orderDao.findAllTimeInterval(LocalDate.parse("2023-07-06",formatter),
                LocalDate.parse("2023-07-06",formatter)));
    }

}