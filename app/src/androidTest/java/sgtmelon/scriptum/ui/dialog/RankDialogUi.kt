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
        private val noteItem: NoteItem,
        private val rankList: List<RankItem>,
        private val callback: Callback
) : ParentRecyclerScreen(R.id.select_dialog_listview),
        IDialogUi {

    private var check = noteItem.rankPs

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

        if (check == noteItem.rankPs) {
            onClickItem()
        } else {
            if (check == -1) noCategoryButton.click() else getItem(rankList[check].name).click()

            assert()
        }
    }

    fun onClickCancel() = waitClose { cancelButton.click() }

    fun onClickApply() = waitClose {
        if (noteItem.rankPs == check) throw IllegalAccessException("Apply button not enabled")

        applyButton.click()
        callback.onResultRankDialog(rankList.getOrNull(check))
    }

    fun assert() {
        recyclerView.isDisplayed()
        titleText.isDisplayed()

        noCategoryButton.isDisplayed().isChecked(checked = check == -1)
        rankList.forEach {
            getItem(it.name).isDisplayed().isChecked(checked = check == it.position)
        }

        cancelButton.isDisplayed().isEnabled().withTextColor(R.attr.clAccent)
        applyButton.isDisplayed().isEnabled(enabled = noteItem.rankPs != check) {
            withTextColor(R.attr.clAccent)
        }
    }


    interface Callback {
        fun onResultRankDialog(rankItem: RankItem?)
    }

    companion object {
        operator fun invoke(func: RankDialogUi.() -> Unit,
                            noteItem: NoteItem,
                            rankList: List<RankItem>,
                            callback: Callback): RankDialogUi {
            return RankDialogUi(noteItem, rankList, callback)
                    .apply { waitOpen { assert() } }
                    .apply(func)
        }
    }

}