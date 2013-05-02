package controller;

import java.util.ArrayList;

import utilities.StringSet;
import model.Board;
import model.Dictionary;
import model.HumanPlayer;
import model.ComputerPlayer;
import model.Player;

/***************************************************************************
 * Game - This class keeps track of the Boggle round for the players.
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
 * Modifications: 12/04/2012 - Updated to support a graphical user interface.
 *                10/28/2012 - Modified to initiate play for either a
 *                             human or computer player.
 *                10/11/2012 - added a Dictionary Object parameter to
 *                             the constructor, a method to get the score, 
 *                             and a method to reject words.
 ***************************************************************************/

public class Game
{
   private int difficulty = 5;
   private int round = 0;
   
   private Board board;
   private Dictionary dictionary;
   private ComputerPlayer computerPlayer;
   private HumanPlayer humanPlayer;
   private Player currentPlayer;
   /*
    * constructors***************
    */

   /************************************************************************
    * Game - This method constructs a Boggle Game object.
    * 
    * @param Dictionary
    ************************************************************************/
   public Game (Dictionary dictionary)
   {
      this.dictionary = dictionary;
      board = new Board ();
      humanPlayer = new HumanPlayer (dictionary, board);
      computerPlayer = new ComputerPlayer (dictionary, board);
      currentPlayer = humanPlayer;
   } // Game

   /*
    * public methods*****************
    */

   /************************************************************************
    * addWords - This method causes the player to add found words.
    * 
    * @return none
    * @param String
    ************************************************************************/
   public void addWords (String wordsToAdd)
   {
      currentPlayer.addWords (wordsToAdd);
   } // addWords

   /************************************************************************
    * computerTurn - This method causes the computer to find words.
    * 
    * @return none
    * @param none
    ************************************************************************/
   public void computerTurn ()
   {
      StringSet wordsOnBoard = board.dictionaryWordsOnBoard (dictionary);
      ArrayList <String> wordsFound = wordsOnBoard.getWordList ();
      currentPlayer = computerPlayer;
      
      for (String word : wordsFound)
      {
         currentPlayer.addWords (word);
      }
   } // computerTurn
   
   /************************************************************************
    * getBoard - This method returns the players' round scores.
    * 
    * @return Board
    * @param none
    ************************************************************************/
   public Board getBoard ()
   {
      return board;
   } // getBoard

   /************************************************************************
    * getComputerTotalScore - This method returns the computer's total score.
    * 
    * @return int
    * @param none
    ************************************************************************/
   public int getComputerTotalScore ()
   {
      return computerPlayer.getScore ();
   } // getComputerTotalScore

   /************************************************************************
    * getComputerWordsFound - This method returns the computer's words.
    * 
    * @return StringSet
    * @param none
    ************************************************************************/
   public StringSet getComputerWordsFound ()
   {
      return computerPlayer.getWords ();
   } // getComputerWordsFound

   /************************************************************************
    * getComputerWordsRejected - This method returns the computer words
    *       rejected by the user.
    * 
    * @return String
    * @param none
    ************************************************************************/
   public StringSet getComputerWordsRejected ()
   {
      return computerPlayer.getRejectedWords ();
   } // getComputerWordsRejected

   /************************************************************************
    * getHumanTotalScore - This method returns the human player's total
    *       score.
    * @return int
    * @param none
    ************************************************************************/
   public int getHumanTotalScore ()
   {
      return humanPlayer.getScore ();
   } // getHumanTotalScore

   /************************************************************************
    * getHumanWordsFound - This method returns the human player's words.
    * 
    * @return StringSet
    * @param none
    ************************************************************************/
   public StringSet getHumanWordsFound ()
   {
      return humanPlayer.getWords ();
   } // getHumanWordsFound

   /************************************************************************
    * getHumanWordsRejected - This method returns the human words rejected
    *       by the user.
    * 
    * @return StringSet
    * @param none
    ************************************************************************/
   public StringSet getHumanWordsRejected ()
   {
      return humanPlayer.getRejectedWords ();
   } // getHumanWordsRejected

   /************************************************************************
    * getInvalidWordsFound - This method returns the list of words in the
    *       given set that are not on the board. Only the human player
    *       is able to enter invalid words.
    * 
    * @return StringSet
    * @param none
    ************************************************************************/
   public StringSet getInvalidWordsFound ()
   {
      return humanPlayer.getInvalidWords ();
   }

   /************************************************************************
    * getRound - This method returns the game's round.
    * 
    * @return int
    * @param none
    ************************************************************************/
   public int getRound ()
   {
      return round;
   } // getRound

   /************************************************************************
    * getRoundScore - This method returns the round scores of a set of 
    *       words.
    * 
    * @return int
    * @param StringSet
    ************************************************************************/
   public int getRoundScore (StringSet wordSet)
   {
      int score = calculateScore (wordSet);
      return score;
   } // getRoundScore

   /************************************************************************
    * newGame - This method starts a new game.
    * 
    * @return none
    * @param none
    ************************************************************************/
   public void newGame ()
   {
      board = new Board ();
      humanPlayer = new HumanPlayer (dictionary, board);
      computerPlayer = new ComputerPlayer (dictionary, board);   
      currentPlayer = humanPlayer;
   } // newGame

   /************************************************************************
    * newRound - This method starts the round play.
    * 
    * @return none
    * @param none
    ************************************************************************/
   public void newRound ()
   {
      round++;
      board = new Board ();
      humanPlayer.newRound (board);
      computerPlayer.newRound (board);
      currentPlayer = humanPlayer;
   } // newRound

   /************************************************************************
    * rejectWords - This method causes the player to reject words.
    * 
    * @return none
    * @param String
    ************************************************************************/
   public void rejectWords (String wordsToReject)
   {
      humanPlayer.rejectWords (wordsToReject);
      computerPlayer.rejectWords (wordsToReject);
   } // rejectWords

   /************************************************************************
    * setDifficulty - This method sets the game's difficulty level.
    * 
    * @return none
    * @param int
    ************************************************************************/
   public void setDifficulty (int difficulty)
   {
      this.difficulty = difficulty;
      dictionary.setDifficulty (difficulty);
   } // setDifficulty

   /************************************************************************
    * setScores - This method sets the scores for the user and computer.
    * 
    * @return none
    * @param int, int
    ************************************************************************/
   public void setScores (int humanScore, int computerScore)
   {
      humanPlayer.setScore (humanScore);
      computerPlayer.setScore (computerScore);
   } // setScores
   
   /************************************************************************
    * calculateScore - This method returns the score for a word list. The 
    *       score is calculated based on word length, as follows: 
    *             Length      Score 
    *               <3          0 
    *              3-4          1
    *               5           2 
    *               6           3 
    *               7           5 
    *               >7          11
    * 
    * @return int
    * @param StringSet
    ************************************************************************/
   private int calculateScore (StringSet wordSet)
   {
      int score = 0;
      ArrayList<String> foundWords = wordSet.getWordList ();

      for (String word : foundWords)
      {
         if (word.length () == 3 || word.length () == 4)
         {
            score += 1;
         }
         else if (word.length () == 5)
         {
            score += 2;
         }
         else if (word.length () == 6)
         {
            score += 3;
         }
         else if (word.length () == 7)
         {
            score += 5;
         }
         else if (word.length () > 7)
         {
            score += 11;
         }
      }
      return score;
   } // calculateScore

} // Game