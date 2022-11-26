package yio.tro.opacha.stuff.calendar;

public class CveMonth {

    public int year;
    public int monthIndex;
    public int daysQuantity;
    public CveColorYio color;
    public String nameKey;


    public void setValues(int year, int monthIndex) {
        this.year = year;
        this.monthIndex = monthIndex;
        updateNameKey();
        updateColor();
        updateDaysQuantity();
    }


    private void updateDaysQuantity() {
        daysQuantity = CalendarManager.getInstance().getDaysQuantity(year, monthIndex);
    }


    private void updateColor() {
        CveColorYio[] values = CveColorYio.values();
        int path = 12 * (year - 2019) + monthIndex - 1;
        int index = path % values.length;
        color = values[index];
    }


    public void updateNameKey() {
        switch (monthIndex) {
            default:
            case 0:
                System.out.println("CveMonth.updateNameKey: problem");
                break;
            case 1:
                nameKey = "january";
                break;
            case 2:
                nameKey = "february";
                break;
            case 3:
                nameKey = "march";
                break;
            case 4:
                nameKey = "april";
                break;
            case 5:
                nameKey = "may";
                break;
            case 6:
                nameKey = "june";
                break;
            case 7:
                nameKey = "july";
                break;
            case 8:
                nameKey = "august";
                break;
            case 9:
                nameKey = "september";
                break;
            case 10:
                nameKey = "october";
                break;
            case 11:
                nameKey = "november";
                break;
            case 12:
                nameKey = "december";
                break;
        }
    }

}
