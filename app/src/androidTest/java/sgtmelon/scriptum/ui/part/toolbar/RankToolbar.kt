package sgtmelon.scriptum.ui.part.toolbar

import androidx.test.espresso.Espresso.closeSoftKeyboard
import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.*
import sgtmelon.scriptum.ui.ParentUi
import sgtmelon.scriptum.ui.screen.main.RankScreen

/**
 * Part of UI abstraction for [RankScreen]
 */
class RankToolbar : ParentUi() {

    //region Views

    private val parentContainer = getViewById(R.id.toolbar_rank_container)
    private val nameEnter = getViewById(R.id.toolbar_rank_enter)

    private val clearButton = getViewById(R.id.toolbar_rank_clear_button)
    private val addButton = getViewById(R.id.toolbar_rank_add_button)

    //endregion

    private var enter = ""

    fun onEnterName(name: String, addEnabled: Boolean = true) = apply {
        enter = name

        nameEnter.typeText(name)
        assert(isAddEnabled = addEnabled)
    }

    fun onClickClear() {
        enter = ""

        clearButton.click()
        assert()
    }

    fun onClickAdd() {
        enter = ""

        closeSoftKeyboard()
        addButton.click()
        assert()
    }

    fun onLongClickAdd() {
        enter = ""

        closeSoftKeyboard()
        addButton.longClick()
        assert()
    }


    fun assert(isAddEnabled: Boolean = false) {
        parentContainer.isDisplayed()

        nameEnter.isDisplayed().apply {
            if (enter.isNotEmpty()) haveText(enter) else haveHint(R.string.hint_enter_rank_new)
        }

        clearButton.isDisplayed().isEnabled(enter.isNotEmpty())
        addButton.isDisplayed().isEnabled(isAddEnabled)
    }

    companion object {
        operator fun invoke(func: RankToolbar.() -> Unit) = RankToolbar().apply(func)
    }

}