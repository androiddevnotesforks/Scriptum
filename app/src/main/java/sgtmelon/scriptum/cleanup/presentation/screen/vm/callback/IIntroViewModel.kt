package sgtmelon.scriptum.cleanup.presentation.screen.vm.callback

import android.os.Bundle
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.IIntroActivity
import sgtmelon.scriptum.cleanup.presentation.screen.vm.impl.IntroViewModel

/**
 * Interface for communication [IIntroActivity] with [IntroViewModel].
 */
interface IIntroViewModel : IParentViewModel {

    fun onSaveData(bundle: Bundle)

    fun onClickEnd()
}