package model;

import java.awt.Toolkit;
import java.util.ArrayList;

import model.Board;
import model.Dictionary;

import utilities.EggTimer;
import utilities.StringSet;
import utilities.TextIO;

/***************************************************************************
 * HumanPlayer - The HumanPlayer class extends the Player class to
 *       represent the user competitor. 
 *        
 * @author Alexander Anderson
 * @version 2.0 (December 4, 2012), 1.0 (October 28, 2012)
 * 
 * Acknowledgements: I acknowledge that I have neither given nor
 *          received assistance for this assignment except as noted below:
 * 
 *          None
 *          
 * Modifications: 12/04/2012: Updated to support graphical user interface.
 * 
 ***************************************************************************/

public class HumanPlayer extends Player
{
   /*
    * attributes*************
    */
   private StringSet invalidWords = new StringSet ();

   /*
    * constructors***************
    */

   /************************************************************************
    * HumanPlayer - This method constructs a Boggle HumanPlayer object.
    * 
    * @param Dictionary
    ************************************************************************/
   public HumanPlayer (Dictionary dictionary, Board board)
   {
      super (dictionary, board);
   } // HumanPlayer

   /*
    * public methods*****************
    */

   /************************************************************************
    * addWords - This method adds to the player's word list.
    * 
    * @param String
    ************************************************************************/

   public void addWords (String words)
   {
      String[] wordsInLine = words.split ("\\W");

      for (String word : wordsInLine)
      {
         if (word.length () > 0)
         {
            String enteredWord = word.toLowerCase ();
            
            if (board.isWordOnBoard (enteredWord))
            {
               roundWords.addWord (enteredWord);
               dictionary.addWordToDictionary (enteredWord);
            }
            else
            {
               invalidWords.addWord (enteredWord);
            }
         }
      }
   } // addWords
   

   /************************************************************************
    * getInvalidWords - This method returns the player's entered words that
    *       are not on the board.
    * 
    * @param StringSet
    ************************************************************************/

   public StringSet getInvalidWords ()
   {
      return invalidWords;
   }
   

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
      invalidWords = new StringSet ();
      rejectedWords = new StringSet ();
      roundWords = new StringSet ();
   } // newRound
} // HumanPlayer