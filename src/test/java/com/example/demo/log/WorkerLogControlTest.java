package com.example.demo.log;

import com.example.demo.entity.WorkerLog;
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

public class WorkerLogControlTest {

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
    void testCreateAndRetrieveWorkerShift() throws SQLException {
        WorkerLogControl workerLogControl = new WorkerLogControl();

        int userId = 1;
        LocalDate localDate = LocalDate.now();
        List<Double> doubleList = Arrays.asList(8.0, 7.5, 8.5);

        workerLogControl.createWorkerLog(connection, localDate, doubleList, userId);

        WorkerLog workerLog = workerLogControl.getWorkerLogByUserIdAndDate(connection, userId, localDate.toString());

        assertNotNull(workerLog);
        assertEquals(doubleList.get(0), workerLog.getShift1());
        assertEquals(doubleList.get(1), workerLog.getShift2());
        assertEquals(doubleList.get(2), workerLog.getShift3());
    }
}