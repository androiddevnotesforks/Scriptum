package sgtmelon.scriptum.ui.cases.list

import sgtmelon.scriptum.parent.ui.feature.ListSnackbarWork
import sgtmelon.scriptum.parent.ui.model.key.Scroll
import sgtmelon.scriptum.parent.ui.parts.recycler.RecyclerPart

interface ListCancelSnackbarCase {

    /**
     * Check insets-spacing for snackbar bottom.
     */
    fun displayInsets()

    fun startDisplayInserts(screen: Any) {
        if (screen !is RecyclerPart<*, *> || screen !is ListSnackbarWork) throwOnCast()

        with(screen) {
            scrollTo(Scroll.END)
            repeat(times = 5) {
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

}