package sgtmelon.scriptum.app.model.item;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import sgtmelon.scriptum.app.model.RankRepo;
import sgtmelon.scriptum.office.annot.DbAnn;
import sgtmelon.scriptum.office.conv.BoolConv;
import sgtmelon.scriptum.office.conv.StringConv;

/**
 * Элемент списка категорий {@link RankRepo}
 */
@Entity(tableName = DbAnn.RK_TB)
@TypeConverters({BoolConv.class, StringConv.class})
public final class RankItem {

    @ColumnInfo(name = DbAnn.RK_ID)
    @PrimaryKey(autoGenerate = true)
    private long id;

    // TODO: 04.11.2018 переделать в List
    @ColumnInfo(name = DbAnn.RK_ID_NT) private Long[] idNote = new Long[0];
    @ColumnInfo(name = DbAnn.RK_PS) private int position;
    @ColumnInfo(name = DbAnn.RK_NM) private String name;
    @ColumnInfo(name = DbAnn.RK_VS) private boolean visible = true;

    @Ignore private int textCount = 0;
    @Ignore private int rollCount = 0;

    public RankItem() {

    }

    @Ignore
    public RankItem(int position, String name) {
        this.position = position;
        this.name = name;
    }

    @Ignore
    public RankItem(int id, int position, String name) {
        this.id = id;
        this.position = position;
        this.name = name;
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
