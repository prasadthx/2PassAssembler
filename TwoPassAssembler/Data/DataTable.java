package TwoPassAssembler.Data;

import java.util.HashMap;

public class DataTable {
    HashMap<String, String> AD, IS, CC, DL, RG;

    public DataTable() {
        this.AD = new HashMap<String, String>();
        this.IS = new HashMap<String, String>();
        this.CC = new HashMap<String, String>();
        this.DL = new HashMap<String, String>();
        this.RG = new HashMap<String, String>();

        DL.put("DC", "01");
        DL.put("DS", "02");

        IS.put("STOP","00");
        IS.put("ADD","01");
        IS.put("SUB","02");
        IS.put("MULT","03");
        IS.put("MOVER","04");
        IS.put("MOVEM","05");
        IS.put("COMP","06");
        IS.put("BC","07");
        IS.put("DIV","08");
        IS.put("READ","09");
        IS.put("PRINT","10");

        CC.put("LT","01");
        CC.put("LE","02");
        CC.put("EQ","03");
        CC.put("GT","04");
        CC.put("GE","05");
        CC.put("ANY","06");

        AD.put("START","01");
        AD.put("END","02");
        AD.put("ORIGIN","03");
        AD.put("EQU","04");
        AD.put("LTORG","05");

        RG.put("AREG","01");
        RG.put("BREG","02");
        RG.put("CREG","03");
        RG.put("DREG","04");
    }

    public String getClassType(String instruction){
        instruction = instruction.toUpperCase();
       if (AD.containsKey(instruction)){
           return "AD";
       }
       else if (RG.containsKey(instruction)){
           return "RG";
       }
       else if (IS.containsKey(instruction)){
           return "IS";
       }
       else if (CC.containsKey(instruction)){
           return "CC";
       }
       else if (DL.containsKey(instruction))
           return "DL";
       else {
        try {
            int con = Integer.parseInt(instruction);
            return "C";
        } catch (Exception e) {
            if (instruction.startsWith("=")){
                return "L";
            }
            else return "S";
        }
       }    
    }

    public String getMnemonicCode(String instruction){
        instruction = instruction.toUpperCase();
        String className = getClassType(instruction);
        if (className == "AD")
            return AD.get(instruction);
        else if (className == "IS")
            return IS.get(instruction);
        else if (className == "RG")
            return RG.get(instruction);
        else if (className == "CC")
            return CC.get(instruction);
        else if (className == "DL")
            return DL.get(instruction);
        else {
            return instruction;
        }
    }

    public Boolean isLabel(String instruction){
        if ("S".equals(getClassType(instruction)))
            return true;
        
        else return false;
    }
}
