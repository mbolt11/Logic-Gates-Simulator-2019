//Body JPanel File for LGS Project

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.nio.*;

public class BodyGUI extends JPanel
{
   private Circuit ourCircuit;
   
   public BodyGUI()
   {
      super();
      
      //ASCII read/save tests
      ReadASCII();
      //ourCircuit.saveToASCII();
      
      //Binary read/save tests
      ourCircuit.saveToBinary();
      ReadBinary();
      
      //do not think we need any layout because will just be drawing
      //setLayout(new FlowLayout());
      
      setPreferredSize(new Dimension(1000,950));   
      setBackground(Color.WHITE);
   }
   
   //Method to read a circuit in from an ascii file
   public void ReadASCII()
   {
      try
      {
         //Open the file and assign a scanner to it
         File infile = new File("ExampleASCII.txt");
         Scanner readfile = new Scanner(infile);
         
         //Create a circuit object to put our gates in
         ourCircuit = new Circuit();
         
         System.out.println("------FILE------");
         
         while(readfile.hasNextLine())
         {
            //Get the current line of text and assign a new scanner to read that line
            String line = readfile.nextLine();
            Scanner readline = new Scanner(line);
            
            //Read the info from this line
            int gatenum = readline.nextInt();
            String type = readline.next();
            System.out.println("Gatenum:"+gatenum+" type:"+type);
            if (type.equals("true") || type.equals("false"))
            {
               //Not sure what this is supposed to be...
            }
            else
            {               
               //Create the appropriate gate type according to string
               Gate theGate;
               switch(type)
               {
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

               //Add this gate to the circuit
               ourCircuit.addGate(theGate);
               
               //If this gate has any inputs from previous gates, record them (just the numbers)
               while(readline.hasNextInt())
               {
                  theGate.addInputInt(readline.nextInt());
               }
            }
         }
         
         //Now that all gates have been made we can fill the ArrayList of input Gates for each one
         populateInputGates(ourCircuit);
                  
         //Now call method to calculate outputs
         allCircuitOutputs(ourCircuit);   
      }
      catch(IOException io)
      {
         System.out.println("File could not be opened.");
      }
   }
   
   //Method to read a circuit in from a binary file
   public void ReadBinary()
   {
      try
      {
         //Create a circuit object to put our gates in
         ourCircuit = new Circuit();
         
         System.out.println("------FILE------");
         
         //Open a new inputstream for the file
         FileInputStream istream = new FileInputStream("SaveBinary.cir");
         
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
            System.out.println("Gatenum:"+numInt+" type:"+typeInt);
            
            //Instantiate the appropriate type of gate according to the type number
            if(typeInt >= 0 && typeInt <= 8)
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
                  default: //case 7 aka NAND
                  {
                     theGate = new AND(numInt,true);
                     break;
                  }
               }

               //Add this gate to the circuit
               ourCircuit.addGate(theGate);
               
               //Read the rest of the bytes as inputs 2 at a time until you read -1
               //Fill in arraylist of input integers with the values
               int inputInt = 0;
               while(inputInt != -1)
               {
                  //Get the next int value and save it to inputInt
                  byte[] input = new byte[2];
                  istream.read(input);
                  inputInt = (input[1] << 8) | input[0];
                  
                  //If the value is -1, we have reached the "end of the line"
                  if(inputInt != -1)
                  {
                     theGate.addInputInt(inputInt);
                  }
               }
            }
            else
            {
               System.out.println("Invalid gate type number");
            }
         }
         
         //Now that all gates have been made we can fill the ArrayList of input Gates for each one
         populateInputGates(ourCircuit);
                  
         //Now call method to calculate outputs
         allCircuitOutputs(ourCircuit);
      }
      catch(IOException io)
      {
         if(io instanceof FileNotFoundException)
         {
            System.out.println("File could not be opened.");
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
      for(int i=1; i < circuit_in.size()+1; i++)
      {
         //For every input line of that gate
         for(int j=0; j < circuit_in.get(i).getInputInts().size(); j++)
         {
            int inGateIndex = circuit_in.get(i).getInputInts().get(j);
            circuit_in.get(i).addInput(circuit_in.get(inGateIndex));
         }
         //Now calculate the depth of this gate
         circuit_in.get(i).calculateDepth();
      }
   }
   
   //Method to calculate outputs of our circuit
   public void allCircuitOutputs(Circuit circuit_in)
   {
     System.out.println("------------CalculateOutput-----------------");
      //call functions for calculate output throughout the circuit
      for(int i = 1; i < circuit_in.size()+1; i++)
      {
         //System.out.println(ourCircuit.get(i).getStringType());
         if(circuit_in.get(i).getStringType() == "OUTPUT")
         {
            circuit_in.get(i).calculateOutput();
            System.out.println("--Result:"+circuit_in.get(i).getOutput());
         }
      }
   }
   
   //overwrites the drawing component to make it draw each of the gates in the circuit
   public void paintComponent(Graphics g)
   {
      super.paintComponent(g);
      System.out.println("\n\n-----------DRAWING----------");
      int row = 0;
      int column = 0;
      int maxColumn = ourCircuit.getColumns();
      int maxRow = ourCircuit.getRows();
      int gatesDrawn = 0;
      
      for(int i = 1; i <= maxColumn; i++)
      {
         column = i;
         row = 0;
         for(int j = 1; j < ourCircuit.size() + 1; j++)
         {
            if((ourCircuit.get(j).getDepth()+1) == i)
            {
               //System.out.println(ourCircuit.get(j).getType() + " at row,column: " +row+","+column+" --DEPTH:"+i);
               ourCircuit.get(j).draw(g, row, column, maxColumn, maxRow);
               gatesDrawn++;
               row++;
            }
         }
      }
      
      //call functions to paint wires *******************************************************************************Comment this out to get rid of lines
         for(int i = 1; i < ourCircuit.size()+1; i++)
         {
            //System.out.println(ourCircuit.get(i).getStringType());
            if(ourCircuit.get(i).getStringType() == "OUTPUT")
            {
               ourCircuit.get(i).drawWires(g, 1, 1);
            }
         }
      
      System.out.println("Done painting");
   }
}