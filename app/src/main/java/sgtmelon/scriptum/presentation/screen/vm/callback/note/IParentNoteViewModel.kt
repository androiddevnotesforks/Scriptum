package sgtmelon.scriptum.presentation.screen.vm.callback.note

import android.os.Bundle
import sgtmelon.scriptum.presentation.control.note.save.SaveControl
import sgtmelon.scriptum.presentation.screen.vm.callback.IParentViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.note.ParentNoteViewModel

/**
 * Parent interface for communicate with children of [ParentNoteViewModel].
 */
interface IParentNoteViewModel : IParentViewModel,
//        INoteMenu,
        SaveControl.Callback {

    fun onSaveData(bundle: Bundle)

    fun onResume()

    fun onPause()

}