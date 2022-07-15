package sgtmelon.scriptum.cleanup.domain.model.annotation

import androidx.annotation.IntDef

/**
 * Describes standard sorting types for notes
 */
@IntDef(Sort.CHANGE, Sort.CREATE, Sort.RANK, Sort.COLOR)
annotation class Sort {
    companion object {
        const val CHANGE = 0
        const val CREATE = 1
        const val RANK = 2
        const val COLOR = 3

        val list = listOf(CHANGE, CREATE, RANK, COLOR)
    }
}