import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.io.FileWriter;

public class PassTwo {
    
    File file = null;
    Scanner scanner = null;
    Table SymbolTable = null;
    Table LiteralTable = null;
    ArrayList<Integer> PoolTable = null;
    FileWriter fWriter = null;
    Integer PoolPtr;
    
    public PassTwo(String IntermediateCode, Table SymbolTable, Table LiteralTable, ArrayList<Integer> PoolTable){
        try {
            file = new File(IntermediateCode);
            scanner = new Scanner(file);
        } catch (Exception e) {
            System.out.println(e);
        }
        this.SymbolTable = SymbolTable;
        this.LiteralTable = LiteralTable;
        this.PoolTable = PoolTable;
        this.PoolPtr = 0;
    }

    public void createMachineCode(){
        try {
            fWriter = new FileWriter("MachineCode.txt");
            while(scanner.hasNextLine()){
                String nextLine = getCodeByLine(scanner.nextLine());
                if (nextLine.equals("LTORGEND")) {
                    fWriter.write(ProcessLiterals());
                }
                else if (nextLine.equals("EMPTY")){
                    continue;
                }
                else fWriter.write(nextLine + "\n");
            }
            fWriter.close();
            System.out.println("PassTwo Completed");
            System.out.println("Machine Code Generated Successfully");
        } catch (Exception e) {
            System.out.println("Error:"+e);
        }
    }

    public String getCodeByLine(String code){
        String[] tempArray = code.split(" \\(|,|\\(|\\)");
        String codeLine = "";
        String code1 = "";
        String code2 = "";
        String code3 = "";
        
        int index=0;
        ArrayList<String> instructions = new ArrayList<>();
        ArrayList<String> Mcodes = new ArrayList<>();
        
        while(index < tempArray.length - 1){
            instructions.add(tempArray[index + 1]);
            Mcodes.add(tempArray[index + 2]);
            index+=3;
        }

        if (instructions.get(0).equals("AD") || instructions.get(1).equals("AD")){
            if ((Mcodes.get(0).equals("05")) || (Mcodes.get(0).equals("02")) ||
                (Mcodes.get(1).equals("05")) || (Mcodes.get(1).equals("05"))) {
                return "LTORGEND";
            }
            else{
                return "EMPTY";
            }
        }

        if (instructions.get(0).equals("S")) {
            if (instructions.get(1).equals("DL")){
                return "EMPTY";
            }
            else code1 = ProcessClass(instructions.get(1), Mcodes.get(1));
        }
        else{
            code1 = ProcessClass(instructions.get(0), Mcodes.get(0));
        }

        if (instructions.get(instructions.size() - 2).equals("RG")) {
            code2 = Mcodes.get(instructions.size() - 2);
        }
        else code2 = "00";

        code3 = ProcessClass(instructions.get(instructions.size()-1), Mcodes.get(Mcodes.size() - 1));
        codeLine = String.format("(%s) (%s) (%s)", code1, code2, code3);
        return codeLine;
    }

    public String ProcessClass(String ClassType, String Mcode){
        switch (ClassType){
            case "IS":{
                return Mcode;
            }
            case "S":{
                return SymbolTable.getLocationByTableIndex(Integer.parseInt(Mcode)).toString();
            }
            case "L":{
                return LiteralTable.getLocationByTableIndex(Integer.parseInt(Mcode)).toString();
            }
            default: return "";
        }
    }

    private String ProcessLiterals(){
        int index = PoolTable.get(PoolPtr);
        String codeLine = "";
        
        while(index != PoolTable.get(PoolPtr+1)){
            codeLine += "(00) (00) (" + LiteralTable.getInstructionByTableIndex(index).split("'|='|=")[1] + ")\n";
            index++;
        }
        PoolPtr = index;
        return codeLine;
    }
}
