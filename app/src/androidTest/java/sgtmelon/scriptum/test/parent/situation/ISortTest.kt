package sgtmelon.scriptum.test.parent.situation

import sgtmelon.scriptum.domain.model.annotation.Sort

/**
 * Interface describes [Sort] tests.
 */
interface ISortTest {

    fun sortChange() = startTest(Sort.CHANGE)

    fun sortCreate() = startTest(Sort.CREATE)

    fun sortRank() = startTest(Sort.RANK)

    fun sortColor() = startTest(Sort.COLOR)

    fun startTest(@Sort sort: Int)

}