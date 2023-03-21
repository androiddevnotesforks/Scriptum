package sgtmelon.scriptum.infrastructure.model.annotation

/**
 * Tags for views, needed inside UI tests for correctly determinate each view.
 */
annotation class TestViewTag {
    companion object {
        const val MAIN = "MAIN"
        const val RANK = "RANK"
        const val NOTES = "NOTES"
        const val BIN = "BIN"

        const val ALARM = "ALARM"
        const val NOTIFICATIONS = "NOTIFICATIONS"

        const val PREF_MENU = "PREF_MENU"
        const val PREF_BACKUP = "PREF_BACKUP"
        const val PREF_NOTE = "PREF_NOTE"
        const val PREF_ALARM = "PREF_ALARM"

        const val NOTE = "NOTE"
        const val TEXT_NOTE = "TEXT_NOTE"
        const val ROLL_NOTE = "ROLL_NOTE"
    }
}