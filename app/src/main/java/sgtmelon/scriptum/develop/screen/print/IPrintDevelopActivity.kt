package sgtmelon.scriptum.develop.screen.print

import sgtmelon.scriptum.develop.model.PrintItem
import sgtmelon.scriptum.develop.model.PrintType

interface IPrintDevelopActivity {

    fun setupView(type: PrintType)

    fun setupInsets()

    fun beforeLoad()

    fun showProgress()

    fun onBindingList()

    fun notifyList(list: List<PrintItem>)

}