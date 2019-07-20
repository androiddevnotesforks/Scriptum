package sgtmelon.scriptum.screen.callback.note.text

import android.os.Bundle
import sgtmelon.scriptum.control.input.watcher.InputTextWatcher
import sgtmelon.scriptum.screen.callback.IParentViewModel
import sgtmelon.scriptum.screen.view.note.TextNoteFragment
import sgtmelon.scriptum.screen.vm.note.TextNoteViewModel

/**
 * Интерфейс для общения [TextNoteFragment] с [TextNoteViewModel]
 *
 * @author SerjantArbuz
 */
interface ITextNoteViewModel : IParentViewModel, ITextNoteMenu, InputTextWatcher.TextChange {

    fun onSetupData(bundle: Bundle?)

    fun onSaveData(bundle: Bundle)

    fun onPause()

    fun onClickBackArrow()

    fun onPressBack(): Boolean

    fun onResultColorDialog(check: Int)

    fun onResultRankDialog(check: BooleanArray)

    fun onResultConvertDialog()

    fun onCancelNoteBind()

}