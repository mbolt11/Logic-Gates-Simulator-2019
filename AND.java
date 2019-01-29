/* AND and NAND Gate Class for LGS Project */
import javax.swing.*;
import java.awt.*;

public class AND extends Gate
{
   private boolean negate;
   private int xInputWireSlot, yInputWireSlot, xOutputWireSlot, yOutputWireSlot, xStart, yStart;
   
   //Constructor
   public AND(int num_in, boolean negate_in)
   {
      super(num_in,gatetype.AND);
      if(negate_in)
      {
         changeType(gatetype.NAND);
      }
      
      negate = negate_in;
      xInputWireSlot = 0;
      yInputWireSlot = 0;
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
      //Calculate result of AND for all input lines
      boolean wire1 = inputs.get(0).calculateOutput();
      boolean wire2 = inputs.get(1).calculateOutput();
      boolean result = wire1 && wire2;
      for(int i=2; i<inputs.size(); i++)
      {
         result = result && inputs.get(i).calculateOutput();
      }
      
      //Negate if it is a NAND
      if(negate)
      {
         result = !result;
      }
      
      //Set the output variable according to result
      setOutput(result);
      System.out.println("AND or NAND is:"+output);
      
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
      //System.out.println("AND drawn");
      xStart = ((int) (((double)column/maxColumn) * 1000)) - 150;
      yStart = ((int) (((double)row/maxRow) * 950)) + 65;
      int size = 60;
      g.drawLine(xStart, yStart, xStart, yStart + 95);
      g.drawLine(xStart, yStart, xStart + size, yStart);
      g.drawLine(xStart, yStart + 95, xStart + size, yStart + 95);
      g.drawArc(xStart+(size/2), yStart, size, 95, 90, -180);
      
      xInputWireSlot = xStart;
      yInputWireSlot = yStart + 47;
      xOutputWireSlot = xStart + (size/2) + size;
      yOutputWireSlot = yInputWireSlot;
      
      if(!negate)
         System.out.println("AND drawn at row,column: "+ row + "," +column + " at coord: "+ xStart + "," + yStart);
      
      if(negate)
      {
         xStart = xStart + (size/2) + size;
         yStart = yStart + 39;
         g.drawOval(xStart, yStart, 10, 10);
         xOutputWireSlot += 10;
         
         System.out.println("NAND drawn at row,column: "+ row + "," +column + " at coord: "+ xStart + "," + yStart);
      }   
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
   
   /*public void drawWires(Graphics g, int xFinish, int yFinish, int branchNumber)
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
      
      
      ////////////////////////////////////CONNECTING INPUTS
      int totalInputs = inputs.size();
      double interval = 85.0/totalInputs;
      
      int upBranches = 0;
      int downBranches = 0;
      //connect to its inputs
      for(int i = 0; i < inputs.size(); i++)
      {
         //upwards branch
         if(inputs.get(i).getyOutputSlot() < yInputWireSlot)
         {
            inputs.get(i).drawWires(g, xInputWireSlot, yInputWireSlot, upBranches);
            
            //after the trunk, the next input must be recognized as the first branch
            if(upBranches == 0 && downBranches == 0)
            {
               upBranches = 2;
               downBranches = 1;
            }
            else //all following branches will be incremented by 2
            {
               upBranches += 2;
            }
         }
         //downwards branch
         else
         {
            inputs.get(i).drawWires(g, xInputWireSlot, yInputWireSlot, downBranches);
            
            //after the trunk, the next input must be recognized as the first branch
            if(upBranches == 0 && downBranches == 0)
            {
               upBranches = 2;
               downBranches = 1;
            }
            else //all following branches will be incremented by 2
            {
               downBranches += 2;
            }
         }
         
         if(i%2 == 0)
          yInputWireSlot+=(interval*(i+1));
         else
          yInputWireSlot-=(interval*(i+1));
      }
   }*/
}