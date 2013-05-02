package boggle;

import view.BoggleGUI;
import model.Dictionary;

/***************************************************************************
 * Boggle - This class contains the main method to run a Boggle game.
 * 
 * @author Alexander Anderson
 * @version: 3.0 (December 5 2012) 
 *           2.0 (October 11, 2012)
 *           1.0 (September 20, 2012)
 * 
 * Acknowledgements: I acknowledge that I have neither given nor
 *          received assistance for this assignment except as noted below:
 *          
 *          None
 * 
 * Modifications:  12/05/2012 - updated to call graphical user interface
 *                 10/10/2012 - Takes in an argument from the command line
 ***************************************************************************/

public class Boggle
{
   public static void main (String[] args)
   {
      String dictionaryFileName = "dictionary.txt";

      if (args.length > 0)
      {
         dictionaryFileName = args[0];
      }
      Dictionary dictionary = new Dictionary (dictionaryFileName);
      BoggleGUI gui = new BoggleGUI (dictionary);
   }
}
