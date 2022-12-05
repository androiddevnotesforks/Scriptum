package sgtmelon.scriptum.infrastructure.model.exception

/**
 * Exception for detecting cases when backup parsing goes wrong.
 */
class BackupParserException(cause: Throwable) : Throwable(cause)