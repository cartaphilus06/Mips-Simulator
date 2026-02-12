package Models;

import Models.Enums.InstructionType;
import Models.Enums.RegisterConstants;

public class Instruction {
    private InstructionType type;
    private int machineCode;
    private int rs;
    private int rt;
    private int rd;
    private int sa;
    private int imm;
    private int address;
    public Instruction(InstructionType type,int machineCode, int rs, int rt, int rd,int sa, int  imm,int address) {
        this.machineCode = machineCode;
        this.type = type;
        this.rs = rs;
        this.rt = rt;
        this.rd = rd;
        this.sa = sa;
        this.imm = imm;
        this.address = address;
    }
    public InstructionType getType() {
        return type;
    }
    public int getRs() {
        return rs;
    }
    public int getRt() {
        return rt;
    }
    public int getRd() {
        return rd;
    }
    public int getSa() {
        return sa;
    }
    public int getImm() {
        return imm;
    }
    public int getAddress() {
        return address;
    }
    public String getInstructionFormat(){
        StringBuilder builder = new StringBuilder();
        builder.append(type.name()).append(" ");
        if(type.getKind()=='r'){
            RegisterConstants Rs=RegisterConstants.valueOf(getRs());
            RegisterConstants Rt=RegisterConstants.valueOf(getRt());
            RegisterConstants Rd=RegisterConstants.valueOf(getRd());
            builder.append(Rd.getDisplayName()).append(", ")
                    .append(Rs.getDisplayName()).append(", ")
                    .append(Rt.getDisplayName());
        }
        else if(type.getKind()=='i'){
            RegisterConstants Rs=RegisterConstants.valueOf(getRs());
            RegisterConstants Rt=RegisterConstants.valueOf(getRt());
            if(type==InstructionType.SW || type==InstructionType.LW){
                builder.append(Rt.getDisplayName()).append(", ")
                        .append(getImm()).append("(")
                        .append(Rs.getDisplayName()).append(")");
            }
            else {
                builder.append(Rt.getDisplayName()).append(", ")
                        .append(Rs.getDisplayName()).append(", ")
                        .append(getImm());
            }
        }
        else{
            builder.append(getAddress());
        }
        return builder.toString();
    }
    public static Instruction getInstruction(int intCode) {
        int opcode=(intCode>>26)&63;
        int funct=intCode&63;
        InstructionType type=InstructionType.getInstructionType(opcode,funct);
        int rs=(intCode>>21)&31;
        int rt=(intCode>>16)&31;
        int rd=(intCode>>11)&31;
        int sa=(intCode>>6)&31;
        int imm = (short)(intCode & 65535);
        int address=intCode&67108863;
        return new Instruction(type,intCode,rs,rt,rd,sa,imm,address);
    }
//    public static Instruction getInstruction(String instruction){
//        //ADD $t0, $t1, $t2
//        String[] parts=instruction.split("\\s+")
//    }

}
