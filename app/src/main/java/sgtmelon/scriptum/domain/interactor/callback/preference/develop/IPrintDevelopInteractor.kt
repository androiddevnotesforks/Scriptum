package sgtmelon.scriptum.domain.interactor.callback.preference.develop

import sgtmelon.scriptum.domain.interactor.impl.preference.develop.PrintDevelopInteractor
import sgtmelon.scriptum.domain.model.item.PrintItem
import sgtmelon.scriptum.domain.model.key.PrintType
import sgtmelon.scriptum.presentation.screen.vm.callback.preference.develop.IPrintDevelopViewModel

/**
 * Interface for communication [IPrintDevelopViewModel] with [PrintDevelopInteractor].
 */
interface IPrintDevelopInteractor {
    suspend fun getList(type: PrintType): List<PrintItem>
}