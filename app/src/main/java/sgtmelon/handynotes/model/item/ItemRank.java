package sgtmelon.handynotes.model.item;

public class ItemRank {

    public ItemRank(){

    }

    public ItemRank(int id, int position, String name){
        this.id = id;
        this.position = position;
        this.name = name;

        create = new String[0];
        visible = true;

        textCount = 0;
        rollCount = 0;
        rollCheck = 0.0;
    }

    //region Variables
    private int id;             //Позиция в базе данных
    private int position;       //Позиция в списке
    private String name;        //Уникальное имя
    private String[] create;    //Даты создания, которые привязаны к заметке
    private boolean visible;    //Видимость категории

    private int textCount;      //Количество тектовых заметок
    private int rollCount;      //Количество списков
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

    public void setRollCheck(double rollCheck) {
        this.rollCheck = rollCheck;
    }
}
