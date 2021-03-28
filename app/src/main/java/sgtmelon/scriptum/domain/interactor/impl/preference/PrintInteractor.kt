package sgtmelon.scriptum.domain.interactor.impl.preference

import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.data.repository.room.callback.IDevelopRepo
import sgtmelon.scriptum.domain.interactor.callback.preference.IPrintInteractor
import sgtmelon.scriptum.domain.interactor.impl.ParentInteractor
import sgtmelon.scriptum.domain.model.item.PrintItem
import sgtmelon.scriptum.domain.model.key.PrintType
import sgtmelon.scriptum.presentation.screen.vm.callback.preference.IPrintViewModel

/**
 * Interactor for [IPrintViewModel].
 */
class PrintInteractor(
    private val developRepo: IDevelopRepo,
    private val preferenceRepo: IPreferenceRepo
) : ParentInteractor(),
    IPrintInteractor {

    override fun getList(type: PrintType): List<PrintItem> {
        when (type) {
            PrintType.NOTE -> TODO()
            PrintType.BIN -> TODO()
            PrintType.ROLL -> TODO()
            PrintType.VISIBLE -> TODO()
            PrintType.RANK -> TODO()
            PrintType.ALARM -> TODO()
            PrintType.PREFERENCE -> TODO()
        }
    }
}