/* AND and NAND Gate Class for LGS Project */
import javax.swing.*;
import java.awt.*;

public class AND extends Gate
{
   private boolean negate;
   private int size;
   
   //Constructor
   public AND(int num_in, boolean negate_in)
   {
      super(num_in,gatetype.AND);
      if(negate_in)
      {
         changeType(gatetype.NAND);
      }
      
      negate = negate_in;
      size = 60;
   }
   
   //Calculates result of gate... 
   //I haven't figured out where to call this from yet
   public boolean calculateOutput()
   {
      boolean result;
      if(inputs.size() == 0)
      {
         result = false;
      }
      else if(inputs.size() == 1)
      {
         result = inputs.get(0).getOutput();
      }
      else
      {
         //Calculate result of AND for all input lines
         boolean wire1 = inputs.get(0).getOutput();
         boolean wire2 = inputs.get(1).getOutput();
         result = wire1 && wire2;
         for(int i=2; i<inputs.size(); i++)
         {
            result = result && inputs.get(i).getOutput();
         }
      }
      
      //Negate if it is a NAND
      if(negate)
      {
         result = !result;
      }
      
      //Set the output variable according to result
      setOutput(result);
      //System.out.println("AND or NAND is:"+output);
      
      return output;
   }
   
   public void resetxFinish()
   {
      xFinish = xStart + size + (size/2) + 10;
      xFinishB = xFinish + 20;
   }
   
   public void resetyFinish()
   {
      yFinish = yStart + 95;
      yFinishB = yFinish + 20;
   }
   
   public void drawGate(Graphics g, int row, int column, int maxColumn, int maxRow)
   {
      //System.out.println("Coord: "+xStart+", "+yStart);
      
      ////System.out.println("AND drawn");
      
      xStart = (column * colSeperation) - 150 + (row*rowShift);
      yStart = (row * rowSeperation) + 65 + (column*columnShift);
      xFinish = xStart + size + (size/2) + 10;
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
      /*g.setColor(new Color(220,220,220));
      g.fillRect(xStart - 20, yStart - 5, (xFinish + 5) - (xStart - 20), (yFinish + 5) - (yStart - 5));*/
      
      if(!isInCircuit())
        g.setColor(Color.RED);
      else
         g.setColor(Color.BLACK);
      
      g.drawLine(xStart, yStart, xStart, yStart + 95);
      g.drawLine(xStart, yStart, xStart + size, yStart);
      g.drawLine(xStart, yStart + 95, xStart + size, yStart + 95);
      g.drawArc(xStart+(size/2), yStart, size, 95, 90, -180);
      
      xInputWireSlot = xStart;
      yInputWireSlot = yStart + 47;
      xOutputWireSlot = xStart + (size/2) + size;
      yOutputWireSlot = yInputWireSlot;
      
      if(!negate)
         //System.out.println("AND drawn at row,column: "+ row + "," +column + " at coord: "+ xStart + "," + yStart);
      
      if(negate)
      {
         int circleXStart = xStart + (size/2) + size;
         int circleYStart = yStart + 42;
         g.drawOval(circleXStart, circleYStart, 10, 10);
         xOutputWireSlot += 10;
         
         //System.out.println("NAND drawn at row,column: "+ row + "," +column + " at coord: "+ xStart + "," + yStart);
      }   
   }
   
   public void redrawGate(Graphics g)
   {
      //System.out.println("Coord: "+xStart+", "+yStart);
      
      //Recalculate finish spots based on updated starts
      xFinish = xStart + size + (size/2) + 10;
      yFinish = yStart + 95;
      
      g.drawLine(xStart, yStart, xStart, yStart + 95);
      g.drawLine(xStart, yStart, xStart + size, yStart);
      g.drawLine(xStart, yStart + 95, xStart + size, yStart + 95);
      g.drawArc(xStart+(size/2), yStart, size, 95, 90, -180);
      
      xInputWireSlot = xStart;
      yInputWireSlot = yStart + 47;
      xOutputWireSlot = xStart + (size/2) + size;
      yOutputWireSlot = yInputWireSlot;
      
      if(negate)
      {
         int circleXStart = xStart + (size/2) + size;
         int circleYStart = yStart + 42;
         g.drawOval(circleXStart, circleYStart, 10, 10);
         xOutputWireSlot += 10;
      }   
   }  
}