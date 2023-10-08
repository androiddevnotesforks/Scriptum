package sgtmelon.scriptum.infrastructure.factory

import android.content.res.Resources
import sgtmelon.safedialog.annotation.MessageType
import sgtmelon.safedialog.dialog.MessageDialog
import sgtmelon.safedialog.dialog.MultipleDialog
import sgtmelon.safedialog.dialog.OptionsDialog
import sgtmelon.safedialog.dialog.SingleDialog
import sgtmelon.safedialog.dialog.time.DateDialog
import sgtmelon.safedialog.dialog.time.TimeDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.infrastructure.dialogs.AboutDialog
import sgtmelon.scriptum.infrastructure.dialogs.ColorDialog
import sgtmelon.scriptum.infrastructure.dialogs.LoadingDialog
import sgtmelon.scriptum.infrastructure.dialogs.RenameDialog
import sgtmelon.scriptum.infrastructure.dialogs.VolumeDialog
import sgtmelon.scriptum.infrastructure.dialogs.sheet.AddSheetDialog
import sgtmelon.scriptum.infrastructure.dialogs.sheet.RepeatSheetDialog
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType

/**
 * Factory for create/get dialogs.
 */
object DialogFactory {

    /** Abstract class for all places where permission needed. */
    abstract class Permissions(protected val resources: Resources) {

        fun getNotificationsDeny(): MessageDialog {
            val dialog = MessageDialog()
            dialog.type = MessageType.Info
            dialog.title = resources.getString(R.string.dialog_title_notification_deny)
            dialog.message = resources.getString(R.string.dialog_text_notification_deny)
            return dialog
        }
    }

    class Alarm {

        fun getRepeat(): RepeatSheetDialog = RepeatSheetDialog()

        companion object {
            private const val PREFIX = "DIALOG_ALARM"

            const val REPEAT = "${PREFIX}_REPEAT"
        }
    }

    class Main(resources: Resources) : Permissions(resources) {

        fun getNotificationsHelp(): MessageDialog {
            val dialog = MessageDialog()
            dialog.type = MessageType.Custom(
                positiveButton = R.string.dialog_notifications_settings,
                negativeButton = R.string.dialog_notifications_channel,
                neutralButton = R.string.dialog_notifications_done
            )
            dialog.title = resources.getString(R.string.dialog_title_notifications)
            dialog.message = resources.getString(R.string.dialog_text_notifications)
            dialog.isCancelable = false
            return dialog
        }

        fun getAdd(): AddSheetDialog = AddSheetDialog()

        fun getRename(): RenameDialog = RenameDialog()

        fun getOptions(): OptionsDialog = OptionsDialog()

        fun getDate(): DateDialog = DateDialog()

        fun getTime(): TimeDialog = TimeDialog()

        fun getClearBin(): MessageDialog {
            val dialog = MessageDialog()
            dialog.type = MessageType.Choice
            dialog.title = resources.getString(R.string.dialog_title_clear_bin)
            dialog.message = resources.getString(R.string.dialog_text_clear_bin)
            return dialog
        }

        companion object {
            private const val PREFIX = "DIALOG_MAIN"

            const val NOTIFICATIONS_DENY = "${PREFIX}_NOTIFICATIONS_DENY"
            const val NOTIFICATIONS = "${PREFIX}_NOTIFICATIONS"
            const val RENAME = "${PREFIX}_RENAME"
            const val ADD = "${PREFIX}_ADD"
            const val OPTIONS = "${PREFIX}_OPTIONS"
            const val DATE = "${PREFIX}_DATE"
            const val TIME = "${PREFIX}_TIME"
            const val CLEAR_BIN = "${PREFIX}_CLEAR_BIN"
        }
    }

    class Note(resources: Resources) : Permissions(resources) {

        fun getConvert(type: NoteType): MessageDialog {
            val dialog = MessageDialog()
            dialog.type = MessageType.Choice
            dialog.title = resources.getString(R.string.dialog_title_convert)
            dialog.message = when (type) {
                NoteType.TEXT -> resources.getString(R.string.dialog_text_convert_text)
                NoteType.ROLL -> resources.getString(R.string.dialog_roll_convert_roll)
            }
            return dialog
        }

        fun getRank(): SingleDialog {
            val dialog = SingleDialog()
            dialog.title = resources.getString(R.string.dialog_title_rank)
            return dialog
        }

        fun getColor(): ColorDialog {
            val dialog = ColorDialog()
            dialog.title = resources.getString(R.string.dialog_title_color)
            return dialog
        }

        fun getDate(): DateDialog = DateDialog()

        fun getTime(): TimeDialog = TimeDialog()

        companion object {
            private const val PREFIX = "DIALOG_NOTE"

            const val NOTIFICATIONS_DENY = "${PREFIX}_NOTIFICATIONS_DENY"
            const val DATE = "${PREFIX}_DATE"
            const val TIME = "${PREFIX}_TIME"
            const val CONVERT = "${PREFIX}_CONVERT"
            const val RANK = "${PREFIX}_RANK"
            const val COLOR = "${PREFIX}_COLOR"
        }
    }

    object Preference {

        class Main(private val resources: Resources) {

            fun getTheme(): SingleDialog {
                val dialog = SingleDialog()
                dialog.title = resources.getString(R.string.pref_title_app_theme)
                dialog.itemArray = resources.getStringArray(R.array.pref_theme)
                return dialog
            }

            fun getAbout(): AboutDialog = AboutDialog()

            companion object {
                private const val PREFIX = "DIALOG_PREF_MAIN"

                const val THEME = "${PREFIX}_THEME"
                const val ABOUT = "${PREFIX}_ABOUT"
            }
        }

        class Backup(private val resources: Resources) {

            fun getExportDeny(): MessageDialog {
                val dialog = MessageDialog()
                dialog.type = MessageType.Info
                dialog.title = resources.getString(R.string.dialog_title_export_deny)
                dialog.message = resources.getString(R.string.dialog_text_export_deny)
                return dialog
            }

            fun getImportDeny(): MessageDialog {
                val dialog = MessageDialog()
                dialog.type = MessageType.Info
                dialog.title = resources.getString(R.string.dialog_title_import_deny)
                dialog.message = resources.getString(R.string.dialog_text_import_deny)
                return dialog
            }

            fun getImport(): SingleDialog {
                val dialog = SingleDialog() // TODO NEUTRAL BUTTON
                dialog.applyEnable = true
                dialog.title = resources.getString(R.string.dialog_title_import)
                return dialog
            }

            fun getLoading(): LoadingDialog = LoadingDialog()

            companion object {
                private const val PREFIX = "DIALOG_PREF_BACKUP"

                const val EXPORT_DENY = "${PREFIX}_EXPORT_DENY"
                const val IMPORT_DENY = "${PREFIX}_IMPORT_DENY"
                const val IMPORT = "${PREFIX}_IMPORT"
                const val LOADING = "${PREFIX}_LOADING"
            }
        }

        class Notes(private val resources: Resources) {

            fun getSort(): SingleDialog {
                val dialog = SingleDialog()
                dialog.title = resources.getString(R.string.pref_title_note_sort)
                dialog.itemArray = resources.getStringArray(R.array.pref_sort)
                return dialog
            }

            fun getColor(): ColorDialog {
                val dialog = ColorDialog()
                dialog.title = resources.getString(R.string.pref_title_note_color)
                return dialog
            }

            fun getSavePeriod(): SingleDialog {
                val dialog = SingleDialog()
                dialog.title = resources.getString(R.string.pref_title_note_save_period)
                dialog.itemArray = resources.getStringArray(R.array.pref_save_period)
                return dialog
            }

            companion object {
                private const val PREFIX = "DIALOG_PREF_NOTES"

                const val SORT = "${PREFIX}_SORT"
                const val COLOR = "${PREFIX}_COLOR"
                const val SAVE_PERIOD = "${PREFIX}_SAVE_PERIOD"
            }
        }

        class Alarm(private val resources: Resources) {

            fun getSignal(): MultipleDialog {
                val dialog = MultipleDialog()
                dialog.atLeastOne = true
                dialog.title = resources.getString(R.string.pref_title_alarm_signal)
                dialog.itemArray = resources.getStringArray(R.array.pref_signal)
                return dialog
            }

            fun getRepeat(): SingleDialog {
                val dialog = SingleDialog()
                dialog.title = resources.getString(R.string.pref_title_alarm_repeat)
                dialog.itemArray = resources.getStringArray(R.array.pref_repeat)
                return dialog
            }

            fun getMelodyAccess(): MessageDialog {
                val dialog = MessageDialog()
                dialog.type = MessageType.Info
                dialog.title = resources.getString(R.string.dialog_title_melody_permission)
                dialog.message = resources.getString(R.string.dialog_text_melody_permission)
                return dialog
            }

            fun getMelody(): SingleDialog {
                val dialog = SingleDialog()
                dialog.title = resources.getString(R.string.pref_title_alarm_melody)
                return dialog
            }

            fun getVolume(): VolumeDialog {
                val dialog = VolumeDialog()
                dialog.title = resources.getString(R.string.pref_title_alarm_volume)
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