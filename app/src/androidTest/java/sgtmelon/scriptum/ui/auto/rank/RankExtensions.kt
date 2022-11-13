package sgtmelon.scriptum.ui.auto.rank

import sgtmelon.scriptum.cleanup.domain.model.item.RankItem
import sgtmelon.scriptum.parent.ui.screen.main.RankScreen
import sgtmelon.scriptum.parent.ui.tests.ParentUiTest

inline fun ParentUiTest.startRankTest(func: RankScreen.() -> Unit = {}) {
    launch { mainScreen { openRank(func = func) } }
}

inline fun ParentUiTest.startRankListTest(
    count: Int = 15,
    crossinline func: RankScreen.(list: MutableList<RankItem>) -> Unit = {}
) {
    val list = db.fillRank(count)
    launch { mainScreen { openRank { func(list) } } }
}

inline fun ParentUiTest.startRankItemTest(
    item: RankItem,
    crossinline func: RankScreen.(RankItem) -> Unit
) {
    launch { mainScreen { openRank { func(item) } } }
}