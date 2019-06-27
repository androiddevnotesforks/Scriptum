package sgtmelon.scriptum.model.annotation

import androidx.annotation.IntDef

/**
 * Для описания стандартных сортировок заметок
 *
 * @author SerjantArbuz
 */
@IntDef(Sort.change, Sort.create, Sort.rank, Sort.color)
annotation class Sort {

    companion object {
        const val change = 0
        const val create = 1
        const val rank = 2
        const val color = 3
    }

}