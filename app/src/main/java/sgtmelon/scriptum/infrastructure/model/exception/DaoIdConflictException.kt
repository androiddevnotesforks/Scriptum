package sgtmelon.scriptum.infrastructure.model.exception

/**
 * Exception for detecting cases when Dao returns IGNORE case on insert operation.
 */
class DaoIdConflictException : Throwable("Dao OnConflictStrategy.IGNORE was called during insert")