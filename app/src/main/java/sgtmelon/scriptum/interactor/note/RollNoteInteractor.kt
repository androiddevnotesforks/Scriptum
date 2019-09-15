package sgtmelon.scriptum.interactor.note

import android.content.Context
import sgtmelon.scriptum.interactor.ParentInteractor
import sgtmelon.scriptum.interactor.callback.note.IRollNoteInteractor
import sgtmelon.scriptum.screen.ui.callback.note.roll.IRollNoteBridge
import sgtmelon.scriptum.screen.vm.note.RollNoteViewModel

/**
 * Interactor for [RollNoteViewModel]
 */
class RollNoteInteractor(context: Context, private var callback: IRollNoteBridge?) :
        ParentInteractor(context),
        IRollNoteInteractor {

    override fun onDestroy(func: () -> Unit) = super.onDestroy { callback = null }

}