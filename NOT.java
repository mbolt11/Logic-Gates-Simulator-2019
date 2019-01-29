/* NOT Gate Class for LGS Project */
import javax.swing.*;
import java.awt.*;

public class NOT extends Gate
{  
   private int xInputWireSlot, yInputWireSlot, xOutputWireSlot, yOutputWireSlot;
   //Constructor
   public NOT(int num_in)
   {
      super(num_in,gatetype.NOT);
      xInputWireSlot = 0;
      yInputWireSlot = 0;
      xOutputWireSlot = 0;
      yOutputWireSlot = 0;
   }
   
   public int getxOutputSlot()
   { return xOutputWireSlot; }
   
   public int getyOutputSlot()
   { return yOutputWireSlot; }
   
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
       //first draw the gate
       drawGate(g, row, column, maxColumn, maxRow);
       
       //drawWires from its inputs to itself
       drawWires(g);
   }
   
   public void drawGate(Graphics g, int row, int column, int maxColumn, int maxRow)
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
      
      xInputWireSlot = (int)xBaseCoord;
      yInputWireSlot = (int)(yBaseCoord + (0.5*95));
      xOutputWireSlot = (int)(xBaseCoord + ((95*Math.sqrt(3))/2) + 10);
      yOutputWireSlot = yInputWireSlot;
      
      System.out.println("NOT drawn at row,column: "+ row + "," +column + " at coord: "+ (int)xBaseCoord + "," + (int)yBaseCoord);
   }
   
   //draws wires from each input gate to the current gate
   public void drawWires(Graphics g)
   {
      g.setColor(Color.BLACK);
      
      ////////////////////////////////////CONNECTING INPUTS      
      int xWireStart, yWireStart, xWireFinish, yWireFinish;
      xWireFinish = xInputWireSlot;
      yWireFinish = yInputWireSlot;
      
      int totalInputs = inputs.size();
      double interval = 85.0/totalInputs;
      
      //connect to its inputs
      for(int i = 0; i < inputs.size(); i++)
      {
         xWireStart = inputs.get(i).getxOutputSlot();
         yWireStart = inputs.get(i).getyOutputSlot();
         
         //draw trunk line outwards by 30 units on each input
         g.drawLine(xWireStart, yWireStart, xWireStart + 15, yWireStart);
         
         xWireStart += 15;       
         
         //draws the connecting line
         g.drawLine(xWireStart, yWireStart, xWireFinish, yWireFinish);
         
         //sets up the correct finish coordinate for the next input
         if(i%2 == 0)
          yWireFinish+=(interval*(i+1));
         else
          yWireFinish-=(interval*(i+1));
      }  
   }
}