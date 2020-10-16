package coolpharaoh.tee.speicher.tea.timer.core.note;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import coolpharaoh.tee.speicher.tea.timer.core.tea.Tea;

// duplicates between entities and Json Object are tolerated
@SuppressWarnings("common-java:DuplicatedBlocks")
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

    public Note(){}

    @Ignore
    public Note(long teaId, int position, String header, String description) {
        this.teaId = teaId;
        this.position = position;
        this.header = header;
        this.description = description;
    }

    @NonNull
    public Long getId() {
        return id;
    }

    public void setId(@NonNull Long id) {
        this.id = id;
    }

    public long getTeaId() {
        return teaId;
    }

    public void setTeaId(long teaId) {
        this.teaId = teaId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
