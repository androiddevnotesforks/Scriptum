package sgtmelon.scriptum.app.model.item;

import java.util.List;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import sgtmelon.scriptum.office.annot.DbAnn;
import sgtmelon.scriptum.office.conv.BoolConv;
import sgtmelon.scriptum.office.conv.ListConv;
import sgtmelon.scriptum.office.conv.StringConv;

@Entity(tableName = DbAnn.RK_TB)
@TypeConverters({BoolConv.class, StringConv.class})
public final class RankItem { // TODO: 02.10.2018 чистая модель

    @ColumnInfo(name = DbAnn.RK_ID)
    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = DbAnn.RK_ID_NT)
    private Long[] idNote;

    @ColumnInfo(name = DbAnn.RK_PS)
    private int position;
    @ColumnInfo(name = DbAnn.RK_NM)
    private String name;
    @ColumnInfo(name = DbAnn.RK_VS)
    private boolean visible;

    @Ignore
    private int textCount;
    @Ignore
    private int rollCount;

    public RankItem() {

    }

    @Ignore
    public RankItem(int position, String name) {
        this.position = position;
        this.name = name;

        idNote = new Long[0];
        visible = true;

        textCount = 0;
        rollCount = 0;
    }

    @Ignore
    public RankItem(int id, int position, String name) {
        this.id = id;
        this.position = position;
        this.name = name;

        idNote = new Long[0];
        visible = true;

        textCount = 0;
        rollCount = 0;
    }

    /**
     * Убирает из массива необходимую дату создания заметки
     *
     * @param noteId - Id заметки
     */
    public void removeId(long noteId) {
        List<Long> createList = ListConv.toList(idNote);
        createList.remove(noteId);
        idNote = ListConv.fromList(createList);
    }

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

}
