//JFrame File for LGS Project

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class LGS_JFrame extends JFrame
{   
   //This variable is true if in editmode, false if in play mode
   private boolean editmode;
      
   private BodyGUI bodypanel;
   private Circuit theCircuit;
   
   public LGS_JFrame()
   {
      //Container for the GUI elements
      Container contents = getContentPane();
      
      //Layout
      FlowLayout fl = new FlowLayout();
      fl.setVgap(0);
      
      //Add header JPanel
      HeaderGUI headerpanel = new HeaderGUI();
      contents.add(headerpanel);
      
      //Add body JPanel
      bodypanel = new BodyGUI();
      contents.add(bodypanel);
      
      //Get access to the circuit which was created in the body panel
      theCircuit = bodypanel.getCircuit();
      
      //Add mouse adapter to body panel
      MouseEventTracking met = new MouseEventTracking();
      bodypanel.addMouseListener(met);
      
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
      private int dx, dy;
      
      public void mousePressed(MouseEvent e)
      {
         //Search through all the gates to see if the mouse clicked on it
         for(int i = 0; i < theCircuit.size(); i++)
         {
            //If the mouse click was within the area of the gate, save the index
            if(theCircuit.getAtIndex(i).getAreaRect().contains(e.getPoint()))
            {
               gateClickedIndex = i;
               System.out.println("Clicked on a gate");
               dx = e.getX() - theCircuit.getAtIndex(i).getxStart();
               dy = e.getY() - theCircuit.getAtIndex(i).getyStart();
               return;
            }
         }
         gateClickedIndex = -1;
         System.out.println("Clicked on screen");
      }
      
      public void mouseDragged(MouseEvent e)
      {
         //If the gate clicked index is -1, scroll the screen
         
         //If the gate clicked index is not -1, move the gate
         if(gateClickedIndex != -1)
         {
            //Change the xStart and yStart positions of the gate according to the mouse drag
            theCircuit.getAtIndex(gateClickedIndex).setxStart(e.getX() - dx);
            theCircuit.getAtIndex(gateClickedIndex).setyStart(e.getY() - dy);
            repaint();
         }
      }        
   }
}
