package com.example.demo.log;

import com.example.demo.entity.OfficerLog;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OfficerLogControlTest {

    private Connection connection;

    @BeforeEach
    void setUp() {
        try {
            connection = DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
            // TODO: Initialize database schema if needed
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void tearDown() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testCreateAndRetrieveOfficerShift() throws SQLException {
        OfficerLogControl officerLogControl = new OfficerLogControl();

        int userId = 1;
        LocalDate localDate = LocalDate.now();
        List<Boolean> booleanList = Arrays.asList(true, false);
        Double timeLate = 1.5;
        Double timeEarly = 0.5;

        officerLogControl.createOfficerLog(connection, localDate, booleanList, timeLate, timeEarly, userId);

        OfficerLog officerLog = officerLogControl.getOfficerLogByUserIdAndDate(connection, userId, localDate.toString());

        assertNotNull(officerLog);
        assertEquals(booleanList.get(0), officerLog.isMorning());
        assertEquals(booleanList.get(1), officerLog.isAfternoon());
        assertEquals(timeLate, officerLog.getTimeLate());
        assertEquals(timeEarly, officerLog.getTimeEarly());
    }

    @Test
    void testEditOfficerLog() throws SQLException {
        OfficerLogControl officerLogControl = new OfficerLogControl();

        officerLogControl.createOfficerLog(connection, LocalDate.now(), Arrays.asList(true, false), 1.5, 0.5, 1);

        OfficerLog originalLog = officerLogControl.getOfficerLogByUserIdAndDate(connection, 1, LocalDate.now().toString());

        officerLogControl.editOfficerLog(connection, originalLog.getId(), Arrays.asList(false, true), 2.0, 1.0);

        OfficerLog updatedLog = officerLogControl.getOfficerLogByUserIdAndDate(connection, 1, LocalDate.now().toString());

        assertNotNull(originalLog);
        assertNotNull(updatedLog);
        assertEquals(originalLog.getId(), updatedLog.getId());
        assertEquals(originalLog.getDay(), updatedLog.getDay());
        assertEquals(originalLog.isMorning(), !updatedLog.isMorning());
        assertEquals(originalLog.isAfternoon(), !updatedLog.isAfternoon());
        assertEquals(2.0, updatedLog.getTimeLate());
        assertEquals(1.0, updatedLog.getTimeEarly());
    }
}
