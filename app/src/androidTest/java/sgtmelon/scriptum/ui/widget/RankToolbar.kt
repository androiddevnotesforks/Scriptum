package sgtmelon.scriptum.ui.widget

import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.basic.BasicMatch

class RankToolbar : ParentUi() {

    fun assert(func: Assert.() -> Unit) = Assert().apply { func() }

    companion object {
        operator fun invoke(func: RankToolbar.() -> Unit) = RankToolbar().apply { func() }
    }

    class Assert : BasicMatch()

}