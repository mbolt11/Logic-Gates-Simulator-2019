/* OUTPUT Gate Class for LGS Project */
import javax.swing.*;
import java.awt.*;

public class OUTPUT extends Gate
{
   //Constructor
   public OUTPUT(int num_in)
   {
      super(num_in,gatetype.OUTPUT);
   }
   
   //Calculates result of gate... 
   //I haven't figured out where to call this from yet
   public boolean calculateOutput()
   {
      //This is the same as its input line
      if(inputs.size() > 0)
         setOutput(inputs.get(0).calculateOutput());
      return output;
   }
   
   public void drawGate(Graphics g, int row, int column, int maxColumn, int maxRow)
   {     
      //output gates must be all the way to the right
      column = maxColumn;
      xStart = (int) ((((double)column/maxColumn) * 1000) - 150);
      yStart = (int) ((((double)row/maxRow) * 950) + 65) + ((column-1)*columnShift);
      xFinish = xStart + 100;
      yFinish = yStart + 95;
      
      //generally placing in the correct row and column area, gate should be 10% of the total dimension in either direction
      g.drawOval(xStart, yStart, 100, 95);
       
      xInputWireSlot = xStart;
      yInputWireSlot = yStart + 47;
      
      //System.out.println("OUTPUT drawn at row,column: "+ row + "," +column + " at coord: "+ xStart + "," + yStart);
   }
   
   public void redrawGate(Graphics g)
   {        
      //Recalculate finish spots based on updated starts
      xFinish = xStart + 100;
      yFinish = yStart + 95;
      
      //generally placing in the correct row and column area, gate should be 10% of the total dimension in either direction
      g.drawOval(xStart, yStart, 100, 95);
       
      xInputWireSlot = xStart;
      yInputWireSlot = yStart + 47;
   }
   
}