package sgtmelon.scriptum.infrastructure.database.model

/**
 * Class for describes dao deprecations.
 */
object DaoDeprecated {
    private const val COMBINATION = "Here a combination of possible errors:"

    const val INSERT_IGNORE =
        "Use a safe function for insert with OnConflictStrategy.IGNORE.\n" +
                "Cause it may return -1 when onConflict happen."

    const val INSERT_FOREIGN_KEY =
        "Use a safe function for inserting an entity, because may happen crash related with" +
                "FOREIGN KEY (if you forgot to insert a related entity)."

    const val INSERT_IGNORE_OR_KEY = COMBINATION
        .plus(other = "\n")
        .plus(INSERT_FOREIGN_KEY)
        .plus(other = "\n")
        .plus(INSERT_IGNORE)

    const val LIST_OVERFLOW =
        "Use a safe function when use list in arguments. It's may cause crash on '?' overflow."
}