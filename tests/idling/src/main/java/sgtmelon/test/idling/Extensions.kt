package sgtmelon.test.idling

import sgtmelon.test.idling.callback.AppIdlingCallback
import sgtmelon.test.idling.callback.WaitIdlingCallback
import sgtmelon.test.idling.impl.AppIdlingResource
import sgtmelon.test.idling.impl.WaitIdlingResource

fun getIdling(): AppIdlingCallback = AppIdlingResource.getInstance()

fun getWaitIdling(): WaitIdlingCallback = WaitIdlingResource.getInstance()