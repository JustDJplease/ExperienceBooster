package me.theblockbender.xpboost.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import me.theblockbender.xpboost.Main;

public class UtilTime {

    Main main;

    public UtilTime(Main main) {
        this.main = main;
    }

    public String translateTime(long time) {
        return convertString(time, 1, "FIT");
    }

    public String convertString(long time, int trim, String type) {
        if (time == -1)
            return "0.0 " + main.getMessage("time-second") + main.getMessage("time-multiple");

        if (type == "FIT") {
            if (time < 60000)
                type = "SECONDS";
            else if (time < 3600000)
                type = "MINUTES";
            else if (time < 86400000)
                type = "HOURS";
            else
                type = "DAYS";
        }

        String text;
        double num;
        if (trim == 0) {
            if (type == "DAYS")
                text = (num = trim(trim, time / 86400000d)) + " " + main.getMessage("time-day");
            else if (type == "HOURS")
                text = (num = trim(trim, time / 3600000d)) + " " + main.getMessage("time-hour");
            else if (type == "MINUTES")
                text = (num = trim(trim, time / 60000d)) + " " + main.getMessage("time-minute");
            else if (type == "SECONDS")
                text = (int) (num = (int) trim(trim, time / 1000d)) + " " + main.getMessage("time-second");
            else
                text = (int) (num = (int) trim(trim, time)) + " " + main.getMessage("time-millisecond");
        } else {
            if (type == "DAYS")
                text = (num = trim(trim, time / 86400000d)) + " " + main.getMessage("time-day");
            else if (type == "HOURS")
                text = (num = trim(trim, time / 3600000d)) + " " + main.getMessage("time-hour");
            else if (type == "MINUTES")
                text = (num = trim(trim, time / 60000d)) + " " + main.getMessage("time-minute");
            else if (type == "SECONDS")
                text = (num = trim(trim, time / 1000d)) + " " + main.getMessage("time-second");
            else
                text = (int) (num = (int) trim(0, time)) + " " + main.getMessage("time-millisecond");
        }

        if (num != 1)
            text += main.getMessage("time-multiple");

        if (text.equalsIgnoreCase("-1.0 Seconds")) {
            return "0.0 " + main.getMessage("time-second") + main.getMessage("time-multiple");
        }
        return text;
    }

    public double trim(int degree, double d) {
        String format = "#.#";

        for (int i = 1; i < degree; i++)
            format += "#";

        DecimalFormatSymbols symb = new DecimalFormatSymbols(Locale.US);
        DecimalFormat twoDForm = new DecimalFormat(format, symb);
        return Double.valueOf(twoDForm.format(d));
    }
}
