package sgtmelon.scriptum.domain.model.data

import androidx.annotation.StringDef
import sgtmelon.scriptum.domain.model.annotation.Color

object IntentData {

    /**
     * Object for store information about NOTE between screens and save it
     * inside onSavedInstanceState.
     */
    object Note {
        @StringDef(Intent.ID, Intent.COLOR, Intent.TYPE)
        annotation class Intent {
            companion object {
                private const val PREFIX = "INTENT_NOTE"

                const val ID = "${PREFIX}_ID"
                const val COLOR = "${PREFIX}_COLOR"
                const val TYPE = "${PREFIX}_TYPE"
            }
        }

        annotation class Default {
            companion object {
                const val ID = -1L
                const val COLOR = Color.UNDEFINED
                const val TYPE = -1
            }
        }
    }
}