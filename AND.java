/* AND and NAND Gate Class for LGS Project */
import javax.swing.*;
import java.awt.*;

public class AND extends Gate
{
   private boolean negate;
   private int xInputWireSlot, yInputWireSlot, xOutputWireSlot, yOutputWireSlot;
   
   //Constructor
   public AND(int num_in, boolean negate_in)
   {
      super(num_in,gatetype.AND);
      if(negate_in)
      {
         changeType(gatetype.NAND);
      }
      
      negate = negate_in;
      xInputWireSlot = 0;
      yInputWireSlot = 0;
      xOutputWireSlot = 0;
      yOutputWireSlot = 0;
   }
   
   //Calculates result of gate... 
   //I haven't figured out where to call this from yet
   public boolean calculateOutput()
   {
      //Calculate result of AND for all input lines
      boolean wire1 = inputs.get(0).calculateOutput();
      boolean wire2 = inputs.get(1).calculateOutput();
      boolean result = wire1 && wire2;
      for(int i=2; i<inputs.size(); i++)
      {
         result = result && inputs.get(i).calculateOutput();
      }
      
      //Negate if it is a NAND
      if(negate)
      {
         result = !result;
      }
      
      //Set the output variable according to result
      setOutput(result);
      System.out.println("AND or NAND is:"+output);
      
      return output;
   }
   
   public void draw(Graphics g, int row, int column, int maxColumn, int maxRow)
   {
      //System.out.println("AND drawn");
      int xStart = ((int) (((double)column/maxColumn) * 1000)) - 150;
      int yStart = ((int) (((double)row/maxRow) * 950)) + 65;
      int size = 60;
      g.drawLine(xStart, yStart, xStart, yStart + 95);
      g.drawLine(xStart, yStart, xStart + size, yStart);
      g.drawLine(xStart, yStart + 95, xStart + size, yStart + 95);
      g.drawArc(xStart+(size/2), yStart, size, 95, 90, -180);
      
      xInputWireSlot = xStart;
      yInputWireSlot = yStart + 47;
      xOutputWireSlot = xStart + (size/2) + size;
      yOutputWireSlot = yInputWireSlot;
      
      if(negate)
      {
         xStart = xStart + (size/2) + size;
         yStart = yStart + 39;
         g.drawOval(xStart, yStart, 10, 10);
         xOutputWireSlot += 10;
      }
   }
   
   public void drawWires(Graphics g, int xFinish, int yFinish)
   {
      g.setColor(Color.BLACK);
      //connect to its output
      g.drawLine(xOutputWireSlot, yOutputWireSlot, xFinish, yFinish);
      
      int totalInputs = inputs.size();
      double interval = 85.0/totalInputs;
      
      //connect to its inputs
      for(int i = 0; i < inputs.size(); i++)
      {
         inputs.get(i).drawWires(g, xInputWireSlot, yInputWireSlot);
         
         if(i%2 == 0)
          yInputWireSlot+=(interval*(i+1));
         else
          yInputWireSlot-=(interval*(i+1));
      }
   }
}