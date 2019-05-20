package sgtmelon.scriptum.model.item

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.key.DbField.Note
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.office.annot.def.ColorDef
import sgtmelon.scriptum.room.converter.BoolConverter
import sgtmelon.scriptum.room.converter.NoteTypeConverter
import sgtmelon.scriptum.room.converter.StringConverter
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
                    check[i] = split[i].toInt()
                }
            }

            return check
        }

    val isAllCheck: Boolean get() = with(check) { get(0) == get(1) }

    fun setCompleteText(check: Int, size: Int) {
        text = "$check/$size"
    }

    fun splitTextForRoll() = text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()

}