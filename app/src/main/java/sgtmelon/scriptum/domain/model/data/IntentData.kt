package sgtmelon.scriptum.domain.model.data

import androidx.annotation.StringDef
import sgtmelon.scriptum.domain.model.annotation.Color

/**
 * Object for store information between screens and save it
 * inside onSavedInstanceState. Also it used inside application receivers.
 */
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

    object Main {
        @StringDef(Intent.FIRST_START, Intent.PAGE_CURRENT)
        annotation class Intent {
            companion object {
                private const val PREFIX = "MAIN"

                const val FIRST_START = "${PREFIX}_FIRST_START"
                const val PAGE_CURRENT = "${PREFIX}_PAGE_CURRENT"
            }
        }
    }

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

    object Preference {

        @StringDef(Intent.SCREEN)
        annotation class Intent {
            companion object {
                private const val PREFIX = "INTENT_PREFERENCE"

                const val SCREEN = "${PREFIX}_SCREEN"
            }
        }

        annotation class Default {
            companion object {
                const val SCREEN = -1
            }
        }
    }

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

    object Eternal {

        @StringDef(Intent.COUNT)
        annotation class Intent {
            companion object {
                private const val PREFIX = "INTENT_BIND"

                const val COUNT = "${PREFIX}_COUNT"
                const val DATE = "${PREFIX}_DATE"
                const val TOAST = "${PREFIX}_TOAST"
            }
        }

        annotation class Default {
            companion object {
                const val COUNT = -1
                const val TOAST = true
            }
        }
    }
}