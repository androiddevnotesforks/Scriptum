package sgtmelon.handynotes.db.item;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.util.List;

import sgtmelon.handynotes.office.annot.Db;
import sgtmelon.handynotes.office.conv.ConvBool;
import sgtmelon.handynotes.office.conv.ConvString;
import sgtmelon.handynotes.office.conv.ConvList;
import sgtmelon.handynotes.office.Help;

@Entity(tableName = Db.RK_TB)
@TypeConverters({ConvBool.class, ConvString.class})
public class ItemRank {

    public ItemRank() {

    }

    @Ignore
    public ItemRank(int position, String name) {
        this.position = position;
        this.name = name;

        idNote = new Long[0];
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

        idNote = new Long[0];
        visible = true;

        textCount = 0;
        rollCount = 0;
        rollCheck = 0.0;
    }

    /**
     * Убирает из массива необходимую дату создания заметки
     *
     * @param noteId - Id заметки
     */
    public void removeId(long noteId) {
        List<Long> createList = ConvList.toList(idNote);
        int index = createList.indexOf(noteId);
        createList.remove(index);
        idNote = ConvList.fromList(createList);
    }

    //region Variables
    @ColumnInfo(name = Db.RK_ID)
    @PrimaryKey(autoGenerate = true)
    private long id;             //Позиция в базе данных
    @ColumnInfo(name = Db.RK_ID_NT)
    private Long[] idNote;    //Id заметок которые привязаны

    @ColumnInfo(name = Db.RK_PS)
    private int position;       //Позиция в списке
    @ColumnInfo(name = Db.RK_NM)
    private String name;        //Уникальное имя
    @ColumnInfo(name = Db.RK_VS)
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

    public Long[] getIdNote() {
        return idNote;
    }

    public void setIdNote(Long[] idNote) {
        this.idNote = idNote;
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
