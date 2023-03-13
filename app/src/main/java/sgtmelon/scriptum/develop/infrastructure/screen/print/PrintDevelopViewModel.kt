package sgtmelon.scriptum.develop.infrastructure.screen.print

import sgtmelon.scriptum.develop.infrastructure.model.PrintItem
import sgtmelon.scriptum.infrastructure.screen.parent.list.ListViewModel

interface PrintDevelopViewModel : ListViewModel<PrintItem> {

    fun updateData()

}