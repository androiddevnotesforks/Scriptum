package sgtmelon.scriptum.app.model.item

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.model.NoteModel
import sgtmelon.scriptum.app.model.key.DbField.Note
import sgtmelon.scriptum.app.model.key.NoteType
import sgtmelon.scriptum.app.room.converter.BoolConverter
import sgtmelon.scriptum.app.room.converter.NoteTypeConverter
import sgtmelon.scriptum.app.room.converter.StringConverter
import sgtmelon.scriptum.office.annot.def.ColorDef
import java.util.*

/**
 * Элемент информации о заметки для [NoteModel]
 *
 * @author SerjantArbuz
 */
@Entity(tableName = Note.TABLE)
@TypeConverters(BoolConverter::class, StringConverter::class, NoteTypeConverter::class)
class NoteItem(
        @ColumnInfo(name = Note.ID) @PrimaryKey(autoGenerate = true) var id: Long = 0,
        @ColumnInfo(name = Note.CREATE) var create: String = "",
        @ColumnInfo(name = Note.CHANGE) var change: String = "",
        @ColumnInfo(name = Note.NAME) var name: String = "",
        @ColumnInfo(name = Note.TEXT) var text: String = "",
        @ColumnInfo(name = Note.COLOR) @ColorDef var color: Int = 0,
        @ColumnInfo(name = Note.TYPE) var type: NoteType = NoteType.TEXT,
        @ColumnInfo(name = Note.RANK_PS) var rankPs: MutableList<Long> = ArrayList(),
        @ColumnInfo(name = Note.RANK_ID) var rankId: MutableList<Long> = ArrayList(),
        @ColumnInfo(name = Note.BIN) var isBin: Boolean = false,
        @ColumnInfo(name = Note.STATUS) var isStatus: Boolean = false
) {

    // TODO переделать
    val check: IntArray
        get() {
            val check = intArrayOf(-1, 0)

            if (type !== NoteType.ROLL) return check

            val split = text.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (split.size == 2) {
                for (i in 0..1) {
                    check[i] = Integer.parseInt(split[i])
                }
            }

            return check
        }

    val isAllCheck: Boolean get() = with(check) { get(0) == get(1) }

    fun getStatusName(context: Context): String = if (name.isEmpty()) context.getString(R.string.hint_view_name) else name

    fun setCompleteText(check: Int, size: Int) {
        text = "$check/$size"
    }

    fun splitTextForRoll() = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()

}