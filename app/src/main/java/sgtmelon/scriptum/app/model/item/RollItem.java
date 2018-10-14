package sgtmelon.scriptum.app.model.item;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import sgtmelon.scriptum.app.model.NoteModel;
import sgtmelon.scriptum.office.annot.DbAnn;
import sgtmelon.scriptum.office.conv.BoolConv;

import static androidx.room.ForeignKey.CASCADE;

/**
 * Элемент списка заметок {@link NoteModel}
 */
@Entity(tableName = DbAnn.RL_TB,
        foreignKeys = @ForeignKey(entity = NoteItem.class,
                parentColumns = DbAnn.NT_ID,
                childColumns = DbAnn.RL_ID_NT,
                onUpdate = CASCADE,
                onDelete = CASCADE),
        indices = {@Index(DbAnn.RL_ID_NT)})
@TypeConverters({BoolConv.class})
public final class RollItem {

    @ColumnInfo(name = DbAnn.RL_ID)
    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = DbAnn.RL_ID_NT)
    private long idNote;

    @ColumnInfo(name = DbAnn.RL_PS)
    private int position;
    @ColumnInfo(name = DbAnn.RL_CH)
    private boolean check = false;
    @ColumnInfo(name = DbAnn.RL_TX)
    private String text;

    /**
     * Добавлен пункт в базу данных или нет
     */
    @Ignore
    private boolean exist = true;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdNote() {
        return idNote;
    }

    public void setIdNote(long idNote) {
        this.idNote = idNote;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isExist() {
        return exist;
    }

    public void setExist(boolean exist) {
        this.exist = exist;
    }

}
