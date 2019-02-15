//Header JPanel File for LGS Project

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class HeaderGUI extends JPanel
{
   public static HeaderGUI headerpanel;
   private JButton open;
   private JButton save;
   private JButton newgate;
   private String message;
   
   public HeaderGUI()
   {
      super();
      headerpanel = this;
      
      setLayout(new FlowLayout(FlowLayout.LEFT));
      
      //Instantiate and add Buttons
      open = new JButton("Open");
      save = new JButton("Save");
      newgate = new JButton("Insert New Gate");
      add(open);
      add(save);
      add(newgate);
      
      //Message to display at top
      message = "Edit Mode";
            
      setPreferredSize(new Dimension(1000,50));
      setBackground(Color.BLUE);
   }
   
   //Accessors for buttons
   public JButton getOpen()
   {
      return open;
   }
   public JButton getSave()
   {
      return save;
   }
   public JButton getING()
   {
      return newgate;
   }
   
   //Method to change the text message
   public void setMessage(String in)
   {
      message = in;
      repaint();
   }
   
   public void paintComponent(Graphics g)
   {
      super.paintComponent(g);
      
      g.setColor(Color.WHITE);
      g.drawString(message,500,30);
   }
}