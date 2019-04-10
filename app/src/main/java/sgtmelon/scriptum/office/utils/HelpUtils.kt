package sgtmelon.scriptum.office.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import sgtmelon.scriptum.R
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.item.RollItem
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.repository.RoomRepo

object HelpUtils {

    /**
     * Копирование текста заметки в память
     */
    fun Context.copyToClipboard(noteItem: NoteItem) {
        var copyText = ""

        /**
         * Если есть название то добавляем его
         */
        if (noteItem.name.isNotEmpty()) copyText = noteItem.name + "\n"

        /**
         * В зависимости от типа составляем текст
         */
        copyText += when (noteItem.type) {
            NoteType.TEXT -> noteItem.text
            NoteType.ROLL -> RoomRepo.getInstance(context = this).getRollListString(noteItem)
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

}