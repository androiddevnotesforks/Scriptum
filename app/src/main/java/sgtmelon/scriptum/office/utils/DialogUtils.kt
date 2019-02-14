package sgtmelon.scriptum.office.utils

import android.content.Context
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.model.item.NoteItem
import sgtmelon.scriptum.office.annot.key.NoteType

object DialogUtils {

    fun fillOptionsDialog(context: Context, noteItem: NoteItem): Array<String> {
        val items: Array<String>

        when (noteItem.type) {
            NoteType.TEXT -> {
                items = context.resources.getStringArray(R.array.dialog_menu_text)

                items[0] = when (noteItem.isStatus) {
                    true -> context.getString(R.string.dialog_menu_status_unbind)
                    false -> context.getString(R.string.dialog_menu_status_bind)
                }
            }
            NoteType.ROLL -> {
                items = context.resources.getStringArray(R.array.dialog_menu_roll)

                items[0] = when (noteItem.isAllCheck) {
                    true -> context.getString(R.string.dialog_menu_check_zero)
                    false -> context.getString(R.string.dialog_menu_check_all)
                }

                items[1] = when (noteItem.isStatus) {
                    true -> context.getString(R.string.dialog_menu_status_unbind)
                    false -> context.getString(R.string.dialog_menu_status_bind)
                }

            }
        }

        return items
    }

}