package TwoPassAssembler.Passes;

import TwoPassAssembler.Data.DataTable;
import TwoPassAssembler.Data.Table;

import java.io.File;
import java.util.*;
import java.util.HashMap;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;

public class PassOne {
        
        File file = null;
        Scanner scanner = null;
        
        public PassOne(String fileName){
            try {
                file = new File(fileName);
                scanner = new Scanner(file);
            } catch (Exception e) {
                System.out.println(e);
            }
            
            PoolTable.add(0);
        }
        
        String[] array;
        private int arrayLineNo = 0;
        private int locationCounter = 0;
        DataTable dataTable = new DataTable();
        FileWriter fWriter;

        boolean endCode = false;

        public Table SymbolTable = new Table();
        public Table LiteralTable = new Table();
        
        public ArrayList<Integer> PoolTable = new ArrayList<Integer>();

        public void createIntermediateCode(){
            try {
                fWriter = new FileWriter("IntermediateCode.txt");
                while(scanner.hasNextLine()){
                    String nextLine = getCodeByLine(scanner.nextLine());
                    if(nextLine == "Invalid code"){
                        fWriter.write("Invalid input at line: " + arrayLineNo);
                        fWriter.close();
                        break;
                    }
                    else if(nextLine == "LTORGEND"){
                        if (endCode) {
                            fWriter.write(ProcessLiterals("END"));
                            System.out.println("PassOne Completed");
                            fWriter.close();
                            return;
                        }
                        else{
                            fWriter.write(ProcessLiterals("LTORG"));
                        }
                        
                    }
                    else{
                        fWriter.write(nextLine + "\n");
                    }
                }
                fWriter.close();
            } catch (Exception e) {
                System.out.println("Error"+e);
            }
        }

        private String getCodeByLine(String line){
            String[] tempArray = line.split(" |,|\\+");
            String codeLine = "";
            if ( (arrayLineNo == 0 && !(tempArray[0].toUpperCase().equals("START"))) ||
                 (arrayLineNo != 0 && (tempArray[0].toUpperCase().equals("START")))){
                     System.out.println(tempArray[0]);
                     System.out.println(arrayLineNo);
                     System.err.println("Invalid code");
                     return "Invalid code";
            }
            if (arrayLineNo == 0){
                locationCounter = Integer.parseInt(tempArray[tempArray.length - 1]);
            }
            if (dataTable.isLabel(tempArray[0])){
                if (SymbolTable.getLocationByInstruction(tempArray[0]) == -1) {
                    SymbolTable.insertRow(tempArray[0], locationCounter);
                }
                else{
                    SymbolTable.setLocation(tempArray[0], locationCounter);
                } 
            }
            arrayLineNo++;

            for (String instruction : tempArray) {
                if (dataTable.getClassType(instruction).equals("AD")) {
                    if (instruction.equals("ORIGIN")) {
                        codeLine = ProcessOrigin(tempArray);
                        return codeLine;
                    }
                    else if (instruction.equals("LTORG")){
                        return "LTORGEND";
                    }
                    else if (instruction.equals("END")){
                        endCode = true;
                        return "LTORGEND";
                    }
                    else if (instruction.equals("START")) {
                        codeLine += ProcessIC(instruction);
                    }
                }
                
                codeLine = codeLine + ProcessCode(instruction);
            }
            
            return codeLine;
        }

        private String ProcessCode(String instruction){
            switch(dataTable.getClassType(instruction)){
                case "IS":{
                    locationCounter++;
                    return ProcessIC(instruction);
                }
                case "DL":{
                    return ProcessIC(instruction);
                }
                case "RG":{
                    return ProcessIC(instruction);
                }
                case "C":{
                    return ProcessIC(instruction);
                }
                case "S":{
                    if (SymbolTable.getLocationByInstruction(instruction) == -1) {
                        SymbolTable.insertRow(instruction, -2);
                    }
                    return "(S," + SymbolTable.getIndexByInstruction(instruction) + ") ";
                }
                case "L":{
                    if (LiteralTable.getLocationByInstruction(instruction) == -1) {
                        LiteralTable.insertRow(instruction, -2);
                    }
                    return "(L," + LiteralTable.getIndexByInstruction(instruction) + ") ";
                }
                default: return "";
            }
        }

        private String ProcessLiterals(String instruction){
            int index = 0;
            if (LiteralTable.TableIndex == 0) {
                return "";
            }
            String instructionMCode = ProcessIC(instruction);
            String codeLine = "";

            index = PoolTable.get(PoolTable.size()-1);
            while (LiteralTable.getLocationByTableIndex(index) == -2){
                LiteralTable.setLocationByTableIndex(index, locationCounter);
                String[] literals = LiteralTable.getInstructionByTableIndex(index).split("'|,='|,=");
                String literal = literals[1];
                codeLine += instructionMCode + "(DL,02) (C," + literal + ") \n";
                locationCounter++;
                index++;
            }
            PoolTable.add(index);
            if (codeLine.equals("")) codeLine=instructionMCode;
            return codeLine;
        }

        public String ProcessOrigin(String[] lineInstructions){
            int index = 0;
            while (index < lineInstructions.length) {
                if(lineInstructions[index].equals("ORIGIN")){
                    break;
                }
                index++;
            }
            index++;
            Integer locationTargetLabel = SymbolTable.getLocationByInstruction(lineInstructions[index]);
            locationCounter = locationTargetLabel + Integer.parseInt(lineInstructions[++index]);
            return ProcessIC("ORIGIN") + "(C," + locationCounter + ") ";
        }

        public String ProcessIC(String instruction){
            String instructionClassType = dataTable.getClassType(instruction);
            String instructionMCode = dataTable.getMnemonicCode(instruction);
            String IC = "(" + instructionClassType + "," + instructionMCode + ") ";
            return IC;
        }

    
}

