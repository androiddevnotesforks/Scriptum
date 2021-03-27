package sgtmelon.scriptum.domain.model.data

import androidx.annotation.StringDef
import sgtmelon.scriptum.domain.model.annotation.Color

object IntentData {

    object Snackbar {
        @StringDef(Intent.POSITIONS, Intent.ITEMS)
        annotation class Intent {
            companion object {
                private const val PREFIX = "INTENT_SNACKBAR"

                const val POSITIONS = "${PREFIX}_POSITIONS"
                const val ITEMS = "${PREFIX}_ITEMS"
            }
        }
    }

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

    /**
     * Object for store information about NOTE between screens and save it
     * inside onSavedInstanceState.
     */
    object Print {
        @StringDef(Intent.TYPE)
        annotation class Intent {
            companion object {
                private const val PREFIX = "INTENT_PRINT"

                const val TYPE = "${PREFIX}_TYPE"
            }
        }

        annotation class Default {
            companion object {
                const val TYPE = -1
            }
        }
    }
}