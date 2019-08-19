package coolpharaoh.tee.speicher.tea.timer.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;
import coolpharaoh.tee.speicher.tea.timer.entities.converter.DateConverter;
import java.util.Date;

@Entity(tableName = "tea")
public class Tea {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "tea_id")
    private Long id;

    private String name;
    private String variety;
    private double amount;
    private String amountkind;
    private int color;
    @TypeConverters(DateConverter.class)
    private Date date;

    @NonNull
    public Long getId() {
        return id;
    }

    public void setId(@NonNull Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVariety() {
        return variety;
    }

    public void setVariety(String variety) {
        this.variety = variety;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getAmountkind() {
        return amountkind;
    }

    public void setAmountkind(String amountkind) {
        this.amountkind = amountkind;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
