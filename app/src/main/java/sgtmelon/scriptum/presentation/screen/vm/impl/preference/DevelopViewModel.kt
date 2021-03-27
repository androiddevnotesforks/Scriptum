package sgtmelon.scriptum.presentation.screen.vm.impl.preference

import android.app.Application
import android.os.Bundle
import sgtmelon.scriptum.domain.interactor.callback.preference.IDevelopInteractor
import sgtmelon.scriptum.presentation.screen.ui.callback.preference.IDevelopFragment
import sgtmelon.scriptum.presentation.screen.vm.callback.preference.IDevelopViewModel
import sgtmelon.scriptum.presentation.screen.vm.impl.ParentViewModel

/**
 * ViewModel for [IDevelopFragment]
 */
class DevelopViewModel(application: Application) : ParentViewModel<IDevelopFragment>(application),
    IDevelopViewModel {

    private lateinit var interactor: IDevelopInteractor

    fun setInteractor(interactor: IDevelopInteractor) {
        this.interactor = interactor
    }


    override fun onSetup(bundle: Bundle?) {
        callback?.setupPrints()
        callback?.setupScreens()
        callback?.setupOther()
    }
}