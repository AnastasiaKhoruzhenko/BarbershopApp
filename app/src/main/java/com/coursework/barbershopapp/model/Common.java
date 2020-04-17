package com.coursework.barbershopapp.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Common {

    public static final int SLOT_COUNT = 33;
    public static final String KEY_SERVICE_STORE = "SERVICE_SAVE";
    public static final String KEY_SERVICE_LOAD_DONE = "SERVICE_LOAD_DONE";

    public static final String KEY_SERVICES_LOAD_DONE = "SERVICES_LOAD_DONE";
    public static final String KEY_SERVICE_SELECTED = "SERVICE_TYPE_SELECTED";

    public static final String KEY_DISPLAY_BARBER = "DISPLAY_BARBER";
    public static final String KEY_BARBER_SELECTED = "BARBER_SELECTED";

    public static final String KEY_STEP = "STEP";
    public static final Object DISABLE_TAG = "DISABLED";
    public static final String KEY_TIME_SLOT = "TIME_SLOT";
    public static final String KEY_CONFURM_BOOKING = "CONFURM_BOOKING";
    public static final String DURATION_OF_SERVICE = "";

    // for booking
    public static String SERVICE_TYPE = "";
    public static final String KEY_DISPLAY_TIMESLOT = "DISPLAY_TIMESLOT";
    public static int STEP = 0;
    public static String SERVICE_KEY = "";
    public static String KEY_NEXT_BTN = "ENABLE_NEXT";
    //for 1 step info (banner = service card)
    public static Banner currentService;

    // for settings user
    public static List<String> list_settings = new ArrayList<String>(){{add("Настройки аккаунта"); add("Настройки приложения"); add("Информация о барбершопе"); add("Выйти");}};
    public static List<String> list_settings_descr = new ArrayList<String>(){{add("Имя, фамилия, дата рождения ..."); add("Push-уведомления, тема, язык"); add("Адрес барбершопа, телефон ..."); add("Выйти из аккаунта");}};

    public static List<String> list_settings_master = new ArrayList<String>(){{add("Настройки аккаунта"); add("Предоставляемые услуги"); add("Настройки приложения"); add("Выйти");}};
    public static List<String> list_descr_settings_master = new ArrayList<String>(){{add("Имя, фамилия, дата рождения ..."); add("Настройка сфер обслуживания"); add("Push-уведомления, язык"); add("Выйти из аккаунта");}};

    public static List<String> list_settings_admin = new ArrayList<String>(){{ add("Информация о салоне"); add("Настройки приложения"); add("Выйти");}};
    public static List<String> list_descr_settings_admin = new ArrayList<String>(){{ add("Информация о салоне"); add("Настройки приложения"); add("Выйти из аккаунта");}};

    public static AboutService currentServiceType;
    //public static String name = "";
    public static Master currentBarber;
    public static int currentTimeSlot = -1;
    public static Calendar currentDate = Calendar.getInstance();


    public static String KEY_EMAIL_USER  = "email";

    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd_MM_yyyy");

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

    public static int convertHourToIndex(int hour){
        switch (hour){
            case 10: return 3;
            case 11: return 6;
            case 12: return 9;
            case 13: return 12;
            case 14: return 15;
            case 15: return 18;
            case 16: return 21;
            case 17: return 24;
            case 18: return 27;
            case 19: return 30;
            default: return -1;
        }
    }
}
