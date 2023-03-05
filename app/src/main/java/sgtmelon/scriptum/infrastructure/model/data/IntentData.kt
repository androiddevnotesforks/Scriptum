package sgtmelon.scriptum.infrastructure.model.data


/**
 * Object for store information between screens and save it inside onSavedState.
 * Also it used inside application receivers.
 */
object IntentData {

    object Splash {

        object Key {
            private const val PREFIX = "INTENT_SPLASH"
            const val OPEN = "${PREFIX}_OPEN"
        }
    }

    object Note {

        object Key {
            private const val PREFIX = "INTENT_NOTE"
            const val IS_EDIT = "${PREFIX}_IS_EDIT"
            const val STATE = "${PREFIX}_STATE"
            const val ID = "${PREFIX}_ID"
            const val TYPE = "${PREFIX}_TYPE"
            const val COLOR = "${PREFIX}_COLOR"
            const val NAME = "${PREFIX}_NAME"
        }

        object Default {
            const val IS_EDIT = false
            const val ID = -1L
            val COLOR = null
            const val NAME = ""
        }
    }

    object Preference {

        object Key {
            private const val PREFIX = "INTENT_PREFERENCE"
            const val SCREEN = "${PREFIX}_SCREEN"
        }
    }

    object Print {

        object Key {
            private const val PREFIX = "INTENT_PRINT"
            const val TYPE = "${PREFIX}_TYPE"
        }
    }

    object Eternal {

        object Key {
            private const val PREFIX = "INTENT_BIND"
            const val COUNT = "${PREFIX}_COUNT"
            const val DATE = "${PREFIX}_DATE"
            const val TOAST = "${PREFIX}_TOAST"
        }

        object Default {
            const val COUNT = -1
            const val TOAST = true
        }
    }

    object Open {

        object Key {
            private const val KEY_PREFIX = "OPEN_STATE"
            const val KEY_CHANGE = "${KEY_PREFIX}_CHANGE"
            const val KEY_VALUE = "${KEY_PREFIX}_VALUE"
            const val KEY_TAG = "${KEY_PREFIX}_TAG"
        }
    }
}