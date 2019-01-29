/* OUTPUT Gate Class for LGS Project */
import javax.swing.*;
import java.awt.*;

public class OUTPUT extends Gate
{
   private int xWireSlot, yWireSlot;
   //Constructor
   public OUTPUT(int num_in)
   {
      super(num_in,gatetype.OUTPUT);
      xWireSlot = 0;
      yWireSlot = 0;
   }
   
   //had to override the abstract method, but no use for these in this child class
   public int getxOutputSlot()
   { return -1; }
   
   public int getyOutputSlot()
   { return -1; }
   
   //Calculates result of gate... 
   //I haven't figured out where to call this from yet
   public boolean calculateOutput()
   {
      //This is the same as its input line
      setOutput(inputs.get(0).calculateOutput());
      return output;
   }
   
   public void draw(Graphics g, int row, int column, int maxColumn, int maxRow)
   {
      g.setColor(Color.RED);
      //output gates must be all the way to the right
      column = maxColumn;
      int xStart = (int) ((((double)column/maxColumn) * 1000) - 150);
      int yStart = (int) ((((double)row/maxRow) * 950) + 65);
      
      //generally placing in the correct row and column area, gate should be 10% of the total dimension in either direction
      g.drawOval(xStart, yStart, 100, 95);
       
      xWireSlot = xStart;
      yWireSlot = yStart + 47;
      
      System.out.println("OUTPUT drawn at row,column: "+ row + "," +column + " at coord: "+ xStart + "," + yStart);
   }
   
   public void drawWires(Graphics g, int xFinish, int yFinish, int branchNumber)
   {
      //connect to its input
      inputs.get(0).drawWires(g, xWireSlot, yWireSlot, branchNumber);
   }
}