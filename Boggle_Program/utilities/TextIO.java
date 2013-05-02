package utilities;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/***************************************************************************
* TextIO - This class keeps track of all user I/O for the Boggle game.
*
* @author Alexander Anderson
* @version 2.0 (October 11, 2012), 1.0 (September 20, 2012)
*
* Acknowledgements: I acknowledge that I have neither given nor
*       received assistance for this assignment except as
*       noted below:
*
*   None
*
* Modifications: 9/30/2012 - added public methods for easier user interface
*                            and added methods to read and writes words
*                            to/from a file.
***************************************************************************/

public class TextIO
{

   /*  public static attributes
    *****************************/


   /*  private static attributes
    ******************************/


   /*  attributes
    ***************/

   private BufferedReader in;
   private BufferedWriter out;

   /*  constructors
    *****************/

   /************************************************************************
   * TextIO - This method constructs a TextIO object.
   *
   * @param none
   ************************************************************************/
   public TextIO ()
   {
      in = new BufferedReader (new InputStreamReader (System.in));
      out = new BufferedWriter (new OutputStreamWriter (System.out));
   }

   /*  public methods
    *******************/

   /************************************************************************
   * fileExists - This method returns true if a file with a given name
   *       exists.
   *
   * @return boolean
   * @param String
   ************************************************************************/
   public boolean fileExists (String fileName)
   {
      File file = new File (fileName);
      return file.exists();
   }

   /************************************************************************
   * getUserInput - This method gets input from the user.
   *
   * @return String
   ************************************************************************/
   public String getUserInput ()
   {
      String str = new String();
        
      try
      {
         str = in.readLine();
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
      return str;
   }

   /************************************************************************
   * inputIsAlphabetic - This method determines if a string input contains
   *       only alphabetic characters.
   *
   * @return boolean
   * @param String
   ************************************************************************/
   public boolean inputIsAlphabetic (String input)
   {
      boolean found = false;
      Pattern isAlphabetic = Pattern.compile ("^[a-zA-Z]+$");
      Matcher matcher = isAlphabetic.matcher(input);  
                
      if (matcher.find())
      {
         found = true;
      }
      return found;
   } // inputIsAlphabetic
        
   /************************************************************************
   * inputIsInteger - This method determines if a string input is an integer.
   *
   * @return boolean
   * @param String
   ************************************************************************/
   public boolean inputIsInteger (String input)
   {
      boolean found = false;
      Pattern isInteger = Pattern.compile ("^[-+]?\\d+$");
      Matcher matcher = isInteger.matcher(input); 
                
      if (matcher.find())
      {
         found = true;
      }
      return found;
   } // inputIsInteger

   /************************************************************************
   * log - This method writes a messages to the console
   *
   * @param String
   ************************************************************************/
   public void log (String str)
   {
      try
      {
         out.write (str);
         out.newLine ();
         out.flush();
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
   } // log

   /************************************************************************
   * prompt - This method prompts the user
   *
   * @param String
   ************************************************************************/
   public void prompt (String str)
   {
      try
      {
         out.write (str);
         out.flush();
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
   } // prompt

   /************************************************************************
   * readWordList - This method reads a text file that contains a word list
   *       and returns the words in an ArrayList.
   *
   * @return ArrayList<String>
   * @param String
   ************************************************************************/
   public ArrayList<String> readWordList (String fileName)
   {
      ArrayList<String> textFromFile = new ArrayList<String>();
        
      try
      {
         BufferedReader reader = new BufferedReader (new FileReader (fileName));      
         String text = null;
            
         while ((text = reader.readLine()) != null)
         {
            if (inputIsAlphabetic (text))
            {
               textFromFile.add (text);
            }
         }
         reader.close();
      }
      catch (FileNotFoundException f)
      {
         //simply catches and does nothing to return an empty word list
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
      
      return textFromFile;
    } // readWordList
    

   /************************************************************************
   * writeWordList - This method reads a text file and returns a StringBuffer
   *
   * @param String, ArrayList<String>
   ************************************************************************/
   public void writeWordList (String fileName, ArrayList<String> wordList)
   {
      BufferedWriter writer = null;
      try
      {
         writer = new BufferedWriter (new FileWriter (fileName));
            
         for (String string : wordList)
         {      
            writer.write (string);
            writer.newLine();
         }
         writer.close();
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
    } // writeWordList

} // TextIO


