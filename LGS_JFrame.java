//JFrame File for LGS Project

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class LGS_JFrame extends JFrame
{   
   //This variable is true if in editmode, false if in play mode
   private boolean editmode;
   
   //Access to our two panels   
   private BodyGUI bodypanel;
   private HeaderGUI headerpanel;
   
   //Access to the currently displaying circuit
   private Circuit theCircuit;
   
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
      
      //Offset of where the mouse clicked to where the x & y starts are
      private int gateDX, gateDY;
      private int originDX, originDY;
      
      public void mousePressed(MouseEvent e)
      {
         //Make sure we have the right circuit
         theCircuit = bodypanel.getCircuit();
         
         //Search through all the gates to see if the mouse clicked on it
         for(int i = 0; i < theCircuit.size(); i++)
         {
            //If the mouse click was within the area of the gate, save the index
            if(theCircuit.getAtIndex(i).getAreaRect().contains(e.getPoint()))
            {
               gateClickedIndex = i;
               System.out.println("Clicked on a gate");
               gateDX = e.getX() - theCircuit.getAtIndex(i).getxStart();
               gateDY = e.getY() - theCircuit.getAtIndex(i).getyStart();
               return;
            }
         }
         
         //If they clicked on the screen, save dx and dy for dragging the screen
         gateClickedIndex = -1;
         System.out.println("Clicked on screen");
         originDX = e.getX();
         originDY = e.getY();
      }
      
      public void mouseDragged(MouseEvent e)
      {
         //If the gate clicked index is -1, scroll the screen
         if(gateClickedIndex == -1)
         {
            //Change the origin offsets as mouse is dragging
            bodypanel.setOriginOffsets((e.getX()-originDX),(e.getY()-originDY));
            repaint();
         }
         
         //If the gate clicked index is not -1, move the gate
         if(gateClickedIndex != -1)
         {  
            //Change the xStart and yStart positions of the gate according to the mouse drag
            theCircuit.getAtIndex(gateClickedIndex).setxStart(e.getX()-gateDX);
            theCircuit.getAtIndex(gateClickedIndex).setyStart(e.getY()-gateDY);
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
         else if (ae.getSource() == headerpanel.getSave()) 
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
         else if (ae.getSource() == headerpanel.getING()) 
         {
            //Code to add new gate
         }
      }
   }
}
