//Body JPanel File for LGS Project

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class BodyGUI extends JPanel
{
   private Circuit ourCircuit;
   
   public BodyGUI()
   {
      super();
      
      //configure the Circuit data structure from specified file
      //for now just an ASCII file
      ReadASCII();
      
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
               
               //If this gate has any inputs from previous gates, record them
               while(readline.hasNextInt())
               {
                  int inputGate = readline.nextInt();
                  theGate.addInput(ourCircuit.get(inputGate));
                  
                  System.out.println("--InputGate:"+inputGate);
               }
            }
         }
      }
      catch(IOException io)
      {
         System.out.println("File could not be opened.");
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
      
      System.out.println("Done painting");
   }
}