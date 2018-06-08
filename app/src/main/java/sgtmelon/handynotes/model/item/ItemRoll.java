package sgtmelon.handynotes.model.item;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import sgtmelon.handynotes.database.DataBaseDescription;
import sgtmelon.handynotes.database.converter.ConverterBool;

@Entity(tableName = "ROLL_TABLE")
@TypeConverters({ConverterBool.class})
public class ItemRoll extends DataBaseDescription{

    //region Variables
    @ColumnInfo(name = RL_ID)
    @PrimaryKey(autoGenerate = true)
    private int id;         //Позиция в базе данных
    //TODO: long type


    @ColumnInfo(name = RL_CR)
    private String create;
    @ColumnInfo(name = RL_PS)
    private int position;   //Позиция пункта в списке
    @ColumnInfo(name = RL_CH)
    private boolean check;  //Состояние (0 - не выполнен, 1 - выполнен)
    @ColumnInfo(name = RL_TX)
    private String text;    //Название пункта

    @Ignore
    private boolean exist;  //Добавлен пункт в базу данных или нет
    //endregion

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreate() {
        return create;
    }

    public void setCreate(String create) {
        this.create = create;
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
