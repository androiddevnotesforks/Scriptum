package sgtmelon.handynotes.app.model.item;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import sgtmelon.handynotes.office.annot.Db;
import sgtmelon.handynotes.office.conv.ConvBool;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = Db.RL_TB,
        foreignKeys = @ForeignKey(entity = ItemNote.class,
                parentColumns = Db.NT_ID,
                childColumns = Db.RL_ID_NT,
                onUpdate = CASCADE,
                onDelete = CASCADE),
        indices = {@Index(Db.RL_ID_NT)})
@TypeConverters({ConvBool.class})
public class ItemRoll {

    //region Variables
    @ColumnInfo(name = Db.RL_ID)
    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = Db.RL_ID_NT)
    private long idNote;

    @ColumnInfo(name = Db.RL_PS)
    private int position;
    @ColumnInfo(name = Db.RL_CH)
    private boolean check = false;
    @ColumnInfo(name = Db.RL_TX)
    private String text;

    @Ignore
    private boolean exist = true;  //Добавлен пункт в базу данных или нет
    //endregion

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
