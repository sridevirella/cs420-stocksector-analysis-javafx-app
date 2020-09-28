package domain;
//separate package
public class MonthlyData {

    private String date;
    private double open;
    private double high;
    private double low;
    private double close;
    private long volume;

    private MonthlyData(){}

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

}