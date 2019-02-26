package sgtmelon.scriptum.app.model.item;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import sgtmelon.scriptum.app.model.RankRepo;
import sgtmelon.scriptum.app.room.converter.BoolConverter;
import sgtmelon.scriptum.app.room.converter.StringConverter;
import sgtmelon.scriptum.office.annot.DbAnn;

/**
 * Элемент списка категорий {@link RankRepo}
 */
@Entity(tableName = DbAnn.Rank.TABLE)
@TypeConverters({BoolConverter.class, StringConverter.class})
public final class RankItem {

    @ColumnInfo(name = DbAnn.Rank.ID)
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = DbAnn.Rank.NOTE_ID) private List<Long> noteId = new ArrayList<>();
    @ColumnInfo(name = DbAnn.Rank.POSITION) private int position;
    @ColumnInfo(name = DbAnn.Rank.NAME) private String name;
    @ColumnInfo(name = DbAnn.Rank.VISIBLE) private boolean visible = true;

    @Ignore private int textCount = 0;
    @Ignore private int rollCount = 0;

    public RankItem() {

    }

    @Ignore
    public RankItem(int position, @NonNull String name) {
        this.position = position;
        this.name = name;
    }

    @Ignore
    public RankItem(int id, int position, @NonNull String name) {
        this.id = id;
        this.position = position;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    public List<Long> getNoteId() {
        return noteId;
    }

    public void setNoteId(@NonNull List<Long> noteId) {
        this.noteId = noteId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public int getTextCount() {
        return textCount;
    }

    public void setTextCount(int textCount) {
        this.textCount = textCount;
    }

    public int getRollCount() {
        return rollCount;
    }

    public void setRollCount(int rollCount) {
        this.rollCount = rollCount;
    }

}