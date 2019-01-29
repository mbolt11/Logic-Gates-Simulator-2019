/* OR and NOR Gate Class for LGS Project */
import javax.swing.*;
import java.awt.*;

public class OR extends Gate
{
   private boolean negate;
   
   //Constructor
   public OR(int num_in, boolean negate_in)
   {
      super(num_in,gatetype.OR);
      if(negate_in)
      {
         changeType(gatetype.NOR);
      }
      
      negate = negate_in;
   }
   
   //Calculates result of gate // ERROR!!!!!!!!!!!!!!!!!!!!!!
   //I haven't figured out where to call this from yet
   public boolean calculateOutput()
   {
      boolean result = false;
      //Calculate result of OR for all input lines
      if(inputs.size() >= 2)
      {boolean wire1 = inputs.get(0).calculateOutput();
      boolean wire2 = inputs.get(1).calculateOutput();
      result = wire1 || wire2;
      for(int i=2; i<inputs.size(); i++)
      {
         result = result || inputs.get(i).calculateOutput();
      }}
      
      //Negate if it is a NOR
      if(negate)
      {
         result = !result;
      }
      
      //Set the output variable according to result
      setOutput(result);
      System.out.println("OR is:"+output);
      return output;
   }
   
   public void drawGate(Graphics g, int row, int column, int maxColumn, int maxRow)
   {
      //System.out.println("OR drawn");
      int xStart = ((int) (((double)column/maxColumn) * 1000)) - 150;
      int yStart = ((int) (((double)row/maxRow) * 950)) + 65;
      //draw the first arc
      g.drawArc(xStart - 105, yStart - 25, 100, 150, 40, -80);
      //draw second arc
      g.drawArc(xStart - 100, yStart, 200, 150, 100, -80);
      //draw third arc
      g.drawArc(xStart - 100, yStart - 50, 200, 150, -100, 80);
      
      xInputWireSlot = xStart;
      yInputWireSlot = yStart + 47;
      xOutputWireSlot = xInputWireSlot + 100;
      yOutputWireSlot = yInputWireSlot;
      
      System.out.println("OR drawn at row,column: "+ row + "," +column + " at coord: "+ xStart + "," + yStart);
   }
}