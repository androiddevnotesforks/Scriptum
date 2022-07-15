package sgtmelon.scriptum.cleanup.domain.model.annotation.firebase

import androidx.annotation.StringDef

/**
 * Describes keys for firebase logs.
 *
 * Don't change keys after release.
 */
@StringDef(FireKey.RUN_TYPE)
annotation class FireKey {
    companion object {
        const val RUN_TYPE = "RUN_TYPE"
    }
}