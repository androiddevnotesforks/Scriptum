package sgtmelon.scriptum.app.factory

import android.content.Context
import android.util.TypedValue
import androidx.fragment.app.FragmentManager
import sgtmelon.safedialog.library.*
import sgtmelon.safedialog.library.color.ColorDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.element.InfoDialog
import sgtmelon.scriptum.element.SortDialog
import sgtmelon.scriptum.office.annot.def.DialogDef
import sgtmelon.scriptum.office.annot.def.ThemeDef
import sgtmelon.scriptum.office.data.ColorData
import sgtmelon.scriptum.office.utils.PrefUtils

object DialogFactory {

    fun getRenameDialog(context: Context, fm: FragmentManager?): RenameDialog {
        val dialog = fm?.findFragmentByTag(DialogDef.RENAME) as RenameDialog? ?: RenameDialog()

        val theme = context.theme
        val attrs = TypedValue()

        theme.resolveAttribute(R.attr.clContent, attrs, true)
        dialog.colorText = attrs.data

        theme.resolveAttribute(R.attr.clDisable, attrs, true)
        dialog.colorHint = attrs.data

        dialog.textHint = context.getString(R.string.hint_enter_rank_rename)
        dialog.textLength = context.resources.getInteger(R.integer.length_note_name)

        return dialog
    }

    fun getSheetDialog(fm: FragmentManager?): SheetDialog =
            fm?.findFragmentByTag(DialogDef.SHEET) as SheetDialog? ?: SheetDialog()

    fun getOptionsDialog(fm: FragmentManager?): OptionsDialog =
            fm?.findFragmentByTag(DialogDef.OPTIONS) as OptionsDialog? ?: OptionsDialog()

    fun getClearBinDialog(context: Context, fm: FragmentManager?): MessageDialog {
        val dialog = fm?.findFragmentByTag(DialogDef.CLEAR_BIN) as MessageDialog? ?: MessageDialog()

        dialog.title = context.getString(R.string.dialog_title_clear_bin)
        dialog.message = context.getString(R.string.dialog_text_clear_bin)

        return dialog
    }

    fun getConvertDialog(context: Context, fm: FragmentManager?): MessageDialog {
        val dialog = fm?.findFragmentByTag(DialogDef.CONVERT) as MessageDialog? ?: MessageDialog()

        dialog.title = context.getString(R.string.dialog_title_convert)

        return dialog
    }

    fun getRankDialog(context: Context, fm: FragmentManager?): MultiplyDialog {
        val dlgRank = fm?.findFragmentByTag(DialogDef.RANK) as MultiplyDialog? ?: MultiplyDialog()

        dlgRank.title = context.getString(R.string.dialog_title_rank)

        return dlgRank
    }

    fun getColorDialog(context: Context, fm: FragmentManager?): ColorDialog {
        val dialog = fm?.findFragmentByTag(DialogDef.COLOR) as ColorDialog? ?: ColorDialog()

        when (PrefUtils(context).theme) {
            ThemeDef.light -> {
                dialog.fillColor = ColorData.light
                dialog.strokeColor = ColorData.dark
                dialog.checkColor = ColorData.dark
            }
            ThemeDef.dark -> {
                dialog.fillColor = ColorData.dark
                dialog.strokeColor = ColorData.dark
                dialog.checkColor = ColorData.light
            }
        }

        dialog.columnCount = context.resources.getInteger(R.integer.column_color)

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