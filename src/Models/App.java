package Models;

public class App {
    private final static Program program=new Program();
    private final static Assembler assembler=new Assembler();
    private final static int dataBoundary=256; //this a memory address, not a byte address!
    private final static int stackBoundary=512;
    private final static int pcAddress=255;
    public static Program getProgram(){
        return program;
    }
    public static Assembler getAssembler(){
        return assembler;
    }
    public static int getDataBoundary(){
        return dataBoundary;
    }
    public static int getIntCodeFromHex(String value){
        int sum=0;
        for(int i=0;i<value.length();i++){
            int v=getNum(value.charAt(i));
            sum |= v<<(28-i*4);
        }
        return sum;
    }
    private static int getNum(char c){
        if(c>='0' && c<='9') return c-'0';
        return (c-'A')+10;
    }
    public static char getHexChar(int value){
        if(value>=0 && value<=9) return (char)('0'+value);
        return (char)('A'+value-10);
    }
    public static String getBinaryString(int value){
        StringBuilder builder=new StringBuilder();
        for(int i=0;i<32;i++){
            builder.append(value&1);
            value>>=1;
        }
        builder.reverse();
        return builder.toString();
    }
    public static String getHexString(int value){
        StringBuilder builder = new StringBuilder();
        builder.append("0x");
        for(int i=0;i<8;i++){
            builder.append(App.getHexChar((value>>(28-4*i))&15));
        }
        return builder.toString();
    }
    public static int getStackBoundary(){
        return stackBoundary;
    }
    public static int getPCAddress(){
        return pcAddress;
    }
}
