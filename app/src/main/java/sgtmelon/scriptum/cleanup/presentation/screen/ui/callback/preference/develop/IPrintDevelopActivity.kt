package sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.preference.develop

import sgtmelon.scriptum.cleanup.domain.model.item.PrintItem
import sgtmelon.scriptum.cleanup.domain.model.key.PrintType

interface IPrintDevelopActivity {

    fun setupView(type: PrintType)

    fun setupInsets()

    fun beforeLoad()

    fun showProgress()

    fun onBindingList()

    fun notifyList(list: List<PrintItem>)

}