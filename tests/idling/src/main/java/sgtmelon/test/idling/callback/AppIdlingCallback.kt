package sgtmelon.test.idling.callback

interface AppIdlingCallback : ParentIdlingCallback {

    fun start(tag: String)

    fun stop(tag: String)

    fun change(isWork: Boolean, tag: String)

    fun printThrow(): Nothing
}