package sgtmelon.scriptum.app.model.item;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import sgtmelon.scriptum.app.model.NoteRepo;
import sgtmelon.scriptum.office.annot.DbAnn;
import sgtmelon.scriptum.office.conv.BoolConv;

import static androidx.room.ForeignKey.CASCADE;

/**
 * Элемент списка заметок {@link NoteRepo}
 */
@Entity(tableName = DbAnn.Roll.TABLE,
        foreignKeys = @ForeignKey(entity = NoteItem.class,
                parentColumns = DbAnn.Note.ID,
                childColumns = DbAnn.Roll.NOTE_ID,
                onUpdate = CASCADE,
                onDelete = CASCADE),
        indices = {@Index(DbAnn.Roll.NOTE_ID)})
@TypeConverters({BoolConv.class})
public final class RollItem {

    @ColumnInfo(name = DbAnn.Roll.ID)
    @PrimaryKey(autoGenerate = true)
    @Nullable
    private Long id;

    @ColumnInfo(name = DbAnn.Roll.NOTE_ID) private long noteId;
    @ColumnInfo(name = DbAnn.Roll.POSITION) private int position;
    @ColumnInfo(name = DbAnn.Roll.CHECK) private boolean check = false;
    @ColumnInfo(name = DbAnn.Roll.TEXT) private String text;

    public RollItem() {

    }

    public RollItem(String data) {
        try {
            final JSONObject jsonObject = new JSONObject(data);

            id = jsonObject.getLong(DbAnn.Roll.ID);
            noteId = jsonObject.getLong(DbAnn.Roll.NOTE_ID);
            position = jsonObject.getInt(DbAnn.Roll.POSITION);
            check = jsonObject.getBoolean(DbAnn.Roll.CHECK);
            text = jsonObject.getString(DbAnn.Roll.TEXT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    public Long getId() {
        return id;
    }

    public void setId(@NonNull Long id) {
        this.id = id;
    }

    public long getNoteId() {
        return noteId;
    }

    public void setNoteId(long noteId) {
        this.noteId = noteId;
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

    @NonNull
    public String getText() {
        return text;
    }

    public void setText(@NonNull String text) {
        this.text = text;
    }

    @NonNull
    @Override
    public String toString() {
        final JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(DbAnn.Roll.ID, id);
            jsonObject.put(DbAnn.Roll.NOTE_ID, noteId);
            jsonObject.put(DbAnn.Roll.POSITION, position);
            jsonObject.put(DbAnn.Roll.CHECK, check);
            jsonObject.put(DbAnn.Roll.TEXT, text);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }

}