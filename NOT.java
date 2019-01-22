/* NOT Gate Class for LGS Project */
import javax.swing.*;
import java.awt.*;

public class NOT extends Gate
{
   private boolean output;
   
   //Constructor
   public NOT(int num_in)
   {
      super(num_in,gatetype.NOT);
      calculateOutput();
   }
   
   //Calculates result of gate // ERROR!!!!!!!!!!!!!!!!!!!!!!!!!!!
   public void calculateOutput()
   {
      //This is the opposite of its input line
      if(inputs.size() > 0)
         setOutput(inputs.get(0).getOutput());
   }
   
   public void draw(Graphics g, int row, int column, int maxColumn, int maxRow)
   {
      g.setColor(Color.RED);
      
      //generally placing in the correct row and column area, gate should be 10% of the total dimension in either direction
      //drawing polygon to make a triangle shape
      double xBaseCoord = (row/maxRow) * 1000;
      
      System.out.println("Not drawn");
      double yBaseCoord = (column/maxColumn) * 950;
      int [] xPoints = {(int)xBaseCoord, (int) (xBaseCoord + (2*xBaseCoord*Math.sqrt(3))), (int) xBaseCoord};
      int [] yPoints = {(int)yBaseCoord, (int) (yBaseCoord + (0.5*95)), (int) (yBaseCoord + 95)};
      g.drawPolygon(xPoints,yPoints,3);
   }
}