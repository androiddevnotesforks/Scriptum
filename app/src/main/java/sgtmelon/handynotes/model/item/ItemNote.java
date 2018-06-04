package sgtmelon.handynotes.model.item;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import sgtmelon.handynotes.service.NoteDB;

public class ItemNote {

    public ItemNote() {

    }

    public ItemNote(Bundle bundle) {
        id = bundle.getInt(NoteDB.KEY_NT_ID);

        create = bundle.getString(NoteDB.KEY_NT_CR);

        change = bundle.getString(NoteDB.KEY_NT_CH);
        name = bundle.getString(NoteDB.KEY_NT_NM);
        text = bundle.getString(NoteDB.KEY_NT_TX);

        color = bundle.getInt(NoteDB.KEY_NT_CL);
        type = bundle.getInt(NoteDB.KEY_NT_TP);

        rankId = bundle.getStringArray(NoteDB.KEY_NT_RK_ID);

        bin = bundle.getBoolean(NoteDB.KEY_NT_BN);
        status = bundle.getBoolean(NoteDB.KEY_NT_ST);
    }

    public Intent fillIntent(Intent intent) {
        intent.putExtra(NoteDB.KEY_NT_ID, id);

        intent.putExtra(NoteDB.KEY_NT_CR, create);

        intent.putExtra(NoteDB.KEY_NT_CH, change);
        intent.putExtra(NoteDB.KEY_NT_NM, name);
        intent.putExtra(NoteDB.KEY_NT_TX, text);

        intent.putExtra(NoteDB.KEY_NT_CL, color);
        intent.putExtra(NoteDB.KEY_NT_TP, type);

        intent.putExtra(NoteDB.KEY_NT_RK_ID, rankId);

        intent.putExtra(NoteDB.KEY_NT_BN, bin);
        intent.putExtra(NoteDB.KEY_NT_ST, status);


        return intent;
    }

    //region Variables
    private int id;             //Позиция в базе данных
    private String create;      //Дата создания
    private String change;      //Дата изменения
    private String name;   //Имя заметки
    private String text;        //Текст заметки (для списка используется как индикатор количества отмеченных элементов)
    private int color;          //Цвет заметки
    private int type;           //Тип заметки (0 - текст, 1 - список)
    private String[] rankId;      //Категории, к которым привязана заметка
    private boolean bin;        //Расположение
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
