/* XOR Gate Class for LGS Project */
import javax.swing.*;
import java.awt.*;

public class XOR extends Gate
{
   private boolean output;
   private int xInputWireSlot, yInputWireSlot, xOutputWireSlot, yOutputWireSlot;
   
   //Constructor
   public XOR(int num_in)
   {
      super(num_in,gatetype.XOR);
      xInputWireSlot = 0;
      yInputWireSlot = 0;
      xOutputWireSlot = 0;
      yOutputWireSlot = 0;
   }
   
   public int getxOutputSlot()
   { return xOutputWireSlot; }
   
   public int getyOutputSlot()
   { return yOutputWireSlot; }
   
   //Calculates result of gate // ERROR!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
   //I haven't figured out where to call this from yet
   public boolean calculateOutput()
   {
      boolean result = false;
      //Calculate result of XOR for all input lines
      if(inputs.size() >= 2)
      {boolean wire1 = inputs.get(0).calculateOutput();
      boolean wire2 = inputs.get(1).calculateOutput();
      result = ((wire1 && !wire2) || (!wire1 && wire2));}
      //Can't do more than 2 input lines for an XOR, right??
      
      //Set the output variable according to result
      setOutput(result);
      System.out.println("XOR is:"+output);
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
      //System.out.println("XOR drawn");
      int xStart = ((int) (((double)column/maxColumn) * 1000)) - 150;
      int yStart = ((int) (((double)row/maxRow) * 950)) + 65;
      //draw extra front arc
      g.drawArc(xStart - 115, yStart - 25, 100, 150, 40, -80);
      //draw the first arc
      g.drawArc(xStart - 105, yStart - 25, 100, 150, 40, -80);
      //draw second arc
      g.drawArc(xStart - 100, yStart, 200, 150, 100, -80);
      //draw third arc
      g.drawArc(xStart - 100, yStart - 50, 200, 150, -100, 80);
      
      xInputWireSlot = xStart;
      yInputWireSlot = yStart + 47;
      xOutputWireSlot = xInputWireSlot + 100;
      yOutputWireSlot = yInputWireSlot;
      
      System.out.println("XOR drawn at row,column: "+ row + "," +column + " at coord: "+ xStart + "," + yStart);
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