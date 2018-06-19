package sgtmelon.handynotes.model.item;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.util.List;

import sgtmelon.handynotes.data.DataInfo;
import sgtmelon.handynotes.data.converter.ConverterBool;
import sgtmelon.handynotes.data.converter.ConverterList;
import sgtmelon.handynotes.data.converter.ConverterString;
import sgtmelon.handynotes.Help;

@Entity(tableName = "RANK_TABLE")
@TypeConverters({ConverterBool.class, ConverterString.class})
public class ItemRank extends DataInfo {

    public ItemRank() {

    }

    @Ignore
    public ItemRank(int position, String name) {
        this.position = position;
        this.name = name;

        create = new String[0];
        visible = true;

        textCount = 0;
        rollCount = 0;
        rollCheck = 0.0;
    }

    @Ignore
    public ItemRank(int id, int position, String name) {
        this.id = id;
        this.position = position;
        this.name = name;

        create = new String[0];
        visible = true;

        textCount = 0;
        rollCount = 0;
        rollCheck = 0.0;
    }

    /**
     * Убирает из массива необходимую дату создания заметки
     *
     * @param noteCreate - Дата создания заметки
     */
    public void removeCreate(String noteCreate) {
        List<String> createList = ConverterList.toList(create);

        int index = createList.indexOf(noteCreate);

        createList.remove(index);

        create = ConverterList.fromList(createList);
    }

    //region Variables
    @ColumnInfo(name = RK_ID)
    @PrimaryKey(autoGenerate = true)
    private int id;             //Позиция в базе данных

    @ColumnInfo(name = RK_PS)
    private int position;       //Позиция в списке
    @ColumnInfo(name = RK_NM)
    private String name;        //Уникальное имя
    @ColumnInfo(name = RK_CR)
    private String[] create;    //Даты создания, которые привязаны к заметке
    @ColumnInfo(name = RK_VS)
    private boolean visible;    //Видимость категории

    @Ignore
    private int textCount;      //Количество тектовых заметок
    @Ignore
    private int rollCount;      //Количество списков
    @Ignore
    private double rollCheck;   //Выполнение списков в процентах
    //endregion

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getCreate() {
        return create;
    }

    public void setCreate(String[] create) {
        this.create = create;
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

    public double getRollCheck() {
        return rollCheck;
    }

    public void setRollCheck(int rollCheckCount, int rollAllCount) {
        rollCheck = Help.Note.getCheckValue(rollCheckCount, rollAllCount);
    }
}
