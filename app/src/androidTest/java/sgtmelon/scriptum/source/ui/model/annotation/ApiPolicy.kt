package sgtmelon.scriptum.source.ui.model.annotation

/**
 * Annotation describe the way how work with [SpecificApi].
 */
annotation class ApiPolicy {
    companion object {
        /** Run tests only on specific api version. */
        const val STRICT = 0
        /** Test run can be launch on any api version. */
        const val LIGHT = 1
    }
}
