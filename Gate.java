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
   protected ArrayList<Gate> inputs;
   protected boolean output;
   
   //Constructor
   public Gate(int num_in, gatetype type_in)
   {
      //Assign num and type
      gatenum = num_in;
      type = type_in;
            
      //Instantiate the inputs ArrayList
      inputs = new ArrayList<Gate>();
   }
   
   //Accessors for member variables
   public ArrayList<Gate> getInputs()
   {
      return inputs;
   }
   
   public int getDepth()
   {
      return depth;
   }
   
   public gatetype getType()
   {
      return type;
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
   
   //Adding and removing an input Gate
   public void addInput(Gate inGate)
   {
      inputs.add(inGate);
      calculateDepth();
   }
   
   public void removeInput(Gate inGate)
   {
      inputs.remove(inGate);
      calculateDepth();
   } 
   
   //Placeholder method for where we can calculate the depth of the gate
   public void calculateDepth()
   {
      for(int i=0; i<inputs.size(); i++)
      {
         if(inputs.get(i).getDepth() >= depth)
         {
            depth = inputs.get(i).getDepth() + 1;
         }
      }     
   }
   
   public abstract void draw(Graphics g, int row, int column, int maxColumn, int maxRow);
   
   public abstract void drawWires(Graphics g, int xFinish, int yFinish);
   
   public abstract boolean calculateOutput();
}