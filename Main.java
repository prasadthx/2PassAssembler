public class Main{
    public static void main(String args[]){
        PassOne passOne = new PassOne(args[0]);
        passOne.createIntermediateCode();
        PassTwo passTwo = new PassTwo("IntermediateCode.txt", passOne.SymbolTable, passOne.LiteralTable, passOne.PoolTable);
        passTwo.createMachineCode();
    }
}