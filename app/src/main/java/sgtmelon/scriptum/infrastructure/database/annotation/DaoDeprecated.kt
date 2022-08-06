package sgtmelon.scriptum.infrastructure.database.annotation

/**
 * Annotation class for describes deprecation
 */
annotation class DaoDeprecated {
    companion object {

        const val INSERT_IGNORE =
            "Use a safe function for insert with OnConflictStrategy.IGNORE.\n" +
                    "Cause it may return -1 when onConflict happen."

        const val INSERT_FOREIGN_KEY =
            "Use a safe function for inserting an entity, because may happen crash related with" +
                    "FOREIGN KEY (if you forgot to insert a related entity)."

        const val LIST_OVERFLOW =
            "Use a safe function when use list in arguments. It's may cause crash on '?' overflow."
    }
}