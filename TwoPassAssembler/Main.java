package TwoPassAssembler;

import TwoPassAssembler.Passes.PassOne;
import TwoPassAssembler.Passes.PassTwo;

public class Main{
    public static void main(String args[]){
        PassOne passOne = new PassOne("input.asm");
        passOne.createIntermediateCode();
        PassTwo passTwo = new PassTwo("IntermediateCode.txt", passOne.SymbolTable, passOne.LiteralTable, passOne.PoolTable);
        passTwo.createMachineCode();
    }
}