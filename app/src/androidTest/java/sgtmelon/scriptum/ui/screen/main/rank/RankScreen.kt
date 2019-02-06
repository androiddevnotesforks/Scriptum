package sgtmelon.scriptum.ui.screen.main.rank

class RankScreen {

    companion object {
        operator fun invoke(func: RankScreen.() -> Unit) = RankScreen().apply { func() }
    }

    fun assert(func: RankAssert.() -> Unit) = RankAssert().apply { func() }

}