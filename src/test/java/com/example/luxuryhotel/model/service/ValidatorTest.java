package com.example.luxuryhotel.model.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class ValidatorTest {
    @Autowired
    Validator valid;
    @Test
    void nullEmail() {
        assertFalse (valid.email(null));
    }
    @Test
    void validEmail() {
        assertTrue (valid.email("gregor@gmail.com"));
        assertTrue (valid.email("gregordsadasfasfasfa@gmail.com"));
    }
    @Test
    void invalidEmail() {
        assertFalse (valid.email("gregor@gmail.commm"));
        assertFalse (valid.email("gregor@gmail..com"));
        assertFalse (valid.email("gregor@гмаил.com"));
    }

    @Test
    void nullUsername() {
        assertFalse (valid.userName(null));
    }
    @Test
    void validUsername() {
        assertTrue (valid.userName("Jhon"));
        assertTrue (valid.userName("123_HjonF_fd"));
    }
    @Test
    void invalidUsername() {
        assertFalse (valid.email("TooLongNameTooLongNameTooLongNameTooLongNameTooLongNameTooLongNameTooLongNameTooLongNameTooLongNameTooLongName"));
        assertFalse (valid.email("ДЖОН"));
        assertFalse (valid.email("S"));
        assertFalse (valid.email("Jhon."));
        assertFalse (valid.email("Jhon$"));
    }

    @Test
    void updateApartmentTst(){
        List<String> messages = valid.updateApartment(null,null, null);
        assertTrue(messages.containsAll(List.of("choseClazz", "incorrectBeds", "incorrectPrice")));
    }

    @Test
    void sendRequest() {
        List<String> messages = valid.sendRequest(LocalDate.now().toString(),LocalDate.now().toString(),null,null, null);
        assertTrue(messages.containsAll(List.of("choseClazz", "incorrectBeds", "writeWishes")));
    }


    @Test
    void timeInterval() {
        List<String> messages = valid.timeInterval(LocalDate.now().toString(), LocalDate.now().toString());
        assertEquals(messages.size(), 0);
        messages = valid.timeInterval(LocalDate.now().plusDays(2).toString(), LocalDate.now().toString());
        assertTrue(messages.contains("wrongDayOrder"));
    }
}