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
   
   public boolean calculateOutput()
   {
      boolean result;
      if(inputs.size() == 0)
      {
         result = false;
      }
      else if(inputs.size() == 1)
      {
         result = inputs.get(0).calculateOutput();
      }
      else
      {
         boolean wire1 = inputs.get(0).calculateOutput();
         boolean wire2 = inputs.get(1).calculateOutput();
         result = wire1 || wire2;
         for(int i=2; i<inputs.size(); i++)
         {
            result = result || inputs.get(i).calculateOutput();
         }
      }
      
      //Negate if it is a NOR
      if(negate)
      {
         result = !result;
      }
      
      //Set the output variable according to result
      setOutput(result);
      //System.out.println("OR is:"+output);
      return output;
   }
   
   public void drawGate(Graphics g, int row, int column, int maxColumn, int maxRow)
   {
      ////System.out.println("OR drawn");
      xStart = (column * colSeperation) - 150 + (row*rowShift);
      yStart = (row * rowSeperation) + 65 + (column*columnShift);
      //The finish points are estimates for now
      xFinish = xStart + 100;
      yFinish = yStart + 95;
      //draw the first arc
      g.drawArc(xStart - 105, yStart - 25, 100, 150, 40, -80);
      //draw second arc
      g.drawArc(xStart - 100, yStart, 200, 150, 100, -80);
      //draw third arc
      g.drawArc(xStart - 100, yStart - 50, 200, 150, -100, 80);
      
      xInputWireSlot = xStart - 10;
      yInputWireSlot = yStart + 47;
      xOutputWireSlot = xInputWireSlot + 105;
      yOutputWireSlot = yInputWireSlot + 3;
      
      if(negate)
      {
         int circleXStart = xOutputWireSlot;
         int circleYStart = yStart + 45;
         g.drawOval(circleXStart, circleYStart, 10, 10);
         xOutputWireSlot += 10;
      }
      
      //System.out.println("OR drawn at row,column: "+ row + "," +column + " at coord: "+ xStart + "," + yStart);
   }
   
   public void redrawGate(Graphics g)
   {
      //Recalculate finish spots based on updated starts
      xFinish = xStart + 100;
      yFinish = yStart + 95;
      
      //draw the first arc
      g.drawArc(xStart - 105, yStart - 25, 100, 150, 40, -80);
      //draw second arc
      g.drawArc(xStart - 100, yStart, 200, 150, 100, -80);
      //draw third arc
      g.drawArc(xStart - 100, yStart - 50, 200, 150, -100, 80);
      
      xInputWireSlot = xStart - 10;
      yInputWireSlot = yStart + 47;
      xOutputWireSlot = xInputWireSlot + 105;
      yOutputWireSlot = yInputWireSlot + 3;
      
      if(negate)
      {
         int circleXStart = xOutputWireSlot;
         int circleYStart = yStart + 45;
         g.drawOval(circleXStart, circleYStart, 10, 10);
         xOutputWireSlot += 10;
      }
   }
}