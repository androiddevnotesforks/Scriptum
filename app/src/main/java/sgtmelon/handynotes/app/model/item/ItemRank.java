package sgtmelon.handynotes.app.model.item;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.util.List;

import sgtmelon.handynotes.app.data.DataInfo;
import sgtmelon.handynotes.office.conv.ConvBool;
import sgtmelon.handynotes.office.conv.ConvString;
import sgtmelon.handynotes.office.conv.ConvList;
import sgtmelon.handynotes.office.Help;

@Entity(tableName = DataInfo.RK_TB)
@TypeConverters({ConvBool.class, ConvString.class})
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
        List<String> createList = ConvList.toList(create);

        int index = createList.indexOf(noteCreate);

        createList.remove(index);

        create = ConvList.fromList(createList);
    }

    //region Variables
    @ColumnInfo(name = RK_ID)
    @PrimaryKey(autoGenerate = true)
    private long id;             //Позиция в базе данных

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

    public long getId() {
        return id;
    }

    public void setId(long id) {
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
