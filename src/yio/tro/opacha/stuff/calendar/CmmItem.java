package yio.tro.opacha.stuff.calendar;

import java.util.ArrayList;

public class CmmItem {

    public int year;
    public int month;
    public ArrayList<Integer> days;


    public CmmItem(int year, int month) {
        this.year = year;
        this.month = month;
        days = new ArrayList<>();

    }


    String encode() {
        if (isFullyCompleted()) {
            return year + " " + month + ">full";
        }
        StringBuilder builder = new StringBuilder();
        for (int day : days) {
            builder.append(day).append(" ");
        }
        return year + " " + month + ">" + builder.toString();
    }


    void addDay(int dayIndex) {
        days.add(dayIndex);
    }


    public boolean isCompleted(int dayIndex) {
        return days.contains(dayIndex);
    }


    public boolean isFullyCompleted() {
        return days.size() == CalendarManager.getInstance().getDaysQuantity(year, month);
    }


    @Override
    public String toString() {
        return "[CmmItem: " +
                year + " " +
                month + " " +
                "(" + days.size() + ")" +
                "]";
    }
}
