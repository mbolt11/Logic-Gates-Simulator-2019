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
         if(arrowbutton.contains(e.getPoint()))
         {
            //Switch between edit and play mode
            System.out.println("Clicked on arrow");
            editmode = !editmode;
            bodypanel.switchMode();
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
               System.out.println("Found in gate");
            }
            else if(theCircuit.getAtIndex(i).getOutputAreaRect().contains(e.getPoint()))
            {
               //This is the gate where the new output will come from
               outGate = theCircuit.getAtIndex(i);
               System.out.println("Found out gate");
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
               System.out.println("Found in gate");
            }
            else if(theCircuit.getNGate(i).getOutputAreaRect().contains(e.getPoint()))
            {
               //This is the gate where the new output will come from
               outGate = theCircuit.getNGate(i);
               System.out.println("Found out gate");
            }
         }
         
         //Draw a new wire if they clicked on both a start and an end
         if(editmode && inGate != null && outGate != null)
         {
            System.out.println("Adding a wire");
            inGate.addInput(outGate);
            
            //If it is a new gate now connected to the circuit, make adjustments
            if(inGate.isInCircuit() || !outGate.isInCircuit())
            {
               theCircuit.addGate(outGate);
               theCircuit.removeBebe(outGate.getGateNum());
               outGate.setIsInCircuit(true);
            }
            else if(outGate.isInCircuit() || !inGate.isInCircuit())
            {
               theCircuit.addGate(inGate);
               theCircuit.removeBebe(inGate.getGateNum());
               inGate.setIsInCircuit(true);
            }
            
            inGate = null;
            outGate = null;
            repaint();
            return;
         }
         
         //Switch the input T/F if in play mode
         if(!editmode)
         {
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
         //If the gate clicked index is -1, scroll the screen
         if(gateClickedIndex < -1)
         {
            //Change the offsets of all the gate start positions
            for(int i = 0; i < theCircuit.size(); i++)
            {
               theCircuit.getAtIndex(i).translate((e.getX()-originDX),(e.getY()-originDY));
               
               //This changes the offset of gates in inactiveGates
               if(i < theCircuit.Nsize())
               {
                  theCircuit.getNGate(i).translate((e.getX()-originDX),(e.getY()-originDY));
               }
            }
            originDX = e.getX();
            originDY = e.getY();
            repaint();
         }
         
         //If the gate clicked index is not -1, move the gate
         if(gateClickedIndex > -1 && editmode)
         {  
            //Change the xStart and yStart positions of the gate according to the mouse drag
            theCircuit.getAtIndex(gateClickedIndex).setxStart(e.getX()-gateDX);
            theCircuit.getAtIndex(gateClickedIndex).setyStart(e.getY()-gateDY);
            repaint();
         }
         
         if(nGateClicked > -1 && editmode)
         {
            //Change the xStart and yStart positions of the gate according to the mouse drag
            theCircuit.getNGate(nGateClicked).setxStart(e.getX()-gateDX);
            theCircuit.getNGate(nGateClicked).setyStart(e.getY()-gateDY);
            repaint();
         }  
      }        
   }
   
   //Button listener class
   private class ButtonListener implements ActionListener
   {
      public void actionPerformed(ActionEvent ae) 
      {
         if (ae.getSource() == headerpanel.getOpen() && editmode) 
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
               bodypanel.ReadASCII(textField.getText());
            }
            else if (result == JOptionPane.NO_OPTION)
            {
               bodypanel.ReadBinary(textField.getText());
            }
         } 
         else if (ae.getSource() == headerpanel.getSave() && editmode) 
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
         else if (ae.getSource() == headerpanel.getING() && editmode) 
         {
            //Make sure we have the right circuit
            theCircuit = bodypanel.getCircuit();
            
            //JOptionPane to get the type of gate
            String[] choices = { "Input", "Output", "And", "Nand", "Or", "Nor","Not","XOr"};
            String input = (String) JOptionPane.showInputDialog(null, "Choose Type",
            "Insert New Gate", JOptionPane.QUESTION_MESSAGE, null, choices,
            choices[0]);
            
            //Calculate gate num and create the correct type of gate
            Gate thegate;
            int gatenum = theCircuit.size()+1;
            gatenum += theCircuit.Nsize();
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
                  default: //case: "XOr"
                  {
                     thegate = new XOR(gatenum);
                     break;
                  }
               }
               //Now add gate to inactive gates list
               theCircuit.addBebe(thegate);
               repaint();
            }
         }
      }
   }
}
