package sgtmelon.handynotes.model.item;

public class ItemRoll {

    //region Variables
    private int id;         //Позиция в базе данных
    private int position;   //Позиция пункта в списке
    private boolean check;  //Состояние (0 - не выполнен, 1 - выполнен)
    private String text;    //Название пункта
    private boolean exist;  //Добавлен пункт в базу данных или нет
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
