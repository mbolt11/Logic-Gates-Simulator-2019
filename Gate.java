/*Gate Class for LGS Project*/

import java.util.*;
import javax.swing.*;
import java.awt.*;

public abstract class Gate
{
   //Define enum for the gate type
   public enum gatetype {INPUT, OUTPUT, AND, OR, XOR, NOT, NOR, NAND};
   
   //Class member variables
   private int gatenum;
   private static int colorCount = 0;
   private static int trunkLength = 15;
   private ArrayList<Color> colors;
   private int depth = 0;
   private gatetype type;
   private ArrayList<Integer> inputints;
   protected ArrayList<Gate> inputs;
   protected boolean output;
   
   //gate GUI measurements
   protected int xInputWireSlot, yInputWireSlot, xOutputWireSlot, yOutputWireSlot;
   protected int xStart, yStart, xFinish, yFinish;
   protected int columnShift = 20;
   protected Rectangle area;
   
   //Constructor
   public Gate(int num_in, gatetype type_in)
   {
      colors = new ArrayList<Color>();
      colors.add(Color.MAGENTA);
      colors.add(Color.BLUE);
      colors.add(Color.RED);
      colors.add(Color.YELLOW);
      colors.add(Color.PINK);
      colors.add(Color.GREEN);
      colors.add(Color.ORANGE);
      
      //Assign num and type
      gatenum = num_in;
      type = type_in;
            
      //Instantiate both the input ArrayLists (ints and Gates)
      inputints = new ArrayList<Integer>();
      inputs = new ArrayList<Gate>();
      
      xInputWireSlot = 0;
      yInputWireSlot = 0;
      xOutputWireSlot = 0;
      yOutputWireSlot = 0;
      
      //Instantiate the area rectangle
      area = new Rectangle();
   }
   
   //Accessors for member variables
   public ArrayList<Gate> getInputs()
   {
      return inputs;
   }
   
   public int getDepth()
   {
      return (depth+1);
   }
   
   public gatetype getType()
   {
      return type;
   }
   
   public int getGateNum()
   {
      return gatenum;
   }
   
   public String getStringType()
   {
      String stringName = type.toString();
      return stringName;
   }

   public boolean getOutput()
   {
      return output;
   }
   
   public int getOutputInt()
   {
      int value = 0;
      
      if(output)
         value = 1;
      
      return value;
   }
   
   public int getxStart()
   { return xStart; }
   
   public int getyStart()
   { return yStart; }
   
   public int getxFinish()
   { return xFinish; }
   
   public int getyFinish()
   { return yFinish; }
   
   public int getxOutputSlot()
   { return xOutputWireSlot; }
   
   public int getyOutputSlot()
   { return yOutputWireSlot; }
   
   public Rectangle getAreaRect()
   {
      //Re-draw the rectangle according to the x and y start positions
      int width = xFinish-xStart;
      int height = yFinish-yStart;
      area.setBounds(xStart,yStart,width,height);
      
      //Return the rectangle
      return area;
   }
   
   //Setter method for output- this is calculated in child class
   public void setOutput(boolean output_in)
   {
      output = output_in;
   }
   
   //Method to change gate type- needed for constructing NAND and NOR
   public void changeType(gatetype gt)
   {
      type = gt;
   }
   
   //Adding and removing an input Gate (the actual Gate object)
   //Must remember to call calculateDepth after every time you call one of these methods
   public void addInput(Gate inGate)
   {
      inputs.add(inGate);
      System.out.println("Gate " + gatenum + " input: " + inGate.getGateNum());
   }
   
   public void removeInput(Gate inGate)
   {
      inputs.remove(inGate);
   }
   
   //Adding to the ArrayList of Integers which represent input gates
   //This is to be used when a file is loaded in, in case the gates are
   //listed out of order in the file
   public void addInputInt(int num)
   {
      inputints.add(num);
   }
   
   public ArrayList<Integer> getInputInts()
   {
      return inputints;
   }
   
   //Placeholder method for where we can calculate the depth of the gate
   public void calculateDepth()
   {
      int temp = 0;
      for(int i=0; i < inputs.size(); i++)
      {
         if((inputs.get(i).getDepth()-1) >= temp)
         {
            temp = (inputs.get(i).getDepth()-1);
            temp++;
         }
      }
      depth = temp;
      //System.out.println(getStringType()+" depth: "+getDepth());
   }
   
   public boolean inPath(int xStart_in, int yStart_in, int xFinish_in, int yFinish_in)
   {
      //gate boundaries
       int top = yStart;
       int bottom = yFinish;
       int left = xStart;
       int right = xFinish;
       
     //line boundaries
       int otop = yStart_in;
       int obottom = yFinish_in;
       int oleft = xStart_in;
       int oright = xFinish_in;           
         
        return !(  (bottom <= otop) || 
                  (top >= obottom) ||
                  (left >= oright) ||
                  (right <= oleft) );
   }

   
   public void draw(Graphics g, int row, int column, int maxColumn, int maxRow)
   {
       //first draw the gate
       drawGate(g, row, column, maxColumn, maxRow);
       
       //draw the output in the gate
       g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
       g.setColor(Color.BLACK);
       g.drawString(Integer.toString(getOutputInt()),(xStart + ((xFinish-xStart)/2) - 10), (yStart + ((yFinish-yStart)/2) + 10));
   }
   
   //draws wires from each input gate to the current gate
   public void drawWires(Graphics g, ArrayList<Gate> drawnGates)
   {  
      ////////////////////////////////////CONNECTING INPUTS      
      int xWireStart, yWireStart, xWireFinish, yWireFinish;
      xWireFinish = xInputWireSlot;
      yWireFinish = yInputWireSlot;
      
      int totalInputs = inputs.size();
      double interval = 85.0/totalInputs;
      
      g.setColor(colors.get(colorCount));
      colorCount++;
      if(colorCount>=colors.size())
         colorCount = 0;
      //connect to its inputs
      for(int i = 0; i < inputs.size(); i++)
      {
         xWireStart = inputs.get(i).getxOutputSlot();
         yWireStart = inputs.get(i).getyOutputSlot();
      
  
         
         //draw trunk line outwards by 15 units on each input
         //the trunk may be drawn multiple times though...might want to fix later
         g.drawLine(xWireStart, yWireStart, xWireStart + trunkLength, yWireStart); 
         
         xWireStart += trunkLength;            
         
         //draws the connecting line
         drawNonDiagLine(g, xWireStart, yWireStart, xWireFinish, yWireFinish, drawnGates);
         
         //adjust trunk length
         trunkLength += 4;
         if(trunkLength > 30)
            trunkLength = 15;
            
         //sets up the correct finish coordinate for the next input
         if(i%2 == 0)
          yWireFinish+=(interval*(i+1));
         else
          yWireFinish-=(interval*(i+1));
      }  
   }
   
   public void drawNonDiagLine(Graphics g, int xStart_in, int yStart_in, int xFinish_in, int yFinish_in, ArrayList<Gate> drawnGates)
   {
      int xFinishTemp = xFinish_in;
      int yFinishTemp = yFinish_in;
      boolean startChanged = false;
      
      //check if it will intersect with a boundary-->need to keep list of either drawn gates or their boundaries
      for(int i = 0; i < drawnGates.size(); i++)
      {
         startChanged = false;
         //if the line path will intersect with a gate on vertical
         //adjust horizontally to pass around the gate 
         if(drawnGates.get(i) != this)
         {
            if(drawnGates.get(i).inPath(xStart_in, yStart_in, xStart_in, yFinishTemp) && !startChanged)
            {
               System.out.println("collision in the y detected");
               startChanged = true;
               //draw vertical to stop near the gate it may intersect with if higher than gate
               if(yStart_in < drawnGates.get(i).getyStart())
               {
                  g.drawLine(xStart_in, yStart_in, xStart_in, drawnGates.get(i).getyStart() - 7);
                  yStart_in = drawnGates.get(i).getyStart() - 7;
               }
               //draw vertical to stop near the gate it may intersect with if lower than gate
               else
               {
                  g.drawLine(xStart_in, yStart_in, xStart_in, drawnGates.get(i).getyFinish() + 7);
                  yStart_in = drawnGates.get(i).getyFinish() + 7;
               }
                  
               //need to adjust the line around the gate by adjusting horizontally
               //always draws from input to output gate so ALWAYS right direction past the obstacle gate
               g.drawLine(xStart_in, yStart_in, drawnGates.get(i).getxFinish() + 7, yStart_in);
               xStart_in = drawnGates.get(i).getyFinish() + 7;
               
               //reset i so that all gates can be checked again...not sure if this is the best way to do this??
               i = 1;
            }
            //if the line path will intersect with a gate on horizontal
            //adjust vertically to pass around the gate 
            if(drawnGates.get(i).inPath(xStart_in, yStart_in, xFinish_in, yStart_in) && !startChanged)
            {
               System.out.println("From: " + xStart_in + "," + yStart_in +" To: " + xFinish_in + "," + yStart_in);
               System.out.println("Interferes with "+ drawnGates.get(i).getStringType() +" Top: "+drawnGates.get(i).getxStart()+","+drawnGates.get(i).getyStart()+" Bottom: "+drawnGates.get(i).getxFinish()+","+drawnGates.get(i).getyFinish());
               startChanged = true;
               System.out.println("collision in the x detected");
               //draw horizontal to stop near the gate it may intersect with if higher than gate
               //always draws from input to output gate so ALWAYS right direction past the obstacle gate
               g.drawLine(xStart_in, yStart_in, drawnGates.get(i).getxStart() - 10, yStart_in);
               xStart_in = drawnGates.get(i).getxStart() - 10;
               
               //need to adjust the line around the gate by adjusting vertically
               //if closer to top of gate then go over top
               if(yStart_in < (drawnGates.get(i).getyStart() + 42))
               {
                  g.drawLine(xStart_in, yStart_in, xStart_in, drawnGates.get(i).getyStart() - 10);
                  yStart_in = drawnGates.get(i).getyStart() - 10;
               }
               //if closer to the bottom then go under the gate
               else
               {
                  g.drawLine(xStart_in, yStart_in, xStart_in, drawnGates.get(i).getyFinish() + 10);
                  yStart_in = drawnGates.get(i).getyFinish() + 10;
               }
   
               //reset i so that all gates can be checked again...not sure if this is the best way to do this??
               i = 1;
            }
         }
         
         System.out.println("Loop at i = "+ i);
      }
      
      //finish to the destination gate && work on this more!!!!!!!!!!!!!!!!!!!
      //draw horizontal line
      g.drawLine(xStart_in, yFinish_in, xFinish_in, yFinish_in);
      //draw vertical line
      g.drawLine(xStart_in, yStart_in, xStart_in, yFinish_in);
   }
   
   public abstract void drawGate(Graphics g, int row, int column, int maxColumn, int maxRow);
   
   public abstract boolean calculateOutput();
}