package sgtmelon.scriptum.basic.extension

import org.junit.Assert.assertNotEquals

// TODO remove

fun waitBefore(time: Long, func: () -> Unit = {}) {
    Thread.sleep(time)
    func()
}

fun waitAfter(time: Long, func: () -> Unit = {}) {
    func()
    Thread.sleep(time)
}

fun <E> Array<E>.getDifferentValues(): Pair<E, E> {
    if (size <= 1) throw IllegalArgumentException("Values must have size greater than 1")

    val firstValue = random()
    var secondValue = random()
    while (secondValue == firstValue) {
        secondValue = random()
    }

    assertNotEquals(firstValue, secondValue)

    return firstValue to secondValue
}

fun <E> Collection<E>.getDifferentValues(): Pair<E, E> {
    if (size <= 1) throw IllegalArgumentException("Values must have size greater than 1")

    val firstValue = random()
    var secondValue = random()
    while (secondValue == firstValue) {
        secondValue = random()
    }

    assertNotEquals(firstValue, secondValue)

    return firstValue to secondValue
}