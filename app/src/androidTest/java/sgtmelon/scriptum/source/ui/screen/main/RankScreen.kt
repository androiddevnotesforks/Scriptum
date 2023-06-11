package sgtmelon.scriptum.source.ui.screen.main

import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.item.RankItem
import sgtmelon.scriptum.infrastructure.model.annotation.TestViewTag
import sgtmelon.scriptum.infrastructure.screen.main.rank.RankFragment
import sgtmelon.scriptum.source.ui.feature.KeyboardClose
import sgtmelon.scriptum.source.ui.feature.ListSnackbarWork
import sgtmelon.scriptum.source.ui.model.exception.EmptyListException
import sgtmelon.scriptum.source.ui.model.key.InfoCase
import sgtmelon.scriptum.source.ui.parts.ContainerPart
import sgtmelon.scriptum.source.ui.parts.SnackbarPart
import sgtmelon.scriptum.source.ui.parts.info.InfoContainerPart
import sgtmelon.scriptum.source.ui.parts.recycler.RecyclerPart
import sgtmelon.scriptum.source.ui.parts.toolbar.RankToolbar
import sgtmelon.scriptum.source.ui.screen.dialogs.RenameDialogUi
import sgtmelon.scriptum.source.ui.screen.item.RankItemUi
import sgtmelon.test.cappuccino.utils.await
import sgtmelon.test.cappuccino.utils.isDisplayed

/**
 * Class for UI control of [RankFragment].
 */
class RankScreen : ContainerPart(TestViewTag.RANK),
    RecyclerPart<RankItem, RankItemUi>,
    ListSnackbarWork,
    KeyboardClose {

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
    ) {
        if (p == null) throw EmptyListException()

        getItem(p).open(title, func)
    }

    fun itemVisible(p: Int? = random) {
        if (p == null) throw EmptyListException()

        getItem(p).visible()
    }

    override fun itemCancel(p: Int?, isWait: Boolean) {
        if (p == null) throw EmptyListException()

        getItem(p).cancel()
        snackbar { assert() }

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