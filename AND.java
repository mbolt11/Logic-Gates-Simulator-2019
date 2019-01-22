/* AND and NAND Gate Class for LGS Project */
import javax.swing.*;
import java.awt.*;

public class AND extends Gate
{
   private boolean negate;
   
   //Constructor
   public AND(int num_in, boolean negate_in)
   {
      super(num_in,gatetype.AND);
      negate = negate_in;
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
      
      if(negate)
      {
         xStart = xStart + (size/2) + size;
         yStart = yStart + 39;
         g.drawOval(xStart, yStart, 10, 10);
      }
   }
}