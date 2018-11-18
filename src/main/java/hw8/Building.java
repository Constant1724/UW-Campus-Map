package hw8;

import com.opencsv.bean.CsvBindByName;

public class Building {
    @CsvBindByName
    private String short_name;

    @CsvBindByName
    private String long_name;

    @CsvBindByName
    private String x;

    @CsvBindByName
    private String y;

    public String getShort_name() {
        return short_name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }

    public String getLong_name() {
        return long_name;
    }

    public void setLong_name(String long_name) {
        this.long_name = long_name;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }
}