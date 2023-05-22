package com.example.mealmovers_merchant.main;

import org.checkerframework.common.returnsreceiver.qual.This;

import java.time.LocalDateTime;

public class ExtensionMethods {

    public static String toHourAndMinute(@This String string) {
        LocalDateTime dateTime = LocalDateTime.parse(string);
        String hour = String.valueOf(dateTime.getHour());
        String minutes = String.valueOf(dateTime.getMinute());
        return hour + ":" + minutes;
    }
}
