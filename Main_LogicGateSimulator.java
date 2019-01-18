//LGS Project main/client file

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;

public class Main_LogicGateSimulator
{
   //The MAIN
   public static void main (String [] args)
   {
      //Open up the frame
      LGS_JFrame theframe = new LGS_JFrame();
      theframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);     
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
         Circuit ourCircuit = new Circuit();
         
         while(readfile.hasNextLine())
         {
            //Get the current line of text and assign a new scanner to read that line
            String line = readfile.nextLine();
            Scanner readline = new Scanner(line);
            
            //Read the info from this line
            int gatenum = readline.nextInt();
            String type = readline.next();
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
               }
            }
         }
         
         //Now that the data has been read into gates/circuits,
         //call a method to draw the circuit
      }
      catch(IOException io)
      {
         System.out.println("File could not be opened.");
      }
   }
}