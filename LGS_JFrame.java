//JFrame File for LGS Project

import javax.swing.*;
import java.awt.*;

public class LGS_JFrame extends JFrame
{
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
      BodyGUI bodypanel = new BodyGUI();
      contents.add(bodypanel);
      
      //Set the size, visibility and layout
      setLayout(fl);
      setSize(1000,1000);
      setVisible(true);
   }
}
