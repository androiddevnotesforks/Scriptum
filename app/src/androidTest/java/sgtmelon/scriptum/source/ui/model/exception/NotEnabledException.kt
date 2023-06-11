package sgtmelon.scriptum.source.ui.model.exception

class NotEnabledException(why: String) :
    IllegalAccessException("Apply button not enabled: $why")