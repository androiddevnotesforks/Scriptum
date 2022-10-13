package sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.preference.develop

import sgtmelon.scriptum.cleanup.domain.model.key.PrintType
import sgtmelon.scriptum.infrastructure.develop.PrintItem

interface IPrintDevelopActivity {

    fun setupView(type: PrintType)

    fun setupInsets()

    fun beforeLoad()

    fun showProgress()

    fun onBindingList()

    fun notifyList(list: List<PrintItem>)

}