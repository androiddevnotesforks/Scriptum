package sgtmelon.scriptum.factory

import android.content.Context
import androidx.fragment.app.FragmentManager
import sgtmelon.safedialog.MessageDialog
import sgtmelon.safedialog.MultiplyDialog
import sgtmelon.safedialog.OptionsDialog
import sgtmelon.safedialog.SingleDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.dialog.*
import sgtmelon.scriptum.model.key.NoteType

/**
 * Фабрика для создания диалогов
 *
 * @author SerjantArbuz
 */
object DialogFactory {

    fun getRenameDialog(fm: FragmentManager?): RenameDialog =
            fm?.findFragmentByTag(Key.RENAME) as? RenameDialog ?: RenameDialog()

    fun getSheetDialog(fm: FragmentManager?): SheetAddDialog =
            fm?.findFragmentByTag(Key.SHEET) as? SheetAddDialog ?: SheetAddDialog()

    fun getOptionsDialog(fm: FragmentManager?): OptionsDialog =
            fm?.findFragmentByTag(Key.OPTIONS) as? OptionsDialog ?: OptionsDialog()

    fun getClearBinDialog(context: Context, fm: FragmentManager?): MessageDialog {
        val dialog = fm?.findFragmentByTag(Key.CLEAR_BIN) as? MessageDialog ?: MessageDialog()

        dialog.title = context.getString(R.string.dialog_title_clear_bin)
        dialog.message = context.getString(R.string.dialog_text_clear_bin)

        return dialog
    }

    fun getRankDialog(context: Context, fm: FragmentManager?): MultiplyDialog {
        val dialog = fm?.findFragmentByTag(Key.RANK) as? MultiplyDialog ?: MultiplyDialog()

        dialog.title = context.getString(R.string.dialog_title_rank)

        return dialog
    }

    fun getColorDialog(fm: FragmentManager?): ColorDialog =
            fm?.findFragmentByTag(Key.COLOR) as? ColorDialog ?: ColorDialog()

    fun getConvertDialog(context: Context, fm: FragmentManager?, type: NoteType): MessageDialog {
        val dialog = fm?.findFragmentByTag(Key.CONVERT) as? MessageDialog ?: MessageDialog()

        dialog.title = context.getString(R.string.dialog_title_convert)
        dialog.message = when (type) {
            NoteType.TEXT -> context.getString(R.string.dialog_text_convert_to_roll)
            NoteType.ROLL -> context.getString(R.string.dialog_roll_convert_to_text)
        }

        return dialog
    }

    fun getSortDialog(fm: FragmentManager?): SortDialog =
            fm?.findFragmentByTag(Key.SORT) as? SortDialog ?: SortDialog()

    fun getSaveTimeDialog(context: Context, fm: FragmentManager?): SingleDialog {
        val dialog = fm?.findFragmentByTag(Key.SAVE_TIME) as? SingleDialog ?: SingleDialog()

        dialog.title = context.getString(R.string.pref_save_time_title)
        dialog.rows = context.resources.getStringArray(R.array.pref_save_time_text)

        return dialog
    }

    fun getThemeDialog(context: Context, fm: FragmentManager?): SingleDialog {
        val dialog = fm?.findFragmentByTag(Key.THEME) as? SingleDialog ?: SingleDialog()

        dialog.title = context.getString(R.string.pref_theme_title)
        dialog.rows = context.resources.getStringArray(R.array.pref_theme_text)

        return dialog
    }

    fun getInfoDialog(fm: FragmentManager?): InfoDialog =
            fm?.findFragmentByTag(Key.INFO) as? InfoDialog ?: InfoDialog()

    object Key {
        private const val PREFIX = "DIALOG"

        const val RENAME = "${PREFIX}_RENAME"
        const val SHEET = "${PREFIX}_SHEET"
        const val OPTIONS = "${PREFIX}_OPTIONS"
        const val CLEAR_BIN = "${PREFIX}_CLEAR_BIN"

        const val CONVERT = "${PREFIX}_CONVERT"
        const val RANK = "${PREFIX}_RANK"
        const val COLOR = "${PREFIX}_COLOR"

        const val SORT = "${PREFIX}_SORT"
        const val SAVE_TIME = "${PREFIX}_SAVE_TIME"
        const val THEME = "${PREFIX}_THEME"
        const val INFO = "${PREFIX}_INFO"
    }

}