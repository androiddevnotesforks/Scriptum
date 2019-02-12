package sgtmelon.scriptum.office.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.database.RoomDb
import sgtmelon.scriptum.app.model.item.NoteItem
import sgtmelon.scriptum.app.model.item.RollItem
import sgtmelon.scriptum.app.model.item.SortItem
import sgtmelon.scriptum.office.annot.def.NoteType
import sgtmelon.scriptum.office.annot.def.SortDef

object HelpUtils {

    /**
     * Копирование текста заметки в память, [noteItem] - заметка для копирования
     */
    fun optionsCopy(context: Context, noteItem: NoteItem) {
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
                val db = RoomDb.provideDb(context)
                copyText = db.daoRoll().getText(noteItem.id)
                db.close()
            }
        }

        /**
         * Сохраняем данные в память
         */
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("NoteText", copyText) // TODO: 02.11.2018 вынеси

        clipboard.primaryClip = clip

        Toast.makeText(context, context.getString(R.string.toast_text_copy), Toast.LENGTH_SHORT).show()
    }

    /**
     * Скрыть клавиатуру, где [view] - текущий фокус
     */
    fun hideKeyboard(context: Context, view: View?) {
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE)
                as InputMethodManager

        if (view != null) {
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    object Note {

        /**
         * [listRoll] - Список для проверки
         */
        fun getCheckCount(listRoll: List<RollItem>): Int {
            var rollCheck = 0

            for (rollItem in listRoll) {
                if (rollItem.isCheck) {
                    rollCheck++
                }
            }

            return rollCheck
        }

        /**
         * [listRoll] - Список для проверки
         */
        fun isAllCheck(listRoll: List<RollItem>): Boolean {
            if (listRoll.isEmpty()) return false

            for (rollItem in listRoll) {
                if (!rollItem.isCheck) {
                    return false
                }
            }

            return true
        }

    }

    object Sort {

        /**
         * Получаем строку сортировки, [listSort] - список моделей из диалога
         */
        fun getSortByList(listSort: List<SortItem>): String {
            val order = StringBuilder()

            for (i in listSort.indices) {
                order.append(Integer.toString(listSort[i].key))
                if (i != listSort.size - 1) {
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