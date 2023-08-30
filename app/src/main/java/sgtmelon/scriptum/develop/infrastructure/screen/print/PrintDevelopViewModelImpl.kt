package sgtmelon.scriptum.develop.infrastructure.screen.print

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import sgtmelon.extensions.launchBack
import sgtmelon.scriptum.develop.domain.GetPrintListUseCase
import sgtmelon.scriptum.develop.infrastructure.model.PrintItem
import sgtmelon.scriptum.develop.infrastructure.model.PrintType
import sgtmelon.scriptum.infrastructure.screen.parent.list.ListStorageImpl
import sgtmelon.scriptum.infrastructure.utils.extensions.clearAdd

class PrintDevelopViewModelImpl(
    private val type: PrintType,
    override val list: ListStorageImpl<PrintItem>,
    private val getList: GetPrintListUseCase
) : ViewModel(),
    PrintDevelopViewModel {

    override fun updateData() {
        viewModelScope.launchBack {
            list.change { it.clearAdd(getList(type)) }
        }
    }
}