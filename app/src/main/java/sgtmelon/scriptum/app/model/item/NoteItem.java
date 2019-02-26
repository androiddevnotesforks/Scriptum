package sgtmelon.scriptum.app.model.item;

import android.content.Context;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.model.NoteRepo;
import sgtmelon.scriptum.app.room.converter.BoolConverter;
import sgtmelon.scriptum.app.room.converter.NoteTypeConverter;
import sgtmelon.scriptum.app.room.converter.StringConverter;
import sgtmelon.scriptum.office.annot.DbAnn;
import sgtmelon.scriptum.office.annot.def.ColorDef;
import sgtmelon.scriptum.office.annot.key.NoteType;

/**
 * Элемент списка заметок {@link NoteRepo}
 */
@Entity(tableName = DbAnn.Note.TABLE)
@TypeConverters({BoolConverter.class, StringConverter.class, NoteTypeConverter.class})
public final class NoteItem {

    @ColumnInfo(name = DbAnn.Note.ID)
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = DbAnn.Note.CREATE) private String create;
    @ColumnInfo(name = DbAnn.Note.CHANGE) private String change;

    @ColumnInfo(name = DbAnn.Note.NAME) private String name = "";
    @ColumnInfo(name = DbAnn.Note.TEXT) private String text = "";

    @ColumnInfo(name = DbAnn.Note.COLOR) private int color;
    @ColumnInfo(name = DbAnn.Note.TYPE) private NoteType type;

    @ColumnInfo(name = DbAnn.Note.RANK_PS) private List<Long> rankPs = new ArrayList<>();
    @ColumnInfo(name = DbAnn.Note.RANK_ID) private List<Long> rankId = new ArrayList<>();

    @ColumnInfo(name = DbAnn.Note.BIN) private boolean bin = false;
    @ColumnInfo(name = DbAnn.Note.STATUS) private boolean status = false;

    public NoteItem() {

    }

    @Ignore
    public NoteItem(@NonNull String create, @ColorDef int color, @NonNull NoteType type) {
        this.create = create;
        this.color = color;
        this.type = type;
    }

    @Ignore
    public NoteItem(@NonNull String create, @NonNull String change, @NonNull String name,
                    @NonNull String text, int color, @NonNull NoteType type, @NonNull List<Long> rankPs,
                    @NonNull List<Long> rankId, boolean bin, boolean status) {
        this.create = create;
        this.change = change;
        this.name = name;
        this.text = text;
        this.color = color;
        this.type = type;
        this.rankPs = rankPs;
        this.rankId = rankId;
        this.bin = bin;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    public String getCreate() {
        return create;
    }

    public void setCreate(@NonNull String create) {
        this.create = create;
    }

    @NonNull
    public String getChange() {
        return change;
    }

    public void setChange(@NonNull String change) {
        this.change = change;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getName(Context context) {
        return TextUtils.isEmpty(name)
                ? context.getString(R.string.hint_view_name)
                : name;
    }

    @NonNull
    public String getText() {
        return text;
    }

    public void setText(@NonNull String text) {
        this.text = text;
    }

    public void setText(int rollCheck, int rollCount) {
        text = rollCheck + "/" + rollCount;
    }

    @ColorDef
    public int getColor() {
        return color;
    }

    public void setColor(@ColorDef int color) {
        this.color = color;
    }

    @NonNull
    public NoteType getType() {
        return type;
    }

    public void setType(@NonNull NoteType type) {
        this.type = type;
    }

    @NonNull
    public List<Long> getRankPs() {
        return rankPs;
    }

    public void setRankPs(@NonNull List<Long> rankPs) {
        this.rankPs = rankPs;
    }

    @NonNull
    public List<Long> getRankId() {
        return rankId;
    }

    public void setRankId(@NonNull List<Long> rankId) {
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

    @NonNull
    public int[] getCheck() {
        final int[] check = new int[]{-1, 0};

        if (type != NoteType.ROLL) return check;

        final String[] split = text.split("/");
        if (split.length == 2) {
            for (int i = 0; i < 2; i++) {
                check[i] = Integer.parseInt(split[i]);
            }
        }

        return check;
    }

    public boolean isAllCheck() {
        final int[] check = getCheck();
        return check[0] == check[1];
    }

}