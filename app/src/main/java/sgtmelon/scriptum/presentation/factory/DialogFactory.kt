package sgtmelon.scriptum.presentation.factory

import android.content.Context
import androidx.fragment.app.FragmentManager
import sgtmelon.safedialog.*
import sgtmelon.safedialog.time.DateDialog
import sgtmelon.safedialog.time.TimeDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.domain.model.key.NoteType
import sgtmelon.scriptum.presentation.dialog.*

/**
 * Factory for create/get dialogs
 */
object DialogFactory {

    class Alarm(private val fm: FragmentManager) {
        fun getRepeatDialog(): SheetRepeatDialog {
            return fm.findFragmentByTag(REPEAT) as? SheetRepeatDialog ?: SheetRepeatDialog()
        }

        companion object {
            private const val PREFIX = "DIALOG_ALARM"

            const val REPEAT = "${PREFIX}_REPEAT"
        }
    }

    class Main(private val context: Context?, private val fm: FragmentManager) {
        fun getRenameDialog(): RenameDialog {
            return fm.findFragmentByTag(RENAME) as? RenameDialog ?: RenameDialog()
        }

        fun getAddDialog(): SheetAddDialog {
            return fm.findFragmentByTag(ADD) as? SheetAddDialog ?: SheetAddDialog()
        }

        fun getOptionsDialog(): OptionsDialog {
            return fm.findFragmentByTag(OPTIONS) as? OptionsDialog ?: OptionsDialog()
        }

        fun getDateDialog(): DateDialog = fm.findFragmentByTag(DATE) as? DateDialog ?: DateDialog()

        fun getTimeDialog(): TimeDialog = fm.findFragmentByTag(TIME) as? TimeDialog ?: TimeDialog()

        fun getClearBinDialog(): MessageDialog {
            val dialog = fm.findFragmentByTag(CLEAR_BIN) as? MessageDialog ?: MessageDialog()

            if (context == null) return dialog

            dialog.type = MessageType.CHOICE
            dialog.title = context.getString(R.string.dialog_title_clear_bin)
            dialog.message = context.getString(R.string.dialog_text_clear_bin)

            return dialog
        }

        companion object {
            private const val PREFIX = "DIALOG_MAIN"

            const val RENAME = "${PREFIX}_RENAME"

            const val ADD = "${PREFIX}_ADD"
            const val OPTIONS = "${PREFIX}_OPTIONS"
            const val DATE = "${PREFIX}_DATE"
            const val TIME = "${PREFIX}_TIME"

            const val CLEAR_BIN = "${PREFIX}_CLEAR_BIN"
        }
    }

    class Note(private val context: Context?, private val fm: FragmentManager) {

        fun getConvertDialog(type: NoteType): MessageDialog {
            val dialog = fm.findFragmentByTag(CONVERT) as? MessageDialog ?: MessageDialog()

            if (context == null) return dialog

            dialog.type = MessageType.CHOICE
            dialog.title = context.getString(R.string.dialog_title_convert)
            dialog.message = when (type) {
                NoteType.TEXT -> context.getString(R.string.dialog_text_convert_text)
                NoteType.ROLL -> context.getString(R.string.dialog_roll_convert_roll)
            }

            return dialog
        }

        fun getRankDialog(): SingleDialog {
            val dialog = fm.findFragmentByTag(RANK) as? SingleDialog ?: SingleDialog()

            if (context == null) return dialog

            dialog.title = context.getString(R.string.dialog_title_rank)

            return dialog
        }

        fun getColorDialog(): ColorDialog {
            val dialog = fm.findFragmentByTag(COLOR) as? ColorDialog ?: ColorDialog()

            if (context == null) return dialog

            dialog.title = context.getString(R.string.dialog_title_color)

            return dialog
        }

        fun getDateDialog(): DateDialog = fm.findFragmentByTag(DATE) as? DateDialog ?: DateDialog()

        fun getTimeDialog(): TimeDialog = fm.findFragmentByTag(TIME) as? TimeDialog ?: TimeDialog()

        companion object {
            private const val PREFIX = "DIALOG_NOTE"

            const val DATE = "${PREFIX}_DATE"
            const val TIME = "${PREFIX}_TIME"
            const val CONVERT = "${PREFIX}_CONVERT"

            const val RANK = "${PREFIX}_RANK"
            const val COLOR = "${PREFIX}_COLOR"
        }

    }

    class Preference(private val context: Context?, private val fm: FragmentManager) {

        fun getThemeDialog(): SingleDialog {
            val dialog = fm.findFragmentByTag(THEME) as? SingleDialog ?: SingleDialog()

            if (context == null) return dialog

            dialog.title = context.getString(R.string.pref_title_app_theme)
            dialog.itemArray = context.resources.getStringArray(R.array.pref_text_app_theme)

            return dialog
        }


        fun getImportPermissionDialog(): MessageDialog {
            val dialog  = fm.findFragmentByTag(IMPORT_PERMISSION) as? MessageDialog
                    ?: MessageDialog()

            if (context == null) return dialog

            dialog.type = MessageType.INFO
            dialog.title = context.getString(R.string.dialog_title_import_permission)
            dialog.message = context.getString(R.string.dialog_text_import_permission)

            return dialog
        }

        fun getImportDialog(): SingleDialog {
            val dialog = fm.findFragmentByTag(IMPORT) as? SingleDialog ?: SingleDialog()

            if (context == null) return dialog

            dialog.applyEnable = true
            dialog.title = context.getString(R.string.dialog_title_import)

            return dialog
        }


        fun getSortDialog(): SingleDialog {
            val dialog = fm.findFragmentByTag(SORT) as? SingleDialog ?: SingleDialog()

            if (context == null) return dialog

            dialog.title = context.getString(R.string.pref_title_note_sort)
            dialog.itemArray = context.resources.getStringArray(R.array.pref_text_note_sort)

            return dialog
        }

        fun getColorDialog(): ColorDialog {
            val dialog = fm.findFragmentByTag(COLOR) as? ColorDialog ?: ColorDialog()

            if (context == null) return dialog

            dialog.title = context.getString(R.string.pref_title_note_color)

            return dialog
        }

        fun getSavePeriodDialog(): SingleDialog {
            val dialog = fm.findFragmentByTag(SAVE_PERIOD) as? SingleDialog ?: SingleDialog()

            if (context == null) return dialog

            dialog.title = context.getString(R.string.pref_title_note_save_period)
            dialog.itemArray = context.resources.getStringArray(R.array.pref_text_note_save_period)

            return dialog
        }


        fun getRepeatDialog(): SingleDialog {
            val dialog = fm.findFragmentByTag(REPEAT) as? SingleDialog ?: SingleDialog()

            if (context == null) return dialog

            dialog.title = context.getString(R.string.pref_title_alarm_repeat)
            dialog.itemArray = context.resources.getStringArray(R.array.pref_text_alarm_repeat)

            return dialog
        }

        fun getSignalDialog(): MultipleDialog {
            val dialog = fm.findFragmentByTag(SIGNAL) as? MultipleDialog ?: MultipleDialog()

            if (context == null) return dialog

            dialog.needOneSelect = true
            dialog.title = context.getString(R.string.pref_title_alarm_signal)
            dialog.itemList = context.resources.getStringArray(R.array.pref_text_alarm_signal).toList()

            return dialog
        }

        fun getMelodyPermissionDialog(): MessageDialog {
            val dialog  = fm.findFragmentByTag(MELODY_PERMISSION) as? MessageDialog
                    ?: MessageDialog()

            if (context == null) return dialog

            dialog.type = MessageType.INFO
            dialog.title = context.getString(R.string.dialog_title_melody_permission)
            dialog.message = context.getString(R.string.dialog_text_melody_permission)

            return dialog
        }

        fun getMelodyDialog(): SingleDialog {
            val dialog = fm.findFragmentByTag(MELODY) as? SingleDialog ?: SingleDialog()

            if (context == null) return dialog

            dialog.title = context.getString(R.string.pref_title_alarm_melody)

            return dialog
        }

        fun getVolumeDialog(): VolumeDialog {
            val dialog = fm.findFragmentByTag(VOLUME) as? VolumeDialog ?: VolumeDialog()

            if (context == null) return dialog

            dialog.title = context.getString(R.string.pref_title_alarm_volume)

            return dialog
        }


        fun getAboutDialog(): AboutDialog {
            return fm.findFragmentByTag(ABOUT) as? AboutDialog ?: AboutDialog()
        }

        companion object {
            private const val PREFIX = "DIALOG_PREF"

            const val THEME = "${PREFIX}_THEME"

            const val IMPORT_PERMISSION = "${PREFIX}_IMPORT_PERMISSION"
            const val IMPORT = "${PREFIX}_IMPORT"

            const val SORT = "${PREFIX}_SORT"
            const val COLOR = "${PREFIX}_COLOR"
            const val SAVE_PERIOD = "${PREFIX}_SAVE_PERIOD"

            const val REPEAT = "${PREFIX}_REPEAT"
            const val SIGNAL = "${PREFIX}_SIGNAL"
            const val MELODY_PERMISSION = "${PREFIX}_MELODY_PERMISSION"
            const val MELODY = "${PREFIX}_MELODY"
            const val VOLUME = "${PREFIX}_VOLUME"

            const val ABOUT = "${PREFIX}_ABOUT"
        }

    }

}