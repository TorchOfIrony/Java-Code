package model;

import java.util.ArrayList;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

import utilities.TextIO;

/***************************************************************************
 * Dictionary - This class reads words (not necessarily sorted) from a text
 *       file (which may not exist or be readable), stores them alphabetically
 *       in an TreeSet, accepts new words and stores them alphabetically, and 
 *       writes the words back to the file when the program is done. The 
 *       dictionary does not store duplicates. The Dictionary will only store 
 *       about 300 words in the file, so if it grows large during execution 
 *       of the program, it will randomly select 300 words to write back to 
 *       the file. The dictionary file stores one word one per line.
 * 
 * @author Alexander Anderson
 * @version 2.0 (October 28, 2012)
 *          1.0 (October 11, 2012)
 * 
 * Acknowledgements: I acknowledge that I have neither given nor
 *          received assistance for this assignment except as noted below:
 * 
 *          None
 * 
 * Modifications: 10/28/2012: Added a method to return all the words in the
 *                            dictionary that start with a given prefix.
 ***************************************************************************/

public class Dictionary
{

   /*
    * attributes*************
    */

   private int difficulty = 5;
   private Random randomNumber = new Random (System.nanoTime ());
   private SortedSet<String> wordList = new TreeSet<String> ();
   private String dictionaryFileName = "dictionary.txt";

   /*
    * constructors***************
    */

   /************************************************************************
    * Dictionary - This method constructs a Boggle Dictionary object with a
    *       default difficulty of 5 and a default dictionary file called
    *       dictionary.txt.
    * 
    * @param none
    ************************************************************************/
   public Dictionary ()
   {
   } // Dictionary

   /************************************************************************
    * Dictionary - This method constructs a Boggle Dictionary object with a
    *       given difficulty rating and dictionary file name.
    * 
    * @param String
    ************************************************************************/
   public Dictionary (String fileName)
   {
      TextIO io = new TextIO ();

      if (io.fileExists (fileName))
      {
         dictionaryFileName = fileName;
      }

      readWordsFromFile ();
   } // Dictionary

   /*
    * public methods*****************
    */

   /************************************************************************
    * addWordToDictionary - This method adds a word to the dictionary based
    *       on the difficulty level. The higher the difficulty, the more the 
    *       words will be "learned" and added to the dictionary.
    * 
    * @return none
    * @param String
    ************************************************************************/
   public void addWordToDictionary (String word)
   {
      // Randomly generate a number between 1 and 10
      int randomInt = randomNumber.nextInt (10) + 1;

      if (randomInt <= difficulty)
      {
         wordList.add (word);
      }
   } // addWordToDictionary

   /************************************************************************
    * removeWordFromDictionary - This method removes a word from the dictionary.
    * 
    * @return none
    * @param String
    ************************************************************************/
   public void removeWordFromDictionary (String word)
   {
      wordList.remove (word);
   } // removeWordFromDictionary

   /************************************************************************
    * setDifficulty - This method sets the difficulty.
    * 
    * @return none
    * @param int
    ************************************************************************/
   public void setDifficulty (int difficulty)
   {
      if (difficulty >= 1 && difficulty <= 10)
      {
         this.difficulty = difficulty;
      }
   } // setDifficulty

   /************************************************************************
    * wordsStartingWith - This method returns words in the dictionary that 
    *       start with the given prefix.
    * 
    * @return SortedSet<String>
    * @param String
    ************************************************************************/
   public SortedSet<String> wordsStartingWith (String prefix)
   {
      SortedSet<String> tail = new TreeSet<String> ();
      tail = wordList.tailSet (prefix);

      if (tail.size () > 0 && prefix.length () > 0)
      {
         int lastCharacter = prefix.length () - 1;
         String next = "";

         if (prefix.length () > 1)
         {
            next = prefix.substring (0, lastCharacter);
         }

         int charValue = prefix.charAt (lastCharacter);
         next += String.valueOf ((char) (charValue + 1));
         SortedSet<String> head = tail.headSet (next);
         tail = head;
      }
      return tail;
   } // wordsStartingWith

   /************************************************************************
    * writeWordsToFile - This method writes the word list to a file. It will
    *       choose about 300 words at random from the dictionary and write 
    *       them to the file for use the next time the program is run.
    * 
    * @return none
    * @param none
    ************************************************************************/
   public void writeWordsToFile ()
   {
      ArrayList<String> allWords = new ArrayList<String> ();
      ArrayList<String> selectedWords = new ArrayList<String> ();
      TextIO io = new TextIO ();

      for (String word : wordList)
      {
         allWords.add (word);
      }

      // Randomly choose about 300 words at random from the word list and
      // write them to the dictionary file for use the next time
      int i = 0;

      while (allWords.size () > 0 && i < 300)
      {
         int randomInt = randomNumber.nextInt (allWords.size ());
         selectedWords.add (allWords.get (randomInt));
         allWords.remove (randomInt);
         i++;
      }

      io.writeWordList (dictionaryFileName, selectedWords);
   } // writeWordsToFile

   /*
    * private methods*****************
    */

   /************************************************************************
    * readWordsFromFile - This method adds words to the dictionary from a file.
    * 
    * @return none
    * @param none
    ************************************************************************/
   private void readWordsFromFile ()
   {
      TextIO io = new TextIO ();
      ArrayList<String> words = io.readWordList (dictionaryFileName);

      for (String word : words)
      {
         wordList.add (word);
      }
   } // readWordsFromFile

   /************************************************************************
    * validWord - This method returns true if a given word is in the dictionary.
    * 
    * @return boolean
    * @param String
    ************************************************************************/
   public boolean validWord (String word)
   {
      boolean valid = wordList.contains (word);
      return valid;
   } // validWord

} // Dictionary