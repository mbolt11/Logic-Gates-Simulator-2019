/* NOT Gate Class for LGS Project */

public class NOT extends Gate
{
   private boolean output;
   
   //Constructor
   public NOT(int num_in)
   {
      super(num_in,gatetype.NOT);
      calculateOutput();
   }
   
   //Calculates result of gate
   public void calculateOutput()
   {
      //This is the opposite of its input line
      setOutput(inputs.get(0).getOutput());
   }
}