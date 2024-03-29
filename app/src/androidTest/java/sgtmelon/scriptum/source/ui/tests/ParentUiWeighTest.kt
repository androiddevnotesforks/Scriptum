package sgtmelon.scriptum.source.ui.tests

import org.junit.After
import sgtmelon.scriptum.cleanup.testData.DbWeightDelegator
import timber.log.Timber

/**
 * Parent class for UI weigh tests.
 */
abstract class ParentUiWeighTest : ParentUiRotationTest() {

    protected val dbWeight by lazy { DbWeightDelegator(component.database) }

    @After override fun tearDown() {
        super.tearDown()

        startTime = 0
        endTime = 0
    }

    private var startTime = 0L
    private var endTime = 0L

    /**
     * Function for time calculation, for detecting problem parts.
     */
    private inline fun calculateTime(func: () -> Unit) {
        startTime = System.currentTimeMillis()
        func()
        endTime = System.currentTimeMillis()

        Timber.i(message = "Calculated (occupied) time: ${endTime - startTime}ms")
    }

    companion object {
        const val ITEM_COUNT = 250
        const val REPEAT_COUNT = 5
        const val SCROLL_COUNT = 15
    }
}