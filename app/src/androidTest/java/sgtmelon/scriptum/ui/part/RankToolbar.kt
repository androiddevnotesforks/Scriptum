package sgtmelon.scriptum.ui.part

import androidx.test.espresso.Espresso.closeSoftKeyboard
import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.*
import sgtmelon.scriptum.ui.ParentUi

/**
 * Часть UI абстракции для [RankScreen]
 */
class RankToolbar : ParentUi() {

    //region Views

    private val parentContainer = getViewById(R.id.toolbar_rank_container)
    private val nameEnter = getViewById(R.id.toolbar_rank_enter)

    private val clearButton = getViewById(R.id.toolbar_rank_clear_button)
    private val addButton = getViewById(R.id.toolbar_rank_add_button)

    //endregion

    private var enter = ""

    fun onEnterName(name: String, isAddEnabled: Boolean) = apply {
        enter = name

        nameEnter.typeText(name)
        assert(isAddEnabled = isAddEnabled)
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
            if (enter.isNotEmpty()) withText(enter) else withHint(R.string.hint_enter_rank_new)
        }

        clearButton.isDisplayed().isEnabled(enter.isNotEmpty())
        addButton.isDisplayed().isEnabled(isAddEnabled)
    }

    companion object {
        operator fun invoke(func: RankToolbar.() -> Unit) = RankToolbar().apply(func)
    }

}