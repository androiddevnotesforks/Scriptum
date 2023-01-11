package sgtmelon.scriptum.infrastructure.model.exception.dao

/**
 * Exception for detecting cases when Dao throws a error while Entity insert.
 * Most likely this error is related to ForeignKey. But it could also be another one.
 */
class DaoForeignException(cause: Throwable) : Throwable(
    message = "Something happened during insertion to db.\n" +
            "May be it's related with ForeignKey (parent entity not exist).\n" +
            "Message: ${cause.message}",
    cause
)