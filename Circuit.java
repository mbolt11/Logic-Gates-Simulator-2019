/* Circuit class for LGS Project */

import java.util.*;
import java.io.*;

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
      calculateColumns(); //Not sure if recalculating this every time will slow it 
      calculateRows();   //down too much... might have to move this later
   }
   
   public void removeGate(Gate gate_in)
   {
      allGates.remove(gate_in);
      calculateColumns(); //Same note as above
      calculateRows();
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
         DataOutputStream ostream = new DataOutputStream(new BufferedOutputStream(
              new FileOutputStream("SaveBinary.bin")));
         
         for(int i=0; i<allGates.size(); i++)
         {
            //Get the info from this gate to write
            int gatenum = allGates.get(i).getGateNum();
            String gatetype = allGates.get(i).getStringType().toLowerCase();
            ArrayList<Integer> inputs = allGates.get(i).getInputInts();
            
            //Write the info in the correct format
            ostream.writeInt(gatenum);
            ostream.writeBytes(" " + gatetype);
            for(int j=0; j < inputs.size(); j++)
            {
               ostream.writeBytes(" ");
               ostream.writeInt(inputs.get(j));
            }
            ostream.writeBytes("\n");
         }
         
         ostream.close();
      }
      catch(IOException io)
      {
         System.out.println("File could not be opened.");
      }
      
   }
}