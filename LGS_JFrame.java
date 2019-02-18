//JFrame File for LGS Project

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class LGS_JFrame extends JFrame
{   
   //This variable is true if in editmode, false if in play mode
   private boolean editmode = true;
   
   //Access to our two panels   
   private BodyGUI bodypanel;
   private HeaderGUI headerpanel;
   
   //Access to the currently displaying circuit
   private Circuit theCircuit;
   
   //Rectangle representation of the arrow button
   private Rectangle arrowbutton = new Rectangle(880,20,100,55);
   
   //Variable for the gates where a new wire is drawn too/from
   private Gate inGate;
   private Gate outGate;
   
   //JFrame Constructor
   public LGS_JFrame()
   {
      //Container for the GUI elements
      Container contents = getContentPane();
      
      //Layout
      FlowLayout fl = new FlowLayout();
      fl.setVgap(0);
      
      //Add header JPanel
      headerpanel = new HeaderGUI();
      contents.add(headerpanel);
      
      //Add body JPanel
      bodypanel = new BodyGUI();
      contents.add(bodypanel);
      
      //Get access to the circuit which was created in the body panel
      theCircuit = bodypanel.getCircuit();
      
      //Add mouse adapter to body panel
      MouseEventTracking met = new MouseEventTracking();
      bodypanel.addMouseListener(met);
      bodypanel.addMouseMotionListener(met);
      
      //Add button listener to each button in the header panel
      ButtonListener bl = new ButtonListener();
      headerpanel.getOpen().addActionListener(bl);
      headerpanel.getSave().addActionListener(bl);
      headerpanel.getING().addActionListener(bl);
      
      //Set the size, visibility and layout
      setLayout(fl);
      setSize(1000,950);
      setVisible(true);
   }
   
   //Mouse Adapter class
   private class MouseEventTracking extends MouseAdapter 
   {  
      //-1 indicates no gate has been clicked
      private int gateClickedIndex = -1;
      private int nGateClicked = -1;
      
      //Offset of where the mouse clicked to where the x & y starts are
      private int gateDX, gateDY;
      private int originDX, originDY;
      
      public void mouseClicked(MouseEvent e)
      {
         //If they click the arrow button
         //May need to check circuit if they are making from scratch
         //check if an inactive gate is connected to an input and an output --> add to circuit and sort circuit
         if(arrowbutton.contains(e.getPoint()))
         {
            //Switch between edit and play mode
            //System.out.println("Clicked on arrow");
            editmode = !editmode;
            bodypanel.switchMode();
            if(editmode)
               headerpanel.setMessage("Edit Mode");
            else
               headerpanel.setMessage("Play Mode");
            
            //if the circuit object is empty, look in inactive gates for gates "attatched" to an input
            //then look for if the same gates are "attatched" to an output
            //does the circuit need to be sorted after adding a gate to it? --> if not optomizing I dont think so
            if(theCircuit.size() == 0 /*&& !editmode*/)
            {
               //System.out.println("Circuit is empty. Searching for valid ouputs and inputs");
               //must search for all outputs connected to inputs first            
               for(int i = 0; i < theCircuit.Nsize(); i++)
               {
                  //check if this output is connected to an input
                  if(theCircuit.getNGate(i).getStringType().equals("OUTPUT"))
                  {
                     //System.out.println("Found OUTPUT of gatenum:"+theCircuit.getNGate(i).getGateNum());
                     Gate temp = theCircuit.getNGate((i));
                     if(theCircuit.isAttatched2Input_BeforeCircuit(temp)) //input added in this function if needed; Also recursive boolean function  
                     {
                        //System.out.println("Added OUTPUT of gatenum:"+temp.getGateNum());
                        //input already added, now add the output
                        theCircuit.removeBebe(temp.getGateNum());
                        theCircuit.addGate(temp);
                        temp.setIsInCircuit(true);
                     }
                  }
               }
            }
            
            repaint();
            return;
         }
         
         //Search through all the gates in circuit to see if the mouse clicked on it
         for(int i = 0; i < theCircuit.size(); i++)
         {
            //If the mouse click was within the area of the gate, save the index
            if(theCircuit.getAtIndex(i).getInputAreaRect().contains(e.getPoint()))
            {
               //This is the gate that we will add an input too
               inGate = theCircuit.getAtIndex(i);
               if(editmode)
                  headerpanel.setMessage("Adding input to Gate "+String.valueOf(inGate.getGateNum()));
               //System.out.println("Found in gate");
            }
            else if(theCircuit.getAtIndex(i).getOutputAreaRect().contains(e.getPoint()))
            {
               //This is the gate where the new output will come from
               outGate = theCircuit.getAtIndex(i);
               if(editmode)
                  headerpanel.setMessage("Adding output from Gate "+String.valueOf(outGate.getGateNum()));
               //System.out.println("Found out gate");
            }
         }
         
         //Search through new gates not in circuit
         for(int i = 0; i < theCircuit.Nsize(); i++)
         {
            //If the mouse click was within the area of the gate, save the index
            if(theCircuit.getNGate(i).getInputAreaRect().contains(e.getPoint()))
            {
               //This is the gate that we will add an input too
               inGate = theCircuit.getNGate(i);
               if(editmode)
                  headerpanel.setMessage("Adding input to Gate "+String.valueOf(inGate.getGateNum()));
               //System.out.println("Found in gate");
            }
            else if(theCircuit.getNGate(i).getOutputAreaRect().contains(e.getPoint()))
            {
               //This is the gate where the new output will come from
               outGate = theCircuit.getNGate(i);
               if(editmode)
                  headerpanel.setMessage("Adding output from Gate "+String.valueOf(outGate.getGateNum()));
               //System.out.println("Found out gate");
            }
         }
         
         //Draw a new wire if they clicked on both a start and an end
         //Prevent direct circular wiring
         if(editmode && inGate != null && outGate != null && inGate != outGate)
         {
            //Series of checks to make sure this new wire is valid
            boolean draw = true;
            String ingatetype = inGate.getStringType();
            if(ingatetype.equals("INPUT") || ingatetype.equals("TRUE") || ingatetype.equals("FALSE"))
            {
               JOptionPane.showMessageDialog(null,"Cannot add an input wire to this gate.",
                  "Error",JOptionPane.ERROR_MESSAGE);
               draw = false;
               headerpanel.setMessage("Edit Mode");
               inGate = null;
            }
            if(ingatetype.equals("OUTPUT") || ingatetype.equals("NOT"))
            {
               if(inGate.getInputs().size() > 0)
               {
                  JOptionPane.showMessageDialog(null,"Cannot add another input wire to this gate.",
                     "Error",JOptionPane.ERROR_MESSAGE);
                  draw = false;
                  headerpanel.setMessage("Edit Mode");
                  inGate = null;
               }
            }
            if(ingatetype.equals("XOR"))
            {
               if(inGate.getInputs().size() >= 2)
               {
                  JOptionPane.showMessageDialog(null,"Cannot add more than 2 input wires to an XOR gate.",
                     "Error",JOptionPane.ERROR_MESSAGE);
                  draw = false;
                  headerpanel.setMessage("Edit Mode");
                  inGate = null;
               }
            }
            if(outGate.getStringType().equals("OUTPUT"))
            {
               JOptionPane.showMessageDialog(null,"Output gate cannot have an output wire.",
                     "Error",JOptionPane.ERROR_MESSAGE);
               draw = false;  
               headerpanel.setMessage("Edit Mode");
               outGate = null;
            }
            
            //If the new wire passed all the validity tests above, draw it
            if(draw)
            {
               //System.out.println("Adding a wire and adding to inputs arraylist");
               inGate.addInput(outGate);
               inGate.addInputInt(outGate.getGateNum());
               headerpanel.setMessage("Drew wire");
               
               //System.out.println("InGate: "+inGate.isInCircuit()+" OutGate:"+outGate.isInCircuit());
               
               //If it is a new gate now connected to the circuit, make adjustments
               if(inGate.isInCircuit() && !outGate.isInCircuit())
               {
                  //System.out.println("Checking all connections for outGate");
                  theCircuit.addGate(outGate);
                  theCircuit.removeBebe(outGate.getGateNum());
                  outGate.setIsInCircuit(true);
                  
                  //check for connected inactive gates
                  theCircuit.AddConnectedInactiveGates();
               }
               else if(outGate.isInCircuit() && !inGate.isInCircuit())
               {
                  //System.out.println("Checking all connections for inGate");
                  theCircuit.addGate(inGate);
                  theCircuit.removeBebe(inGate.getGateNum());
                  inGate.setIsInCircuit(true);
                  
                  //check for connected inactive gates
                  theCircuit.AddConnectedInactiveGates();
               }
               //If both gates are not in the circuit then need to check if they an inactive input and inactive output have been connected
               //check for another mini circuit that is created needs to be added to theCircuit object
               else
               {
                  //System.out.println("Checking for new mini circuit");

                  //must search for all outputs connected to inputs first            
                  for(int i = 0; i < theCircuit.Nsize(); i++)
                  {
                     //check if this output is connected to an input
                     if(theCircuit.getNGate(i).getStringType().equals("OUTPUT"))
                     {
                        //System.out.println("Found OUTPUT of gatenum:"+theCircuit.getNGate(i).getGateNum());
                        Gate temp = theCircuit.getNGate((i));
                        if(theCircuit.isAttatched2Input_BeforeCircuit(temp)) //input added in this function if needed; Also recursive boolean function  
                        {
                           //System.out.println("Added OUTPUT of gatenum:"+temp.getGateNum());
                           //input already added, now add the output
                           theCircuit.removeBebe(temp.getGateNum());
                           theCircuit.addGate(temp);
                           temp.setIsInCircuit(true);
                        }
                     }
                  }
                  
                  //check for connected inactive gates
                  theCircuit.AddConnectedInactiveGates();
               }
            }
            inGate = null;
            outGate = null;
            repaint();
            return;
         }
         
         //Switch the input T/F if in play mode
         if(!editmode)
         {
            //recalculates the outputs if they switch an input gate value
            if((inGate != null) && (inGate.getStringType().equals("INPUT")))
            {
               inGate.setOutput(!inGate.getOutput());
               bodypanel.allCircuitOutputs(theCircuit);
            }
            else if((outGate != null) && (outGate.getStringType().equals("INPUT")))
            {
               outGate.setOutput(!outGate.getOutput());
               bodypanel.allCircuitOutputs(theCircuit);
            }
            inGate = null;
            outGate = null;
            repaint();
            return;
         } 
      }
      
      public void mousePressed(MouseEvent e)
      {
         //Make sure we have the right circuit
         theCircuit = bodypanel.getCircuit();
         
         //Search through all the gates in circuit to see if the mouse clicked on it
         for(int i = 0; i < theCircuit.size(); i++)
         {
            //If the mouse click was within the area of the gate, save the index
            if(theCircuit.getAtIndex(i).getAreaRect().contains(e.getPoint()))
            {
               gateClickedIndex = i;
               nGateClicked = -1;
               //System.out.println("Clicked on a gate");
               gateDX = e.getX() - theCircuit.getAtIndex(i).getxStart();
               gateDY = e.getY() - theCircuit.getAtIndex(i).getyStart();
               return;
            }
         }
         
         //Search through new gates not in circuit
         for(int i = 0; i < theCircuit.Nsize(); i++)
         {
            if(theCircuit.getNGate(i).getAreaRect().contains(e.getPoint()))
            {
               nGateClicked = i;
               gateClickedIndex = -1;
               //System.out.println("Clicked on a gate");
               gateDX = e.getX() - theCircuit.getNGate(i).getxStart();
               gateDY = e.getY() - theCircuit.getNGate(i).getyStart();
               return;
            }
         }
         
         //If they clicked on the screen, save dx and dy for dragging the screen
         gateClickedIndex = -2;
         nGateClicked = -2;
         //System.out.println("Clicked on screen");
         originDX = e.getX();
         originDY = e.getY();
      }
      
      public void mouseDragged(MouseEvent e)
      {
         //If the gate clicked index is less than -1, scroll the screen
         if(gateClickedIndex < -1)
         {
            //Change the offsets of all the gate start positions
            for(int i = 0; i < theCircuit.size(); i++)
            {
               theCircuit.getAtIndex(i).translate((e.getX()-originDX),(e.getY()-originDY));
            }
            //This changes the offset of gates in inactiveGates
            for(int i = 0; i < theCircuit.Nsize(); i++)
            {
               theCircuit.getNGate(i).translate((e.getX()-originDX),(e.getY()-originDY));
            }
               
            originDX = e.getX();
            originDY = e.getY();
            repaint();
         }
         
         //If the gate clicked index is not -1, move the gate
         if(gateClickedIndex > -1 && editmode) //check value of gateDX/gateDY and check which direction it could be colliding
         {  
            //Change the xStart and yStart positions of the gate according to the mouse drag
            //these methods also update boundary positions
            theCircuit.getAtIndex(gateClickedIndex).setxStart(e.getX()-gateDX);
            theCircuit.getAtIndex(gateClickedIndex).setyStart(e.getY()-gateDY);
            
           //System.out.println("Checking Gate vs. Gate Boundaries");
            
            //set all gate wire validities to true
            for(int i = 0; i < theCircuit.size(); i++)
            {
               theCircuit.getAtIndex(i).setValidWires(true);
            }
            
            for(int i = 0; i < theCircuit.Nsize(); i++)
            {
               theCircuit.getNGate(i).setValidWires(true);
            }
            
            Gate ogate;
            
            //check if the new placement makes invalid wires anywhere
            //must do nested for loop here and check every gate against every other gate
            for(int i = 0; i < theCircuit.size(); i++)
            {
               for(int j = 0; j < theCircuit.size(); j++)
               {
                  if(theCircuit.getAtIndex(i) != theCircuit.getAtIndex(j))
                  {
                     ogate = theCircuit.getAtIndex(j);
                     if(theCircuit.getAtIndex(i).gatesCollide(ogate.getxStartB(),ogate.getyStartB(),ogate.getxFinishB(),ogate.getyFinishB()))
                     {
                        ogate.setValidWires(false);
                        theCircuit.getAtIndex(i).setValidWires(false);
                       //System.out.println(ogate.getStringType()+" is coll w/ "+theCircuit.getAtIndex(i).getStringType());
                     }
                  }
               }
            }
            
            for(int i = 0; i < theCircuit.Nsize(); i++)
            {
               for(int j = 0; j < theCircuit.Nsize(); j++)
               {
                  ogate = theCircuit.getNGate(j);
                  if(theCircuit.getNGate(i) != ogate)
                  {
                     if(theCircuit.getNGate(i).gatesCollide(ogate.getxStartB(),ogate.getyStartB(),ogate.getxFinishB(),ogate.getyFinishB()))
                     {
                        ogate.setValidWires(false);
                        theCircuit.getNGate(i).setValidWires(false);
                       //System.out.println(ogate.getStringType()+" is coll w/ "+theCircuit.getNGate(i).getStringType());
                     }
                  }
               }
            }
            
            for(int i = 0; i < theCircuit.size(); i++)
            {
               for(int j = 0; j < theCircuit.Nsize(); j++)
               {
                  ogate = theCircuit.getNGate(j);
                  if(theCircuit.getAtIndex(i) != ogate)
                  {
                     if(theCircuit.getAtIndex(i).gatesCollide(ogate.getxStartB(),ogate.getyStartB(),ogate.getxFinishB(),ogate.getyFinishB()))
                     {
                        ogate.setValidWires(false);
                        theCircuit.getAtIndex(i).setValidWires(false);
                       //System.out.println(ogate.getStringType()+" is coll w/ "+theCircuit.getAtIndex(i).getStringType());
                     }
                  }
               }
            }
            
            repaint();
         }
         
         if(nGateClicked > -1 && editmode)
         {
            //Change the xStart and yStart positions of the gate according to the mouse drag
            theCircuit.getNGate(nGateClicked).setxStart(e.getX()-gateDX);
            theCircuit.getNGate(nGateClicked).setyStart(e.getY()-gateDY);
            
            //set all gate wire validities to true
            for(int i = 0; i < theCircuit.size(); i++)
            {
               theCircuit.getAtIndex(i).setValidWires(true);
            }
            
            for(int i = 0; i < theCircuit.Nsize(); i++)
            {
               theCircuit.getNGate(i).setValidWires(true);
            }
            
            Gate ogate;
            
            //check if the new placement makes invalid wires anywhere
            //must do nested for loop here and check every gate against every other gate
            for(int i = 0; i < theCircuit.size(); i++)
            {
               for(int j = 0; j < theCircuit.size(); j++)
               {
                  if(theCircuit.getAtIndex(i) != theCircuit.getAtIndex(j))
                  {
                     ogate = theCircuit.getAtIndex(j);
                     if(theCircuit.getAtIndex(i).gatesCollide(ogate.getxStartB(),ogate.getyStartB(),ogate.getxFinishB(),ogate.getyFinishB()))
                     {
                        ogate.setValidWires(false);
                        theCircuit.getAtIndex(i).setValidWires(false);
                       //System.out.println(ogate.getStringType()+" is coll w/ "+theCircuit.getAtIndex(i).getStringType());
                     }
                  }
               }
            }
            
            for(int i = 0; i < theCircuit.Nsize(); i++)
            {
               for(int j = 0; j < theCircuit.Nsize(); j++)
               {
                  ogate = theCircuit.getNGate(j);
                  if(theCircuit.getNGate(i) != ogate)
                  {
                     if(theCircuit.getNGate(i).gatesCollide(ogate.getxStartB(),ogate.getyStartB(),ogate.getxFinishB(),ogate.getyFinishB()))
                     {
                        ogate.setValidWires(false);
                        theCircuit.getNGate(i).setValidWires(false);
                       //System.out.println(ogate.getStringType()+" is coll w/ "+theCircuit.getNGate(i).getStringType());
                     }
                  }
               }
            }
            
            for(int i = 0; i < theCircuit.size(); i++)
            {
               for(int j = 0; j < theCircuit.Nsize(); j++)
               {
                  ogate = theCircuit.getNGate(j);
                  if(theCircuit.getAtIndex(i) != ogate)
                  {
                     if(theCircuit.getAtIndex(i).gatesCollide(ogate.getxStartB(),ogate.getyStartB(),ogate.getxFinishB(),ogate.getyFinishB()))
                     {
                        ogate.setValidWires(false);
                        theCircuit.getAtIndex(i).setValidWires(false);
                       //System.out.println(ogate.getStringType()+" is coll w/ "+theCircuit.getAtIndex(i).getStringType());
                     }
                  }
               }
            }
            repaint();
         }  
      }        
   }
   
   //Button listener class
   private class ButtonListener implements ActionListener
   {
      public void actionPerformed(ActionEvent ae) 
      {
         if (ae.getSource() == headerpanel.getOpen()) 
         {
            if(editmode)
            {
               //Create a JPanel with custom options and a text field
               Object[] options = {"Open as ASCII File","Open as Binary File", "Cancel"};
               JPanel openpanel = new JPanel();
               openpanel.add(new JLabel("Name of file:"));
               JTextField textField = new JTextField(20);
               openpanel.add(textField);
               
               //Put the JPanel into a JOptionPane
               int result = JOptionPane.showOptionDialog(null, openpanel, "Open Circuit",
                  JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, null);
               
               //Get the result and call read ascii or read binary function
               if (result == JOptionPane.YES_OPTION)
               {
                  headerpanel.setMessage("Loading file...");
                  bodypanel.ReadASCII(textField.getText());
               }
               else if (result == JOptionPane.NO_OPTION)
               {
                  headerpanel.setMessage("Loading file...");
                  bodypanel.ReadBinary(textField.getText());
               }
            }
            else
            {
               headerpanel.setMessage("Cannot open files in play mode");
            }
         } 
         else if (ae.getSource() == headerpanel.getSave()) 
         {
            if(editmode)
            {
               //Make sure we have the right circuit
               theCircuit = bodypanel.getCircuit();
               
               //Create a JPanel with custom options and a text field
               Object[] options = {"Save as an ASCII File","Save as a Binary File", "Cancel"};
               JPanel openpanel = new JPanel();
               openpanel.add(new JLabel("Name of file:"));
               JTextField textField = new JTextField(20);
               openpanel.add(textField);
               
               //Put the JPanel into a JOptionPane
               int result = JOptionPane.showOptionDialog(null, openpanel, "Save Circuit",
                  JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, null);
               
               //Get the result and call read ascii or read binary function
               if (result == JOptionPane.YES_OPTION)
               {
                  theCircuit.saveToASCII(textField.getText());
               }
               else if (result == JOptionPane.NO_OPTION)
               {
                  theCircuit.saveToBinary(textField.getText());
               }
            }
            else
            {
               headerpanel.setMessage("Cannot save circuit in play mode");
            }
         }
         else if (ae.getSource() == headerpanel.getING()) 
         {
            if(editmode)
            {
               //Make sure we have the right circuit
               theCircuit = bodypanel.getCircuit();
               
               //JOptionPane to get the type of gate
               String[] choices = { "Input", "Output", "And", "Nand", "Or", "Nor","Not","XOr","True","False"};
               String input = (String) JOptionPane.showInputDialog(null, "Choose Type",
               "Insert New Gate", JOptionPane.QUESTION_MESSAGE, null, choices,
               choices[0]);
               
               //Calculate gate num and create the correct type of gate
               Gate thegate;
               int gatenum = theCircuit.size()+1;
               gatenum += theCircuit.Nsize();
               //System.out.println("Active Size:"+theCircuit.size()+" Inactive Size:"+theCircuit.Nsize());
               //System.out.println("Gatenum: "+gatenum);
               if(input != null)
               {
                  switch(input)
                  {
                     case "Input":
                     {
                        thegate = new INPUT(gatenum);
                        break;
                     }
                     case "Output":
                     {
                        thegate = new OUTPUT(gatenum);
                        break;
                     }
                     case "And":
                     {
                        thegate = new AND(gatenum,false);
                        break;
                     }
                     case "Nand":
                     {
                        thegate = new AND(gatenum,true);
                        break;
                     }
                     case "Or":
                     {
                        thegate = new OR(gatenum,false);
                        break;
                     }
                     case "Nor":
                     {
                        thegate = new OR(gatenum,true);
                        break;
                     }
                     case "Not":
                     {
                        thegate = new NOT(gatenum);
                        break;
                     }
                     case "XOr":
                     {
                        thegate = new XOR(gatenum);
                        break;
                     }
                     case "True":
                     {
                        thegate = new TRUE(gatenum);
                        break;
                     }
                     default: //case "False"
                     {
                        thegate = new FALSE(gatenum);
                        break;
                     }
                  }
                  //Now add gate to inactive gates list
                  theCircuit.addBebe(thegate);
                  repaint();
               }
            }
            else
            {
               headerpanel.setMessage("Cannot add gates in play mode");
            }
         }
      }
   }
}
