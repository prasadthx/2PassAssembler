import java.util.HashMap;

public class Table {
    public int TableIndex;
    private HashMap<String, Integer> TableContent;
    private HashMap<Integer, String> tableIndexHMap;
    
    public Table(){
        this.TableIndex = 0;
        this.TableContent = new HashMap<>();
        this.tableIndexHMap = new HashMap<>();
    }

    public void insertRow(String instruction, Integer location){
        TableContent.put(instruction, location);
        tableIndexHMap.put(TableIndex, instruction);
        TableIndex++;
    }

    public void setLocation(String instruction, Integer location){
        TableContent.put(instruction, location);
    }

    public void setLocationByTableIndex(Integer index, Integer location){
        String instruction = tableIndexHMap.get(index);
        TableContent.put(instruction, location);
    }

    public Integer getIndexByInstruction(String instruction){
        Integer index = -1;
        for (Integer key : tableIndexHMap.keySet()) {
            if (tableIndexHMap.get(key).equals(instruction)) {
                index = key;
                break;
            }
        }
        return index;
    }

    public Integer getLocationByInstruction(String instruction){
        if (TableContent.containsKey(instruction)){
            return TableContent.get(instruction);
        }
        else return -1;
    }

    public Integer getLocationByTableIndex(Integer index){
        String instruction = tableIndexHMap.get(index);
        return getLocationByInstruction(instruction);
    }

    public String getInstructionByTableIndex(Integer index){
        return tableIndexHMap.get(index);
    }
}
