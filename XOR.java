/* XOR Gate Class for LGS Project */

public class XOR extends Gate
{
   private boolean output;
   
   //Constructor
   public XOR(int num_in)
   {
      super(num_in,gatetype.XOR);
      calculateOutput();
   }
   
   //Calculates result of gate
   public void calculateOutput()
   {
      //Calculate result of XOR for all input lines
      boolean wire1 = inputs.get(0).getOutput();
      boolean wire2 = inputs.get(1).getOutput();
      boolean result = ((wire1 && !wire2) || (!wire1 && wire2));
      //Can't do more than 2 input lines for an XOR, right??
      
      //Set the output variable according to result
      setOutput(result);
   }
}