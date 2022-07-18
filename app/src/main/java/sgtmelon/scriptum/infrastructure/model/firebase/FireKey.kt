package sgtmelon.scriptum.infrastructure.model.firebase

import androidx.annotation.StringDef

/**
 * Describes keys for firebase logs.
 *
 * Don't change keys after release.
 */
@StringDef(
    FireKey.RUN_TYPE,
    FireKey.IN_PREFERENCES
)
annotation class FireKey {
    companion object {
        const val RUN_TYPE = "RUN_TYPE"

        const val IN_PREFERENCES = "IN_PREFERENCES"
    }
}