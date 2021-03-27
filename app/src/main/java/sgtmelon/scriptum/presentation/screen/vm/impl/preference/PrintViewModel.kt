package sgtmelon.scriptum.presentation.screen.vm.impl.preference

import android.app.Application
import android.os.Bundle
import sgtmelon.scriptum.domain.interactor.callback.preference.IPrintInteractor
import sgtmelon.scriptum.presentation.screen.ui.callback.preference.IPrintActivity
import sgtmelon.scriptum.presentation.screen.vm.callback.preference.IPrintViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.ParentViewModel

/**
 * ViewModel for [IPrintActivity].
 */
class PrintViewModel(
    application: Application
) : ParentViewModel<IPrintActivity>(application),
    IPrintViewModel {

    private lateinit var interactor: IPrintInteractor

    fun setInteractor(interactor: IPrintInteractor) {
        this.interactor = interactor
    }

    override fun onSetup(bundle: Bundle?) {
        TODO("Not yet implemented")
    }
}