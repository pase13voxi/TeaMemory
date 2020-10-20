package coolpharaoh.tee.speicher.tea.timer.core.note;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity(tableName = "note", foreignKeys =
@ForeignKey(entity = Tea.class, parentColumns = "tea_id", childColumns = "tea_id", onDelete = ForeignKey.CASCADE), indices = {@Index("tea_id")})
public class Note {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "note_id")
    private Long id;

    @ColumnInfo(name = "tea_id")
    private long teaId;

    @ColumnInfo(name = "position")
    private int position;

    @ColumnInfo(name = "header")
    private String header;

    @ColumnInfo(name = "description")
    private String description;

    @Ignore
    public Note(long teaId, int position, String header, String description) {
        this.teaId = teaId;
        this.position = position;
        this.header = header;
        this.description = description;
    }
}
