package com.example.demo.qlns;

import com.example.demo.entity.User;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ExportTest {

    public static class ExportFake {
        public String testExport(List<User> userList, int fileType, int departmentId, int month) {
            if(userList.isEmpty()) {
                return "Need at least 1 user";
            }
            else {
                String result = "";
                if(fileType == 1) {
                    result += "File type: Excel, ";
                }
                else {
                    result += "File type: CSV, ";
                }
                if(departmentId == 1) {
                    result += "workerData in month " + month;
                }
                else {
                    result += "officerData of department " + departmentId + " in month " + month;
                }
                return result;
            }
        }
    }

    public static class TestCase {
        @Test
        public void testExportExcelWorkerData() {
            ExportFake exportFake = new ExportFake();
            List<User> userList = new ArrayList<>();
            userList.add(new User(1, "John"));
            String result = exportFake.testExport(userList, 1, 1, 12);
            assertEquals("File type: Excel, workerData in month 12", result);
        }

        @Test
        public void testExportCSVOfficerData() {
            ExportFake exportFake = new ExportFake();
            List<User> userList = new ArrayList<>();
            userList.add(new User(1, "John"));
            String result = exportFake.testExport(userList, 2, 2, 6);
            assertEquals("File type: CSV, officerData of department 2 in month 6", result);
        }

        @Test
        public void testExportEmptyUserList() {
            ExportFake exportFake = new ExportFake();
            List<User> userList = new ArrayList<>();
            String result = exportFake.testExport(userList, 1, 1, 12);
            assertEquals("Need at least 1 user", result);
        }
    }

}
