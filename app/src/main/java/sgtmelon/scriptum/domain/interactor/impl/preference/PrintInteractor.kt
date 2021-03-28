package sgtmelon.scriptum.domain.interactor.impl.preference

import sgtmelon.scriptum.domain.interactor.callback.preference.IPrintInteractor
import sgtmelon.scriptum.domain.interactor.impl.ParentInteractor
import sgtmelon.scriptum.presentation.screen.vm.callback.preference.IPrintViewModel

/**
 * Interactor for [IPrintViewModel].
 */
class PrintInteractor : ParentInteractor(),
    IPrintInteractor {

    override fun getList(): List<Any> {
        TODO("Not yet implemented")
    }
}