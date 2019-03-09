package sgtmelon.scriptum.app.model.item

import android.content.Context
import android.text.TextUtils
import androidx.room.*
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.model.NoteModel
import sgtmelon.scriptum.app.model.key.NoteType
import sgtmelon.scriptum.app.room.converter.BoolConverter
import sgtmelon.scriptum.app.room.converter.NoteTypeConverter
import sgtmelon.scriptum.app.room.converter.StringConverter
import sgtmelon.scriptum.office.annot.DbAnn
import sgtmelon.scriptum.office.annot.def.ColorDef
import java.util.*

/**
 * Элемент списка заметок [NoteModel]
 */
@Entity(tableName = DbAnn.Note.TABLE)
@TypeConverters(BoolConverter::class, StringConverter::class, NoteTypeConverter::class)
class NoteItem {

    @ColumnInfo(name = DbAnn.Note.ID) @PrimaryKey(autoGenerate = true) var id: Long = 0

    @ColumnInfo(name = DbAnn.Note.CREATE) var create = ""
    @ColumnInfo(name = DbAnn.Note.CHANGE) var change = ""

    @ColumnInfo(name = DbAnn.Note.NAME) var name = ""
    @ColumnInfo(name = DbAnn.Note.TEXT) var text = ""

    @ColumnInfo(name = DbAnn.Note.COLOR) @get:ColorDef var color: Int = 0
    @ColumnInfo(name = DbAnn.Note.TYPE) var type: NoteType = NoteType.TEXT

    @ColumnInfo(name = DbAnn.Note.RANK_PS) var rankPs: List<Long> = ArrayList()
    @ColumnInfo(name = DbAnn.Note.RANK_ID) var rankId: List<Long> = ArrayList()

    @ColumnInfo(name = DbAnn.Note.BIN) var isBin = false
    @ColumnInfo(name = DbAnn.Note.STATUS) var isStatus = false

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

    constructor()

    @Ignore constructor(create: String, @ColorDef color: Int, type: NoteType) {
        this.create = create
        this.color = color
        this.type = type
    }

    fun getStatusName(context: Context): String = if (TextUtils.isEmpty(name)) context.getString(R.string.hint_view_name) else name

    fun setRollText(rollCheck: Int, rollCount: Int) {
        text = rollCheck.toString() + "/" + rollCount
    }

}