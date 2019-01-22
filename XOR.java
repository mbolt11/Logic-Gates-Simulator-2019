/* XOR Gate Class for LGS Project */
import javax.swing.*;
import java.awt.*;

public class XOR extends Gate
{
   private boolean output;
   
   //Constructor
   public XOR(int num_in)
   {
      super(num_in,gatetype.XOR);
   }
   
   //Calculates result of gate // ERROR!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
   //I haven't figured out where to call this from yet
   public void calculateOutput()
   {
      boolean result = false;
      //Calculate result of XOR for all input lines
      if(inputs.size() >= 2)
      {boolean wire1 = inputs.get(0).getOutput();
      boolean wire2 = inputs.get(1).getOutput();
      result = ((wire1 && !wire2) || (!wire1 && wire2));}
      //Can't do more than 2 input lines for an XOR, right??
      
      //Set the output variable according to result
      setOutput(result);
   }
   
   public void draw(Graphics g, int row, int column, int maxColumn, int maxRow)
   {
   }
}