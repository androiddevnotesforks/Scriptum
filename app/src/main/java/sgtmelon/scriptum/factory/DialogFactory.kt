package sgtmelon.scriptum.factory

import android.content.Context
import androidx.fragment.app.FragmentManager
import sgtmelon.safedialog.*
import sgtmelon.scriptum.R
import sgtmelon.scriptum.dialog.*
import sgtmelon.scriptum.model.key.NoteType

/**
 * Фабрика для создания диалогов
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

        fun getClearBinDialog(context: Context, fm: FragmentManager?): MessageDialog {
            val dialog = fm?.findFragmentByTag(CLEAR_BIN) as? MessageDialog ?: MessageDialog()

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

    object Note {

        fun getConvertDialog(context: Context, fm: FragmentManager?, type: NoteType): MessageDialog {
            val dialog = fm?.findFragmentByTag(CONVERT) as? MessageDialog ?: MessageDialog()

            dialog.title = context.getString(R.string.dialog_title_convert)
            dialog.message = when (type) {
                NoteType.TEXT -> context.getString(R.string.dialog_text_convert_to_roll)
                NoteType.ROLL -> context.getString(R.string.dialog_roll_convert_to_text)
            }

            return dialog
        }

        fun getRankDialog(context: Context, fm: FragmentManager?): MultiplyDialog {
            val dialog = fm?.findFragmentByTag(RANK) as? MultiplyDialog ?: MultiplyDialog()

            dialog.title = context.getString(R.string.dialog_title_rank)

            return dialog
        }

        fun getColorDialog(context: Context, fm: FragmentManager?): ColorDialog {
            val dialog = fm?.findFragmentByTag(COLOR) as? ColorDialog ?: ColorDialog()

            dialog.title = context.getString(R.string.dialog_title_color)

            return dialog
        }

        fun getDateDialog(fm: FragmentManager?) =
                fm?.findFragmentByTag(DATE) as? DateDialog ?: DateDialog()

        fun getTimeDialog(fm: FragmentManager?) =
                fm?.findFragmentByTag(TIME) as? TimeDialog ?: TimeDialog()

        private const val PREFIX = "DIALOG"

        const val DATE = "${PREFIX}_DATE"
        const val TIME = "${PREFIX}_TIME"
        const val CONVERT = "${PREFIX}_CONVERT"

        const val RANK = "${PREFIX}_RANK"
        const val COLOR = "${PREFIX}_COLOR"

    }

    object Preference {

        fun getThemeDialog(context: Context, fm: FragmentManager?): SingleDialog {
            val dialog = fm?.findFragmentByTag(THEME) as? SingleDialog ?: SingleDialog()

            dialog.title = context.getString(R.string.title_app_theme)
            dialog.rows = context.resources.getStringArray(R.array.text_app_theme)

            return dialog
        }

        fun getRepeatDialog(context: Context, fm: FragmentManager?): SingleDialog {
            val dialog = fm?.findFragmentByTag(REPEAT) as? SingleDialog ?: SingleDialog()

            dialog.title = context.getString(R.string.title_alarm_repeat)
            dialog.rows = context.resources.getStringArray(R.array.text_alarm_repeat)

            return dialog
        }

        fun getSignalDialog(context: Context, fm: FragmentManager?): MultiplyDialog {
            val dialog = fm?.findFragmentByTag(SIGNAL) as? MultiplyDialog ?: MultiplyDialog()

            dialog.needOneSelect = true
            dialog.title = context.getString(R.string.title_alarm_signal)
            dialog.name = context.resources.getStringArray(R.array.text_alarm_signal).toList()

            return dialog
        }

        fun getMelodyDialog(context: Context, fm: FragmentManager?): SingleDialog {
            val dialog = fm?.findFragmentByTag(MELODY) as? SingleDialog ?: SingleDialog()

            dialog.title = context.getString(R.string.title_alarm_melody)

            return dialog
        }

        fun getVolumeDialog(context: Context, fm: FragmentManager?): VolumeDialog {
            val dialog = fm?.findFragmentByTag(VOLUME) as? VolumeDialog ?: VolumeDialog()

            dialog.title = context.getString(R.string.title_alarm_volume)

            return dialog
        }

        fun getSortDialog(context: Context, fm: FragmentManager?): SingleDialog {
            val dialog = fm?.findFragmentByTag(SORT) as? SingleDialog ?: SingleDialog()

            dialog.title = context.getString(R.string.title_note_sort)
            dialog.rows = context.resources.getStringArray(R.array.text_note_sort)

            return dialog
        }

        fun getColorDialog(context: Context, fm: FragmentManager?): ColorDialog {
            val dialog = fm?.findFragmentByTag(COLOR) as? ColorDialog ?: ColorDialog()

            dialog.title = context.getString(R.string.title_note_color)

            return dialog
        }

        fun getSaveTimeDialog(context: Context, fm: FragmentManager?): SingleDialog {
            val dialog = fm?.findFragmentByTag(SAVE_TIME) as? SingleDialog ?: SingleDialog()

            dialog.title = context.getString(R.string.title_save_time)
            dialog.rows = context.resources.getStringArray(R.array.text_save_time)

            return dialog
        }

        fun getAboutDialog(fm: FragmentManager?): AboutDialog =
                fm?.findFragmentByTag(ABOUT) as? AboutDialog ?: AboutDialog()

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