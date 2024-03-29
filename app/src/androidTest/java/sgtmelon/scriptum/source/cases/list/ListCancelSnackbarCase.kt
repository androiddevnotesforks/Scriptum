package sgtmelon.scriptum.source.cases.list

import sgtmelon.scriptum.source.ui.feature.ListSnackbarWork
import sgtmelon.scriptum.source.ui.model.key.Scroll
import sgtmelon.scriptum.source.ui.parts.recycler.RecyclerPart

interface ListCancelSnackbarCase {

    /** Check insets-spacing for snackbar bottom. */
    fun displayInsets()

    fun startDisplayInsets(screen: Any) {
        if (screen !is RecyclerPart<*, *> || screen !is ListSnackbarWork) throwOnCast()

        with(screen) {
            scrollTo(Scroll.END)
            repeat(times = 3) {
                itemCancel(last, isWait = true)
                assertSnackbarDismissed()
            }
        }
    }

    fun singleActionClick()

    fun manyActionClick()

    fun scrollTopAfterAction()

    fun scrollBottomAfterAction()

    fun restoreAfterPause()

    fun clearCacheOnDismiss()

    fun dismissTimeout()

    private fun throwOnCast(): Nothing = throw ClassCastException()

    fun workAfterRotation()

}