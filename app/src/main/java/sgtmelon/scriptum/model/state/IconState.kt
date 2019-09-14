package sgtmelon.scriptum.model.state

/**
 * Состояние контроля анимации иконок
 */
class IconState {

    var animate = true

    fun notAnimate(func: () -> Unit) {
        animate = false
        func()
        animate = true
    }

}