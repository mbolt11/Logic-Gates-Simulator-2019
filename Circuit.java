/* Circuit class for LGS Project */

import java.util.*;

public class Circuit
{
   //Member variables
   private ArrayList<Gate> allGates; 
   private int columns = 0;
   
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
                          //down too much... might have to move this later
   }
   
   public void removeGate(Gate gate_in)
   {
      allGates.remove(gate_in);
      calculateColumns(); //Same note as above
   }
   
   //Accessor for specific gates according to their gate number
   public Gate get(int gatenum)
   {
      return allGates.get(gatenum-1);
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
}