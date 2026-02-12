package Models.Enums;

public enum InstructionType {
    ADD('r',0,32),
    SUB('r',0,34),
    SLT('r',0,42),
    ADDI('i',8,-1),
    LW('i',35,-1),
    SW('i',43,-1),
    BEQ('i',4,-1),
    J('j',2,-1);
    private final char kind;
    private final int opcode;
    private final int funct;
    InstructionType(char kind, int opcode, int funct){
        this.kind = kind;
        this.opcode = opcode;
        this.funct = funct;
    }
    public char getKind(){
        return kind;
    }
    public int getOpcode(){
        return opcode;
    }
    public int getFunct(){
        return funct;
    }
    public static InstructionType getInstructionType(int opcode,int funct){
        if(opcode==0){
            if(funct==32) return ADD;
            else if(funct==34) return SUB;
            else return SLT;
        }
        else if(opcode==8) return ADDI;
        else if(opcode==35) return LW;
        else if(opcode==43) return SW;
        else if(opcode==4) return BEQ;
        else return J;
    }
    public static char getKindByType(String type){
        for(InstructionType t:InstructionType.values()){
            if(t.name().equalsIgnoreCase(type)) return t.kind;
        }
        return '?';
    }
    public static InstructionType getInstructionType(String type){
        for(InstructionType t:InstructionType.values()){
            if(t.name().equalsIgnoreCase(type)) return t;
        }
        return null;
    }
}
