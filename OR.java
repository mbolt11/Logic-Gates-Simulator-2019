/* OR and NOR Gate Class for LGS Project */
import javax.swing.*;
import java.awt.*;

public class OR extends Gate
{
   private boolean negate;
   private int xInputWireSlot, yInputWireSlot, xOutputWireSlot, yOutputWireSlot;
   
   //Constructor
   public OR(int num_in, boolean negate_in)
   {
      super(num_in,gatetype.OR);
      if(negate_in)
      {
         changeType(gatetype.NOR);
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
   
   //Calculates result of gate // ERROR!!!!!!!!!!!!!!!!!!!!!!
   //I haven't figured out where to call this from yet
   public boolean calculateOutput()
   {
      boolean result = false;
      //Calculate result of OR for all input lines
      if(inputs.size() >= 2)
      {boolean wire1 = inputs.get(0).calculateOutput();
      boolean wire2 = inputs.get(1).calculateOutput();
      result = wire1 || wire2;
      for(int i=2; i<inputs.size(); i++)
      {
         result = result || inputs.get(i).calculateOutput();
      }}
      
      //Negate if it is a NOR
      if(negate)
      {
         result = !result;
      }
      
      //Set the output variable according to result
      setOutput(result);
      System.out.println("OR is:"+output);
      return output;
   }
   
   public void draw(Graphics g, int row, int column, int maxColumn, int maxRow)
   {
      //System.out.println("OR drawn");
      int xStart = ((int) (((double)column/maxColumn) * 1000)) - 150;
      int yStart = ((int) (((double)row/maxRow) * 950)) + 65;
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
      
      System.out.println("OR drawn at row,column: "+ row + "," +column + " at coord: "+ xStart + "," + yStart);
   }
   
   public void drawWires(Graphics g, int xFinish, int yFinish, int branchNumber)
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
   }
}