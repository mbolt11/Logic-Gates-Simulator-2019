/* INPUT Gate Class for LGS Project */
import javax.swing.*;
import java.awt.*;

public class INPUT extends Gate
{
   //Constructor
   public INPUT(int num_in)
   {
      super(num_in,gatetype.INPUT);
      setOutput(false); //default to start
   }
   
   //Calculates result of gate... 
   //I haven't figured out where to call this from yet
   public boolean calculateOutput()
   {
      //This changes according to user click
      return output;
   }
   
   public void drawGate(Graphics g, int row, int column, int maxColumn, int maxRow)
   {
      //input gates must be all the way to the left
      column = 1;
      xStart = (column * colSeperation) - 150;
      yStart = (row * rowSeperation) + 65 + (column*columnShift);
      xFinish = xStart + 100;
      yFinish = yStart + 95;
      
      //generally placing in the correct row and column area, gate should be 10% of the total dimension in either direction
      g.drawRect(xStart, yStart, 100, 95);
      
      xOutputWireSlot = xStart+100;
      yOutputWireSlot = yStart + 47;
      
      //System.out.println("Input drawn at row,column: "+ row + "," +column + " at coord: "+ xStart + "," + yStart);
   }
   
   public void redrawGate(Graphics g)
   {  
      //Recalculate finish spots based on updated starts
      xFinish = xStart + 100;
      yFinish = yStart + 95;
      
      //generally placing in the correct row and column area, gate should be 10% of the total dimension in either direction
      g.drawRect(xStart, yStart, 100, 95);
      
      xOutputWireSlot = xStart+100;
      yOutputWireSlot = yStart + 47;
   }
}