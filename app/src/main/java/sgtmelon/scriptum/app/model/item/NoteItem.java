package sgtmelon.scriptum.app.model.item;

import android.content.Context;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.model.NoteRepo;
import sgtmelon.scriptum.office.annot.DbAnn;
import sgtmelon.scriptum.office.annot.def.CheckDef;
import sgtmelon.scriptum.office.annot.def.ColorDef;
import sgtmelon.scriptum.office.annot.def.TypeNoteDef;
import sgtmelon.scriptum.office.conv.BoolConv;
import sgtmelon.scriptum.office.conv.StringConv;

/**
 * Элемент списка заметок {@link NoteRepo}
 */
@Entity(tableName = DbAnn.NT_TB)
@TypeConverters({BoolConv.class, StringConv.class})
public final class NoteItem {

    @ColumnInfo(name = DbAnn.NT_ID)
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = DbAnn.NT_CR) private String create;
    @ColumnInfo(name = DbAnn.NT_CH) private String change;

    @ColumnInfo(name = DbAnn.NT_NM) private String name = "";
    @ColumnInfo(name = DbAnn.NT_TX) private String text = "";

    @ColumnInfo(name = DbAnn.NT_CL) private int color;
    @ColumnInfo(name = DbAnn.NT_TP) private int type;

    @ColumnInfo(name = DbAnn.NT_RK_PS) private List<Long> rankPs = new ArrayList<>();
    @ColumnInfo(name = DbAnn.NT_RK_ID) private List<Long> rankId = new ArrayList<>();

    @ColumnInfo(name = DbAnn.NT_BN) private boolean bin = false;
    @ColumnInfo(name = DbAnn.NT_ST) private boolean status = false;

    public NoteItem() {

    }

    @Ignore
    public NoteItem(String create, @ColorDef int color, @TypeNoteDef int type) {
        this.create = create;
        this.color = color;
        this.type = type;
    }

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

    public String getName(Context context) {
        return TextUtils.isEmpty(name)
                ? context.getString(R.string.hint_view_name)
                : name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setText(int rollCheck, int rollCount) {
        text = rollCheck + CheckDef.divider + rollCount;
    }

    public int getColor() {
        return color;
    }

    public void setColor(@ColorDef int color) {
        this.color = color;
    }

    public int getType() {
        return type;
    }

    public void setType(@TypeNoteDef int type) {
        this.type = type;
    }

    public List<Long> getRankPs() {
        return rankPs;
    }

    public void setRankPs(List<Long> rankPs) {
        this.rankPs = rankPs;
    }

    public List<Long> getRankId() {
        return rankId;
    }

    public void setRankId(List<Long> rankId) {
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

    public int[] getCheck() {
        final int[] check = new int[]{-1, 0};

        if (type == TypeNoteDef.roll) {
            final String[] split = text.split(CheckDef.divider);
            if (split.length == 2) {
                for (int i = 0; i < 2; i++) {
                    check[i] = Integer.parseInt(split[i]);
                }
            }
        }

        return check;
    }

    public boolean isAllCheck() {
        final int[] check = getCheck();
        return check[0] == check[1];
    }

}
