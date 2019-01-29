/* Circuit class for LGS Project */

import java.util.*;
import java.io.*;
import java.nio.ByteBuffer;

public class Circuit
{
   //Member variables
   private ArrayList<Gate> allGates; 
   private int columns = 0;
   private int rows = 0;
   
   //Constructor
   public Circuit()
   {
      allGates = new ArrayList<Gate>();
   }
   
   //Add and remove gates
   public void addGate(Gate gate_in)
   {
      allGates.add(gate_in);
   }
   
   public void removeGate(Gate gate_in)
   {
      allGates.remove(gate_in);  
   }
   
   //Accessor for specific gates according to their gate number
   public Gate get(int gatenum)
   {
      for(int i=0; i < allGates.size(); i++)
      {
         if(allGates.get(i).getGateNum() == gatenum)
         {
            return allGates.get(i);
         }
      }
      System.out.println("Gate number was not found.");
      return null;
   }
   
   public int size()
   {
      return allGates.size();
   }
   
   //Method to sort the gates into number order
   public void sortCircuit()
   {
      ArrayList<Gate> temp = new ArrayList<Gate>();
      int counter = 1;
      while(allGates.size() > 0)
      {
         for(int i = 0; i < allGates.size(); i++)
         {
            if(allGates.get(i).getGateNum() == counter)
            {
               temp.add(allGates.get(i));
               allGates.remove(i);
               break;
            }
         }
         counter++;
      }
      allGates = temp;
   }
   
   //Method to calculate the depth for all the gates in the circuit
   public void calculateAllDepths()
   {
      System.out.println("&&&&&&&&&&&&&&&&&_Starting calculate depth");
      for(int i = 0; i < allGates.size(); i++)
      {
         allGates.get(i).calculateDepth();
      }
   }
   
   //Method to calculate the total number of columns needed for this circuit
   public void calculateColumns()
   {
      int maxdepth = 0;
      for(int i=0; i<allGates.size(); i++)
      {
         if(allGates.get(i).getDepth() > maxdepth)
         {
            maxdepth = allGates.get(i).getDepth();
         }
      }
      columns = maxdepth + 1;
   }
   
   public int getColumns()
   {
      return columns;
   }
   
   //Method to calculate the total number of rows needed for this circuit
   public void calculateRows()
   {
      int maxRows = 0;
      int [] columnsArr = new int[columns];
      
      for(int i=0; i<allGates.size(); i++)
      {
         columnsArr[allGates.get(i).getDepth()] += 1;
      }
      
      for(int i=0; i<columnsArr.length; i++)
      {
         if(columnsArr[i] > maxRows)
            maxRows = columnsArr[i];
      }
      
      rows = maxRows;
   }
   
   public int getRows()
   {
      return rows;
   }
   
   //Method to save circuit to ascii file
   public void saveToASCII()
   {
      try
      {
         //Open the file writer and create buffered writer
         FileWriter save = new FileWriter("SaveASCII.txt");
         BufferedWriter writer = new BufferedWriter(save);
         
         for(int i=0; i<allGates.size(); i++)
         {
            //Get the info from this gate to write
            int gatenum = allGates.get(i).getGateNum();
            String gatetype = allGates.get(i).getStringType().toLowerCase();
            ArrayList<Integer> inputs = allGates.get(i).getInputInts();
            
            //Write the info in the correct format
            writer.write(gatenum + " " + gatetype);
            for(int j=0; j < inputs.size(); j++)
            {
               writer.write(" " + inputs.get(j));
            }
            writer.newLine();
         }
         
         writer.close();
      }
      catch(IOException io)
      {
         System.out.println("File could not be opened.");
      }
      
   }
   
   //Method to save circuit to binary file
   public void saveToBinary()
   {
      try
      {
         FileOutputStream fostream = new FileOutputStream("SaveBinary.cir");
                  
         for(int i=0; i<allGates.size(); i++)
         {
            //Get the gate number of this gate and convert into 2 bytes to write
            int gatenum = allGates.get(i).getGateNum();
            byte[] array1 = new byte[2];
            array1[0] = (byte) (gatenum & 0xFF);
            array1[1] = (byte) ((gatenum >> 8) & 0xFF);
            fostream.write(array1);
            
            //Get the gate type, convert to numerical value to be stored in 2 bytes
            int gatetype = allGates.get(i).getType().ordinal();
            byte[] array2 = new byte[2];
            array2[0] = (byte) (gatetype & 0xFF);
            array2[1] = (byte) ((gatetype >> 8) & 0xFF);
            fostream.write(array2);
            
            //Get the arraylist of inputs and write them
            ArrayList<Integer> inputs = allGates.get(i).getInputInts();
            for(int j=0; j < inputs.size(); j++)
            {
               int inputline = inputs.get(j);
               byte[] array3 = new byte[2];
               array3[0] = (byte) (inputline & 0xFF);
               array3[1] = (byte) ((inputline >> 8) & 0xFF);
               fostream.write(array3);
            }
            
            //Write a -1 to indicate a new line
            int eol = -1;
            byte[] array4 = new byte[2];
            array4[0] = (byte) (eol & 0xFF);
            array4[1] = (byte) ((eol >> 8) & 0xFF);
            fostream.write(array4);
         }
         //Flush and close stream
         fostream.flush();
         fostream.close();
      }
      catch(IOException io)
      {
         System.out.println("File could not be opened.");
      }
      
   }
}