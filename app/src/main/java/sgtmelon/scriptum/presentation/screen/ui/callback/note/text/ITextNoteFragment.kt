package sgtmelon.scriptum.presentation.screen.ui.callback.note.text

import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.presentation.control.note.input.IInputControl
import sgtmelon.scriptum.presentation.screen.ui.callback.note.IParentNoteFragment
import sgtmelon.scriptum.presentation.screen.ui.impl.note.TextNoteFragment
import sgtmelon.scriptum.presentation.screen.vm.impl.note.TextNoteViewModel

/**
 * Interface for communication [TextNoteViewModel] with [TextNoteFragment].
 */
interface ITextNoteFragment : IParentNoteFragment<NoteItem.Text> {

    /**
     * Setup elements for binding which is constants
     */
    fun setupBinding(@Theme theme: Int)

    fun setupToolbar(@Theme theme: Int, @Color color: Int)

    fun setupDialog(rankNameArray: Array<String>)

    fun setupEnter(iInputControl: IInputControl)


    fun onBindingLoad(isRankEmpty: Boolean)


    fun onPressBack(): Boolean

    fun setToolbarBackIcon(isCancel: Boolean, needAnim: Boolean)

    fun focusOnEdit(isCreate: Boolean)

    fun changeText(text: String, cursor: Int)

}