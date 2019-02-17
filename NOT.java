/* NOT Gate Class for LGS Project */
import javax.swing.*;
import java.awt.*;

public class NOT extends Gate
{  
   //Special drawing variables for NOT
   private double xBaseCoord, yBaseCoord;
   private int [] xPoints, yPoints;
   
   //Constructor
   public NOT(int num_in)
   {
      super(num_in,gatetype.NOT);
      xPoints = new int[3];
      yPoints = new int[3];
   }
   
   //Calculates result of gate
   //I haven't figured out where to call this from yet
   public boolean calculateOutput()
   {
      //This is the opposite of its input line
      if(inputs.size() > 0)
         setOutput(!inputs.get(0).calculateOutput());
         
      //System.out.println("NOT is:"+output);
         
      return output;
   }
   
   public void drawGate(Graphics g, int row, int column, int maxColumn, int maxRow)
   {
      
      //generally placing in the correct row and column area, gate should be 10% of the total dimension in either direction
      //drawing polygon to make a triangle shape
      xBaseCoord = (double)((column * colSeperation) - 150 + (row*rowShift));
      yBaseCoord = (double)((row * rowSeperation) + 65 + (column*columnShift));
      xPoints[0] = (int)xBaseCoord;
      xPoints[1] = (int)(xBaseCoord + ((95*Math.sqrt(3))/2));
      xPoints[2] = (int)xBaseCoord;
      yPoints[0] = (int)yBaseCoord;
      yPoints[1] = (int)(yBaseCoord + (0.5*95));
      yPoints[2] = (int)(yBaseCoord + 95);
      g.drawPolygon(xPoints,yPoints,3);
      
      xStart = (int)xBaseCoord;
      yStart = (int)yBaseCoord;
      xFinish = (int) (xBaseCoord + ((95*Math.sqrt(3))/2));
      yFinish = (int) (yBaseCoord + 95);
      
      //draw little ball at end of triangle
      g.drawOval((int) (xBaseCoord + ((95*Math.sqrt(3))/2)), (int) ((yBaseCoord + (0.5*95)-5)), 10, 10);
      
      xInputWireSlot = (int)xBaseCoord;
      yInputWireSlot = (int)(yBaseCoord + (0.5*95));
      xOutputWireSlot = (int)(xBaseCoord + ((95*Math.sqrt(3))/2) + 10);
      yOutputWireSlot = yInputWireSlot;
      
      //System.out.println("NOT drawn at row,column: "+ row + "," +column + " at coord: "+ (int)xBaseCoord + "," + (int)yBaseCoord);
   }
   
   public void redrawGate(Graphics g)
   {  
      //Update variables according to where x and y starts have moved
      xBaseCoord = (double)xStart;
      yBaseCoord = (double)yStart;
      xFinish = (int) (xBaseCoord + ((95*Math.sqrt(3))/2));
      yFinish = (int) (yBaseCoord + 95);
      
      //Draw triangle
      xPoints[0] = (int)xBaseCoord;
      xPoints[1] = (int)(xBaseCoord + ((95*Math.sqrt(3))/2));
      xPoints[2] = (int)xBaseCoord;
      yPoints[0] = (int)yBaseCoord;
      yPoints[1] = (int)(yBaseCoord + (0.5*95));
      yPoints[2] = (int)(yBaseCoord + 95);
      g.drawPolygon(xPoints,yPoints,3);
      
      //draw little ball at end of triangle
      g.drawOval((int) (xBaseCoord + ((95*Math.sqrt(3))/2)), (int) ((yBaseCoord + (0.5*95)-5)), 10, 10);
      
      xInputWireSlot = (int)xBaseCoord;
      yInputWireSlot = (int)(yBaseCoord + (0.5*95));
      xOutputWireSlot = (int)(xBaseCoord + ((95*Math.sqrt(3))/2) + 10);
      yOutputWireSlot = yInputWireSlot;
   }
}