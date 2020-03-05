package com.coursework.barbershopapp.model;

public class Common {

    public static final int SLOT_COUNT = 33;
    public static String KEY_DISPLAY_TIMESLOT = "";
    public static int STEP = 0;
    public static String SERVICE_KEY = "";
    public static AboutService currentService;

    public static String convertTimeSlotToString(int position) {

        switch (position){
            case 0:
                return "10:00";
            case 1:
                return "10:20";
            case 2:
                return "10:40";
            case 3:
                return "11:00";
            case 4:
                return "11:20";
            case 5:
                return "11:40";
            case 6:
                return "12:00";
            case 7:
                return "12:20";
            case 8:
                return "12:40";
            case 9:
                return "13:00";
            case 10:
                return "13:20";
            case 11:
                return "13:40";
            case 12:
                return "14:00";
            case 13:
                return "14:20";
            case 14:
                return "14:40";
            case 15:
                return "15:00";
            case 16:
                return "15:20";
            case 17:
                return "15:40";
            case 18:
                return "16:00";
            case 19:
                return "16:20";
            case 20:
                return "16:40";
            case 21:
                return "17:00";
            case 22:
                return "17:20";
            case 23:
                return "17:40";
            case 24:
                return "18:00";
            case 25:
                return "18:20";
            case 26:
                return "18:40";
            case 27:
                return "19:00";
            case 28:
                return "19:20";
            case 29:
                return "19:40";
            case 30:
                return "20:00";
            case 31:
                return "20:20";
            case 32:
                return "20:40";
                default:
                    return "Барбершоп закрыт!";
        }

    }
}
