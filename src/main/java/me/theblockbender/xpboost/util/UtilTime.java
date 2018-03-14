package me.theblockbender.xpboost.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import me.theblockbender.xpboost.Main;

public class UtilTime {

    private Main main;

    public UtilTime(Main main) {
        this.main = main;
    }

    public String translateTime(long time) {
        return convertString(time);
    }

    private String convertString(long time) {
        if (time == -1)
            return "0.0 " + main.getMessage("time-second") + main.getMessage("time-multiple");

        String type;
        int  trim = 1;
        if (time < 60000)
            type = "SECONDS";
        else if (time < 3600000)
            type = "MINUTES";
        else if (time < 86400000)
            type = "HOURS";
        else
            type = "DAYS";
        String text;
        double num;
        switch (type) {
            case "DAYS":
                text = (num = trim(trim, time / 86400000d)) + " " + main.getMessage("time-day");
                break;
            case "HOURS":
                text = (num = trim(trim, time / 3600000d)) + " " + main.getMessage("time-hour");
                break;
            case "MINUTES":
                text = (num = trim(trim, time / 60000d)) + " " + main.getMessage("time-minute");
                break;
            case "SECONDS":
                text = (num = trim(trim, time / 1000d)) + " " + main.getMessage("time-second");
                break;
            default:
                text = (int) (num = (int) trim(0, time)) + " " + main.getMessage("time-millisecond");
                break;
        }


        if (num != 1)
            text += main.getMessage("time-multiple");

        if (text.equalsIgnoreCase("-1.0 Seconds")) {
            return "0.0 " + main.getMessage("time-second") + main.getMessage("time-multiple");
        }
        return text;
    }

    private double trim(int degree, double d) {
        StringBuilder format = new StringBuilder("#.#");

        for (int i = 1; i < degree; i++)
            format.append("#");

        DecimalFormatSymbols symb = new DecimalFormatSymbols(Locale.US);
        DecimalFormat twoDForm = new DecimalFormat(format.toString(), symb);
        return Double.valueOf(twoDForm.format(d));
    }
}
