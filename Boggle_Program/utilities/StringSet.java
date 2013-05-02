package utilities;

import java.util.ArrayList;

/***************************************************************************
 * StringSet - This class implements sets of Strings using an ArrayList. 
 *       This class must has public methods for adding words, deleting words, 
 *       and selecting words from the list. The set of strings will have no 
 *       duplicates. This class also implements the following set methods: 
 *             StringSet difference (StringSet other) - returns a StringSet 
 *                   containing the words that are unique to the current set 
 *                   (minus the words common to both sets). 
 *             StringSet intersection (StringSet other) - returns a StringSet 
 *                   containing the word common to both the current set and 
 *                   the other set. 
 *             StringSet union (StringSet other) - returns a StringSet 
 *                   containing all of the unique words from both the current
 *                   set at the other sets.
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
 * Modifications: 10/28/2012: Updated set operations
 ***************************************************************************/
public class StringSet
{

   /*
    * attributes*************
    */

   private ArrayList<String> wordList = new ArrayList<String> ();

   /*
    * constructors***************
    */

   /************************************************************************
    * StringSet - This method constructs a Boggle StringSet object.
    * 
    * @param none
    ************************************************************************/
   public StringSet ()
   {
   } // StringSet

   /*
    * public methods*****************
    */

   /************************************************************************
    * addWord - This method adds a word to the set of words.
    * 
    * @return none
    * @param String
    ************************************************************************/
   public void addWord (String word)
   {
      if (!isDuplicate (word))
      {
         wordList.add (word);
      }
   } // addWord

   /************************************************************************
    * deleteWord - This method deletes a word from the set of words.
    * 
    * @return none
    * @param String
    ************************************************************************/
   public void deleteWord (String word)
   {
      wordList.remove (word.toLowerCase ());
   } // deleteWord

   /************************************************************************
    * difference - This method returns a StringSet containing the words that 
    *       are unique to the current set (minus the words common to both 
    *       sets).
    * 
    * @return StringSet
    * @param StringSet
    ************************************************************************/
   public StringSet difference (StringSet other)
   {
      StringSet differentWords = new StringSet ();

      for (String wordInList : wordList)
      {
         if (!other.contains (wordInList))
         {
            differentWords.addWord (wordInList);
         }
      }
      return differentWords;
   } // difference

   /************************************************************************
    * getWordList - This method returns the word list.
    * 
    * @return ArrayList<String>
    * @param none
    ************************************************************************/
   public ArrayList<String> getWordList ()
   {
      return wordList;
   } // getWordList

   /************************************************************************
    * getNumberOfWordsInList - This method returns the number of words in 
    *       the list.
    * 
    * @return int
    * @param none
    ************************************************************************/
   public int getNumberOfWordsInList ()
   {
      int numberOfWords = wordList.size ();
      return numberOfWords;
   } // getNumberOfWordsInList

   /************************************************************************
    * indexOf - This method returns the index of the given word in the word
    *       list. If the word is not in the word list, this method 
    *       returns -1.
    * 
    * @return int
    * @param String
    ************************************************************************/
   public int indexOf (String word)
   {
      int index = -1;

      for (int i = 0; i < wordList.size (); i++)
      {
         if (word.equalsIgnoreCase (wordList.get (i)))
         {
            index = i;
            break;
         }
      }
      return index;
   } // indexOf

   /************************************************************************
    * intersection - returns a StringSet containing the word common to both 
    *       the current set and the other set.
    * 
    * @return StringSet
    * @param StringSet
    ************************************************************************/
   public StringSet intersection (StringSet other)
   {
      StringSet commonWords = new StringSet ();

      for (String wordInList : wordList)
      {
         if (other.contains (wordInList))
         {
            commonWords.addWord (wordInList);
         }
      }
      return commonWords;
   } // intersection

   /************************************************************************
    * union - returns a StringSet containing all of the unique words from 
    *       both the current set at the other sets.
    * 
    * @return StringSet
    * @param StringSet
    ************************************************************************/
   public StringSet union (StringSet other)
   {
      StringSet allWords = new StringSet ();

      for (String wordInList : wordList)
      {
         allWords.addWord (wordInList);
      }
      for (int i = 0; i < other.size (); i++)
      {
         allWords.addWord (other.get (i));
      }
      return allWords;
   } // union

   /************************************************************************
    * toString - returns a string representation of the set.
    * 
    * @return String
    * @param none
    ************************************************************************/
   public String toString ()
   {
      return wordList.toString ();
   } // toString

   /************************************************************************
    * wordAt - This method returns the word at a given index.
    * 
    * @return String
    * @param int
    ************************************************************************/
   public String wordAt (int index)
   {
      String word = "";

      if (index >= 0 && index < wordList.size ())
      {
         word = wordList.get (index);
      }
      return word;
   } // wordAt

   /*
    * private methods*****************
    */

   /************************************************************************
    * contains - This method determines if the given word is already in the
    *       word list.
    * 
    * @return boolean
    * @param String
    ************************************************************************/
   private boolean contains (String word)
   {
      return wordList.contains (word);
   } // contains

   /************************************************************************
    * get - This method the word at the given index in the set.
    * 
    * @return String
    * @param int
    ************************************************************************/
   private String get (int index)
   {
      String word = null;

      if (isInBounds (index))
      {
         word = wordList.get (index);
      }
      return word;
   } // get

   /************************************************************************
    * isInBounds - This method determines if the given index is in bounds
    *       for the size of the set.
    * 
    * @return boolean
    * @param String
    ************************************************************************/
   private boolean isInBounds (int index)
   {
      return (index > -1 && index < wordList.size ());
   } // isInBounds

   /************************************************************************
    * isDuplicate - This method determines if the given words is already in 
    *       the word list.
    * 
    * @return boolean
    * @param String
    ************************************************************************/
   private boolean isDuplicate (String word)
   {
      boolean duplicate = false;

      for (String wordInList : wordList)
      {
         if (word.equalsIgnoreCase (wordInList))
         {
            duplicate = true;
            break;
         }
      }
      return duplicate;
   } // isDuplicate

   /************************************************************************
    * size - This method returns the size of the set. 
    * 
    * @return boolean
    * @param none
    ************************************************************************/
   private int size ()
   {
      return wordList.size ();
   } // size

} // StringSet