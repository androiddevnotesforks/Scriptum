package sgtmelon.scriptum.cleanup.ui.dialog

import android.view.ViewGroup.LayoutParams
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.ui.ParentScreen
import sgtmelon.scriptum.infrastructure.dialogs.LoadingDialog
import sgtmelon.scriptum.source.ui.feature.DialogUi
import sgtmelon.test.cappuccino.utils.isDisplayed
import sgtmelon.test.cappuccino.utils.withBackgroundDrawable
import sgtmelon.test.cappuccino.utils.withParent
import sgtmelon.test.cappuccino.utils.withSizeCode
import sgtmelon.test.cappuccino.utils.withText

/**
 * Class for UI control of [LoadingDialog].
 */
class LoadingDialogUi : ParentScreen(), DialogUi {

    private val parentContainer = getViewById(R.id.loading_parent_container)
    private val contentContainer = getViewById(R.id.loading_content_container)
    private val progressBar = getViewById(R.id.loading_progress_bar)
    private val loadingText = getViewById(R.id.loading_text)

    fun assert() {
        parentContainer.isDisplayed()
            .withSizeCode(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)

        contentContainer.isDisplayed()
            .withSizeCode(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            .withParent(parentContainer)
            .withBackgroundDrawable(R.drawable.bg_dialog)

        progressBar.isDisplayed()
            .withSizeCode(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            .withParent(contentContainer)

        loadingText.isDisplayed()
            .withSizeCode(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            .withParent(contentContainer)
            .withText(R.string.dialog_text_loading, R.attr.clContent, R.dimen.text_16sp)
    }

    companion object {
        inline operator fun invoke(func: LoadingDialogUi.() -> Unit): LoadingDialogUi {
            return LoadingDialogUi().apply { waitOpen { assert() } }.apply(func)
        }
    }
}