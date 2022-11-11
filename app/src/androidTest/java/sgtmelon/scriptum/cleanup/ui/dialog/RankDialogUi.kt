package sgtmelon.scriptum.cleanup.ui.dialog

import sgtmelon.safedialog.dialog.SingleDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.RankItem
import sgtmelon.scriptum.cleanup.ui.ParentRecyclerScreen
import sgtmelon.scriptum.parent.ui.feature.DialogUi
import sgtmelon.test.cappuccino.utils.click
import sgtmelon.test.cappuccino.utils.isChecked
import sgtmelon.test.cappuccino.utils.isDisplayed
import sgtmelon.test.cappuccino.utils.isEnabled
import sgtmelon.test.cappuccino.utils.withTextColor

/**
 * Class for UI control of [SingleDialog] with rank list.
 */
class RankDialogUi(
    private val item: NoteItem,
    private val rankList: List<RankItem>,
    private val callback: Callback
) : ParentRecyclerScreen(R.id.select_dialog_listview),
    DialogUi {

    // TODO make it common (RepeatDialogUi, RankDialogUi, ThemeDialogUi)

    private var check = item.rankPs

    //region Views

    private val titleText = getViewByText(R.string.dialog_title_rank)

    private val noCategoryButton = getViewByText(R.string.dialog_item_rank_empty)

    private val cancelButton = getViewByText(sgtmelon.safedialog.R.string.dialog_button_cancel)
    private val applyButton = getViewByText(sgtmelon.safedialog.R.string.dialog_button_apply)

    fun getItem(name: String) = getViewByText(name)

    //endregion


    fun onClickItem(p: Int? = random): RankDialogUi = apply {
        if (p == null) return@apply

        check = p - 1

        if (check == item.rankPs) {
            onClickItem()
        } else {
            if (check == -1) noCategoryButton.click() else getItem(rankList[check].name).click()

            assert()
        }
    }

    fun onClickCancel() = waitClose { cancelButton.click() }

    fun onClickApply() = waitClose {
        if (item.rankPs == check) throw IllegalAccessException("Apply button not enabled")

        applyButton.click()
        callback.onResultRankDialog(rankList.getOrNull(check))
    }

    fun assert() {
        recyclerView.isDisplayed()
        titleText.isDisplayed().withTextColor(R.attr.clContent)

        noCategoryButton.isDisplayed()
            .isChecked(value = check == -1)
            .withTextColor(R.attr.clContent)

        for (it in rankList) {
            getItem(it.name).isDisplayed()
                .isChecked(value = check == it.position)
                .withTextColor(R.attr.clContent)
        }

        cancelButton.isDisplayed().isEnabled().withTextColor(R.attr.clContentSecond)
        applyButton.isDisplayed().isEnabled(value = item.rankPs != check) {
            withTextColor(R.attr.clAccent)
        }
    }


    interface Callback {
        fun onResultRankDialog(item: RankItem?)
    }

    companion object {
        inline operator fun invoke(
            func: RankDialogUi.() -> Unit,
            item: NoteItem,
            rankList: List<RankItem>,
            callback: Callback
        ): RankDialogUi {
            return RankDialogUi(item, rankList, callback)
                .apply { waitOpen { assert() } }
                .apply(func)
        }
    }
}