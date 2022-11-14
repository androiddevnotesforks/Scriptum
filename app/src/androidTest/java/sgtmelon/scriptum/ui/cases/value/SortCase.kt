package sgtmelon.scriptum.ui.cases.value

import sgtmelon.scriptum.infrastructure.model.key.preference.Sort

/**
 * Interface describes [Sort] tests.
 */
interface SortCase {

    fun sortChange() = startTest(Sort.CHANGE)

    fun sortCreate() = startTest(Sort.CREATE)

    fun sortRank() = startTest(Sort.RANK)

    fun sortColor() = startTest(Sort.COLOR)

    fun startTest(value: Sort)
}