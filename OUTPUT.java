/* OUTPUT Gate Class for LGS Project */
import javax.swing.*;
import java.awt.*;

public class OUTPUT extends Gate
{
   private int xInputWireSlot, yInputWireSlot;
   //Constructor
   public OUTPUT(int num_in)
   {
      super(num_in,gatetype.OUTPUT);
      xInputWireSlot = 0;
      yInputWireSlot = 0;
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
       //first draw the gate
       drawGate(g, row, column, maxColumn, maxRow);
       
       //drawWires from its inputs to itself
       drawWires(g);
   }
   
   public void drawGate(Graphics g, int row, int column, int maxColumn, int maxRow)
   {
      g.setColor(Color.RED);
      //output gates must be all the way to the right
      column = maxColumn;
      int xStart = (int) ((((double)column/maxColumn) * 1000) - 150);
      int yStart = (int) ((((double)row/maxRow) * 950) + 65);
      
      //generally placing in the correct row and column area, gate should be 10% of the total dimension in either direction
      g.drawOval(xStart, yStart, 100, 95);
       
      xInputWireSlot = xStart;
      yInputWireSlot = yStart + 47;
      
      System.out.println("OUTPUT drawn at row,column: "+ row + "," +column + " at coord: "+ xStart + "," + yStart);
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