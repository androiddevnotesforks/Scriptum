package sgtmelon.scriptum.ui.auto.bin

import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.parent.ui.screen.main.BinScreen
import sgtmelon.scriptum.parent.ui.tests.ParentUiTest


inline fun ParentUiTest.startBinListTest(
    count: Int = 15,
    crossinline func: BinScreen.(list: MutableList<NoteItem>) -> Unit = {}
) {
    val list = db.fillBin(count)
    launch { mainScreen { openBin { func(list) } } }
}