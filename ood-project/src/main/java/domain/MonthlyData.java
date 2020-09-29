package domain;

import java.util.Objects;

public class MonthlyData {

    private String date;
    private double open;
    private double high;
    private double low;
    private double close;
    private long volume;

    public MonthlyData(String date, double open, double high, double low, double close, long volume) {

        this.date = date;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
    }

    public String getDate() {
        return date;
    }

    public void setDate( String month ){
        this.date = month;
    }

    @Override
    public String toString() {
        return "MonthlyPrice{ " +
                "Month = " + date +
                ", open = " + open +
                ", high = " + high +
                ", low = " + low +
                ", close = " + close +
                ", volume = " + volume +
                " }";
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (!(o instanceof MonthlyData)) return false;
        MonthlyData that = (MonthlyData) o;
        return Double.compare(that.open, open) == 0 &&
                Double.compare(that.high, high) == 0 &&
                Double.compare(that.low, low) == 0 &&
                Double.compare(that.close, close) == 0 &&
                volume == that.volume;
    }

    @Override
    public int hashCode() {
        return Objects.hash(open, high, low, close, volume);
    }
}