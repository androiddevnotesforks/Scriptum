package sgtmelon.scriptum.factory

import android.content.Context
import androidx.fragment.app.FragmentManager
import sgtmelon.safedialog.MessageDialog
import sgtmelon.safedialog.MultiplyDialog
import sgtmelon.safedialog.OptionsDialog
import sgtmelon.safedialog.SingleDialog
import sgtmelon.safedialog.time.DateDialog
import sgtmelon.safedialog.time.TimeDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.dialog.*
import sgtmelon.scriptum.model.key.NoteType

/**
 * Factory for create/get dialogs
 *
 * @author SerjantArbuz
 */
object DialogFactory {

    object Main {
        fun getRenameDialog(fm: FragmentManager?): RenameDialog =
                fm?.findFragmentByTag(RENAME) as? RenameDialog ?: RenameDialog()

        fun getAddDialog(fm: FragmentManager?): SheetAddDialog =
                fm?.findFragmentByTag(ADD) as? SheetAddDialog ?: SheetAddDialog()

        fun getOptionsDialog(fm: FragmentManager?): OptionsDialog =
                fm?.findFragmentByTag(OPTIONS) as? OptionsDialog ?: OptionsDialog()

        fun getClearBinDialog(context: Context?, fm: FragmentManager?): MessageDialog {
            val dialog = fm?.findFragmentByTag(CLEAR_BIN) as? MessageDialog ?: MessageDialog()

            if (context == null) return dialog

            dialog.title = context.getString(R.string.dialog_title_clear_bin)
            dialog.message = context.getString(R.string.dialog_text_clear_bin)

            return dialog
        }

        private const val PREFIX = "DIALOG_MAIN"

        const val RENAME = "${PREFIX}_RENAME"
        const val ADD = "${PREFIX}_ADD"
        const val OPTIONS = "${PREFIX}_OPTIONS"
        const val CLEAR_BIN = "${PREFIX}_CLEAR_BIN"
    }

    class Note(private val context: Context?, private val fm: FragmentManager?) {

        fun getConvertDialog(type: NoteType): MessageDialog {
            val dialog = fm?.findFragmentByTag(CONVERT) as? MessageDialog ?: MessageDialog()

            if (context == null) return dialog

            dialog.title = context.getString(R.string.dialog_title_convert)
            dialog.message = when (type) {
                NoteType.TEXT -> context.getString(R.string.dialog_text_convert_to_roll)
                NoteType.ROLL -> context.getString(R.string.dialog_roll_convert_to_text)
            }

            return dialog
        }

        fun getRankDialog(): SingleDialog {
            val dialog = fm?.findFragmentByTag(RANK) as? SingleDialog ?: SingleDialog()

            if (context == null) return dialog

            dialog.title = context.getString(R.string.dialog_title_rank)

            return dialog
        }

        fun getColorDialog(): ColorDialog {
            val dialog = fm?.findFragmentByTag(COLOR) as? ColorDialog ?: ColorDialog()

            if (context == null) return dialog

            dialog.title = context.getString(R.string.dialog_title_color)

            return dialog
        }

        fun getDateDialog(): DateDialog =
                fm?.findFragmentByTag(DATE) as? DateDialog ?: DateDialog()

        fun getTimeDialog(): TimeDialog =
                fm?.findFragmentByTag(TIME) as? TimeDialog ?: TimeDialog()

        companion object {
            private const val PREFIX = "DIALOG_NOTE"

            const val DATE = "${PREFIX}_DATE"
            const val TIME = "${PREFIX}_TIME"
            const val CONVERT = "${PREFIX}_CONVERT"

            const val RANK = "${PREFIX}_RANK"
            const val COLOR = "${PREFIX}_COLOR"
        }

    }

    class Preference(private val context: Context?, private val fm: FragmentManager?) {

        fun getThemeDialog(): SingleDialog {
            val dialog = fm?.findFragmentByTag(THEME) as? SingleDialog ?: SingleDialog()

            if (context == null) return dialog

            dialog.title = context.getString(R.string.title_app_theme)
            dialog.itemArray = context.resources.getStringArray(R.array.text_app_theme)

            return dialog
        }

        fun getRepeatDialog(): SingleDialog {
            val dialog = fm?.findFragmentByTag(REPEAT) as? SingleDialog ?: SingleDialog()

            if (context == null) return dialog

            dialog.title = context.getString(R.string.title_alarm_repeat)
            dialog.itemArray = context.resources.getStringArray(R.array.text_alarm_repeat)

            return dialog
        }

        fun getSignalDialog(): MultiplyDialog {
            val dialog = fm?.findFragmentByTag(SIGNAL) as? MultiplyDialog ?: MultiplyDialog()

            if (context == null) return dialog

            dialog.needOneSelect = true
            dialog.title = context.getString(R.string.title_alarm_signal)
            dialog.itemList = context.resources.getStringArray(R.array.text_alarm_signal).toList()

            return dialog
        }

        fun getMelodyDialog(): SingleDialog {
            val dialog = fm?.findFragmentByTag(MELODY) as? SingleDialog ?: SingleDialog()

            if (context == null) return dialog

            dialog.title = context.getString(R.string.title_alarm_melody)

            return dialog
        }

        fun getVolumeDialog(): VolumeDialog {
            val dialog = fm?.findFragmentByTag(VOLUME) as? VolumeDialog ?: VolumeDialog()

            if (context == null) return dialog

            dialog.title = context.getString(R.string.title_alarm_volume)

            return dialog
        }

        fun getSortDialog(): SingleDialog {
            val dialog = fm?.findFragmentByTag(SORT) as? SingleDialog ?: SingleDialog()

            if (context == null) return dialog

            dialog.title = context.getString(R.string.title_note_sort)
            dialog.itemArray = context.resources.getStringArray(R.array.text_note_sort)

            return dialog
        }

        fun getColorDialog(): ColorDialog {
            val dialog = fm?.findFragmentByTag(COLOR) as? ColorDialog ?: ColorDialog()

            if (context == null) return dialog

            dialog.title = context.getString(R.string.title_note_color)

            return dialog
        }

        fun getSaveTimeDialog(): SingleDialog {
            val dialog = fm?.findFragmentByTag(SAVE_TIME) as? SingleDialog ?: SingleDialog()

            if (context == null) return dialog

            dialog.title = context.getString(R.string.title_save_time)
            dialog.itemArray = context.resources.getStringArray(R.array.text_save_time)

            return dialog
        }

        fun getAboutDialog(): AboutDialog =
                fm?.findFragmentByTag(ABOUT) as? AboutDialog ?: AboutDialog()

        companion object {
            private const val PREFIX = "DIALOG_PREF"

            const val THEME = "${PREFIX}_THEME"
            const val REPEAT = "${PREFIX}_REPEAT"
            const val SIGNAL = "${PREFIX}_SIGNAL"
            const val MELODY = "${PREFIX}_MELODY"
            const val VOLUME = "${PREFIX}_VOLUME"
            const val SORT = "${PREFIX}_SORT"
            const val COLOR = "${PREFIX}_COLOR"
            const val SAVE_TIME = "${PREFIX}_SAVE_TIME"
            const val ABOUT = "${PREFIX}_ABOUT"
        }

    }

}