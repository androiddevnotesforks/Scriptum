package sgtmelon.scriptum.infrastructure.model.data


/**
 * Object for store information between screens and save it inside onSavedState.
 * Also it used inside application receivers.
 */
object IntentData {

    object Snackbar {

        object Intent {
            private const val PREFIX = "INTENT_SNACKBAR"
            const val POSITIONS = "${PREFIX}_POSITIONS"
            const val ITEMS = "${PREFIX}_ITEMS"
        }
    }

    object Main {

        object Intent {
            private const val PREFIX = "MAIN"
            const val FIRST_START = "${PREFIX}_FIRST_START"
            const val PAGE_CURRENT = "${PREFIX}_PAGE_CURRENT"
        }
    }

    object Note {

        object Intent {
            private const val PREFIX = "INTENT_NOTE_1231231"
            const val ID = "${PREFIX}_ID"
            const val COLOR = "${PREFIX}_COLOR"
            const val TYPE = "${PREFIX}_TYPE"
        }

        object Default {
            const val ID = -1L
            const val COLOR = -1
            const val TYPE = -1
        }
    }

    object Preference {

        object Intent {
            private const val PREFIX = "INTENT_PREFERENCE"
            const val SCREEN = "${PREFIX}_SCREEN"
        }

        object Default {
            const val SCREEN = -1
        }
    }

    object Print {

        object Intent {
            private const val PREFIX = "INTENT_PRINT"
            const val TYPE = "${PREFIX}_TYPE"
        }

        object Default {
            const val TYPE = -1
        }
    }

    object Eternal {

        object Intent {
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

        object Intent {
            private const val KEY_PREFIX = "OPEN_STATE"
            const val KEY_CHANGE = "${KEY_PREFIX}_CHANGE"
            const val KEY_VALUE = "${KEY_PREFIX}_VALUE"
            const val KEY_TAG = "${KEY_PREFIX}_TAG"
        }
    }
}