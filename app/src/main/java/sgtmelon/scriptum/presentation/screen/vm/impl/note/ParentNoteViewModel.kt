package sgtmelon.scriptum.presentation.screen.vm.impl.note

import android.app.Application
import android.os.Bundle
import androidx.annotation.VisibleForTesting
import sgtmelon.scriptum.domain.interactor.callback.IBindInteractor
import sgtmelon.scriptum.domain.interactor.callback.note.IParentNoteInteractor
import sgtmelon.scriptum.domain.model.data.NoteData
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.model.state.IconState
import sgtmelon.scriptum.domain.model.state.NoteState
import sgtmelon.scriptum.presentation.control.note.input.IInputControl
import sgtmelon.scriptum.presentation.control.note.input.InputControl
import sgtmelon.scriptum.presentation.control.note.save.ISaveControl
import sgtmelon.scriptum.presentation.control.note.save.SaveControl
import sgtmelon.scriptum.presentation.screen.ui.callback.note.INoteConnector
import sgtmelon.scriptum.presentation.screen.vm.callback.note.IParentNoteViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.ParentViewModel

/**
 * Parent viewModel for [TextNoteViewModel] and [RollNoteViewModel].
 */
abstract class ParentNoteViewModel<C, I : IParentNoteInteractor, N: NoteItem>(
        application: Application
) : ParentViewModel<C>(application),
    IParentNoteViewModel {

    //region Variables

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    var parentCallback: INoteConnector? = null
        private set

    fun setParentCallback(callback: INoteConnector?) {
        parentCallback = callback
    }

    protected lateinit var interactor: I
    protected lateinit var bindInteractor: IBindInteractor

    fun setInteractor(interactor: I, bindInteractor: IBindInteractor) {
        this.interactor = interactor
        this.bindInteractor = bindInteractor
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    var inputControl: IInputControl = InputControl()

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    val saveControl: ISaveControl by lazy {
        SaveControl(context, interactor.getSaveModel(), callback = this)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    var id: Long = NoteData.Default.ID

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    var color: Int = NoteData.Default.COLOR

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    lateinit var noteItem: N

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    lateinit var restoreItem: N

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    var noteState = NoteState()

    /**
     * App doesn't have ranks if size == 1.
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    var rankDialogItemArray: Array<String> = arrayOf()

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    var iconState = IconState()

    //endregion

    protected fun isNoteInitialized(): Boolean = ::noteItem.isInitialized

    override fun onDestroy(func: () -> Unit) = super.onDestroy {
        interactor.onDestroy()
        parentCallback = null
        saveControl.setSaveEvent(isWork = false)
    }


    override fun onSaveData(bundle: Bundle) {
        bundle.apply {
            putLong(NoteData.Intent.ID, id)
            putInt(NoteData.Intent.COLOR, color)
        }
    }

    override fun onResume() {
        if (noteState.isEdit) {
            saveControl.setSaveEvent(isWork = true)
        }
    }

    override fun onPause() {
        if (noteState.isEdit) {
            saveControl.onPauseSave()
            saveControl.setSaveEvent(isWork = false)
        }
    }

}