package sgtmelon.scriptum.infrastructure.factory

import android.content.Context
import androidx.fragment.app.FragmentManager
import sgtmelon.safedialog.annotation.MessageType
import sgtmelon.safedialog.dialog.MessageDialog
import sgtmelon.safedialog.dialog.MultipleDialog
import sgtmelon.safedialog.dialog.OptionsDialog
import sgtmelon.safedialog.dialog.SingleDialog
import sgtmelon.safedialog.dialog.time.DateDialog
import sgtmelon.safedialog.dialog.time.TimeDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.presentation.dialog.AboutDialog
import sgtmelon.scriptum.cleanup.presentation.dialog.ColorDialog
import sgtmelon.scriptum.cleanup.presentation.dialog.LoadingDialog
import sgtmelon.scriptum.cleanup.presentation.dialog.RenameDialog
import sgtmelon.scriptum.cleanup.presentation.dialog.VolumeDialog
import sgtmelon.scriptum.cleanup.presentation.dialog.sheet.AddSheetDialog
import sgtmelon.scriptum.cleanup.presentation.dialog.sheet.RepeatSheetDialog
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.infrastructure.utils.getFragmentByTag

/**
 * Factory for create/get dialogs.
 */
object DialogFactory {

    class Alarm(private val fm: FragmentManager) {

        fun getRepeat(): RepeatSheetDialog = fm.getFragmentByTag(REPEAT) ?: RepeatSheetDialog()

        companion object {
            private const val PREFIX = "DIALOG_ALARM"

            const val REPEAT = "${PREFIX}_REPEAT"
        }
    }

    class Main(private val context: Context?, private val fm: FragmentManager) {

        fun getRename(): RenameDialog = fm.getFragmentByTag(RENAME) ?: RenameDialog()

        fun getAdd(): AddSheetDialog = fm.getFragmentByTag(ADD) ?: AddSheetDialog()

        fun getOptions(): OptionsDialog = fm.getFragmentByTag(OPTIONS) ?: OptionsDialog()

        fun getDate(): DateDialog = fm.getFragmentByTag(DATE) ?: DateDialog()

        fun getTime(): TimeDialog = fm.getFragmentByTag(TIME) ?: TimeDialog()

        fun getClearBin(): MessageDialog {
            val dialog = fm.getFragmentByTag(CLEAR_BIN) ?: MessageDialog()

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

        fun getConvert(type: NoteType): MessageDialog {
            val dialog = fm.getFragmentByTag(CONVERT) ?: MessageDialog()

            if (context == null) return dialog

            dialog.type = MessageType.CHOICE
            dialog.title = context.getString(R.string.dialog_title_convert)
            dialog.message = when (type) {
                NoteType.TEXT -> context.getString(R.string.dialog_text_convert_text)
                NoteType.ROLL -> context.getString(R.string.dialog_roll_convert_roll)
            }

            return dialog
        }

        fun getRank(): SingleDialog {
            val dialog = fm.getFragmentByTag(RANK) ?: SingleDialog()

            if (context == null) return dialog

            dialog.title = context.getString(R.string.dialog_title_rank)

            return dialog
        }

        fun getColor(): ColorDialog {
            val dialog = fm.getFragmentByTag(COLOR) ?: ColorDialog()

            if (context == null) return dialog

            dialog.title = context.getString(R.string.dialog_title_color)

            return dialog
        }

        fun getDate(): DateDialog = fm.getFragmentByTag(DATE) ?: DateDialog()

        fun getTime(): TimeDialog = fm.getFragmentByTag(TIME) ?: TimeDialog()

        companion object {
            private const val PREFIX = "DIALOG_NOTE"

            const val DATE = "${PREFIX}_DATE"
            const val TIME = "${PREFIX}_TIME"
            const val CONVERT = "${PREFIX}_CONVERT"
            const val RANK = "${PREFIX}_RANK"
            const val COLOR = "${PREFIX}_COLOR"
        }
    }

    object Preference {

        class Main(private val context: Context?, private val fm: FragmentManager) {

            fun getTheme(): SingleDialog {
                val dialog = fm.getFragmentByTag(THEME) ?: SingleDialog()

                if (context == null) return dialog

                dialog.title = context.getString(R.string.pref_title_app_theme)
                dialog.itemArray = context.resources.getStringArray(R.array.pref_theme)

                return dialog
            }

            fun getAbout(): AboutDialog = fm.getFragmentByTag(ABOUT) ?: AboutDialog()

            companion object {
                private const val PREFIX = "DIALOG_PREF_MAIN"

                const val THEME = "${PREFIX}_THEME"
                const val ABOUT = "${PREFIX}_ABOUT"
            }
        }

        class Backup(private val context: Context?, private val fm: FragmentManager) {

            fun getExportPermission(): MessageDialog {
                val dialog = fm.getFragmentByTag(EXPORT_PERMISSION) ?: MessageDialog()

                if (context == null) return dialog

                dialog.type = MessageType.INFO
                dialog.title = context.getString(R.string.dialog_title_export_permission)
                dialog.message = context.getString(R.string.dialog_text_export_permission)

                return dialog
            }

            fun getExportDeny(): MessageDialog {
                val dialog = fm.getFragmentByTag(EXPORT_DENY) ?: MessageDialog()

                if (context == null) return dialog

                dialog.type = MessageType.INFO
                dialog.title = context.getString(R.string.dialog_title_export_deny)
                dialog.message = context.getString(R.string.dialog_text_export_deny)

                return dialog
            }

            fun getLoading(): LoadingDialog = fm.getFragmentByTag(LOADING) ?: LoadingDialog()

            fun getImportPermission(): MessageDialog {
                val dialog = fm.getFragmentByTag(IMPORT_PERMISSION) ?: MessageDialog()

                if (context == null) return dialog

                dialog.type = MessageType.INFO
                dialog.title = context.getString(R.string.dialog_title_import_permission)
                dialog.message = context.getString(R.string.dialog_text_import_permission)

                return dialog
            }

            fun getImportDeny(): MessageDialog {
                val dialog = fm.getFragmentByTag(IMPORT_DENY) ?: MessageDialog()

                if (context == null) return dialog

                dialog.type = MessageType.INFO
                dialog.title = context.getString(R.string.dialog_title_import_deny)
                dialog.message = context.getString(R.string.dialog_text_import_deny)

                return dialog
            }

            fun getImport(): SingleDialog {
                val dialog = fm.getFragmentByTag(IMPORT) ?: SingleDialog()

                if (context == null) return dialog

                dialog.applyEnable = true
                dialog.title = context.getString(R.string.dialog_title_import)

                return dialog
            }

            companion object {
                private const val PREFIX = "DIALOG_PREF_BACKUP"

                const val EXPORT_PERMISSION = "${PREFIX}_EXPORT_PERMISSION"
                const val EXPORT_DENY = "${PREFIX}_EXPORT_DENY"
                const val LOADING = "${PREFIX}_LOADING"
                const val IMPORT_PERMISSION = "${PREFIX}_IMPORT_PERMISSION"
                const val IMPORT_DENY = "${PREFIX}_IMPORT_DENY"
                const val IMPORT = "${PREFIX}_IMPORT"
            }
        }

        class Notes(private val context: Context?, private val fm: FragmentManager) {

            fun getSort(): SingleDialog {
                val dialog = fm.getFragmentByTag(SORT) ?: SingleDialog()

                if (context == null) return dialog

                dialog.title = context.getString(R.string.pref_title_note_sort)
                dialog.itemArray = context.resources.getStringArray(R.array.pref_sort)

                return dialog
            }

            fun getColor(): ColorDialog {
                val dialog = fm.getFragmentByTag(COLOR) ?: ColorDialog()

                if (context == null) return dialog

                dialog.title = context.getString(R.string.pref_title_note_color)

                return dialog
            }

            fun getSavePeriod(): SingleDialog {
                val dialog = fm.getFragmentByTag(SAVE_PERIOD) ?: SingleDialog()

                if (context == null) return dialog

                dialog.title = context.getString(R.string.pref_title_note_save_period)
                dialog.itemArray = context.resources.getStringArray(R.array.pref_save_period)

                return dialog
            }

            companion object {
                private const val PREFIX = "DIALOG_PREF_NOTES"

                const val SORT = "${PREFIX}_SORT"
                const val COLOR = "${PREFIX}_COLOR"
                const val SAVE_PERIOD = "${PREFIX}_SAVE_PERIOD"
            }
        }

        class Alarm(private val context: Context?, private val fm: FragmentManager) {

            fun getRepeat(): SingleDialog {
                val dialog = fm.getFragmentByTag(REPEAT) ?: SingleDialog()

                if (context == null) return dialog

                dialog.title = context.getString(R.string.pref_title_alarm_repeat)
                dialog.itemArray = context.resources.getStringArray(R.array.pref_repeat)

                return dialog
            }

            fun getSignal(): MultipleDialog {
                val dialog = fm.getFragmentByTag(SIGNAL) ?: MultipleDialog()

                if (context == null) return dialog

                dialog.atLeastOne = true
                dialog.title = context.getString(R.string.pref_title_alarm_signal)
                dialog.itemArray = context.resources.getStringArray(R.array.pref_signal)

                return dialog
            }

            fun getMelodyAccess(): MessageDialog {
                val dialog = fm.getFragmentByTag(MELODY_ACCESS) ?: MessageDialog()

                if (context == null) return dialog

                dialog.type = MessageType.INFO
                dialog.title = context.getString(R.string.dialog_title_melody_permission)
                dialog.message = context.getString(R.string.dialog_text_melody_permission)

                return dialog
            }

            fun getMelody(): SingleDialog {
                val dialog = fm.getFragmentByTag(MELODY) ?: SingleDialog()

                if (context == null) return dialog

                dialog.title = context.getString(R.string.pref_title_alarm_melody)

                return dialog
            }

            fun getVolume(): VolumeDialog {
                val dialog = fm.getFragmentByTag(VOLUME) ?: VolumeDialog()

                if (context == null) return dialog

                dialog.title = context.getString(R.string.pref_title_alarm_volume)

                return dialog
            }

            companion object {
                private const val PREFIX = "DIALOG_PREF_ALARM"

                const val REPEAT = "${PREFIX}_REPEAT"
                const val SIGNAL = "${PREFIX}_SIGNAL"
                const val MELODY_ACCESS = "${PREFIX}_MELODY_ACCESS"
                const val MELODY = "${PREFIX}_MELODY"
                const val VOLUME = "${PREFIX}_VOLUME"
            }
        }
    }
}