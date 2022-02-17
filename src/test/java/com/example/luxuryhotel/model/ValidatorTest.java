package com.example.luxuryhotel.model;


import com.example.luxuryhotel.model.service.Validator;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class ValidatorTest {

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

}