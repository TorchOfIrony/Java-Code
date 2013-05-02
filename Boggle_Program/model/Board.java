package model;

import java.util.ArrayList;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

import utilities.GridPosition;
import utilities.StringSet;

/***************************************************************************
 * Board - This class holds a Boggle game board. It is responsible for shuffling
 * or mixing up the letters as indicated in the specifications, and providing
 * client access to individual letters on the board. The board keeps the letters
 * in a 4x4 char array.
 * 
 * @author Alexander Anderson
 * @version 2.0 (October 28, 2012) 
 *          1.0 (September 20, 2012)
 * 
 * Acknowledgements: I acknowledge that I have neither given nor
 *          received assistance for this assignment except as noted below:
 * 
 *          None
 * 
 * Modifications:10/28/2012 - Modified Board to provide methods to
 *                            check for a given word is on the board 
 *                            using recursion and to find all the dictionary
 *                            words on the board.
 *          
 ***************************************************************************/

public class Board
{

   /*
    * public static attributes***************************
    */

   /*
    * private static attributes****************************
    */

   private static char[] letters =
   {
         'a', 'a', 'a', 'a', 'a', 'a', 'b', 'b', 'c', 'c', 'd', 'd', 'd', 'e', 'e',
         'e', 'e', 'e', 'e', 'e', 'e', 'e', 'e', 'f', 'f', 'g', 'g', 'h', 'h',
         'h', 'h', 'h', 'i', 'i', 'i', 'i', 'i', 'i', 'j', 'k', 'l', 'l', 'l',
         'l', 'l', 'm', 'm', 'n', 'n', 'n', 'n', 'n', 'n', 'o', 'o', 'o', 'o',
         'o', 'o', 'p', 'p', 'q', 'r', 'r', 'r', 'r', 'r', 's', 's', 's', 's',
         's', 's', 't', 't', 't', 't', 't', 't', 't', 't', 't', 't', 'u', 'u',
         'u', 'v', 'v', 'w', 'w', 'w', 'x', 'x', 'x', 'y', 'z'
   };

   /*
    * attributes*************
    */

   private boolean[][] visited =
   {
      { false, false, false, false },
      { false, false, false, false },
      { false, false, false, false },
      { false, false, false, false } 
   };

   private char[][] board = new char[4][4];
   private StringSet prefixes = new StringSet ();

   /*
    * constructors***************
    */

   /************************************************************************
    * Board - This method constructs a Boggle game board, randomly
    *       generating a new board.
    * 
    * @param none
    ************************************************************************/
   public Board ()
   {
      shuffle ();
   } // Board

   /************************************************************************
    * Board - This method constructs a Boggle game board given a preset board.
    * 
    * @param char[][]
    ************************************************************************/
   public Board (char[][] board)
   {
      this.board = board;
   } // Board

   /*
    * public methods*****************
    */

   /************************************************************************
    * dictionaryWordsOnBoard - This method returns all the words on the
    *       board that are in the dictionary.
    * 
    * @return StringSet
    * @param Dictionary
    ************************************************************************/
   public StringSet dictionaryWordsOnBoard (Dictionary dictionary)
   {
      StringSet wordsOnBoard = new StringSet ();

      // Get all 3-letter prefixes on board
      for (int i = 0; i < 4; i++)
      {
         for (int j = 0; j < 4; j++)
         {
            GridPosition position = new GridPosition (i, j);
            visited[i][j] = true;
            getPrefixes (position, "");
            visited[i][j] = false;
         }
      }

      // Get all dictionary words for all the prefixes
      SortedSet<String> allWords = new TreeSet<String> ();
      ArrayList<String> prefixList = prefixes.getWordList ();

      for (String prefix : prefixList)
      {
         allWords.addAll (dictionary.wordsStartingWith (prefix));
      }

      for (String word : allWords)
      {
         if (isWordOnBoard (word))
         {
            wordsOnBoard.addWord (word);
         }
      }
      return wordsOnBoard;
   } // dictionaryWordsOnBoard


   /************************************************************************
    * getLetter - This method returns a character on the board.
    * 
    * @return char
    * @param int, int
    ************************************************************************/
   public char getLetter (int i, int j)
   {
      return board[i][j];
   }
   
   /************************************************************************
    * isWordOnBoard - This method returns true if the string sequence provided
    * in the word given is valid on the Boggle board. Letters in the word must
    * be adjacent (horizontally, vertically, or diagonally) and can only be used
    * once.
    * 
    * @return boolean
    * @param String
    ************************************************************************/
   public boolean isWordOnBoard (String word)
   {
      boolean wordIsOnBoard = false;

      if (word != null && word.length () > 0)
      {
         char letter = word.charAt (0);
         int nextLetter = 1;

         if (nextLetter < word.length ())
         {
            for (int i = 0; i < 4; i++)
            {
               for (int j = 0; j < 4; j++)
               {
                  if (!wordIsOnBoard)
                  {
                     GridPosition boardPosition = new GridPosition (i, j);
                     
                     if (letter == board[i][j])
                     {
                        // Use recursion to check for the word on the board
                        visited[boardPosition.getX ()][boardPosition.getY ()] = true;
                        wordIsOnBoard = checkWord (word, nextLetter, boardPosition);
                        visited[boardPosition.getX ()][boardPosition.getY ()] = false;                        
                     }
                  }
               }
            }
         }
      }
      return wordIsOnBoard;
   } // isWordOnBoard

   /************************************************************************
    * toString - This method provides a string representation of the board.
    * 
    * @return String
    * @param none
    ************************************************************************/
   public String toString ()
   {
      StringBuffer str = new StringBuffer ();

      for (int i = 0; i < 4; i++)
      {
         for (int j = 0; j < 4; j++)
         {
            str.append (board[i][j]);
            str.append (' ');
         }
         str.append ('\n');
      }
      return str.toString ().toUpperCase ();
   } // toString

   /*
    * private methods*****************
    */

   /************************************************************************
    * checkWord - This method recursively checks if a word is on the board.
    * 
    * @return boolean
    * @param String
    *           , char, BoardPosition
    ************************************************************************/
   private boolean checkWord (String word, int nextLetter,
         GridPosition oldPosition)
   {
      boolean wordIsOnBoard = false;
      char letter = word.charAt (nextLetter);

		for (int i = 0; i < 4; i++)
		{
			for (int j = 0; j < 4; j++)
			{				
				if (!wordIsOnBoard && !visited[i][j] && letter == board[i][j])
				{
	      		GridPosition newPosition = new GridPosition (i, j);
					
					if (GridPosition.isAdjacent (oldPosition, newPosition))
					{
            		if (nextLetter >= word.length () - 1)
            		{
               		wordIsOnBoard = true;
            		}
            		else
            		{
							visited[i][j] = true;
               		wordIsOnBoard = checkWord (word, nextLetter + 1, newPosition);
							visited[i][j] = false;
            		}
					}
				}
			}
		}
      return wordIsOnBoard;
   } // checkWord

   /************************************************************************
    * getPrefixes - This method finds 3 character prefixes on the board.
    * 
    * @return none
    * @param BoardPosition, String
    ************************************************************************/
   private void getPrefixes (GridPosition position, String prefix)
   {
      int x = position.getX ();
      int y = position.getY ();
      String newPrefix = prefix + board[x][y];

      if (newPrefix.length () == 3)
      {
         prefixes.addWord (newPrefix);
         visited[x][y] = false;
      }
      else
      {
         for (int i = x - 1; i <= x + 1; i++)
         {
            for (int j = y - 1; j <= y + 1; j++)
            {
               if (i >= 0 && j >= 0 && i < 4 && j < 4)
               {
                  if (! ( (i == x) && (j == y)))
                  {
                     if (!visited[i][j])
                     {
                        GridPosition newPosition = new GridPosition (i, j);
                        visited[i][j] = true;
                        getPrefixes (newPosition, newPrefix);
                        visited[i][j] = false;
                     }
                  }
               }
            }
         }
      }
   } // getPrefixes

   /************************************************************************
    * shuffle - This method randomly assigns letters to the board based on
    *       the following probabilities: 
    *       Probability    Letter 
    *            1/96    J, K, Q, Y, Z 
    *            2/96    B, C, F, G, M, P, V 
    *            3/96    D, U, W, X 
    *            5/96    H, L, R 
    *            6/96    A, I, N, S, O 
    *            10/96   E, T
    * 
    * @param none
    ************************************************************************/
   private void shuffle ()
   {
      Random randomNumber = new Random (System.nanoTime ());

      for (int i = 0; i < 4; i++)
      {
         for (int j = 0; j < 4; j++)
         {
            int randomInt = randomNumber.nextInt (95);
            board[i][j] = letters[randomInt];
         }
      }
   } // shuffle

} // Board