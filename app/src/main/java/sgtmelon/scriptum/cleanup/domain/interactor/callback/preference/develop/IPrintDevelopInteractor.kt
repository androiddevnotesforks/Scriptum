package sgtmelon.scriptum.cleanup.domain.interactor.callback.preference.develop

import sgtmelon.scriptum.cleanup.domain.interactor.impl.preference.develop.PrintDevelopInteractor
import sgtmelon.scriptum.cleanup.domain.model.item.PrintItem
import sgtmelon.scriptum.cleanup.domain.model.key.PrintType
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.preference.develop.IPrintDevelopViewModel

/**
 * Interface for communication [IPrintDevelopViewModel] with [PrintDevelopInteractor].
 */
interface IPrintDevelopInteractor {
    suspend fun getList(type: PrintType): List<PrintItem>
}