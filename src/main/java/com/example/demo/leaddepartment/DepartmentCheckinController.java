package com.example.demo.leaddepartment;

import com.example.demo.entity.*;
import com.example.demo.log.LogControl;
import com.example.demo.qlnssystem.IQLNSSystem;
import com.example.demo.qlnssystem.QLNSSystemData;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class DepartmentCheckinController {
    IQLNSSystem iqlnsSystem = new QLNSSystemData();
    LogControl logControl = new LogControl();
    public List<OfficerLog> generateOfficerData(int departmentId) {
        List<OfficerLog> data;
        data = logControl.getOfficerLogData(departmentId);
        return data;
    }
    public List<WorkerLog> generateWorkerData() {
        List<WorkerLog> data;
        data = logControl.getWorkerLogData();
        return data;
    }
    public List<OfficerLog> filterOfficerData(List<OfficerLog> data, List<User> userList, String partialName) {
        List<User> matchingUsers = userList.stream()
                .filter(user -> user.getName().contains(partialName))
                .toList();

        if (!matchingUsers.isEmpty()) {
            List<Integer> matchingUserIds = matchingUsers.stream()
                    .map(User::getId)
                    .toList();

            return data.stream()
                    .filter(log -> matchingUserIds.contains(log.getId()))
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    public List<OfficerLog> filterOfficerData(List<OfficerLog> data, List<User> userList, String partialName, int targetMonth) {
        List<User> matchingUsers = userList.stream()
                .filter(user -> user.getName().contains(partialName))
                .toList();

        if (!matchingUsers.isEmpty()) {
            List<Integer> matchingUserIds = matchingUsers.stream()
                    .map(User::getId)
                    .toList();

            return data.stream()
                    .filter(log -> matchingUserIds.contains(log.getId()) && log.getDay().getMonth() == targetMonth - 1)
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    // NOTE: Filter by name
    public List<WorkerLog> filterWorkerData(List<WorkerLog> data, List<User> userList, String partialName) {
        List<User> matchingUsers = userList.stream()
                .filter(user -> user.getName().contains(partialName))
                .toList();

        if (!matchingUsers.isEmpty()) {
            List<Integer> matchingUserIds = matchingUsers.stream()
                    .map(User::getId)
                    .toList();

            return data.stream()
                    .filter(log -> matchingUserIds.contains(log.getId()))
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    // NOTE: Filter by name and time
    public List<WorkerLog> filterWorkerData(List<WorkerLog> data, List<User> userList, String partialName, int targetMonth) {
        List<User> matchingUsers = userList.stream()
                .filter(user -> user.getName().contains(partialName))
                .toList();

        if (!matchingUsers.isEmpty()) {
            List<Integer> matchingUserIds = matchingUsers.stream()
                    .map(User::getId)
                    .toList();

            return data.stream()
                    .filter(log -> matchingUserIds.contains(log.getId()) && log.getDay().getMonth() == targetMonth - 1)
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    // NOTE: Get user from departmentId
    public List<User> getUser(int departmentId) throws SQLException {
        List<User> users;
        users = iqlnsSystem.getUsersByDepartment(departmentId);
        return users;
    }

    public int countDay(List<? extends Log> data) {
        if(data.isEmpty()) return 0;
        int [] personIdCount = new int[100];
        for (Log log : data) {
            int personId = log.getId();
            personIdCount[personId]++;
        }
        return personIdCount[data.get(0).getId()];
    }
}
