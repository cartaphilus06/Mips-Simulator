package Models;

import Models.Enums.InstructionType;
import Models.Enums.RegisterConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Assembler {
    private final HashMap<String, Integer> symbolTable=new HashMap<>();
    public void assemble(ArrayList<String> text){
        lookFirst(text); //for setting up symbol table
        lookSecondTime(text);
    }
    public void run(){
        App.getProgram().run();
    }
    public void lookFirst(ArrayList<String> text) {
        int instructionCount = 0;
        for (String s : text) {
            s = s.trim();
            if (s.isEmpty()) continue;
            if (isLabel(s)) {
                symbolTable.put(getStringWithoutLastIndex(s), instructionCount);
            } else {
                instructionCount++;
            }
        }
    }
    public void lookSecondTime(ArrayList<String> text){
        Program program=App.getProgram();
        int memoryAddress=App.getDataBoundary();
        int labelCounts=0;
        for(int i=0;i<text.size();i++){
            String ins=text.get(i).trim();
            String[] parts=ins.split("\\s+");
            String insType = parts[0].trim();
            if(isLabel(insType)){
                labelCounts++;
                continue;
            }
            InstructionType type = InstructionType.getInstructionType(insType);
            char kind = type.getKind();
            int intCode;
            if (kind == 'r') {
                String regex="^.*\\$(?<rd>.*),\\s*\\$(?<rs>.*),\\s*\\$(?<rt>.*)$";
                Matcher m= Pattern.compile(regex).matcher(ins);
                if(m.matches()){
                    int rd= RegisterConstants.getNumberFromName(m.group("rd").trim());
                    int rs=RegisterConstants.getNumberFromName(m.group("rs").trim());
                    int rt=RegisterConstants.getNumberFromName(m.group("rt").trim());
                    int sa=0;
                    intCode=constructRTypeMC(type,rs,rt,rd,sa);
                    program.getMemoryByAddress(memoryAddress++).setContent(intCode);
                }
            } else if (kind == 'i') {
                if(type==InstructionType.BEQ){
                    String regex="^.*\\$(?<rt>.*),\\s*\\$(?<rs>.*),\\s*(?<label>.*)$";
                    Matcher m= Pattern.compile(regex).matcher(ins);
                    if(m.matches()){
                        int rt=RegisterConstants.getNumberFromName(m.group("rt").trim());
                        int rs=RegisterConstants.getNumberFromName(m.group("rs").trim());
                        String label=m.group("label").trim();
                        int currentLine=i-labelCounts;
                        int Imm=calculateOffsetForBeq(currentLine,label);
                        intCode=constructITypeMC(type,rt,rs,Imm);
                        program.getMemoryByAddress(memoryAddress++).setContent(intCode);
                    }
                }
                else{
                    String regex="^.*\\$(?<rt>.*),\\s*\\$(?<rs>.*),\\s*(?<Imm>-?\\d+)$";
                    Matcher m= Pattern.compile(regex).matcher(ins);
                    if(m.matches()){
                        int rt=RegisterConstants.getNumberFromName(m.group("rt").trim());
                        int rs=RegisterConstants.getNumberFromName(m.group("rs").trim());
                        int Imm=Integer.parseInt(m.group("Imm").trim());
                        intCode=constructITypeMC(type,rs,rt,Imm);
                        program.getMemoryByAddress(memoryAddress++).setContent(intCode);
                    }
                }
            } else if(kind == 'j'){
                String regex="^\\s*J|j\\s+(?<label>.*)$";
                Matcher m= Pattern.compile(regex).matcher(ins);
                if(m.matches()){
                    String label=m.group("label").trim();
                    int address=calculateAddressForJ(label);
                    intCode=constructJTypeMC(type,address);
                    program.getMemoryByAddress(memoryAddress++).setContent(intCode);
                }
            }
        }
    }
    private String getStringWithoutLastIndex(String s){
        StringBuilder n= new StringBuilder();
        for(int i=0;i<s.length()-1;i++){
            n.append(s.charAt(i));
        }
        return n.toString();
    }
    private boolean isLabel(String s){
        String regex="^[a-zA-Z0-9]+:$";
        Matcher m= Pattern.compile(regex).matcher(s);
        return m.matches();
    }
    public int constructRTypeMC(InstructionType type,int rs,int rt,int rd,int sa){
        int opcode=type.getOpcode();
        int funct=type.getFunct();
        int intCode=0;
        intCode |= (opcode&63)<<26;
        intCode |= (rs&31)<<21;
        intCode |= (rt&31)<<16;
        intCode |= (rd&31)<<11;
        intCode |= (sa&31)<<6;
        intCode |= (funct&63);
        return intCode;
    }
    public int constructITypeMC(InstructionType type,int rs,int rt,int Imm){
        int opcode=type.getOpcode();
        int intCode=0;
        intCode |= (opcode&63)<<26;
        intCode |= (rs&31)<<21;
        intCode |= (rt&31)<<16;
        intCode |= (Imm&65535);
        return intCode;
    }
    public int constructJTypeMC(InstructionType type,int address){
        int opcode=type.getOpcode();
        int intCode=0;
        intCode |= (opcode&63)<<26;
        intCode |= (address&67108863);
        return intCode;
    }
    public int calculateOffsetForBeq(int currentInstructionIndex, String targetLabel) {
        if (symbolTable.containsKey(targetLabel)) {
            int targetIndex = symbolTable.get(targetLabel);
            return targetIndex - (currentInstructionIndex + 1);
        }
        return 0;
    }

    public int calculateAddressForJ(String targetLabel) {
        if (symbolTable.containsKey(targetLabel)) {
            int targetIndex = symbolTable.get(targetLabel);
            return App.getDataBoundary() + targetIndex;
        }
        return 0;
    }
}
