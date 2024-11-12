package com.example.demo.qlns;

import com.example.demo.entity.Department;
import com.example.demo.log.LogControl;
import com.example.demo.qlnssystem.IQLNSSystem;
import com.example.demo.qlnssystem.QLNSSystemData;

import java.sql.SQLException;
import java.util.List;

public class CompanyDataController {
    private final IQLNSSystem iqlnsSystem = new QLNSSystemData();
    private final LogControl logControl = new LogControl();


    public CompanyDataController() {

    }

    public List<Department> getDepartmentList() throws SQLException {
        return iqlnsSystem.getAllDepartments();
    }

    public double getShift1(int year, int departmentId, int option, int month, int quad) {
        double data = 0.0;
        if(departmentId > 1) {
            return data;
        }
        if(option == 1) {
            data = logControl.getSumShift1(year);
        } else if (option == 2) {
            if(quad < 1 || quad > 4) {
                return 0.0;
            }
            data = logControl.getSumShift1Quad(quad, year);
        } else {
            data = logControl.getSumShift1(month, year);
        }
        return data;
    }

    public double getShift2(int year, int departmentId, int option, int month, int quad) {
        double data = 0.0;
        if(departmentId > 1) {
            return data;
        }
        if(option == 1) {
            data = logControl.getSumShift2(year);
        } else if (option == 2) {
            if(quad < 1 || quad > 4) {
                return 0.0;
            }
            data = logControl.getSumShift2Quad(quad, year);
        } else {
            data = logControl.getSumShift2(month, year);
        }
        return data;
    }

    public double getShift3(int year, int departmentId, int option, int month, int quad) {
        double data = 0.0;
        if(departmentId > 1) {
            return data;
        }
        if(option == 1) {
            data = logControl.getSumShift3(year);
        } else if (option == 2) {
            if(quad < 1 || quad > 4) {
                return 0.0;
            }
            data = logControl.getSumShift3Quad(quad, year);
        } else {
            data = logControl.getSumShift3(month, year);
        }
        return data;
    }

    public double getTimeLate(int year, int departmentId, int option, int month, int quad) throws SQLException {
        double data = 0.0;
        if(departmentId == 0) {
            List<Department> departments = getDepartmentList();
            if(option == 1) {
                for(Department department : departments) {
                    if(department.getId() != 1) {
                        data += logControl.getSumTimeLate(department.getId(), year);
                    }
                }
            }
            else if (option == 2) {
                for(Department department : departments) {
                    if(department.getId() != 1) {
                        data += logControl.getSumTimeLateQuad(department.getId(), quad, year);
                    }
                }
            }
            else {
                for(Department department : departments) {
                    if(department.getId() != 1) {
                        data += logControl.getSumTimeLate(department.getId(), month, year);
                    }
                }
            }
            return data;
        }
        else if (departmentId == 1) return data;
        else {
            if(option == 1) {
                data = logControl.getSumTimeLate(departmentId, year);
            } else if (option == 2) {
                data = logControl.getSumTimeLateQuad(departmentId, quad, year);
            } else {
                data = logControl.getSumTimeLate(departmentId, month, year);
            }
            return data;
        }
    }

    public double getTimeEarly(int year, int departmentId, int option, int month, int quad) throws SQLException {
        double data = 0.0;
        if(departmentId == 0) {
            List<Department> departments = getDepartmentList();
            if(option == 1) {
                for(Department department : departments) {
                    if(department.getId() != 1) {
                        data += logControl.getSumTimeEarly(department.getId(), year);
                    }
                }
            }
            else if (option == 2) {
                for(Department department : departments) {
                    if(department.getId() != 1) {
                        data += logControl.getSumTimeEarlyQuad(department.getId(), quad, year);
                    }
                }
            }
            else {
                for(Department department : departments) {
                    if(department.getId() != 1) {
                        data += logControl.getSumTimeEarly(department.getId(), month, year);
                    }
                }
            }
            return data;
        }
        else if (departmentId == 1) return data;
        else {
            if(option == 1) {
                data = logControl.getSumTimeEarly(departmentId, year);
            } else if (option == 2) {
                data = logControl.getSumTimeEarlyQuad(departmentId, quad, year);
            } else {
                data = logControl.getSumTimeEarly(departmentId, month, year);
            }
            return data;
        }
    }

}
