package sgtmelon.handynotes.model.item;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.content.Intent;
import android.os.Bundle;

import java.util.List;

import sgtmelon.handynotes.data.DataInfo;
import sgtmelon.handynotes.data.converter.ConverterBool;
import sgtmelon.handynotes.data.converter.ConverterList;
import sgtmelon.handynotes.data.converter.ConverterString;

@Entity(tableName = "NOTE_TABLE")
@TypeConverters({ConverterBool.class, ConverterString.class})
public class ItemNote extends DataInfo {

    public ItemNote() {

    }

    public ItemNote(Bundle bundle) {
        id = bundle.getInt(NT_ID);

        create = bundle.getString(NT_CR);

        change = bundle.getString(NT_CH);
        name = bundle.getString(NT_NM);
        text = bundle.getString(NT_TX);

        color = bundle.getInt(NT_CL);
        type = bundle.getInt(NT_TP);

        rankPs = bundle.getStringArray(NT_RK_PS);
        rankId = bundle.getStringArray(NT_RK_ID);

        bin = bundle.getBoolean(NT_BN);
        status = bundle.getBoolean(NT_ST);
    }

    public Intent fillIntent(Intent intent) {
        intent.putExtra(NT_ID, id);

        intent.putExtra(NT_CR, create);

        intent.putExtra(NT_CH, change);
        intent.putExtra(NT_NM, name);
        intent.putExtra(NT_TX, text);

        intent.putExtra(NT_CL, color);
        intent.putExtra(NT_TP, type);

        intent.putExtra(NT_RK_PS, rankPs);
        intent.putExtra(NT_RK_ID, rankId);

        intent.putExtra(NT_BN, bin);
        intent.putExtra(NT_ST, status);


        return intent;
    }

    /**
     * Убирает из массивов ненужную категорию
     *
     * @param noteRankId - Id ненужной категории
     */
    public void removeRank(String noteRankId) {
        List<String> rankIdList = ConverterList.toList(rankId);
        List<String> rankPsList = ConverterList.toList(rankPs);

        int index = rankIdList.indexOf(noteRankId);

        rankIdList.remove(index);
        rankPsList.remove(index);

        rankId = ConverterList.fromList(rankIdList);
        rankPs = ConverterList.fromList(rankPsList);
    }

    //region Variables
    @ColumnInfo(name = NT_ID)
    @PrimaryKey(autoGenerate = true)
    private int id;             //Позиция в базе данных

    @ColumnInfo(name = NT_CR)
    private String create;      //Дата создания
    @ColumnInfo(name = NT_CH)
    private String change;      //Дата изменения

    @ColumnInfo(name = NT_NM)
    private String name;        //Имя заметки
    @ColumnInfo(name = NT_TX)
    private String text;        //Текст заметки (для списка используется как индикатор количества отмеченных элементов)

    @ColumnInfo(name = NT_CL)
    private int color;          //Цвет заметки
    @ColumnInfo(name = NT_TP)
    private int type;           //Тип заметки (0 - текст, 1 - список)

    @ColumnInfo(name = NT_RK_PS)
    private String[] rankPs;
    @ColumnInfo(name = NT_RK_ID)
    private String[] rankId;    //Категории, к которым привязана заметка

    @ColumnInfo(name = NT_BN)
    private boolean bin;        //Расположение
    @ColumnInfo(name = NT_ST)
    private boolean status;     //Привязка к шторке
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

    public void setType(int type) {
        this.type = type;
    }

    public String[] getRankPs() {
        return rankPs;
    }

    public void setRankPs(String[] rankPs) {
        this.rankPs = rankPs;
    }

    public String[] getRankId() {
        return rankId;
    }

    public void setRankId(String[] rankId) {
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
