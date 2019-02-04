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
   private static int trunkLength = 5;
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
   protected int rowShift = 20; //dont know if this is the best way to solve issue
   protected Rectangle area;
   
   //Constructor
   public Gate(int num_in, gatetype type_in)
   {
      colors = new ArrayList<Color>();
      colors.add(Color.MAGENTA);
      colors.add(Color.BLUE);
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
   
   public void setxStart(int x)
   { xStart = x; }
   
   public int getyStart()
   { return yStart; }
   
   public void setyStart(int y)
   { yStart = y; }
   
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
      //gate boundaries -- should be the gate now comparing to
       int top = yStart - 5;
       int bottom = yFinish + 5;
       int left = xStart - 20;
       int right = xFinish + 5;
       
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

   //This method is used when opening a file or optimizing
   public void draw(Graphics g, int row, int column, int maxColumn, int maxRow)
   {
       //first draw the gate
       drawGate(g, row, column, maxColumn, maxRow);
       
       //draw the boundaries for checking PLEASE DONT REMOVE
       /*g.setColor(Color.RED);
       g.drawLine(xStart - 20, yStart - 5, xFinish + 5, yStart - 5);
       g.drawLine(xStart - 20, yStart - 5, xStart - 20, yFinish + 5);
       g.drawLine(xStart - 20, yFinish + 5, xFinish + 5, yFinish + 5);
       g.drawLine(xFinish + 5, yStart - 5, xFinish + 5, yFinish + 5);
       */
       
       //draw the output in the gate
       g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
       g.setColor(Color.BLACK);
       g.drawString(Integer.toString(getOutputInt()),(xStart + ((xFinish-xStart)/2) - 10), (yStart + ((yFinish-yStart)/2) + 10));
   }
   
   //This is used for repainting when editing
   public void redraw(Graphics g)
   {
      redrawGate(g);
      
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
      
      int origgateNum = -1;
      
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
         origgateNum = inputs.get(i).getGateNum();
  
         
         //draw trunk line outwards by 15 units on each input
         //the trunk may be drawn multiple times though...might want to fix later
         g.drawLine(xWireStart, yWireStart, xWireStart + trunkLength, yWireStart); 
         
         xWireStart += trunkLength;            
         
         //draws the connecting line
         drawNonDiagLine(g, xWireStart, yWireStart, xWireFinish, yWireFinish, drawnGates, gatenum, origgateNum);
         
         //adjust trunk length
         trunkLength += 4;
         if(trunkLength > 30)
            trunkLength = 5;
            
         //sets up the correct finish coordinate for the next input
         if(i%2 == 0)
          yWireFinish+=(interval*(i+1));
         else
          yWireFinish-=(interval*(i+1));
      }  
   }
   
   public void drawNonDiagLine(Graphics g, int xStart_in, int yStart_in, int xFinish_in, int yFinish_in, ArrayList<Gate> drawnGates, int destgateNum, int origgateNum)
   {
      boolean startChanged = false;
      int gateNum = -1;
      
      System.out.println("Dest: " + destgateNum + " Origin: " + origgateNum);
      
      //draw in the horizontal direction until reach x destination
      while(xStart_in != (xFinish_in-20))
      {
         for(int i = 0; i < drawnGates.size(); i++)
         {
            if((destgateNum != drawnGates.get(i).getGateNum()) && (origgateNum != drawnGates.get(i).getGateNum()) && drawnGates.get(i).inPath(xStart_in, yStart_in, xFinish_in - 5, yStart_in))
            {
               System.out.println("issue found");
               gateNum = i;
               break;
            }
         }
         
         //draw horizontally past that gate, after moving correct y above/below the obstacle gate
         if(gateNum >= 0)
         {
            //g.setColor(Color.BLUE);
            //draw vertically up around the gate
            if(yFinish_in < yStart_in)
            {
               g.drawLine(xStart_in, yStart_in, xStart_in, drawnGates.get(gateNum).getyStart() - 7);
               yStart_in = drawnGates.get(gateNum).getyStart() - 7;
            }
            //draw vertically below the gate
            else
            {
               g.drawLine(xStart_in, yStart_in, xStart_in, drawnGates.get(gateNum).getyFinish() + 7);
               yStart_in = drawnGates.get(gateNum).getyFinish() + 7;
            }
            
            //g.setColor(Color.GREEN);
            //now draw horizontally past the gate
            if((xFinish_in - 5) > drawnGates.get(gateNum).getxFinish() + 5)
            {
               g.drawLine(xStart_in, yStart_in, drawnGates.get(gateNum).getxFinish() + 5, yStart_in);
               xStart_in = drawnGates.get(gateNum).getxFinish() + 5;
            }
            else
            {
               g.drawLine(xStart_in, yStart_in, xFinish_in - 20, yStart_in);
               xStart_in = xFinish_in - 20;
            }
         }
         //no gate intersected with in X direction
         else
         {
            g.drawLine(xStart_in, yStart_in, xFinish_in - 20, yStart_in);
            xStart_in = xFinish_in - 20;
         }
         
         //after making an adjustment reset gateNum
         gateNum = -1;
      }
      
      while(yStart_in != yFinish_in)
      {
         for(int i = 0; i < drawnGates.size(); i++)
         {
            if((destgateNum != drawnGates.get(i).getGateNum()) && (origgateNum != drawnGates.get(i).getGateNum()) && drawnGates.get(i).inPath(xStart_in, yStart_in, xStart_in, yFinish_in))
            {
               gateNum = i;
               break;
            }
         }
         
         //draw verticall past the gate, after moving correct x right of the gate
         if(gateNum >= 0)
         {
            int tempX = xStart_in;
            //move correct x right/past the gate
            g.drawLine(xStart_in, yStart_in, drawnGates.get(gateNum).getxFinish() + 7, yStart_in);
            xStart_in = drawnGates.get(gateNum).getxFinish() + 7;
            
            //move vertically past the gate
            if(yFinish_in < yStart_in)
            {   
               g.drawLine(xStart_in, yStart_in, xStart_in, drawnGates.get(gateNum).getyStart() - 5);
               yStart_in = drawnGates.get(gateNum).getyStart() - 5;
            }
            else
            {
               g.drawLine(xStart_in, yStart_in, xStart_in, drawnGates.get(gateNum).getyFinish() + 5);
               yStart_in = drawnGates.get(gateNum).getyFinish() + 5;
            }
            
            //move xStart back to correct place
            g.drawLine(xStart_in, yStart_in, tempX, yStart_in);
            xStart_in = tempX;
         }
         //no gate blocking vertical line
         else
         {
            g.drawLine(xStart_in, yStart_in, xStart_in, yFinish_in);
            yStart_in = yFinish_in;
         }
         
         //reset gateNum
         gateNum = -1;
      }
      
      //line should now be right in front of destination gate
      g.drawLine(xStart_in, yStart_in, xStart_in + 20, yStart_in);     
   }
   
   public abstract void drawGate(Graphics g, int row, int column, int maxColumn, int maxRow);
  
   public abstract void redrawGate(Graphics g);
  
   public abstract boolean calculateOutput();
}