package sgtmelon.scriptum.office.st;

/**
 * Состояние для страниц, сохраняющее её номер
 */
public final class PageSt {

    private int page;

    public PageSt() {

    }

    public PageSt(int page) {
        this.page = page;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

}
