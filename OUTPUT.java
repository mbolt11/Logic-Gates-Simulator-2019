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
   
   public void resetxFinish()
   {
      xFinish = xStart + 100;
      xFinishB = xFinish + 20;
   }
   
   public void resetyFinish()
   {
      yFinish = yStart + 95;
      yFinishB = yFinish + 20;
   }
   
   public void drawGate(Graphics g, int row, int column, int maxColumn, int maxRow)
   {     
      //output gates must be all the way to the right
      column = maxColumn;
      xStart = (column * colSeperation) - 150;
      yStart = (row * rowSeperation) + 65 + ((column-1)*columnShift);
      xFinish = xStart + 100;
      yFinish = yStart + 95;
      
      //set Gate vs Gate Boundaries
      xStartB = xStart - 35;
      yStartB = yStart - 20;
      xFinishB = xFinish + 20;
      yFinishB = yFinish + 20;
      
      //color Gate vs Gate Boundary
if(!validWires)
      {
         g.setColor(new Color(225,209,223));
         g.fillRect(xStartB, yStartB, xFinishB - xStartB, yFinishB - yStartB);
      }
      
      //color Gate vs Line Boundary
      g.setColor(new Color(220,220,220));
      g.fillRect(xStart - 20, yStart - 5, (xFinish + 5) - (xStart - 20), (yFinish + 5) - (yStart - 5));
      
      if(!isInCircuit())
        g.setColor(Color.RED);
      else
         g.setColor(Color.BLACK);
      
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