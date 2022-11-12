package sgtmelon.scriptum.ui.auto.rank

import sgtmelon.scriptum.cleanup.domain.model.item.RankItem
import sgtmelon.scriptum.parent.ui.screen.main.RankScreen
import sgtmelon.scriptum.parent.ui.tests.ParentUiTest

inline fun ParentUiTest.startRankListTest(
    count: Int = 15,
    crossinline func: RankScreen.(list: MutableList<RankItem>) -> Unit = {}
) {
    val list = db.fillRank(count)
    launch { mainScreen { openRank { func(list) } } }
}