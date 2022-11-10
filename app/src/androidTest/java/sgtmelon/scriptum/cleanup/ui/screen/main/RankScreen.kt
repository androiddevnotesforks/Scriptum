package sgtmelon.scriptum.cleanup.ui.screen.main

import org.junit.Assert.assertTrue
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.item.RankItem
import sgtmelon.scriptum.cleanup.ui.dialog.RenameDialogUi
import sgtmelon.scriptum.cleanup.ui.item.RankItemUi
import sgtmelon.scriptum.cleanup.ui.part.toolbar.RankToolbar
import sgtmelon.scriptum.infrastructure.model.annotation.TestViewTag
import sgtmelon.scriptum.infrastructure.screen.main.rank.RankFragment
import sgtmelon.scriptum.parent.ui.model.exception.EmptyListException
import sgtmelon.scriptum.parent.ui.model.key.InfoCase
import sgtmelon.scriptum.parent.ui.parts.ContainerPart
import sgtmelon.scriptum.parent.ui.parts.SnackbarPart
import sgtmelon.scriptum.parent.ui.parts.info.InfoContainerPart
import sgtmelon.scriptum.parent.ui.parts.recycler.RecyclerPart
import sgtmelon.test.cappuccino.utils.await
import sgtmelon.test.cappuccino.utils.click
import sgtmelon.test.cappuccino.utils.isDisplayed

/**
 * Class for UI control of [RankFragment].
 */
class RankScreen : ContainerPart(TestViewTag.RANK),
    RecyclerPart {

    //region Views

    override val recyclerView = getView(R.id.recycler_view)

    private val infoContainer = InfoContainerPart(InfoCase.Rank)

    fun getSnackbar(func: SnackbarPart.() -> Unit = {}): SnackbarPart {
        val message = R.string.snackbar_message_rank
        val action = R.string.snackbar_action_cancel

        return SnackbarPart(message, action, func)
    }

    private fun getItem(p: Int) = RankItemUi(recyclerView, p)

    fun toolbar(func: RankToolbar.() -> Unit) = RankToolbar(func)

    //endregion

    fun openRenameDialog(
        title: String,
        p: Int? = random,
        func: RenameDialogUi.() -> Unit = {}
    ) = apply {
        if (p == null) throw EmptyListException()

        getItem(p).view.click()
        RenameDialogUi(func, title)
    }

    fun onClickVisible(p: Int? = random) = apply {
        if (p == null) throw EmptyListException()

        getItem(p).visibleButton.click()
    }

    fun itemCancel(p: Int? = random, isWait: Boolean = false) = apply {
        if (p == null) throw EmptyListException()

        getItem(p).cancelButton.click()
        getSnackbar { assert() }

        if (isWait) {
            await(SnackbarPart.DISMISS_TIME)
        }
    }

    //region Assertion

    fun assertItem(item: RankItem, p: Int? = random) {
        if (p == null) throw EmptyListException()

        getItem(p).assert(item)
    }

    fun assert(isEmpty: Boolean) = apply {
        toolbar { assert() }

        parentContainer.isDisplayed()

        infoContainer.assert(isEmpty)
        recyclerView.isDisplayed(!isEmpty)
    }

    fun assertSnackbarDismiss() {
        assertTrue(try {
            getSnackbar().assert()
            false
        } catch (e: Throwable) {
            true
        })
    }

    //endregion

    companion object {
        inline operator fun invoke(func: RankScreen.() -> Unit, isEmpty: Boolean): RankScreen {
            return RankScreen().assert(isEmpty).apply(func)
        }
    }
}