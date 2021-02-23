package sgtmelon.scriptum.basic.extension

// TODO remove

fun waitBefore(time: Long, func: () -> Unit = {}) {
    Thread.sleep(time)
    func()
}

fun waitAfter(time: Long, func: () -> Unit = {}) {
    func()
    Thread.sleep(time)
}