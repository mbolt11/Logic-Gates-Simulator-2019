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
   private int depth = 0;
   private gatetype type;
   private ArrayList<Integer> inputints;
   protected ArrayList<Gate> inputs;
   protected boolean output;
   protected int xInputWireSlot, yInputWireSlot, xOutputWireSlot, yOutputWireSlot;
   
   //Constructor
   public Gate(int num_in, gatetype type_in)
   {
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
   
   public int getxOutputSlot()
   { return xOutputWireSlot; }
   
   public int getyOutputSlot()
   { return yOutputWireSlot; }
   
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

   
   public void draw(Graphics g, int row, int column, int maxColumn, int maxRow)
   {
       //first draw the gate
       drawGate(g, row, column, maxColumn, maxRow);
       
       //drawWires from its inputs to itself
       drawWires(g);
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
   
   public abstract void drawGate(Graphics g, int row, int column, int maxColumn, int maxRow);
   
   public abstract boolean calculateOutput();
}