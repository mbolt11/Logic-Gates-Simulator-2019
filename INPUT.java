/* INPUT Gate Class for LGS Project */
import javax.swing.*;
import java.awt.*;

public class INPUT extends Gate
{
   //Constructor
   public INPUT(int num_in)
   {
      super(num_in,gatetype.INPUT);
   }
   
   //Calculates result of gate... 
   //I haven't figured out where to call this from yet
   public boolean calculateOutput()
   {
      //This changes according to user click
      setOutput(false); //default to start
      return output;
   }
   
   public void drawGate(Graphics g, int row, int column, int maxColumn, int maxRow)
   {
      g.setColor(Color.RED);
      //input gates must be all the way to the left
      column = 1;
      xStart = ((int) (((double)column/maxColumn) * 1000)) - 150;
      yStart = ((int) (((double)row/maxRow) * 950)) + 65 + (column*10);
      xFinish = xStart + 100;
      yFinish = yStart + 95;
      
      //generally placing in the correct row and column area, gate should be 10% of the total dimension in either direction
      g.drawRect(xStart, yStart, 100, 95);
      
      xOutputWireSlot = xStart+100;
      yOutputWireSlot = yStart + 47;
      
      System.out.println("Input drawn at row,column: "+ row + "," +column + " at coord: "+ xStart + "," + yStart);
   }
}