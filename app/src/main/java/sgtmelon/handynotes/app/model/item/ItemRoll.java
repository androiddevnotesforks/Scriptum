package sgtmelon.handynotes.app.model.item;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import sgtmelon.handynotes.office.annotation.Db;
import sgtmelon.handynotes.office.converter.ConvBool;

import static android.arch.persistence.room.ForeignKey.CASCADE;

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
