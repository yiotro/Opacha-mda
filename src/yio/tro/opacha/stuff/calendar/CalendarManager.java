package yio.tro.opacha.stuff.calendar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import yio.tro.opacha.YioGdxGame;
import yio.tro.opacha.game.gameplay.model.MatchResults;
import yio.tro.opacha.stuff.DateYio;
import yio.tro.opacha.stuff.TimeMeasureYio;

import java.util.ArrayList;

public class CalendarManager {

    private static CalendarManager instance = null;
    public ArrayList<CveMonth> months;
    public static final String PREFS = "yio.tro.opacha.calendar";
    private ArrayList<CmmItem> items;
    private DateYio currentDate;


    public CalendarManager() {
        months = new ArrayList<>();
        items = new ArrayList<>();
    }


    public void loadValues() {
        initCurrentDate();
        initMonths();
        loadProgress();
    }


    private void initCurrentDate() {
        currentDate = new DateYio();
        currentDate.applyCurrentDay();
    }


    private void loadProgress() {
        items.clear();
        Preferences preferences = getPreferences();
        String source = preferences.getString("items");
        for (String token : source.split(",")) {
            restoreSingleItem(token);
        }
    }


    private void restoreSingleItem(String token) {
        if (token.length() < 3) return;
        String[] mainSplit = token.split(">");
        String[] firstSplit = mainSplit[0].split(" ");
        int year = Integer.valueOf(firstSplit[0]);
        int month = Integer.valueOf(firstSplit[1]);
        CmmItem cmmItem = new CmmItem(year, month);
        items.add(cmmItem);
        if (mainSplit[1].equals("full")) {
            int daysQuantity = getDaysQuantity(year, month);
            for (int i = 1; i <= daysQuantity; i++) {
                cmmItem.addDay(i);
            }
        } else {
            for (String dayString : mainSplit[1].split(" ")) {
                if (dayString.length() == 0) continue;
                int dayIndex = Integer.valueOf(dayString);
                cmmItem.addDay(dayIndex);
            }
        }
    }


    public CmmItem getItem(int year, int month) {
        for (CmmItem cmmItem : items) {
            if (cmmItem.year != year) continue;
            if (cmmItem.month != month) continue;
            return cmmItem;
        }
        return null;
    }


    public boolean isCompleted(int year, int month, int day) {
        CmmItem cmmItem = getItem(year, month);
        if (cmmItem == null) return false;
        return cmmItem.isCompleted(day);
    }


    public boolean isLocked(int year, int month, int day) {
        if (year < currentDate.year) return false;
        if (month < currentDate.month) return false;
        return day > currentDate.day;
    }


    public void onCalendarDayCompleted(MatchResults matchResults) {
        int year = matchResults.year;
        int month = matchResults.month;
        int day = matchResults.day;
        CmmItem cmmItem = getItem(year, month);
        if (cmmItem == null) {
            // first completed day in month
            cmmItem = new CmmItem(year, month);
            items.add(cmmItem);
        }
        if (cmmItem.isCompleted(day)) return; // already completed
        cmmItem.addDay(day);
        saveProgress();
    }


    private void saveProgress() {
        Preferences preferences = getPreferences();
        StringBuilder builder = new StringBuilder();
        for (CmmItem cmmItem : items) {
            builder.append(cmmItem.encode()).append(",");
        }
        preferences.putString("items", builder.toString());
        preferences.flush();
    }


    private void initMonths() {
        months.clear();
        int year = currentDate.year;
        int monthIndex = currentDate.month;
        while (year != 2020 || monthIndex != 12) {
            CveMonth cveMonth = new CveMonth();
            cveMonth.setValues(year, monthIndex);
            months.add(cveMonth);
            monthIndex--;
            if (monthIndex == 0) {
                monthIndex = 12;
                year--;
            }
        }
    }


    public static void initialize() {
        instance = null;
    }


    public static CalendarManager getInstance() {
        if (instance == null) {
            instance = new CalendarManager();
        }
        return instance;
    }


    public int getWeeklyIndex(int year, int monthIndex, int dayIndex) {
        // 28.12.2020 is a monday
        int tempYear = 2020;
        int tempMonthIndex = 12;
        int tempDayIndex = 28;
        int weeklyIndex = 0;

        while (year != tempYear || monthIndex != tempMonthIndex || dayIndex != tempDayIndex) {
            weeklyIndex++;
            tempDayIndex++;
            if (tempDayIndex > getDaysQuantity(tempYear, tempMonthIndex)) {
                tempDayIndex = 1;
                tempMonthIndex++;
                if (tempMonthIndex > 12) {
                    tempMonthIndex = 1;
                    tempYear++;
                }
            }
        }

        return weeklyIndex % 7;
    }


    public int getDaysQuantity(int year, int monthIndex) {
        if (monthIndex == 2 && year % 4 == 0) {
            return 29;
        }
        switch (monthIndex) {
            default:
            case 0:
                System.out.println("CveMonth.updateDaysQuantity: problem");
                return -1;
            case 1:
                return 31;
            case 2:
                return 28;
            case 3:
                return 31;
            case 4:
                return 30;
            case 5:
                return 31;
            case 6:
                return 30;
            case 7:
                return 31;
            case 8:
                return 31;
            case 9:
                return 30;
            case 10:
                return 31;
            case 11:
                return 30;
            case 12:
                return 31;
        }
    }


    private Preferences getPreferences() {
        return Gdx.app.getPreferences(PREFS);
    }


    public void showInConsole() {
        System.out.println();
        System.out.println("CalendarManager.showInConsole");
        for (CmmItem cmmItem : items) {
            System.out.println("- " + cmmItem);
        }
    }
}
