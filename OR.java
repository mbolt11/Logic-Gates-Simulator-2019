/* OR and NOR Gate Class for LGS Project */
import javax.swing.*;
import java.awt.*;

public class OR extends Gate
{
   private boolean output;
   private boolean negate;
   
   //Constructor
   public OR(int num_in, boolean negate_in)
   {
      super(num_in,gatetype.OR);
      negate = negate_in;
   }
   
   //Calculates result of gate // ERROR!!!!!!!!!!!!!!!!!!!!!!
   //I haven't figured out where to call this from yet
   public void calculateOutput()
   {
      boolean result = false;
      //Calculate result of OR for all input lines
      if(inputs.size() >= 2)
      {boolean wire1 = inputs.get(0).getOutput();
      boolean wire2 = inputs.get(1).getOutput();
      result = wire1 || wire2;
      for(int i=2; i<inputs.size(); i++)
      {
         result = result || inputs.get(i).getOutput();
      }}
      
      //Negate if it is a NOR
      if(negate)
      {
         result = !result;
      }
      
      //Set the output variable according to result
      setOutput(result);
   }
   
   public void draw(Graphics g, int row, int column, int maxColumn, int maxRow)
   {
      //System.out.println("OR drawn");
      int xStart = ((int) (((double)column/maxColumn) * 1000)) - 150;
      int yStart = ((int) (((double)row/maxRow) * 950)) + 65;
      //draw the first arc
      g.drawArc(xStart - 105, yStart - 25, 100, 150, 40, -80);
      //draw second arc
      g.drawArc(xStart - 100, yStart, 200, 150, 100, -80);
      //draw third arc
      g.drawArc(xStart - 100, yStart - 50, 200, 150, -100, 80);
   }
}