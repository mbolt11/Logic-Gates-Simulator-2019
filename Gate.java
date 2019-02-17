/*Gate Class for LGS Project*/

import java.util.*;
import javax.swing.*;
import java.awt.*;

public abstract class Gate
{
   //Define enum for the gate type
   public enum gatetype {INPUT, OUTPUT, AND, OR, XOR, NOT, NOR, NAND, TRUE, FALSE};
   
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
   private boolean isInCircuit;
   
   //gate GUI measurements
   protected int xInputWireSlot, yInputWireSlot, xOutputWireSlot, yOutputWireSlot;
   protected int xStart, yStart, xFinish, yFinish;
   protected int columnShift = 20;
   protected int rowShift = 20; 
   protected int colSeperation = 250;
   protected int rowSeperation = 250;
   
   //Rectangle representations of the input, output area, and whole area
   protected Rectangle inArea;
   protected Rectangle outArea;
   protected Rectangle wholeArea;
   
   //Constructor
   public Gate(int num_in, gatetype type_in)
   {
      colors = new ArrayList<Color>();
      colors.add(Color.MAGENTA);
      colors.add(Color.BLUE);
      colors.add(Color.RED);
      colors.add(Color.PINK);
      colors.add(Color.GREEN);
      colors.add(Color.ORANGE);
      
      //Assign num and type 
      gatenum = num_in;
      type = type_in;
      
      isInCircuit = false;
            
      //Instantiate both the input ArrayLists (ints and Gates)
      inputints = new ArrayList<Integer>();
      inputs = new ArrayList<Gate>();
      
      //Initial values for wire slots
      xInputWireSlot = 0;
      yInputWireSlot = 0;
      xOutputWireSlot = 0;
      yOutputWireSlot = 0;
      
      //Initial x and y start
      xStart = 400;
      yStart = 410;
      
      //Instantiate the area rectangles
      wholeArea = new Rectangle();
      inArea = new Rectangle();
      outArea = new Rectangle();
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
   
   public void resetColorCount()
   {
      colorCount = 0;
   }
   
   public boolean isInCircuit()
   {
      return isInCircuit;
   }
   
   public boolean isAttatched2Gate(Gate gate)
   {
      boolean result = false;
      
      for(int i = 0; i < inputs.size(); i++)
      {
         if(result == true)
            break;
         if(inputs.get(i).getGateNum() == gate.getGateNum())
            return true;
         else
         {
            result = inputs.get(i).isAttatched2Gate(gate);
         }
      }
      
      return result;
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
   
   //Method to translate the gate start coordinate for scrolling screen
   public void translate(int dx, int dy)
   {
      xStart+=dx;
      yStart+=dy;
   }
   
   public Rectangle getAreaRect()
   {
      //Re-draw the rectangle according to the x and y start positions
      int width = xFinish-xStart;
      int height = yFinish-yStart;
      wholeArea.setBounds(xStart,yStart,width,height);
      
      //Return the rectangle
      return wholeArea;
   }
   
   public Rectangle getInputAreaRect()
   {
      //Re-draw the rectangle according to the x and y start positions
      int halfwidth = (xFinish-xStart)/2;
      int height = yFinish-yStart;
      inArea.setBounds(xStart,yStart,halfwidth,height);
      
      //Return the rectangle
      return inArea;
   }
   
   public Rectangle getOutputAreaRect()
   {
      //Re-draw the rectangle according to the x and y start positions
      int halfwidth = (xFinish-xStart)/2;
      int height = yFinish-yStart;
      outArea.setBounds((xStart+halfwidth),yStart,halfwidth,height);
      
      //Return the rectangle
      return outArea;
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
   
   //Method to call when the gate is officially added to the circuit
   public void setIsInCircuit(boolean in)
   {
      isInCircuit = in;
   }
   
   public boolean isTouchingActiveGate(Circuit circuit_in)
   {
      //check only its immediate inputs
      for(int i = 0; i < inputs.size(); i++)
      {
         if(inputs.get(i).isInCircuit())
         {
            //System.out.println("is an output to an active gate");
            return true;
         }
      }
      
      //check if any active gates are attatched to it immediately
      for(int i = 0; i < circuit_in.size(); i++)
      {
         //System.out.println("Gate "+ circuit_in.get(i).getStringType());
         //System.out.println("Inputs: "+ circuit_in.get(i).getInputs());
         //System.out.println("Inputs num: "+ circuit_in.get(i).getInputs().size());
         for(int j = 0; j < circuit_in.getAtIndex(i).getInputs().size(); j++)
         {
            if(circuit_in.getAtIndex(i).getInputs().get(j) == this)
            {
               //System.out.println("is an input to an active gate");
               return true;
            }
         }
      }
      
      return false;
   }
   
   public void activateInputs(Circuit currentCircuit)
   {
      for(int i = 0; i < inputs.size(); i++)
      {
         if(!inputs.get(i).isInCircuit())
         {
            Gate temp = inputs.get(i);
            currentCircuit.removeBebe(temp.getGateNum());
            currentCircuit.addGate(temp);
            temp.setIsInCircuit(true);
         }
         
         inputs.get(i).activateInputs(currentCircuit);   
      }
   }
   
   public void activateOutputs(Circuit currentCircuit)
   {
      for(int i = 0; i < currentCircuit.Nsize(); i++)
      {  
         if(!currentCircuit.getNGate(i).isInCircuit())
         {
            if(currentCircuit.getNGate(i).isAttatched2Gate(this))
            {
               //activate this gate's inputs
               currentCircuit.getNGate(i).activateInputs(currentCircuit);
               
               //activate this gate
               Gate temp = currentCircuit.getNGate(i);
               currentCircuit.removeBebe(temp.getGateNum());
               currentCircuit.addGate(temp);
               temp.setIsInCircuit(true);
               i--;
            }
         }
      }
   }  
   
   //Adding and removing an input Gate (the actual Gate object)
   //Must remember to call calculateDepth after every time you call one of these methods
   public void addInput(Gate inGate)
   {
      inputs.add(inGate);
      //System.out.println("Gate " + gatenum + " input: " + inGate.getGateNum());
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
      ////System.out.println(getStringType()+" depth: "+getDepth());
   }
   
   public boolean inPath(int xStart_in, int yStart_in, int xFinish_in, int yFinish_in)
   {
      //gate boundaries -- should be the gate now comparing to
       int top = yStart - 5;
       int bottom = yFinish + 5;
       int left = xStart - 20;
       int right = xFinish + 5;
       
       int oleft, oright, otop, obottom;
       
     //line boundaries
     if(xStart_in < xFinish_in)
     {
         oleft = xStart_in;
         oright = xFinish_in; 
     }
     else
     {
         oleft = xFinish_in;
         oright = xStart_in; 
     }
     
     if(yStart_in < yFinish_in)
     {
         otop = yStart_in;
         obottom = yFinish_in;
     }
     else
     {
         otop = yFinish_in;
         obottom = yStart_in;
     } 
       
       //print bool values
       /*if(bottom <= otop)
        //System.out.println("--No Coll because (bottom <= otop)");
       if(top >= obottom)
        //System.out.println("--No Coll because (top >= obottom)");
       if(left >= oright)
        //System.out.println("--No Coll because (left >= oright)");
       if(right <= oleft)
        //System.out.println("--No Coll because (right <= oleft)");  */
         
        return !(  (bottom <= otop) || 
                  (top >= obottom) ||
                  (left >= oright) ||
                  (right <= oleft) );
   }

   //This method is used when opening a file or optimizing
   public void draw(Graphics g, int row, int column, int maxColumn, int maxRow)
   {
      g.setColor(new Color(220,220,220));
      g.fillRect(xStart - 20, yStart - 5, (xFinish + 5) - (xStart - 20), (yFinish + 5) - (yStart - 5));
      
      //first draw the gate
      if(!isInCircuit())
        g.setColor(Color.RED);
      else
         g.setColor(Color.BLACK);
          
      drawGate(g, row, column, maxColumn, maxRow);
       
      //draw the boundaries for checking PLEASE DONT REMOVE
      /*g.setColor(Color.RED);
      g.drawLine(xStart - 20, yStart - 5, xFinish + 5, yStart - 5);
      g.drawLine(xStart - 20, yStart - 5, xStart - 20, yFinish + 5);
      g.drawLine(xStart - 20, yFinish + 5, xFinish + 5, yFinish + 5);
      g.drawLine(xFinish + 5, yStart - 5, xFinish + 5, yFinish + 5);*/
      
       
      /*
      g.setColor(Color.RED);
      g.drawLine(xStart, yStart, xFinish, yStart);
      g.drawLine(xStart, yStart, xStart, yFinish);
      g.drawLine(xStart, yFinish, xFinish, yFinish);
      g.drawLine(xFinish, yStart, xFinish, yFinish);
      */
       
      if(!BodyGUI.panel.isInEdit())
      {
         //draw the output in the gate
         g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
         g.setColor(Color.BLACK);
         if(type != gatetype.TRUE && type != gatetype.FALSE)
         {
            g.drawString(Integer.toString(getOutputInt()),(xStart + ((xFinish-xStart)/2) - 10), (yStart + ((yFinish-yStart)/2) + 10));
         }
      }
   }
   
   //This is used for repainting when editing
   public void redraw(Graphics g)
   {
      g.setColor(new Color(220,220,220));
      g.fillRect(xStart - 20, yStart - 5, (xFinish + 5) - (xStart - 20), (yFinish + 5) - (yStart - 5));
      
      if(!isInCircuit())
      {
         //System.out.println(getStringType()+" is not in circuit");
         g.setColor(Color.RED);
      }
      else
         g.setColor(Color.BLACK);
         
      redrawGate(g);
      
      /*g.setColor(Color.ORANGE);
      g.drawLine(xStart - 20, yStart - 5, xFinish + 5, yStart - 5);
      g.drawLine(xStart - 20, yStart - 5, xStart - 20, yFinish + 5);
      g.drawLine(xStart - 20, yFinish + 5, xFinish + 5, yFinish + 5);
      g.drawLine(xFinish + 5, yStart - 5, xFinish + 5, yFinish + 5);*/
      
      //Testing area rectangle
      /*
      g.setColor(Color.RED);
       g.drawLine(xStart, yStart, xFinish, yStart);
       g.drawLine(xStart, yStart, xStart, yFinish);
       g.drawLine(xStart, yFinish, xFinish, yFinish);
       g.drawLine(xFinish, yStart, xFinish, yFinish);
       */

      if(!BodyGUI.panel.isInEdit())
      {
         //draw the output in the gate
         g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
         g.setColor(Color.BLACK);
         if(type != gatetype.TRUE && type != gatetype.FALSE)
         {
            g.drawString(Integer.toString(getOutputInt()),(xStart + ((xFinish-xStart)/2) - 10), (yStart + ((yFinish-yStart)/2) + 10));
         }
      }
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
      
      //reset trunkLength for each gate per paint cycle
      trunkLength = 15;
      
      //all input wires to one gate will have the same color
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
  
         
         //draw trunk line from 11 to 30 units
         //the trunk may be drawn multiple times though...might want to fix later
         g.drawLine(xWireStart, yWireStart, xWireStart + trunkLength, yWireStart); 
         
         xWireStart += trunkLength;            
         
         //draws the connecting line
         drawNonDiagLine(g, xWireStart, yWireStart, xWireFinish, yWireFinish, drawnGates, gatenum, origgateNum);
         
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
   
   public void drawNonDiagLine(Graphics g, int xStart_in, int yStart_in, int xFinish_in, int yFinish_in, ArrayList<Gate> drawnGates, int destgateNum, int origgateNum)
   {
      boolean startChanged = false;
      int gateNum = -1;
      
      //System.out.println("Dest: " + destgateNum + " Origin: " + origgateNum);
      
      //draw in the horizontal direction until reach x destination
      while(xStart_in != (xFinish_in-21))
      {
         for(int i = 0; i < drawnGates.size(); i++)
         {
         
            //must also avoid passing through the original and destination gate
            //check if start x of a gate is between the origin and finish location of the desired line
            if(xStart_in < (xFinish_in - 21))
            {
              //System.out.println("1Checking Horix Coll for "+ drawnGates.get(i).getStringType());
               if((drawnGates.get(i).getxStart() - 20 > xStart_in && drawnGates.get(i).getxStart() - 20 < (xFinish_in - 21)) && drawnGates.get(i).inPath(xStart_in, yStart_in, drawnGates.get(i).getxStart(), yStart_in))
               {
                 //System.out.println("issue w/ "+drawnGates.get(i).getStringType()+" num: "+drawnGates.get(i).getGateNum()+" in HORIZ");
                  gateNum = i;
                  break;
               }
            }
            else
            {
              //System.out.println("2Checking Horix Coll for "+ drawnGates.get(i).getStringType());
               if((drawnGates.get(i).getxFinish() + 5 < xStart_in && drawnGates.get(i).getxFinish() + 5 > (xFinish_in - 21)) && drawnGates.get(i).inPath(xStart_in, yStart_in, drawnGates.get(i).getxStart(), yStart_in))
               {
                 //System.out.println("issue w/ "+drawnGates.get(i).getStringType()+" num: "+drawnGates.get(i).getGateNum()+" in HORIZ");
                  gateNum = i;
                  break;
               }
            }
            /*if(() && drawnGates.get(i).inPath(xStart_in, yStart_in, xFinish_in - 21, yStart_in))
            {
              //System.out.println("issue w/ "+drawnGates.get(i).getStringType()+" num: "+drawnGates.get(i).getGateNum()+" in HORIZ");
               gateNum = i;
               break;
            }*/
         }
         
         //g.setColor(Color.YELLOW);
         //draw horizontally past that gate, after moving correct y above/below the obstacle gate
         if(gateNum >= 0)
         {
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
            
            //now draw horizontally past the gate
            //draw right
            
            if((xFinish_in - 21) > drawnGates.get(gateNum).getxFinish() + 5) //why by 5 instead of 20?
            {
               if(Math.abs(xStart_in - (xFinish_in - 21)) < 5)
               {
                  g.drawLine(xStart_in, yStart_in, (xFinish_in - 21), yStart_in);
                  xStart_in = (xFinish_in - 21);
               }
               else
               {
                  g.drawLine(xStart_in, yStart_in, drawnGates.get(gateNum).getxFinish() + 5, yStart_in);
                  xStart_in = drawnGates.get(gateNum).getxFinish() + 5;
               }
            }
            //draw left
            else
            {
               if((xFinish_in - 21) > drawnGates.get(gateNum).getxStart())
               {
                  //g.setColor(Color.GREEN);
                  g.drawLine(xStart_in, yStart_in, (xFinish_in - 21), yStart_in);
                  xStart_in = (xFinish_in - 21);
               }
               else
               {  
                  //g.setColor(Color.BLACK);
                  //g.setColor(Color.BLACK);
                  g.drawLine(xStart_in, yStart_in, drawnGates.get(gateNum).getxStart() - 21, yStart_in);
                  xStart_in = drawnGates.get(gateNum).getxStart() - 21;
               }
               
               //g.setColor(colors.get(colorCount));
            }
         }
         //no gate intersected with in X direction
         else
         {
            //g.setColor(Color.MAGENTA);
            g.drawLine(xStart_in, yStart_in, xFinish_in - 21, yStart_in);
            xStart_in = xFinish_in - 21;
         }
         
         //after making an adjustment reset gateNum
         gateNum = -1;
      }
      
      while(yStart_in != yFinish_in)
      {
         for(int i = 0; i < drawnGates.size(); i++)
         {
            
            //must also check orifinal and destination gates
            if(yStart_in < yFinish_in)
            {
              //System.out.println("1Checking VERT Coll for "+ drawnGates.get(i).getStringType());
               if((drawnGates.get(i).getyFinish() + 5 > yStart_in && drawnGates.get(i).getyFinish() + 5 < yFinish_in) && drawnGates.get(i).inPath(xStart_in, yStart_in, xStart_in, drawnGates.get(i).getyStart()))
               {
                 //System.out.println("issue w/ "+drawnGates.get(i).getStringType()+" num: "+drawnGates.get(i).getGateNum()+" in VERT");
                  gateNum = i;
                  break;
               }
            }
            else
            {
              //System.out.println("1Checking VERT Coll for "+ drawnGates.get(i).getStringType());
               if((drawnGates.get(i).getyStart() - 5 < yStart_in && drawnGates.get(i).getyStart() - 5 > yFinish_in) && drawnGates.get(i).inPath(xStart_in, yStart_in, xStart_in, drawnGates.get(i).getyStart()))
               {
                 //System.out.println("issue w/ "+drawnGates.get(i).getStringType()+" num: "+drawnGates.get(i).getGateNum()+" in VERT");
                  gateNum = i;
                  break;
               }
            }
            
            /*if((destgateNum != drawnGates.get(i).getGateNum()) && (origgateNum != drawnGates.get(i).getGateNum()) && drawnGates.get(i).inPath(xStart_in, yStart_in, xStart_in, yFinish_in))
            {
              //System.out.println("issue w/ "+drawnGates.get(i).getStringType()+" num: "+drawnGates.get(i).getGateNum()+" in VERT");
               gateNum = i;
               break;
            }*/
         }
         
         //draw verticall past the gate, after moving correct x right of the gate
         
         if(gateNum >= 0)
         {
            
            int tempX = xStart_in;
            
            //draw up to the gate first to avoid cutting into other gates when moving right or left
            if(yStart_in < drawnGates.get(gateNum).getyStart() - 5)
            {
               g.drawLine(xStart_in, yStart_in, xStart_in, drawnGates.get(gateNum).getyStart() - 5);
               yStart_in = drawnGates.get(gateNum).getyStart() - 5;
            }
            else
            {
               g.drawLine(xStart_in, yStart_in, xStart_in, drawnGates.get(gateNum).getyFinish() + 5);
               yStart_in = drawnGates.get(gateNum).getyFinish() + 5;
            }
            
            //Only move right to avoid "broken" wires
            //move correct x right/past the gate
            //move right
            //g.setColor(Color.RED);
            g.drawLine(xStart_in, yStart_in, drawnGates.get(gateNum).getxFinish() + 7, yStart_in);
            xStart_in = drawnGates.get(gateNum).getxFinish() + 7;
               
            //move left
            /*else
            {
               //dont want to cut into the boundary so much --> by 21?
               g.setColor(Color.YELLOW);
               g.drawLine(xStart_in, yStart_in, drawnGates.get(gateNum).getxStart() - 50, yStart_in);
               xStart_in = drawnGates.get(gateNum).getxStart() - 50;
            }*/
            
            //move vertically past the gate
            //move up
            
            if(yFinish_in < yStart_in)
            {   
            //g.setColor(Color.BLUE);
               if(Math.abs(yStart_in - yFinish_in) < 5)
               {
                  g.drawLine(xStart_in, yStart_in, xStart_in, yFinish_in);
                  yStart_in = yFinish;
               }
               else
               {
               g.drawLine(xStart_in, yStart_in, xStart_in, drawnGates.get(gateNum).getyStart() - 5);
               yStart_in = drawnGates.get(gateNum).getyStart() - 5;
               }
            }
            //move down
            else
            {
               //g.setColor(Color.GRAY);
               if(Math.abs(yStart_in - yFinish_in) < 5)
               {
                  g.drawLine(xStart_in, yStart_in, xStart_in, yFinish_in);
                  yStart_in = yFinish;
               }
               else
               {
                  g.drawLine(xStart_in, yStart_in, xStart_in, drawnGates.get(gateNum).getyFinish() + 5);
                  yStart_in = drawnGates.get(gateNum).getyFinish() + 5;
               }
            }
            
            //move xStart back to correct place
            //this may run back through a gate though?
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
      g.drawLine(xStart_in, yStart_in, xStart_in + 21, yStart_in);     
   }
   
   public abstract void drawGate(Graphics g, int row, int column, int maxColumn, int maxRow);
  
   public abstract void redrawGate(Graphics g);
  
   public abstract boolean calculateOutput();
}

//may delete later
/*(destgateNum != drawnGates.get(i).getGateNum()) && (origgateNum != drawnGates.get(i).getGateNum()) &&*/ 