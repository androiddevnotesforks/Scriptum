package sgtmelon.scriptum.parent.ui.screen.main

import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.item.RankItem
import sgtmelon.scriptum.cleanup.ui.dialog.RenameDialogUi
import sgtmelon.scriptum.cleanup.ui.item.RankItemUi
import sgtmelon.scriptum.infrastructure.model.annotation.TestViewTag
import sgtmelon.scriptum.infrastructure.screen.main.rank.RankFragment
import sgtmelon.scriptum.parent.ui.feature.SnackbarWork
import sgtmelon.scriptum.parent.ui.model.exception.EmptyListException
import sgtmelon.scriptum.parent.ui.model.key.InfoCase
import sgtmelon.scriptum.parent.ui.parts.ContainerPart
import sgtmelon.scriptum.parent.ui.parts.SnackbarPart
import sgtmelon.scriptum.parent.ui.parts.info.InfoContainerPart
import sgtmelon.scriptum.parent.ui.parts.recycler.RecyclerPart
import sgtmelon.scriptum.parent.ui.parts.toolbar.RankToolbar
import sgtmelon.test.cappuccino.utils.await
import sgtmelon.test.cappuccino.utils.isDisplayed

/**
 * Class for UI control of [RankFragment].
 */
class RankScreen : ContainerPart(TestViewTag.RANK),
    RecyclerPart<RankItem, RankItemUi>,
    SnackbarWork {

    //region Views

    override val recyclerView = getView(R.id.recycler_view)

    private val infoContainer = InfoContainerPart(parentContainer, InfoCase.Rank)

    override val snackbarMessage = R.string.snackbar_message_rank
    override val snackbarAction = R.string.snackbar_action_cancel

    override fun getItem(p: Int) = RankItemUi(recyclerView, p)

    fun toolbar(func: RankToolbar.() -> Unit = {}) = RankToolbar(func, parentContainer)

    //endregion

    fun openRenameDialog(
        title: String,
        p: Int? = random,
        func: RenameDialogUi.() -> Unit = {}
    ) = apply {
        if (p == null) throw EmptyListException()

        getItem(p).open(title, func)
    }

    fun itemVisible(p: Int? = random) = apply {
        if (p == null) throw EmptyListException()

        getItem(p).visible()
    }

    fun itemCancel(p: Int? = random, isWait: Boolean = false) = apply {
        if (p == null) throw EmptyListException()

        getItem(p).cancel()
        snackbar().assert()

        if (isWait) {
            await(SnackbarPart.DISMISS_TIME)
        }
    }

    fun assert(isEmpty: Boolean) = apply {
        parentContainer.isDisplayed()
        toolbar().assert()
        infoContainer.assert(isEmpty)
        recyclerView.isDisplayed(!isEmpty)
    }

    companion object {
        inline operator fun invoke(func: RankScreen.() -> Unit, isEmpty: Boolean): RankScreen {
            return RankScreen().assert(isEmpty).apply(func)
        }
    }
}