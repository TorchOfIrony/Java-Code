package model;

import utilities.StringSet;
import model.Board;
import model.Dictionary;

/***************************************************************************
* ComputerPlayer - The computer player extends the Player class to find 
*      Boggle words in the dictionary.
*      
* @author Alexander Anderson
* @version 2.0 (December 04, 2012), 1.0 (October 28, 2012)
*
* Acknowledgements: I acknowledge that I have neither given nor
*       received assistance for this assignment except as
*       noted below:
*
*   None
*          
* Modifications: 12/04/2012: Updated to support graphical user interface.
*
***************************************************************************/

public class ComputerPlayer extends Player
{
   /*  attributes
    ***************/

   /*  constructors
    *****************/

   /************************************************************************
   * Player - This method constructs a Boggle Player object.
   *
   * @param Dictionary
   ************************************************************************/
   public ComputerPlayer (Dictionary dictionary, Board board)
   {
      super (dictionary, board);
   } // ComputerPlayer


   /*  public methods
    *******************/

   /************************************************************************
   * playRound - This method gets the words the computer finds for a round.
   *
   * @return StringSet
   * @param Board board
   ************************************************************************/
   public void addWords (String word)
   {
      roundWords.addWord (word);   
   } // playRound

} // ComputerPlayer