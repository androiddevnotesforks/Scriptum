package sgtmelon.scriptum.infrastructure.model.exception.dao

/**
 * Exception for detecting cases when Dao returns IGNORE case on insert operation.
 */
class DaoConflictIdException : Throwable(
    message = "Dao OnConflictStrategy.IGNORE was called during insertion to db."
)