package Models;

public class Register {
    private int content;
    private final int number;
    public Register(int number) {
        content = 0;
        this.number = number;
    }
    public int getContent() {
        return content;
    }
    public void setContent(int content) {
        if(this.number != 0) {
            this.content = content;
        }
    }
}
