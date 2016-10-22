package com.company;

import java.io.IOException;
import java.io.OutputStream;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;

/**
 * Created by Hemanth on 10/8/16.
 */
public class Birthday {
    private int month;
    private int day;
    private int year;
    private String name;
    private String input;
    private OutputStream out;

    //parse birthday variables to data
    public Birthday(String input, OutputStream out1){
        this.out=out1;

       String splits = input.split("\\?")[1];
       String[] split = splits.split("&");
       System.out.println(Arrays.toString(split));
       for (int i =0;i < split.length;i++){
           setVar(split[i]);

       }
        System.out.println("Name: " + this.name);
        System.out.println("Month: " + this.month);
        System.out.println("Day: " + this.day);
        System.out.println("Year: " + this.year);
        checkDates();

    }
    //Set variables to
    private void setVar(String var){

        String[] split = var.split("=");
        switch(split[0]){
            case "person":
                this.name=(split[1]);
                break;
            case "num1":
                this.month=Integer.valueOf(split[1]);
                break;
            case "num2":
                this.day=Integer.valueOf(split[1]);
                break;
            case "num3":
                this.year=Integer.valueOf(split[1]);
                break;
            default:


        }

    }


    //Check if input is valid
    private void checkDates(){
    try{
        LocalDate birth =LocalDate.of(year,month,day);
        getBirthday(birth);
    }catch (DateTimeException e){
        System.out.println(e.getMessage());
        DateError(e.getMessage());
    }
    }
    //Find Age from date
    private void getBirthday(LocalDate birth){
        LocalDate now = LocalDate.now();
        Period years = Period.between(birth,now);
        System.out.println(years.getYears());
        sendBirthday(years.getYears());
    }
    //Send Birthday HTML formatted
    private void sendBirthday(int age){
        System.out.println("Birthday");
        String sendBirth = "<h1>Hey " + this.name + "!</h1>" + "\n" + "<p> You are " + age
                + " Years Old</p>";
        try{
            out.write("HTTP/1.1 200\n".getBytes());
            out.write("Content-Type: text/html\n".getBytes());
            out.write(("Content-Length: " + sendBirth.length() + "\n").getBytes());
            out.write("\r\n".getBytes());
            out.write(sendBirth.getBytes());
        }catch (IOException e){
            System.out.println();

        }
    }
    //Custom Error Message depending on Birthday entered
    private void DateError(String errorMessage){
    System.out.println("Error");
        String error = "<h1>" + errorMessage+"</h1>";
        System.out.println(error);
        try {
            out.write("HTTP/1.1 200\n".getBytes());
            out.write("Content-Type: text/html\n".getBytes());
            out.write((("Content-Length: " + error.length() + "\n").getBytes()));
            out.write("\r\n".getBytes());
            out.write(error.getBytes());
            System.out.println("End of output");
        }catch (IOException e){
            System.out.println(e.getMessage());
        }

    }
}
