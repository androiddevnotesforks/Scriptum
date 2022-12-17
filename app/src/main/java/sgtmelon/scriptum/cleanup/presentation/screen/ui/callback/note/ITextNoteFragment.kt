package sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.note

import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.presentation.control.note.input.IInputControl
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.TextNoteFragmentImpl
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.note.TextNoteViewModel
import sgtmelon.scriptum.infrastructure.model.key.preference.Color

/**
 * Interface for communication [TextNoteViewModel] with [TextNoteFragmentImpl].
 */
interface ITextNoteFragment : IParentNoteFragment<NoteItem.Text> {

    /**
     * Setup elements for binding which is constants
     */
    fun setupBinding()

    fun setupToolbar(color: Color)

    fun setupDialog(rankNameArray: Array<String>)

    fun setupEnter(inputControl: IInputControl)


    fun onBindingLoad(isRankEmpty: Boolean)


    fun onPressBack(): Boolean

    fun setToolbarBackIcon(isCancel: Boolean, needAnim: Boolean)

    fun focusOnEdit(isCreate: Boolean)

    fun changeText(text: String, cursor: Int)

}