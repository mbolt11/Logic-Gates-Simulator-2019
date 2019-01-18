/* AND and NAND Gate Class for LGS Project */

public class AND extends Gate
{
   private boolean negate;
   
   //Constructor
   public AND(int num_in, boolean negate_in)
   {
      super(num_in,gatetype.AND);
      negate = negate_in;
   }
   
   //Calculates result of gate
   public void calculateOutput()
   {
      //Calculate result of AND for all input lines
      boolean wire1 = inputs.get(0).getOutput();
      boolean wire2 = inputs.get(1).getOutput();
      boolean result = wire1 && wire2;
      for(int i=2; i<inputs.size(); i++)
      {
         result = result && inputs.get(i).getOutput();
      }
      
      //Negate if it is a NAND
      if(negate)
      {
         result = !result;
      }
      
      //Set the output variable according to result
      setOutput(result);
   }
}