package com.daniilzverev.shopserver.dao;

import com.daniilzverev.shopserver.wrapper.AddressWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Sql(scripts = {"/insert_test_data_address_dao.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"/delete_test_data_address_dao.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class AddressDaoTest {
    @Autowired
    AddressDao underTest;

    @Test
    void findAllWrapper(){
        List<AddressWrapper> expected = new ArrayList<>();
        expected.add(new AddressWrapper(-2L,"idk1","idk1"));
        expected.add(new AddressWrapper(-1L,"idk","idk"));


        List<AddressWrapper> actualResponse = underTest.findAllWrapper(-1L);

        assertEquals(expected, actualResponse);
    }

}