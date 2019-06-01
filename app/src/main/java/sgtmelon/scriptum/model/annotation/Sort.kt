package sgtmelon.scriptum.model.annotation

import androidx.annotation.IntDef

/**
 * Ключи сортировки данных
 */
@IntDef(Sort.create, Sort.change, Sort.rank, Sort.color)
annotation class Sort {

    companion object {

        const val divider = ", "

        const val create = 0
        const val change = 1
        const val rank = 2
        const val color = 3

        const val def = "$create$divider$rank$divider$color"
    }

}