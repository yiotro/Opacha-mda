package yio.tro.opacha.game.gameplay.model;

public class MatchResults {

    public FractionType winner;
    public boolean humanWon;
    public boolean campaignMode;
    public boolean calendarMode;
    public int levelIndex;
    public int year;
    public int month;
    public int day;


    public MatchResults() {
        campaignMode = false;
        calendarMode = false;
        levelIndex = -1;
        year = -1;
        month = -1;
        day = -1;
    }


    public void setWinner(FractionType winner) {
        this.winner = winner;
    }


    public void setCampaignMode(boolean campaignMode) {
        this.campaignMode = campaignMode;
    }


    public void setLevelIndex(int levelIndex) {
        this.levelIndex = levelIndex;
    }


    public void setHumanWon(boolean humanWon) {
        this.humanWon = humanWon;
    }


    public void setCalendarMode(boolean calendarMode) {
        this.calendarMode = calendarMode;
    }


    public void setDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }
}
