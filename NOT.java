/* NOT Gate Class for LGS Project */
import javax.swing.*;
import java.awt.*;

public class NOT extends Gate
{  
   //Constructor
   public NOT(int num_in)
   {
      super(num_in,gatetype.NOT);
   }
   
   //Calculates result of gate // ERROR!!!!!!!!!!!!!!!!!!!!!!!!!!!
   //I haven't figured out where to call this from yet
   public boolean calculateOutput()
   {
      //This is the opposite of its input line
      if(inputs.size() > 0)
         setOutput(!inputs.get(0).calculateOutput());
         
      System.out.println("NOT is:"+output);
         
      return output;
   }
   
   public void draw(Graphics g, int row, int column, int maxColumn, int maxRow)
   {
      g.setColor(Color.RED);
      
      //generally placing in the correct row and column area, gate should be 10% of the total dimension in either direction
      //drawing polygon to make a triangle shape
      double xBaseCoord = (((double)column/maxColumn) * 1000) - 150;
      double yBaseCoord = (((double)row/maxRow) * 950) + 65;
      int [] xPoints = {(int)xBaseCoord, (int) (xBaseCoord + ((95*Math.sqrt(3))/2)), (int) xBaseCoord};
      int [] yPoints = {(int)yBaseCoord, (int) (yBaseCoord + (0.5*95)), (int) (yBaseCoord + 95)};
      g.drawPolygon(xPoints,yPoints,3);
      
      //draw little ball at end of triangle
      g.drawOval((int) (xBaseCoord + ((95*Math.sqrt(3))/2)), (int) ((yBaseCoord + (0.5*95)-5)), 10, 10);
      System.out.println("NOT drawn at row,column: "+ row + "," +column + " at coord: "+ (int)xBaseCoord + "," + (int)yBaseCoord);
   }
}