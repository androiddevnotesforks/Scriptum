package sgtmelon.scriptum.cleanup.ui.dialog

import android.view.ViewGroup.LayoutParams
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.basic.extension.isDisplayed
import sgtmelon.scriptum.cleanup.basic.extension.withParent
import sgtmelon.scriptum.cleanup.basic.extension.withSizeCode
import sgtmelon.scriptum.cleanup.basic.extension.withText
import sgtmelon.scriptum.cleanup.presentation.dialog.LoadingDialog
import sgtmelon.scriptum.cleanup.ui.IDialogUi
import sgtmelon.scriptum.cleanup.ui.ParentUi

/**
 * Class for UI control of [LoadingDialog].
 */
class LoadingDialogUi : ParentUi(), IDialogUi {

    private val parentContainer = getViewById(R.id.loading_parent_container)
    private val progressBar = getViewById(R.id.loading_progress_bar)
    private val loadingText = getViewById(R.id.loading_text)

    fun assert() {
        parentContainer.isDisplayed()
                .withSizeCode(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)

        progressBar.isDisplayed()
                .withSizeCode(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
                .withParent(parentContainer)

        loadingText.isDisplayed()
                .withSizeCode(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
                .withParent(parentContainer)
                .withText(R.string.dialog_text_loading, R.attr.clContent, R.dimen.text_16sp)
    }

    companion object {
        operator fun invoke(func: LoadingDialogUi.() -> Unit): LoadingDialogUi {
            return LoadingDialogUi().apply { waitOpen { assert() } }.apply(func)
        }
    }
}