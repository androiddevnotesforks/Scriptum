package sgtmelon.scriptum.app.factory

import android.content.Context
import androidx.fragment.app.FragmentManager
import sgtmelon.safedialog.MessageDialog
import sgtmelon.safedialog.MultiplyDialog
import sgtmelon.safedialog.OptionsDialog
import sgtmelon.safedialog.SingleDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.app.model.key.NoteType
import sgtmelon.scriptum.dialog.*
import sgtmelon.scriptum.office.annot.def.DialogDef

object DialogFactory {

    fun getRenameDialog(fm: FragmentManager?): RenameDialog =
            fm?.findFragmentByTag(DialogDef.RENAME) as RenameDialog? ?: RenameDialog()

    fun getSheetDialog(fm: FragmentManager?): SheetAddDialog =
            fm?.findFragmentByTag(DialogDef.SHEET) as SheetAddDialog? ?: SheetAddDialog()

    fun getOptionsDialog(fm: FragmentManager?): OptionsDialog =
            fm?.findFragmentByTag(DialogDef.OPTIONS) as OptionsDialog? ?: OptionsDialog()

    fun getClearBinDialog(context: Context, fm: FragmentManager?): MessageDialog {
        val dialog = fm?.findFragmentByTag(DialogDef.CLEAR_BIN) as MessageDialog? ?: MessageDialog()

        dialog.title = context.getString(R.string.dialog_title_clear_bin)
        dialog.message = context.getString(R.string.dialog_text_clear_bin)

        return dialog
    }

    fun getRankDialog(context: Context, fm: FragmentManager?): MultiplyDialog {
        val dlgRank = fm?.findFragmentByTag(DialogDef.RANK) as MultiplyDialog? ?: MultiplyDialog()

        dlgRank.title = context.getString(R.string.dialog_title_rank)

        return dlgRank
    }

    fun getColorDialog(fm: FragmentManager?): ColorDialog =
            fm?.findFragmentByTag(DialogDef.COLOR) as ColorDialog? ?: ColorDialog()

    fun getConvertDialog(context: Context, fm: FragmentManager?, type: NoteType): MessageDialog {
        val dialog = fm?.findFragmentByTag(DialogDef.CONVERT) as MessageDialog? ?: MessageDialog()

        dialog.title = context.getString(R.string.dialog_title_convert)
        dialog.message = when (type) {
            NoteType.TEXT -> context.getString(R.string.dialog_text_convert_to_roll)
            NoteType.ROLL -> context.getString(R.string.dialog_roll_convert_to_text)
        }

        return dialog
    }

    fun getSortDialog(fm: FragmentManager?): SortDialog =
            fm?.findFragmentByTag(DialogDef.SORT) as SortDialog? ?: SortDialog()

    fun getSaveTimeDialog(context: Context, fm: FragmentManager?): SingleDialog {
        val dlgSaveTime = fm?.findFragmentByTag(DialogDef.SAVE_TIME) as SingleDialog?
                ?: SingleDialog()

        dlgSaveTime.title = context.getString(R.string.pref_save_time_title)
        dlgSaveTime.rows = context.resources.getStringArray(R.array.pref_save_time_text)

        return dlgSaveTime
    }

    fun getThemeDialog(context: Context, fm: FragmentManager?): SingleDialog {
        val dlgTheme = fm?.findFragmentByTag(DialogDef.THEME) as SingleDialog? ?: SingleDialog()

        dlgTheme.title = context.getString(R.string.pref_theme_title)
        dlgTheme.rows = context.resources.getStringArray(R.array.pref_theme_text)

        return dlgTheme
    }

    fun getInfoDialog(fm: FragmentManager?): InfoDialog =
            fm?.findFragmentByTag(DialogDef.INFO) as InfoDialog? ?: InfoDialog()

}