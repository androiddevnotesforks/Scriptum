package sgtmelon.scriptum.interactor.note

import android.content.Context
import sgtmelon.scriptum.interactor.ParentInteractor
import sgtmelon.scriptum.interactor.callback.note.ITextNoteInteractor
import sgtmelon.scriptum.screen.ui.callback.note.text.ITextNoteBridge
import sgtmelon.scriptum.screen.vm.note.TextNoteViewModel

/**
 * Interactor for [TextNoteViewModel]
 */
class TextNoteInteractor(context: Context, private var callback: ITextNoteBridge?) :
        ParentInteractor(context),
        ITextNoteInteractor {

    override fun onDestroy(func: () -> Unit) = super.onDestroy { callback = null }

}