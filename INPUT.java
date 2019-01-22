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
   public void calculateOutput()
   {
      //This changes according to user click
      setOutput(false); //default to start
   }
   
   public void draw(Graphics g, int row, int column, int maxColumn, int maxRow)
   {
      g.setColor(Color.RED);
      
      //generally placing in the correct row and column area, gate should be 10% of the total dimension in either direction
      g.drawRect((column/maxColumn) * 950 , (row/maxRow) * 1000, 100, 95);
      
      System.out.println("Input drawn at row,column: "+ row + "," +column);
   }
}