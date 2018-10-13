package sgtmelon.scriptum.app.model.item;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.List;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.office.Help;
import sgtmelon.scriptum.office.annot.DbAnn;
import sgtmelon.scriptum.office.annot.def.db.CheckDef;
import sgtmelon.scriptum.office.annot.def.db.TypeDef;
import sgtmelon.scriptum.office.conv.BoolConv;
import sgtmelon.scriptum.office.conv.ListConv;
import sgtmelon.scriptum.office.conv.StringConv;

@Entity(tableName = DbAnn.NT_TB)
@TypeConverters({BoolConv.class, StringConv.class})
public final class NoteItem { // TODO: 02.10.2018 чистая модель

    @ColumnInfo(name = DbAnn.NT_ID)
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = DbAnn.NT_CR)
    private String create;
    @ColumnInfo(name = DbAnn.NT_CH)
    private String change;

    @ColumnInfo(name = DbAnn.NT_NM)
    private String name = "";
    @ColumnInfo(name = DbAnn.NT_TX)
    private String text = "";

    @ColumnInfo(name = DbAnn.NT_CL)
    private int color;
    @ColumnInfo(name = DbAnn.NT_TP)
    private int type;

    @ColumnInfo(name = DbAnn.NT_RK_PS)
    private Long[] rankPs = new Long[0];
    @ColumnInfo(name = DbAnn.NT_RK_ID)
    private Long[] rankId = new Long[0];

    @ColumnInfo(name = DbAnn.NT_BN)
    private boolean bin = false;
    @ColumnInfo(name = DbAnn.NT_ST)
    private boolean status = false;

    public NoteItem() {

    }

    public NoteItem(Context context, @TypeDef int type) {
        create = Help.Time.getCurrentTime(context);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        color = pref.getInt(context.getString(R.string.pref_key_color), context.getResources().getInteger(R.integer.pref_color_default));

        this.type = type;
    }

    /**
     * @param noteRankId - Убирает из массивов ненужную категорию по id
     */
    public void removeRank(long noteRankId) {
        List<Long> rankIdList = ListConv.toList(rankId);
        List<Long> rankPsList = ListConv.toList(rankPs);

        int index = rankIdList.indexOf(noteRankId);

        rankIdList.remove(index);
        rankPsList.remove(index);

        rankId = ListConv.fromList(rankIdList);
        rankPs = ListConv.fromList(rankPsList);
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

    public void setChange(Context context) {
        change = Help.Time.getCurrentTime(context);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName(Context context) {
        if (!name.equals("")) return name;
        else return context.getString(R.string.hint_view_name);
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

    public void setColor(int color) {
        this.color = color;
    }

    public int getType() {
        return type;
    }

    public void setType(@TypeDef int type) {
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

    public int[] getCheck() {
        int[] check = new int[2];

        if (type == TypeDef.roll) {
            String[] split = text.split(CheckDef.divider);
            for (int i = 0; i < 2; i++) {
                check[i] = Integer.parseInt(split[i]);
            }
        }

        return check;
    }

    public boolean isAllCheck() {
        int[] check = getCheck();
        return check[0] == check[1];
    }

}
