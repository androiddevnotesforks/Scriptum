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
import sgtmelon.scriptum.office.annot.def.db.DefCheck;
import sgtmelon.scriptum.office.annot.def.db.DefDb;
import sgtmelon.scriptum.office.annot.def.db.DefType;
import sgtmelon.scriptum.office.conv.ConvBool;
import sgtmelon.scriptum.office.conv.ConvList;
import sgtmelon.scriptum.office.conv.ConvString;

@Entity(tableName = DefDb.NT_TB)
@TypeConverters({ConvBool.class, ConvString.class})
public class ItemNote {

    @ColumnInfo(name = DefDb.NT_ID)
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = DefDb.NT_CR)
    private String create;
    @ColumnInfo(name = DefDb.NT_CH)
    private String change;

    @ColumnInfo(name = DefDb.NT_NM)
    private String name = "";
    @ColumnInfo(name = DefDb.NT_TX)
    private String text = "";

    @ColumnInfo(name = DefDb.NT_CL)
    private int color;
    @ColumnInfo(name = DefDb.NT_TP)
    private int type;

    @ColumnInfo(name = DefDb.NT_RK_PS)
    private Long[] rankPs = new Long[0];
    @ColumnInfo(name = DefDb.NT_RK_ID)
    private Long[] rankId = new Long[0];

    @ColumnInfo(name = DefDb.NT_BN)
    private boolean bin = false;
    @ColumnInfo(name = DefDb.NT_ST)
    private boolean status = false;

    public ItemNote() {

    }

    public ItemNote(Context context, @DefType int type) {
        create = Help.Time.getCurrentTime(context);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        color = pref.getInt(context.getString(R.string.pref_key_color), context.getResources().getInteger(R.integer.pref_default_color));

        this.type = type;
    }

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
        text = rollCheck + DefCheck.divider + rollCount;
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

    public void setStatus() {
        status = !status;
    }

    public int[] getCheck() {
        int[] check = new int[2];

        if (type == DefType.roll) {
            String[] split = text.split(DefCheck.divider);
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
