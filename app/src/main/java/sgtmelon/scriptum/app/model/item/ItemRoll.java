package sgtmelon.scriptum.app.model.item;

import androidx.room.*;
import sgtmelon.scriptum.office.annot.AnnDb;
import sgtmelon.scriptum.office.conv.ConvBool;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = AnnDb.RL_TB,
        foreignKeys = @ForeignKey(entity = ItemNote.class,
                parentColumns = AnnDb.NT_ID,
                childColumns = AnnDb.RL_ID_NT,
                onUpdate = CASCADE,
                onDelete = CASCADE),
        indices = {@Index(AnnDb.RL_ID_NT)})
@TypeConverters({ConvBool.class})
public final class ItemRoll {

    @ColumnInfo(name = AnnDb.RL_ID)
    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = AnnDb.RL_ID_NT)
    private long idNote;

    @ColumnInfo(name = AnnDb.RL_PS)
    private int position;
    @ColumnInfo(name = AnnDb.RL_CH)
    private boolean check = false;
    @ColumnInfo(name = AnnDb.RL_TX)
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
