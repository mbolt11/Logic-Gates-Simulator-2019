//Header JPanel File for LGS Project

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class HeaderGUI extends JPanel
{
   private JButton open;
   private JButton save;
   private JButton newgate;
   
   public HeaderGUI()
   {
      super();
      
      setLayout(new FlowLayout(FlowLayout.LEFT));
      
      //Instantiate and add Buttons
      open = new JButton("Open");
      save = new JButton("Save");
      newgate = new JButton("Insert New Gate");
      add(open);
      add(save);
      add(newgate);
            
      setPreferredSize(new Dimension(1000,50));
      setBackground(Color.BLUE);
   }
}