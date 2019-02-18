/* Circuit class for LGS Project */

import java.util.*;
import java.io.*;
import java.nio.ByteBuffer;

public class Circuit
{
   //Member variables
   private ArrayList<Gate> activeGates;
   private ArrayList<Gate> inactiveGates; 
   private int columns = 0;
   private int rows = 0;
   
   //Constructor
   public Circuit()
   {
      activeGates = new ArrayList<Gate>();
      inactiveGates = new ArrayList<Gate>();
   }
   
   //Methods to add/remove to/from inactive gates
   public void addBebe(Gate gate_in)
   {
      inactiveGates.add(gate_in);
   }
   
   //check for all instances that call this!!!!!!!!!!!!!
   public void removeBebe(int gatenum)
   { 
      //find the index more efficiently
      for(int i = 0; i < inactiveGates.size(); i++)
      {
         if(inactiveGates.get(i).getGateNum() == gatenum)
         {
           //System.out.println("Gatenum: "+gatenum+", index: "+i);
           inactiveGates.remove(i);
           break; 
         }
      }
   }
   
   //Add and remove gates
   public void addGate(Gate gate_in)
   {
      activeGates.add(gate_in);
   }
   
   public void removeGate(int gatenum)
   {
      activeGates.remove(gatenum-1);  
   }
   
   //Accessor for specific gates according to their gate number
   public Gate get(int gatenum)
   {
      for(int i=0; i < activeGates.size(); i++)
      {
         if(activeGates.get(i).getGateNum() == gatenum)
         {
            return activeGates.get(i);
         }
      }
      //System.out.println("Gate number was not found.");
      return null;
   }
   
   //Accessor for gates according to their index in the arraylist
   public Gate getAtIndex(int index)
   {
      return activeGates.get(index);
   }
   
   //Accessor for inactive gates
   public Gate getNGate(int index)
   {
      return inactiveGates.get(index);
   }
   
   //Size functions for 2 arraylists
   public int size()
   {
      return activeGates.size();
   }
   
   public int Nsize()
   {
      return inactiveGates.size();
   }
   
   
   public boolean isAttatched2Input(Gate nGate_in)
   {
      boolean result = false;
      for(int i = 0; i < nGate_in.getInputs().size(); i++)
      {
         result = isAttatched2Input(nGate_in.getInputs().get(i));
         if(result == true)
            return result;
      }
      if(nGate_in.isInCircuit()) //Input/True/False must be an active gate
      { 
         if(nGate_in.getStringType().equals("INPUT") || nGate_in.getStringType().equals("TRUE") || nGate_in.getStringType().equals("FALSE") )
            result = true;
      }
      return result;
   }
   
   public boolean isAttatched2Input_BeforeCircuit(Gate nGate_in)
   {
      boolean result = false;
      for(int i = 0; i < nGate_in.getInputs().size(); i++)
      {
         result = isAttatched2Input_BeforeCircuit(nGate_in.getInputs().get(i));
      }
      //Add input to circuit
      if(nGate_in.getStringType().equals("INPUT") || nGate_in.getStringType().equals("TRUE") || nGate_in.getStringType().equals("FALSE") )
      {
         result = true;
         
         if(!nGate_in.isInCircuit())
         {
            addGate(nGate_in);
            removeBebe(nGate_in.getGateNum());
            nGate_in.setIsInCircuit(true);
         }
      }
      return result;
   }
   
   public void AddConnectedInactiveGates()
   {
      //search through each gate in Ngates, and if one of its inputs is active then make active
      //then re-search from beginning
      /*System.out.println("STARTING SEARCH");
      for(int i = 0; i < inactiveGates.size(); i++)
      {
         System.out.println(inactiveGates.get(i).getStringType()+ " is in Ngates");
      }
      
      System.out.println();*/
      
      for(int i = 0; i < inactiveGates.size(); i++)
      {
         //System.out.println("i: "+ i);
         for(int j = 0; j < inactiveGates.get(i).getInputs().size(); j++)
         {
            //System.out.println("-j: "+ j);
            if(inactiveGates.get(i).getInputs().get(j).isInCircuit())
            {
               //System.out.println("Input "+inactiveGates.get(i).getInputs().get(j).getStringType()+" of "+inactiveGates.get(i).getStringType()+" is in circuit, YAY");
               //add the Ngate to the circuit and reset i = 0
               Gate temp = inactiveGates.get(i);
               addGate(temp);
               removeBebe(temp.getGateNum());
               temp.setIsInCircuit(true);
               i = -1;
               break;
            }
            else
            {
               //System.out.println("Input "+inactiveGates.get(i).getInputs().get(j).getStringType()+" of "+inactiveGates.get(i).getStringType()+" is not in circuit");
            }
         }
         //System.out.println("bottom of outer for reached w/ i: "+i);
      }
      
      //System.out.println("END SEARCH");
      /*for(int i = 0; i < inactiveGates.size(); i++)
      {
         System.out.println(inactiveGates.get(i).getStringType()+ " is in Ngates");
      }
      System.out.println();*/
   }
   
   //Method to sort the gates into number order
   public void sortCircuit()
   {
      //Insertion sort
      int i, j, num; 
      Gate temp;
      for (i = 1; i < activeGates.size(); i++) 
      { 
         temp = activeGates.get(i);
         num = temp.getGateNum(); 
         j = i-1; 
         while (j >= 0 && activeGates.get(j).getGateNum() > num) 
         { 
             activeGates.set(j+1,activeGates.get(j)); 
             j = j-1; 
         } 
         activeGates.set(j+1,temp); 
      }
   }
   
   //Method to calculate the depth for all the gates in the circuit
   public void calculateAllDepths()
   {
      //System.out.println("&&&&&&&&&&&&&&&&&_Starting calculate depth");
      for(int i = 0; i < activeGates.size(); i++)
      {
         activeGates.get(i).calculateDepth();
      }
   }
   
   //Method to calculate the total number of columns needed for this circuit
   public void calculateColumns()
   {
      int maxdepth = 0;
      for(int i=0; i<activeGates.size(); i++)
      {
         if(activeGates.get(i).getDepth() > maxdepth)
         {
            maxdepth = activeGates.get(i).getDepth();
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
      
      for(int i=0; i<activeGates.size(); i++)
      {
         columnsArr[activeGates.get(i).getDepth()] += 1;
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
   public void saveToASCII(String filename)
   {
      try
      {
         //Create a file with the given name and assign it to a buffered writer
         File outfile = new File(filename);
         FileWriter fw = new FileWriter(outfile);
         BufferedWriter writer = new BufferedWriter(fw);
         
         for(int i=0; i<activeGates.size(); i++)
         {
            //Get the info from this gate to write
            int gatenum = activeGates.get(i).getGateNum();
            String gatetype = activeGates.get(i).getStringType().toLowerCase();
            ArrayList<Integer> inputs = activeGates.get(i).getInputInts();
            
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
         //System.out.println("File could not be opened.");
      }
      
   }
   
   //Method to save circuit to binary file
   public void saveToBinary(String filename)
   {
      try
      {
         //Create a file with the given name and assign it to an output stream
         FileOutputStream fostream = new FileOutputStream(filename);
         //System.out.println(activeGates.size());
                  
         for(int i=0; i<activeGates.size(); i++)
         {
            //Get the gate number of this gate and convert into 2 bytes to write
            int gatenum = activeGates.get(i).getGateNum();
            byte[] array1 = new byte[2];
            array1[0] = (byte) (gatenum & 0xFF);
            array1[1] = (byte) ((gatenum >> 8) & 0xFF);
            fostream.write(array1);
            
            //Get the gate type, convert to numerical value to be stored in 2 bytes
            int gatetype = activeGates.get(i).getType().ordinal();
            byte[] array2 = new byte[2];
            array2[0] = (byte) (gatetype & 0xFF);
            array2[1] = (byte) ((gatetype >> 8) & 0xFF);
            fostream.write(array2);
            
            //Get the arraylist of inputs and write them
            ArrayList<Integer> inputs = activeGates.get(i).getInputInts();
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
         //System.out.println("File could not be opened.");
      }
      
   }
}