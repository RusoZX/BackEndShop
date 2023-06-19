package com.daniilzverev.shopserver.dao;

import com.daniilzverev.shopserver.constants.Constants;
import com.daniilzverev.shopserver.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserDaoTest {

    @Autowired
    private UserDao underTest;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.TIME_FORMAT);
    //Reminder to add the example user via sql and to delete it
    @Test
    void existingEmail(){
        //given
        String email="example@example.com";
        //Expected
        User expected = new User();
        expected.setId(-1L);
        expected.setName("test");
        expected.setSurname("test1");
        expected.setBirthDate(LocalDate.parse("2001-09-11"));
        expected.setEmail(email);
        expected.setPwd("someEncryptedData");


        //then
        User actualResponse = underTest.findByEmail(email);
        //asert
        assertEquals(expected, actualResponse);
    }
    @Test
    void nonExistingEmail(){
        //given
        String email="NonExistingEmail";

        //then
        User actualResponse = underTest.findByEmail(email);
        //asert
        assertTrue(Objects.isNull(actualResponse));
    }
}