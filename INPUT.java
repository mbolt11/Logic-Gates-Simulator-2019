/* INPUT Gate Class for LGS Project */
import javax.swing.*;
import java.awt.*;

public class INPUT extends Gate
{
   private int xOutputWireSlot, yOutputWireSlot;
   //Constructor
   public INPUT(int num_in)
   {
      super(num_in,gatetype.INPUT);
      xOutputWireSlot = 0;
      yOutputWireSlot = 0;
   }
   
   //Calculates result of gate... 
   //I haven't figured out where to call this from yet
   public boolean calculateOutput()
   {
      //This changes according to user click
      setOutput(false); //default to start
      return output;
   }
   
   public void draw(Graphics g, int row, int column, int maxColumn, int maxRow)
   {
      g.setColor(Color.RED);
      //input gates must be all the way to the left
      column = 1;
      int xStart = ((int) (((double)column/maxColumn) * 1000)) - 150;
      int yStart = ((int) (((double)row/maxRow) * 950)) + 65;
      
      //generally placing in the correct row and column area, gate should be 10% of the total dimension in either direction
      g.drawRect(xStart, yStart, 100, 95);
      
      xOutputWireSlot = xStart+100;
      yOutputWireSlot = yStart + 47;
      
      System.out.println("Input drawn at row,column: "+ row + "," +column + " at coord: "+ xStart + "," + yStart);
   }
   
   public void drawWires(Graphics g, int xFinish, int yFinish)
   {
      g.setColor(Color.BLACK);
      //connect to its output
      g.drawLine(xOutputWireSlot, yOutputWireSlot, xFinish, yFinish);
   }
}