package sgtmelon.scriptum.screen.ui.callback.note.text

import android.app.PendingIntent
import sgtmelon.scriptum.control.input.InputCallback
import sgtmelon.scriptum.control.input.InputControl
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.state.NoteState
import sgtmelon.scriptum.screen.ui.note.TextNoteFragment
import sgtmelon.scriptum.screen.vm.note.TextNoteViewModel
import java.util.*

/**
 * Interface for communication [TextNoteViewModel] with [TextNoteFragment]
 *
 * @author SerjantArbuz
 */
interface ITextNoteFragment {

    /**
     * Установка элементов для биндинга, которые постоянные
     */
    fun setupBinding(@Theme theme: Int, rankEmpty: Boolean)

    fun setupToolbar(@Theme theme: Int, @Color color: Int, noteState: NoteState)

    fun setupDialog(rankNameArray: Array<String>)

    fun setupEnter(inputCallback: InputCallback)

    fun bindNote(noteModel: NoteModel)

    fun bindEdit(editMode: Boolean, noteModel: NoteModel)

    fun bindInput(inputAccess: InputControl.Access, noteModel: NoteModel)

    fun onPressBack(): Boolean

    fun tintToolbar(@Color from: Int, @Color to: Int)

    fun tintToolbar(@Color color: Int)

    fun changeToolbarIcon(drawableOn: Boolean, needAnim: Boolean)

    fun focusOnEdit()

    fun changeName(text: String, cursor: Int)

    fun changeText(text: String, cursor: Int)

    fun hideKeyboard()

    fun showRankDialog(check: Int)

    fun showColorDialog(@Color color: Int)

    fun showDateDialog(calendar: Calendar)

    fun showTimeDialog(calendar: Calendar, dateList: List<String>)

    fun showConvertDialog()


    fun setAlarm(calendar: Calendar, intent: PendingIntent)

    fun cancelAlarm(intent: PendingIntent)

}