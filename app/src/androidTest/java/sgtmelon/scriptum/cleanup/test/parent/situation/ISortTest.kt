package sgtmelon.scriptum.cleanup.test.parent.situation

import sgtmelon.scriptum.infrastructure.model.key.Sort

/**
 * Interface describes [Sort] tests.
 */
interface ISortTest {

    fun sortChange() = startTest(Sort.CHANGE)

    fun sortCreate() = startTest(Sort.CREATE)

    fun sortRank() = startTest(Sort.RANK)

    fun sortColor() = startTest(Sort.COLOR)

    fun startTest(value: Sort)
}