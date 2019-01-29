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
   
   //Constructor
   public Gate(int num_in, gatetype type_in)
   {
      //Assign num and type
      gatenum = num_in;
      type = type_in;
            
      //Instantiate both the input ArrayLists (ints and Gates)
      inputints = new ArrayList<Integer>();
      inputs = new ArrayList<Gate>();
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
      System.out.println(getStringType()+" depth: "+getDepth());
   }

   
   public abstract void draw(Graphics g, int row, int column, int maxColumn, int maxRow);
   
   public abstract void drawWires(Graphics g);
   
   public abstract boolean calculateOutput();
   
   public abstract int getxOutputSlot();
   
   public abstract int getyOutputSlot();
}