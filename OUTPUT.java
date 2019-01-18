/* OUTPUT Gate Class for LGS Project */

public class OUTPUT extends Gate
{
   //Constructor
   public OUTPUT(int num_in)
   {
      super(num_in,gatetype.OUTPUT);
   }
   
   //Calculates result of gate... 
   //I haven't figured out where to call this from yet
   public void calculateOutput()
   {
      //This is the same as its input line
      setOutput(inputs.get(0).getOutput());
   }
}