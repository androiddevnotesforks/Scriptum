package sgtmelon.handynotes.office.st;

public class StOpen {

    private boolean open = false;

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public void setOpen() {
        open = !open;
    }

}
