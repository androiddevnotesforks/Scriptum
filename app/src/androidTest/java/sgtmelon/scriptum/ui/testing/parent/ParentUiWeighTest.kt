package sgtmelon.scriptum.ui.testing.parent

import sgtmelon.scriptum.parent.RoomWorker
import sgtmelon.scriptum.parent.di.ParentInjector

/**
 * Parent class for UI weigh tests.
 */
abstract class ParentUiWeighTest : ParentUiTest(),
    RoomWorker {

    override val database = ParentInjector.provideDatabase()

    protected val dbWeight = ParentInjector.provideDbWeightDelegator()

}