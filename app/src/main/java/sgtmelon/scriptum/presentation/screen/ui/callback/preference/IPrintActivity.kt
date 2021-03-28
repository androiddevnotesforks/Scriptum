package sgtmelon.scriptum.presentation.screen.ui.callback.preference

import sgtmelon.scriptum.domain.model.key.PrintType

interface IPrintActivity {

    fun setupView(type: PrintType)

    fun setupInsets()

    fun beforeLoad()

    fun showProgress()

    fun onBindingList()

    fun notifyList(list: List<Any>)

}