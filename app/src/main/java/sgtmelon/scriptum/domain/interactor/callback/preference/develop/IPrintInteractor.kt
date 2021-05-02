package sgtmelon.scriptum.domain.interactor.callback.preference.develop

import sgtmelon.scriptum.domain.interactor.impl.preference.develop.PrintInteractor
import sgtmelon.scriptum.domain.model.item.PrintItem
import sgtmelon.scriptum.domain.model.key.PrintType
import sgtmelon.scriptum.presentation.screen.vm.callback.preference.develop.IPrintViewModel

/**
 * Interface for communication [IPrintViewModel] with [PrintInteractor].
 */
interface IPrintInteractor {
    suspend fun getList(type: PrintType): List<PrintItem>
}