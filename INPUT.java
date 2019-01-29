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
   
   public int getxOutputSlot()
   { return xOutputWireSlot; }
   
   public int getyOutputSlot()
   { return yOutputWireSlot; }
   
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
   
   
   public void drawWires(Graphics g, int xFinish, int yFinish, int branchNumber)
   {
      g.setColor(Color.BLACK);
      
      //draw trunk line outwards by 30 units
      g.drawLine(xOutputWireSlot, yOutputWireSlot, xOutputWireSlot + 30, yOutputWireSlot);
      
      //set new start xOutput
      xOutputWireSlot = xOutputWireSlot + 30;
      
      //if the branch number is > 0, then an additional gate requires the output and a "branch" must be made
      //if the branch number = 0, then this is the original output wire "trunk"
      if(branchNumber > 0)
      {
         int mult = branchNumber/2;
         //if branch is even then it's an upper branch and the integer division of 2 is how much the branch distance should be multiplied
         if(branchNumber % 2 == 0)
         {
            //draw upwards the branch interval(10) * mult starting at the trunk
            g.drawLine(xOutputWireSlot, yOutputWireSlot, xOutputWireSlot, yOutputWireSlot - (mult * 10));
            
            //set new ystart to draw from to the finish
            yOutputWireSlot = yOutputWireSlot - (mult * 10);
         }
         else
         {
            //draw downwards the branch interval * mult starting at the trunk
            mult++;
            g.drawLine(xOutputWireSlot, yOutputWireSlot, xOutputWireSlot, yOutputWireSlot + (mult * 10));
            
            //set new ystart to draw from to the finish
            yOutputWireSlot = yOutputWireSlot + (mult * 10);
         }   
      }
      
      
      //connect to its output
      g.drawLine(xOutputWireSlot, yOutputWireSlot, xFinish, yFinish);
   }
}