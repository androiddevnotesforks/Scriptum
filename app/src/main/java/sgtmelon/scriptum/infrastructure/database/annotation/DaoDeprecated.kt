package sgtmelon.scriptum.infrastructure.database.annotation

/**
 * Annotation class for describes deprecation
 */
annotation class DaoDeprecated {
    companion object {

        const val INSERT_IGNORE =
            "Use a safe function for insert with OnConflictStrategy.IGNORE.\n" +
                    "Cause it may return -1 when onConflict happen."

        const val LIST_OVERFLOW =
            "Use a safe function when use list in arguments. It's may cause crash on '?' overflow."
    }
}