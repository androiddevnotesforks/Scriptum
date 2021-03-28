package sgtmelon.scriptum.domain.interactor.callback.preference

import sgtmelon.scriptum.domain.interactor.impl.preference.PrintInteractor
import sgtmelon.scriptum.domain.model.item.PrintItem
import sgtmelon.scriptum.domain.model.key.PrintType
import sgtmelon.scriptum.presentation.screen.vm.callback.preference.IPrintViewModel

/**
 * Interface for communication [IPrintViewModel] with [PrintInteractor].
 */
interface IPrintInteractor {
    suspend fun getList(type: PrintType): List<PrintItem>
}