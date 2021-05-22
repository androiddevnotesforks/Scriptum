package sgtmelon.scriptum.ui.dialog

import sgtmelon.safedialog.SingleDialog
import sgtmelon.scriptum.R
import sgtmelon.scriptum.basic.extension.*
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.model.item.RankItem
import sgtmelon.scriptum.ui.IDialogUi
import sgtmelon.scriptum.ui.ParentRecyclerScreen

/**
 * Class for UI control of [SingleDialog] with rank list.
 */
class RankDialogUi(
    private val item: NoteItem,
    private val rankList: List<RankItem>,
    private val callback: Callback
) : ParentRecyclerScreen(R.id.select_dialog_listview),
        IDialogUi {

    // TODO make it common (RepeatDialogUi, RankDialogUi, ThemeDialogUi)

    private var check = item.rankPs

    //region Views

    private val titleText = getViewByText(R.string.dialog_title_rank)

    private val noCategoryButton = getViewByText(R.string.dialog_item_rank)

    private val cancelButton = getViewByText(R.string.dialog_button_cancel)
    private val applyButton = getViewByText(R.string.dialog_button_apply)

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
        titleText.isDisplayed()

        noCategoryButton.isDisplayed().isChecked(isChecked = check == -1)
        for (it in rankList) {
            getItem(it.name).isDisplayed().isChecked(isChecked = check == it.position)
        }

        cancelButton.isDisplayed().isEnabled().withTextColor(R.attr.clAccent)
        applyButton.isDisplayed().isEnabled(isEnabled = item.rankPs != check) {
            withTextColor(R.attr.clAccent)
        }
    }


    interface Callback {
        fun onResultRankDialog(item: RankItem?)
    }

    companion object {
        operator fun invoke(
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