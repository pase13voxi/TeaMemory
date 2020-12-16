package coolpharaoh.tee.speicher.tea.timer.core.tea;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

import coolpharaoh.tee.speicher.tea.timer.core.converter.DateConverter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity(tableName = "tea")
public class Tea {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "tea_id")
    private Long id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "variety")
    private String variety;

    @ColumnInfo(name = "amount")
    private int amount;

    @ColumnInfo(name = "amountkind")
    private String amountKind;

    @ColumnInfo(name = "color")
    private int color;

    @ColumnInfo(name = "rating")
    private int rating;

    @ColumnInfo(name = "favorite")
    private boolean favorite;

    @ColumnInfo(name = "nextinfusion")
    private int nextInfusion;

    @TypeConverters(DateConverter.class)
    @ColumnInfo(name = "date")
    private Date date;

    @Ignore
    public Tea(String name, String variety, int amount, String amountKind, int color, int nextInfusion, Date date) {
        this.name = name;
        this.variety = variety;
        this.amount = amount;
        this.amountKind = amountKind;
        this.color = color;
        this.nextInfusion = nextInfusion;
        this.date = date;
        rating = 0;
        favorite = false;
    }
}
