package sgtmelon.test.idling.callback

interface WaitIdlingCallback : ParentIdlingCallback {

    fun start(waitMillis: Long)
}