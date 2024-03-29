package com.app.biblio.bean;

import static org.junit.jupiter.api.Assertions.assertEquals;


import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class NotificationTest {

    private Notification notification;

    @BeforeEach
    public void setUp() {
        notification = new Notification();
    }

    @Test
    public void testId() {
        Long expectedId = 1L;
        notification.setId(expectedId);
        assertEquals(expectedId, notification.getId());
    }

    @Test
    public void testUser() {
        User expectedUser = new User();
        expectedUser.setUsername("Test User");
        notification.setUser(expectedUser);
        assertEquals(expectedUser, notification.getUser());
    }

    @Test
    public void testMessage() {
        String expectedMessage = "Test Message";
        notification.setMessage(expectedMessage);
        assertEquals(expectedMessage, notification.getMessage());
    }

    @Test
    public void testCreatedAt() {
        LocalDateTime expectedDateTime = LocalDateTime.now();
        notification.setCreatedAt(expectedDateTime);
        assertEquals(expectedDateTime, notification.getCreatedAt());
    }

    @Test
    public void testRead() {
        boolean expectedReadStatus = true;
        notification.setRead(expectedReadStatus);
        assertEquals(expectedReadStatus, notification.isRead());
    }
}
