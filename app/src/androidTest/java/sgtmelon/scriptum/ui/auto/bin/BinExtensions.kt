package sgtmelon.scriptum.ui.auto.bin

import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.parent.ui.screen.main.BinScreen
import sgtmelon.scriptum.parent.ui.tests.ParentUiTest

inline fun ParentUiTest.startBinListTest(
    count: Int = 15,
    crossinline func: BinScreen.(list: MutableList<NoteItem>) -> Unit = {}
) {
    val list = db.fillBin(count)
    launchSplash { mainScreen { openBin { func(list) } } }
}

inline fun <T : NoteItem> ParentUiTest.startBinItemTest(
    item: T,
    crossinline func: BinScreen.(T) -> Unit
) {
    launchSplash { mainScreen { openBin { func(item) } } }
}