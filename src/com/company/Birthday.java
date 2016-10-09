package com.company;

import java.time.DateTimeException;
import java.time.LocalDate;

/**
 * Created by Hemanth on 10/8/16.
 */
public class Birthday {
    private int month;
    private int day;
    private int year;
    private String name;
    public Birthday(int month,int day,int year){
        this.month=month;
        this.day=day;
        this.year=year;
    }

    public String getHTML(){

        return "";
    }

    private void checkDates(){
    try{
        LocalDate.of(year,month,day);
        getBirthday();
    }catch (DateTimeException e){
        DateError();
    }
    }

    private void getBirthday(){

    }

    private void DateError(){

    }
}
