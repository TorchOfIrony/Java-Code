package model;

import java.awt.Toolkit;
import java.util.ArrayList;

import utilities.StringSet;

import model.Board;
import model.Dictionary;

/***************************************************************************
 * Player - The player objects represent the user and the computer 
 *       competitors. The Player is an abstract class which knows the round 
 *       score and total score for the competitor, and the words found by 
 *       the competitor in each round. At the end of the round, the
 *       player is allowed to reject entered words - those are discarded from 
 *       the list. Words found by the player that are not rejected are added 
 *       to the dictionary.
 * 
 * @author Alexander Anderson
 * 
 * @version 4.0 (December 04, 2012)
 *          3.0 (October 28, 2012)
 *          2.0 (October 11, 2012)
 *          1.0 (September 20, 2012)
 * 
 * Acknowledgements: I acknowledge that I have neither given nor
 *          received assistance for this assignment except as noted below:
 * 
 *          None
 * 
 * Modifications: 12/04/2012: Updated to support graphical user interface.
 *                10/28/2012: Made this class an abstract class to allow
 *                            for inheritance by both human and computer
 *                            competitors.
 *                9/30/2012: Changed word list to a StringSet and added
 *                           an ArrayList to keep word lists for each round. 
 *                           Added a dictionary attribute initialized in the 
 *                           constructor and added methods to add and remove 
 *                           words to/from the dictionary.
 ***************************************************************************/

public abstract class Player
{
   /*
    * attributes*************
    */

   protected ArrayList<StringSet> allWords = new ArrayList<StringSet> ();
   protected Board board;
   protected Dictionary dictionary;
   protected int score = 0;
   protected StringSet rejectedWords = new StringSet ();
   protected StringSet roundWords = new StringSet ();

   /*
    * constructors***************
    */

   /************************************************************************
    * Player - This method constructs a Boggle Player object.
    * 
    * @param Dictionary
    ************************************************************************/
   public Player (Dictionary dictionary, Board board)
   {
      this.dictionary = dictionary;
      this.board = board;
   } // Player

   /*
    * public methods*****************
    */

   /************************************************************************
    * playRound - This method gets the words the Player enters for a round.
    * 
    * @return StringSet
    * @param Board
    ************************************************************************/
   public abstract void addWords (String words); // addWords

   /************************************************************************
    * getScore - This method returns the player's score.
    * 
    * @param int
    ************************************************************************/

   public int getScore ()
   {
      return score;
   }
   
   /************************************************************************
    * getRejectedWords - This method provides the words rjected for a 
    *       given round.
    * 
    * @return none
    * @param none
    ************************************************************************/
   public StringSet getRejectedWords ()
   {
      return rejectedWords;
   } // getRejectedWords

   /************************************************************************
    * getWords - This method provides the words entered for a given round.
    * 
    * @return none
    * @param none
    ************************************************************************/
   public StringSet getWords ()
   {
      return roundWords;
   } // getWords


   /************************************************************************
    * newRound - This method resets for a new round.
    * 
    * @return none
    * @param none
    ************************************************************************/
   public void newRound (Board board)
   {
      // Archive the previous rounds' words
      allWords.add (roundWords);
      
      this.board = board;
      rejectedWords = new StringSet ();
      roundWords = new StringSet ();
   } // newRound


   /************************************************************************
    * rejectWords - This method removes words from the player's word list.
    * 
    * @param String
    ************************************************************************/

   public void rejectWords (String wordsToReject)
   {
      String[] wordsInLine = wordsToReject.split ("\\W");

      for (String word : wordsInLine)
      {
         if (word.length () > 0)
         {
            String rejectedWord = word.toLowerCase ();
            roundWords.deleteWord (rejectedWord);
            rejectedWords.addWord (rejectedWord);
            dictionary.removeWordFromDictionary (rejectedWord);
         }
      }
   } // rejectWords

   /************************************************************************
    * setScore - This method sets the player's score.
    * 
    * @param int
    ************************************************************************/

   public void setScore (int score)
   {
      this.score = score;
   }
   
} // Player