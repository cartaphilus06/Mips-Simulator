import Models.App;
import Models.Memory;
import Models.Program;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("0 for machine code, 1 for asm");
        Scanner scanner = new Scanner(System.in);
        int ans=scanner.nextInt();
        scanner.nextLine();
        System.out.println("Which Input do you want to run?");
        String fileName = scanner.nextLine();
        scanner.close();
        if(ans==0) {
            runMC(fileName);
        }
        else if(ans==1) {
            runAsm(fileName);
        }
    }
    public static void runMC(String fileName) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader("src/Inputs/MC/"+fileName));
        int memoryAddress=App.getDataBoundary();
        Program program=App.getProgram();
        while(br.ready()){
            Memory mem=program.getMemoryByAddress(memoryAddress++);
            mem.setContent(App.getIntCodeFromHex(br.readLine()));
        }
        br.close();
        program.run();
    }
    public static void runAsm(String fileName) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader("src/Inputs/Asm/"+fileName));
        ArrayList<String> text=new ArrayList<>();
        while(br.ready()){
            String line=br.readLine();
            if(!Pattern.compile("^\\s+$").matcher(line).matches()) text.add(line);
        }
        App.getAssembler().assemble(text);
        App.getAssembler().run();
    }
}