package sgtmelon.scriptum.office.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.text.TextUtils
import android.widget.Toast
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.model.item.NoteItem
import sgtmelon.scriptum.app.model.item.RollItem
import sgtmelon.scriptum.app.model.item.SortItem
import sgtmelon.scriptum.app.model.key.NoteType
import sgtmelon.scriptum.app.room.RoomDb
import sgtmelon.scriptum.office.annot.def.SortDef

object HelpUtils {

    /**
     * Копирование текста заметки в память
     */
    fun Context.copyToClipboard(noteItem: NoteItem) {
        var copyText = ""

        /**
         * Если есть название то добавляем его
         */
        if (!TextUtils.isEmpty(noteItem.name)) {
            copyText = noteItem.name + "\n"
        }

        /**
         * В зависимости от типа составляем текст
         */
        when (noteItem.type) {
            NoteType.TEXT -> copyText += noteItem.text
            NoteType.ROLL -> {
                val db = RoomDb.getInstance(this)
                copyText = db.daoRoll().getText(noteItem.id)
                db.close()
            }
        }

        /**
         * Сохраняем данные в память
         */
        val clipboard = this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("NoteText", copyText) // TODO: 02.11.2018 вынеси

        clipboard.primaryClip = clip

        Toast.makeText(this, this.getString(R.string.toast_text_copy), Toast.LENGTH_SHORT).show()
    }

    object Note {

        fun MutableList<RollItem>.getCheck(): Int {
            var rollCheck = 0
            this.forEach { if (it.isCheck) rollCheck++ }
            return rollCheck
        }

        fun MutableList<RollItem>.isAllCheck(): Boolean {
            if (this.isEmpty()) return false

            this.forEach { if (!it.isCheck) return false }

            return true
        }

    }

    object Sort {

        /**
         * Получаем строку сортировки, [this] - список моделей из диалога
         */
        fun MutableList<SortItem>.getSort(): String {
            val order = StringBuilder()

            for (i in indices) {
                order.append(Integer.toString(this[i].key))
                if (i != this.size - 1) {
                    order.append(SortDef.divider)
                }
            }

            return order.toString()
        }

        fun getSortEqual(keys1: String, keys2: String): Boolean {
            val keysArr1 = keys1.split(SortDef.divider.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val keysArr2 = keys2.split(SortDef.divider.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            for (i in keysArr1.indices) {
                if (keysArr1[i] != keysArr2[i]) {
                    return false
                }

                if (keysArr1[i] == Integer.toString(SortDef.create) || keysArr1[i] == Integer.toString(SortDef.change)) {
                    break
                }
            }

            return true
        }

    }

}