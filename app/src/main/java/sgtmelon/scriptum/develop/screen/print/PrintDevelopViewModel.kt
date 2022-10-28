package sgtmelon.scriptum.develop.screen.print

import android.os.Bundle
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.IParentViewModel

interface PrintDevelopViewModel : IParentViewModel {

    fun onSaveData(bundle: Bundle)

    fun onUpdateData()
}