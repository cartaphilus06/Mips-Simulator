package Models;

public class Memory {
    /*
    0-255: data
    256-511: program
    512-1023: stack
     */
    private int content;
    private final int address;
    public Memory(int address) {
        this.content = 0;
        this.address = address;
    }
    public void setContent(int content) {
        this.content = content;
    }
    public int getContent() {
        return content;
    }
}
