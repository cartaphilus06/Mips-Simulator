package Models;

import Models.Enums.RegisterConstants;

public class Program implements Runnable{
    private int programCounter;
    private final Memory[] memory = new Memory[1024];
    private final Register[] registers = new Register[32];
    public Program(){
        programCounter=App.getDataBoundary()*4;
        for (int i = 0; i < 1024; i++) {
            memory[i] = new Memory(i);
        }
        for (int i = 0; i < 32; i++) {
            registers[i]=new Register(i);
        }
        memory[App.getPCAddress()].setContent(programCounter);
        registers[29].setContent(4092); //stack pointer at the top of the memory
    }
    @Override
    public void run(){
        while(!isEOF()){
            int machineCode=getMemoryByByteAddress(programCounter).getContent();
            Instruction eqvIns=Instruction.getInstruction(machineCode);
            updateProgramCounter();
            instructionDecode(eqvIns);
            if(checkStackOverflow()) {
                System.err.println("Stack Overflow!");
                break;
            }
        }
        showRegisterContents();
    }
    public void instructionDecode(Instruction instruction){
        switch (instruction.getType()){
            case ADD -> add(instruction);
            case SUB -> sub(instruction);
            case SLT -> slt(instruction);
            case ADDI -> addi(instruction);
            case LW -> lw(instruction);
            case SW -> sw(instruction);
            case BEQ -> beq(instruction);
            case J -> j(instruction);
        }
    }
    public void add(Instruction instruction){
        Register rs=getRegisterByNumber(instruction.getRs());
        Register rt=getRegisterByNumber(instruction.getRt());
        Register rd=getRegisterByNumber(instruction.getRd());
        rd.setContent(rs.getContent()+rt.getContent());
    }
    public void sub(Instruction instruction){
        Register rs=getRegisterByNumber(instruction.getRs());
        Register rt=getRegisterByNumber(instruction.getRt());
        Register rd=getRegisterByNumber(instruction.getRd());
        rd.setContent(rs.getContent()-rt.getContent());
    }
    public void addi(Instruction instruction){
        Register rs=getRegisterByNumber(instruction.getRs());
        Register rt=getRegisterByNumber(instruction.getRt());
        rt.setContent(rs.getContent()+instruction.getImm());
    }
    public void slt(Instruction instruction){
        Register rs=getRegisterByNumber(instruction.getRs());
        Register rt=getRegisterByNumber(instruction.getRt());
        Register rd=getRegisterByNumber(instruction.getRd());
        rd.setContent(rs.getContent()<rt.getContent()?1:0);
    }
    public void lw(Instruction instruction){
        Register rs=getRegisterByNumber(instruction.getRs());
        Register rt=getRegisterByNumber(instruction.getRt());
        rt.setContent(getMemoryByAddress((rs.getContent()+instruction.getImm())/4).getContent());
    }
    public void sw(Instruction instruction){
        Register rs=getRegisterByNumber(instruction.getRs());
        Register rt=getRegisterByNumber(instruction.getRt());
        getMemoryByAddress((rs.getContent()+instruction.getImm())/4).setContent(rt.getContent());
    }
    public void beq(Instruction instruction){
        Register rs=getRegisterByNumber(instruction.getRs());
        Register rt=getRegisterByNumber(instruction.getRt());
        if(rs.getContent()==rt.getContent()){
            programCounter+=(instruction.getImm()<<2);
        }
    }
    public void j(Instruction instruction){
        programCounter=instruction.getAddress()<<2;
    }
    public void updateProgramCounter(){
        programCounter+=4;
        memory[App.getPCAddress()].setContent(programCounter);
    }
    private boolean isEOF(){
        return getMemoryByByteAddress(programCounter).getContent()==0;
    }
    public void showRegisterContents(){
        for(int i=0;i<registers.length;i++){
            Register register=registers[i];
            System.out.printf("Register %d (%s): %d\n",i,
                    RegisterConstants.valueOf(i).getDisplayName(),
                    register.getContent());
        }
    }
    public Memory getMemoryByByteAddress(int address){
        return memory[address/4];
    }
    public Memory getMemoryByAddress(int address){
        return memory[address];
    }
    public Register getRegisterByNumber(int number){
        return registers[number];
    }
    public boolean checkStackOverflow(){
        return (registers[29].getContent()/4) < App.getStackBoundary();
    }
}
