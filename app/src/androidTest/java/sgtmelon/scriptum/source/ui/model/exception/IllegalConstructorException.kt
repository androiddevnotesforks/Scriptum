package sgtmelon.scriptum.source.ui.model.exception

/**
 * Exception for catch moments when developer somehow not pass correct data into constructor.
 */
class IllegalConstructorException :
    NullPointerException("Need provide one of constructor arguments")