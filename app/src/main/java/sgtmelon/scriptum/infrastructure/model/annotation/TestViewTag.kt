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
    }
}