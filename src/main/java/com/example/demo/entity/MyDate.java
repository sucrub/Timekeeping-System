package com.example.demo.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyDate extends Date {
    public MyDate() {
        // Call the no-argument constructor of the superclass
        super();
    }

    public MyDate(long date) {
        // Call the constructor of the superclass with the specified date
        super(date);
    }
    public MyDate(String dateString) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(dateString);
        setTime(date.getTime());
    }

    public MyDate(int year, int month, int day) {
        // Create a MyDate object with the specified year, month, and day
        super(year, month - 1, day);
    }

    @Override
    public String toString() {
        return getDate() + "-" + (getMonth() + 1) + "-" + (getYear() + 1900);
    }
}
