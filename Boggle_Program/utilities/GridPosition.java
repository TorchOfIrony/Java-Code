package utilities;

/***************************************************************************
 * GridPosition - . 
 *        
 * @author Alexander Anderson
 * @version 1.0 (October 28, 2012)
 * 
 * Acknowledgements: I acknowledge that I have neither given nor
 *          received assistance for this assignment except as noted below:
 * 
 * Modifications
 *          None
 * 
 ***************************************************************************/

public class GridPosition
{
   /*
    * attributes*************
    */
   private int x = 0;
   private int y = 0;

   /************************************************************************
    * GridPosition - This method constructs a GridPosition object.
    * 
    * @return none
    * @param int, int
    ************************************************************************/
   public GridPosition (int x, int y)
   {
      this.x = x;
      this.y = y;
   }

   /*
    * public methods*************
    */
   
   /************************************************************************
    * getX.
    * 
    * @return int
    * @param none
    ************************************************************************/
   public int getX ()
   {
      return x;
   } // getX

   /************************************************************************
    * getY
    * 
    * @return int
    * @param none
    ************************************************************************/
   public int getY ()
   {
      return y;
   } // getY
   
   /************************************************************************
    * isAdjacent - This method determines if a given position is adjacent
    *       (horizontally, vertically, or diagonally) to another given 
    *       position.
    * 
    * @return boolean
    * @param BoardPosition, BoardPosition
    ************************************************************************/
   public static boolean isAdjacent (GridPosition first, GridPosition second)
   {
      boolean adjacent = false;
      int firstX = first.getX();
      int firstY = first.getY();
      int secondX = second.getX();
      int secondY = second.getY();

      if (firstX == secondX)
      {
         if (Math.abs (firstY - secondY) == 1)
         {
            adjacent = true;
         }
      }
      else if (firstY == secondY)
      {
         if (Math.abs (firstX - secondX) == 1)
         {
            adjacent = true;
         }
      }
      else
      {
         if (Math.abs (firstX - secondX) == 1
               && Math.abs (firstY - secondY) == 1)
         {
            adjacent = true;
         }
      }

      return adjacent;
   } // isAdjacent


   /************************************************************************
    * validPosition.
    * 
    * @return boolean
    * @param none
    ************************************************************************/
   public boolean validPosition ()
   {
      return (x >= 0 && x <= 3 && y >= 0 && y <= 3);
   } // validPosition
   
} // GridPosition

