package com.example.hairreservationsystem;

public class DataClass {

    private String day_txt;
    private int weekday;

    DataClass(String day_txt, int weekday){
        this.day_txt = day_txt;
        this.weekday = weekday;
    }

    public String getDay_txt(){
        return this.day_txt;
    }

    public String getWeekday(){
        switch (this.weekday){
            case 1:
                return "일";
            case 2:
                return "월";
            case 3:
                return "화";
            case 4:
                return "수";
            case 5:
                return "목";
            case 6:
                return "금";
            default:
                return "토";
        }
    }

}