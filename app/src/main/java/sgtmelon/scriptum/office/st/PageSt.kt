package sgtmelon.scriptum.office.st

/**
 * Состояние для страниц, сохраняющее её номер
 */
class PageSt {

    var page: Int = 0

    constructor() {

    }

    constructor(page: Int) {
        this.page = page
    }

}