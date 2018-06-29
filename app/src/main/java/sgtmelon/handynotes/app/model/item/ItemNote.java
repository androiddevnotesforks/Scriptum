package sgtmelon.handynotes.app.model.item;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.util.List;

import sgtmelon.handynotes.office.annot.Db;
import sgtmelon.handynotes.office.conv.ConvBool;
import sgtmelon.handynotes.office.conv.ConvString;
import sgtmelon.handynotes.office.conv.ConvList;
import sgtmelon.handynotes.office.annot.def.data.DefType;

@Entity(tableName = Db.NT_TB)
@TypeConverters({ConvBool.class, ConvString.class})
public class ItemNote {

    /**
     * @param noteRankId - Убирает из массивов ненужную категорию по id
     */
    public void removeRank(long noteRankId) {
        List<Long> rankIdList = ConvList.toList(rankId);
        List<Long> rankPsList = ConvList.toList(rankPs);

        int index = rankIdList.indexOf(noteRankId);

        rankIdList.remove(index);
        rankPsList.remove(index);

        rankId = ConvList.fromList(rankIdList);
        rankPs = ConvList.fromList(rankPsList);
    }

    //region Variables
    @ColumnInfo(name = Db.NT_ID)
    @PrimaryKey(autoGenerate = true)
    private long id;             //Позиция в базе данных

    @ColumnInfo(name = Db.NT_CR)
    private String create;      //Дата создания
    @ColumnInfo(name = Db.NT_CH)
    private String change;      //Дата изменения

    @ColumnInfo(name = Db.NT_NM)
    private String name;        //Имя заметки
    @ColumnInfo(name = Db.NT_TX)
    private String text;        //Текст заметки (для списка используется как индикатор количества отмеченных элементов)

    @ColumnInfo(name = Db.NT_CL)
    private int color;          //Цвет заметки
    @ColumnInfo(name = Db.NT_TP)
    private int type;           //Тип заметки (0 - текст, 1 - список)

    @ColumnInfo(name = Db.NT_RK_PS)
    private Long[] rankPs;
    @ColumnInfo(name = Db.NT_RK_ID)
    private Long[] rankId;    //Категории, к которым привязана заметка

    @ColumnInfo(name = Db.NT_BN)
    private boolean bin;        //Расположение
    @ColumnInfo(name = Db.NT_ST)
    private boolean status;     //Привязка к шторке
    //endregion

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCreate() {
        return create;
    }

    public void setCreate(String create) {
        this.create = create;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getType() {
        return type;
    }

    public void setType(@DefType int type) {
        this.type = type;
    }

    public Long[] getRankPs() {
        return rankPs;
    }

    public void setRankPs(Long[] rankPs) {
        this.rankPs = rankPs;
    }

    public Long[] getRankId() {
        return rankId;
    }

    public void setRankId(Long[] rankId) {
        this.rankId = rankId;
    }

    public boolean isBin() {
        return bin;
    }

    public void setBin(boolean bin) {
        this.bin = bin;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

}
