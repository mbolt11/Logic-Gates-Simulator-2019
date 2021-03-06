//Body JPanel File for LGS Project

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.nio.*;

public class BodyGUI extends JPanel
{
   public static BodyGUI panel;
   private Circuit ourCircuit;
   private HeaderGUI headerpanel;
   private boolean editmode;//Whether we are in editmode or not
   private boolean openning;//Whether we are opening/optimizing or not
   
   //Singleton for the circuit
   public Circuit getCircuit()
   {
      if(ourCircuit == null)
      {
         ourCircuit = new Circuit();
         return ourCircuit;
      }
      else
      {
         return ourCircuit;
      }
   }
   
   //Constructor
   public BodyGUI()
   {
      super();
      
      editmode = true;
      panel = this;
      
      setPreferredSize(new Dimension(1000,950));   
      setBackground(Color.WHITE);
   }
   
   //Method to switch in and out of editmode and access
   public void switchMode()
   {
      editmode = !editmode;
   }
   public boolean isInEdit()
   {
      return editmode;
   }
   
   //Method to read a circuit in from an ascii file
   public void ReadASCII(String filename)
   {
      try
      {
         //Open the file and assign a scanner to it
         File infile = new File(filename);
         Scanner readfile = new Scanner(infile);
         openning = true;
         
         //Create a new circuit object to put our gates in
         ourCircuit = new Circuit();
         
         //System.out.println("------FILE------");
         
         while(readfile.hasNextLine())
         {
            //Get the current line of text and assign a new scanner to read that line
            String line = readfile.nextLine();
            Scanner readline = new Scanner(line);
            
            //Read the info from this line
            int gatenum = readline.nextInt();
            String type = readline.next();
            //System.out.println("Gatenum:"+gatenum+" type:"+type);
                     
            //Create the appropriate gate type according to string
            Gate theGate;
            switch(type)
            {
               case "true":
               {
                  theGate = new TRUE(gatenum);
                  break;
               }
               case "false":
               {
                  theGate = new FALSE(gatenum);
                  break;
               }   
               case "input":
               {
                  theGate = new INPUT(gatenum);
                  break;
               }
               case "and":
               {
                  theGate = new AND(gatenum,false);
                  break;
               }
               case "or":
               {
                  theGate = new OR(gatenum,false);
                  break;
               }
               case "xor":
               {
                  theGate = new XOR(gatenum);
                  break;
               }
               case "not":
               {
                  theGate = new NOT(gatenum);
                  break;
               }
               case "nor":
               {
                  theGate = new OR(gatenum,true);
                  break;
               }
               case "nand":
               {
                  theGate = new AND(gatenum,true);
                  break;
               }
               default: //case: "output"
               {
                  theGate = new OUTPUT(gatenum);
                  break;
               }
            }

            //Add this gate to the circuit and mark that it is offically added
            ourCircuit.addGate(theGate);
            theGate.setIsInCircuit(true);
            
            //If this gate has any inputs from previous gates, record them (just the numbers)
            while(readline.hasNextInt())
            {
               theGate.addInputInt(readline.nextInt());
            }
         }
         
         //Now that all gates have been made we can fill the ArrayList of input Gates for each one
         populateInputGates(ourCircuit);
         
         //Sort the circuit, then calculate depths
         ourCircuit.sortCircuit();
         
         ourCircuit.calculateAllDepths();
         
         //calculate columns and rows
         ourCircuit.calculateColumns();
         ourCircuit.calculateRows(); 
                  
         //Now call method to calculate outputs
         allCircuitOutputs(ourCircuit); 
         
         //Now draw the circuit
         repaint();
         
         HeaderGUI.headerpanel.setMessage("Opened "+filename);
      }
      catch(IOException io)
      {
         //System.out.println("File could not be opened.");
      }
   }
   
   //Method to read a circuit in from a binary file
   public void ReadBinary(String filename)
   {
      try
      {
         //Create a circuit object to put our gates in
         ourCircuit = new Circuit();
         openning = true;
         
         //System.out.println("------FILE------");
         
         //Open a new inputstream for the file
         FileInputStream istream = new FileInputStream(filename);
         
         while(istream.available() > 0)
         {
            //Read the first two bytes into the gate number, cast to integer
            byte[] gatenum = new byte[2];
            istream.read(gatenum);
            int numInt = (gatenum[1] << 8) | gatenum[0];
            
            //Read the second two bytes into gate type number according to enum index
            byte[] gatetype = new byte[2];
            istream.read(gatetype);
            int typeInt = (gatetype[1] << 8) | gatetype[0];
            
            //Test statement
            //System.out.println("Gatenum:"+numInt+" type:"+typeInt);
            
            //Instantiate the appropriate type of gate according to the type number
            if(typeInt >= 0 && typeInt <= 9)
            {               
               Gate theGate;
               switch(typeInt)
               {
                  case 0:
                  {
                     theGate = new INPUT(numInt);
                     break;
                  }
                  case 1:
                  {
                     theGate = new OUTPUT(numInt);
                     break;
                  }
                  case 2:
                  {
                     theGate = new AND(numInt,false);
                     break;
                  }
                  case 3:
                  {
                     theGate = new OR(numInt,false);
                     break;
                  }
                  case 4:
                  {
                     theGate = new XOR(numInt);
                     break;
                  }
                  case 5:
                  {
                     theGate = new NOT(numInt);
                     break;
                  }
                  case 6:
                  {
                     theGate = new OR(numInt,true);
                     break;
                  }
                  case 7:
                  {
                     theGate = new AND(numInt,true);
                     break;
                  }
                  case 8:
                  {
                     theGate = new TRUE(numInt);
                     break;
                  }
                  default: //case 9 aka FALSE
                  {
                     theGate = new FALSE(numInt);
                     break;
                  }
               }

               //Add this gate to the circuit and mark that it is offically added
               ourCircuit.addGate(theGate);
               theGate.setIsInCircuit(true);
               
               //Read the rest of the bytes as inputs 2 at a time until you read -1
               //Fill in arraylist of input integers with the values
               int inputInt = 0;
               while(inputInt != -1)
               {
                  //Get the next int value and save it to inputInt
                  byte[] input = new byte[2];
                  istream.read(input);
                  inputInt = (input[1] << 8) | input[0];
                  //System.out.println("Gate "+numInt+" input: "+inputInt);
                  
                  //If the value is -1, we have reached the "end of the line"
                  if(inputInt != -1)
                  {
                     theGate.addInputInt(inputInt);
                  }
               }
            }
            else
            {
               //System.out.println("Invalid gate type number");
            }
         }
         
         //Print what is in the circuit for debugging
         /*for(int i=0; i<ourCircuit.size(); i++)
         {
           //System.out.println(ourCircuit.getAtIndex(i).getStringType()+":");
           //System.out.println("Gate Num:"+ourCircuit.getAtIndex(i).getGateNum());
         }*/
         
         //Now that all gates have been made we can fill the ArrayList of input Gates for each one
         populateInputGates(ourCircuit);
         
         //Sort the circuit, then calculate depths
         ourCircuit.sortCircuit();
         ourCircuit.calculateAllDepths();
         
         //calculate columns and rows
         ourCircuit.calculateColumns(); 
         ourCircuit.calculateRows();   
                  
         //Now call method to calculate outputs
         allCircuitOutputs(ourCircuit);
         
         //Now draw the circuit
         repaint();
         
         HeaderGUI.headerpanel.setMessage("Opened "+filename);
      }
      catch(IOException io)
      {
         if(io instanceof FileNotFoundException)
         {
            //System.out.println("File could not be opened.");
         }
         else
         {
            io.printStackTrace();
         }
      }
   }
   
   //Method to populate the array of input Gates for each gate once file has been loaded
   public void populateInputGates(Circuit circuit_in)
   {
      //For every gate in the circuit
      for(int i=0; i < circuit_in.size(); i++)
      {         
         //For every input line of that gate
         for(int j=0; j < circuit_in.getAtIndex(i).getInputInts().size(); j++)
         {
            int inGateNum = circuit_in.getAtIndex(i).getInputInts().get(j);
            circuit_in.getAtIndex(i).addInput(circuit_in.get(inGateNum));
         }
      }
   }
   
   //Method to calculate outputs of our circuit
   public void allCircuitOutputs(Circuit circuit_in)
   {
     //System.out.println("------------CalculateOutput-----------------");
      //call functions for calculate output throughout the circuit
      /*for(int i = 0; i < circuit_in.size(); i++)
      {
         if(circuit_in.getAtIndex(i).getStringType() == "OUTPUT")
         {
            circuit_in.getAtIndex(i).calculateOutput();
            //System.out.println("--Result:"+circuit_in.getAtIndex(i).getOutput());
         }
      }*/
      
      for(int i = 1; i <= circuit_in.getMaxDepth(); i++)
      {
         for(int j = 0; j < circuit_in.size(); j++)
         {
            if(circuit_in.getAtIndex(j).getDepth() == i)
            {
               circuit_in.getAtIndex(j).calculateOutput();
            }
         }
         
         for(int j = 0; j < circuit_in.Nsize(); j++)
         {
            if(circuit_in.getNGate(j).getDepth() == i)
               {
                  circuit_in.getNGate(j).calculateOutput();
               }
         }
      } 
   }
   
   //overwrites the drawing component to make it draw each of the gates in the circuit
   public void paintComponent(Graphics g)
   {
      super.paintComponent(g);
      Graphics2D g2 = (Graphics2D) g;
      g2.setStroke(new BasicStroke(3));
      
      //must create a list of the gates drawn so far in order to check drawing wires against gate boundaries as drawing progresses
      ArrayList<Gate> drawnGates = new ArrayList<Gate>();
      
      //This is what gets painted when opening for the first time or optimizing
      if(openning)
      {
         openning = false;
         //System.out.println("\n\n-----------DRAWING---------- Circuit of size "+ourCircuit.size());
         int row = 0;
         int column = 0;
         int maxColumn = ourCircuit.getColumns();
         int maxRow = ourCircuit.getRows();
         int gatesDrawn = 0;
         
         //System.out.println("columns: " + maxColumn + " rows: " + maxRow);
         
         //draws the gates in order
         for(int i = 1; i <= maxColumn; i++)
         {
            column = i;
            row = 0;
            for(int j = 0; j < ourCircuit.size(); j++)
            {
               if(ourCircuit.getAtIndex(j).getDepth() == i)
               {
                  //draws the gates and places drawn gates in another arraylist
                  ourCircuit.getAtIndex(j).draw(g, row, column, maxColumn, maxRow);
                  drawnGates.add(ourCircuit.getAtIndex(j));
                  gatesDrawn++;
                  row++;
               }
            }
         } 
      }
      else //This is what gets painted when just editing
      {
         //This draws active gates
         for(int i = 0; i < ourCircuit.size(); i++)
         {
            ourCircuit.getAtIndex(i).redraw(g);
            drawnGates.add(ourCircuit.getAtIndex(i));
         }
         //This draws new/inactive gates
         for(int i = 0; i < ourCircuit.Nsize(); i++)
         {
            ourCircuit.getNGate(i).redraw(g);
            drawnGates.add(ourCircuit.getNGate(i));
         }
      }
      
      if(drawnGates.size() > 0)
      {
        //System.out.println("STARTING WIRE DRAW");
         //draws the wires to the gates
         for(int i = 0; i < drawnGates.size(); i++)
         {
            if(i == 0)
               drawnGates.get(i).resetColorCount();
               
            drawnGates.get(i).drawWires(g, drawnGates);
         }
        //System.out.println("FINISH WIRE DRAW\n");
      }
      //System.out.println("made it");
      
      //Draw the arrow button
      g.drawRect(880,20,100,55);
      if(editmode)
      {
         g.setColor(Color.CYAN);
         g.fillRect(880,20,100,55);
         g.setColor(Color.BLACK);
      }
      else
      {
         g.clearRect(880,20,100,55);
      }
      g.drawLine(890,30,890,65);
      g.drawLine(890,30,950,30);
      g.drawLine(890,65,950,65);
      g.drawLine(950,30,950,25);
      g.drawLine(950,65,950,70);
      g.drawLine(950,25,970,47);
      g2.drawLine(950,70,970,47);
   }
}