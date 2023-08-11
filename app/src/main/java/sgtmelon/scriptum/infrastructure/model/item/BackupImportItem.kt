package sgtmelon.scriptum.infrastructure.model.item

/**
 * Class for identify which way files will be imported
 */
sealed class BackupImportItem {
    data class AutoFetch(val name: String): BackupImportItem()
    data class Manual(val uri: String): BackupImportItem()
}