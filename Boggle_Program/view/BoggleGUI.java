package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextPane;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import controller.Game;

import utilities.StringSet;

import model.Dictionary;
import model.Board;

/***************************************************************************
 * BoggleGui - This class provide a graphic user interface for the
 *       Boggle program.
 * 
 * @author Alexander Anderson
 * @version 1.0 (December 5, 2012)
 * 
 * Acknowledgements: I acknowledge that I have neither given nor
 *          received assistance for this assignment except as noted below:
 * 
 *          None
 * 
 * Modifications: None
 ***************************************************************************/

public class BoggleGUI extends JFrame
{
   /**
    * 
    */
   private static final long serialVersionUID = 1L;

   private final Color BACKGROUND_COLOR = new Color (173, 216, 230);

   private Dictionary dictionary;
   private Game game;
   private GamePanel gamePanel;
   private PlayPanel playPanel;
   private ResultsPanel resultsPanel;
   private ScoringPanel scoringPanel;
   
   private int computerTotalScore = 0;
   private int humanTotalScore = 0;
   private int pointsNeededToWin = 100;
   
   private StringSet humanWordsFound = new StringSet ();
   private StringSet computerWordsFound = new StringSet ();
   private StringSet commonWordsFound = new StringSet ();
   private StringSet invalidWordsFound = new StringSet ();
           
   /************************************************************************
    * BoggleGUI Constructor
    * 
    * @return none
    * @param Dictionary
    ************************************************************************/
   public BoggleGUI (Dictionary dictionary)  
   {
      super ("Welcome to Boggle!");              
      this.dictionary = dictionary;
      game = new Game (dictionary);
      
      setLayout (new BorderLayout());
      setUpMenuBar ();
      setUpPanels ();
      setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
      pack ();
      setVisible (true);
      setSize (750, 700);
      setResizable (false);
   }

   /*
    * private methods*****************
    */

   /************************************************************************
    * endRound - This method ends a Boggle round - the scores are tabulated
    *       and displayed.
    * 
    * @return none
    * @param none
    ************************************************************************/
   private void endRound ()
   {      
      StringSet humanWordsEntered = game.getHumanWordsFound ();
      invalidWordsFound = game.getInvalidWordsFound ();

      game.computerTurn ();
      
      StringSet computerWordsEntered = game.getComputerWordsFound ();
      computerWordsFound = computerWordsEntered.difference 
            (humanWordsEntered);
      commonWordsFound = computerWordsEntered.intersection 
            (humanWordsEntered);
      humanWordsFound = humanWordsEntered.difference 
            (computerWordsEntered);
      
      resultsPanel.endRound ();
      
      // Compute Scores
      int computerRoundScore = game.getRoundScore (computerWordsFound);
      int humanRoundScore = game.getRoundScore (humanWordsFound);
      computerTotalScore = computerRoundScore + game.getComputerTotalScore ();
      humanTotalScore = humanRoundScore + game.getHumanTotalScore ();
      
      scoringPanel.updateScores (computerRoundScore, humanRoundScore);
   } // endRound

   /************************************************************************
    * gameOver - This method processes when a game is over.
    * 
    * @return none
    * @param String
    ************************************************************************/
   private void gameOver (String winner)
   {
      resultsPanel.gameOver (winner);
      
      // Set up for a new game
      game = new Game (dictionary);
      computerTotalScore = 0;
      humanTotalScore = 0;
   } // gameOver

   /************************************************************************
    * newGame - This method starts a new Boggle game.
    * 
    * @return none
    * @param none
    ************************************************************************/
   private void newGame ()
   {
      game = new Game (dictionary);
      computerTotalScore = 0;
      humanTotalScore = 0;
      
      // Clear word lists
      humanWordsFound = new StringSet ();
      computerWordsFound = new StringSet ();
      commonWordsFound = new StringSet ();
      invalidWordsFound = new StringSet ();
      
      // Update the panels for a new game.
      playPanel.newGame ();
      scoringPanel.newGame ();
      resultsPanel.newGame ();
   } //newGame
   
   /************************************************************************
    * newRound - This method starts a new round.
    * 
    * @return none
    * @param none
    ************************************************************************/
   private void newRound ()
   {
      game.setScores (humanTotalScore, computerTotalScore);
      game.newRound ();
      
      // Clear word lists
      humanWordsFound = new StringSet ();
      computerWordsFound = new StringSet ();
      commonWordsFound = new StringSet ();
      invalidWordsFound = new StringSet ();
      
      if (game.getRound () == 1)
      {
         // Update the panels for a new game.
         playPanel.newGame ();
         scoringPanel.newGame ();
         resultsPanel.newGame ();         
      }
      
      // Update the panels when a new round begins.
      playPanel.newRound ();
      scoringPanel.newRound ();
      resultsPanel.newRound ();
   } // newRound
      
   /************************************************************************
    * quitGame - This method terminates the program gracefully.
    * 
    * @return none
    * @param none
    ************************************************************************/
   private void quitGame ()
   {
      dictionary.writeWordsToFile ();
      System.exit(0);
   } // quitGame
   
   /************************************************************************
    * rejectWords - This method updates the user interface after words
    *       are rejected by the player.
    * 
    * @return none
    * @param String
    ************************************************************************/
   private void rejectWords (String rejectedWords)
   {
      game.rejectWords (rejectedWords);
      
      computerWordsFound = computerWordsFound.difference 
            (game.getComputerWordsRejected ());
      humanWordsFound = humanWordsFound.difference 
            (game.getHumanWordsRejected ());
      commonWordsFound = commonWordsFound.difference 
            (game.getHumanWordsRejected ());
      commonWordsFound = commonWordsFound.difference 
            (game.getComputerWordsRejected ());
      
      // Compute Scores
      int computerRoundScore = game.getRoundScore (computerWordsFound);
      int humanRoundScore = game.getRoundScore (humanWordsFound);
      computerTotalScore = computerRoundScore + game.getComputerTotalScore ();
      humanTotalScore = humanRoundScore + game.getHumanTotalScore ();
      
      scoringPanel.updateScores (computerRoundScore, humanRoundScore);
   } // rejectWords
   
   /************************************************************************
    * setUpMenuBar - This method initializes the Boggle menu bar.
    * 
    * @return none
    * @param none
    ************************************************************************/
   private void setUpMenuBar ()  
   {
      //Set Up Menu Bar
      JMenuBar menu = new JMenuBar ();
                   
      // Game Menu
      JMenu gameMenu = new JMenu ("Game");
      gameMenu.setMnemonic ('G');
      menu.add (gameMenu);

      // New Game
      JMenuItem newGame = new JMenuItem ("New Game");
      gameMenu.add (newGame);
      newGame.setMnemonic ('N');      
      ActionListener newGameListener = new ActionListener ()
      {
         public void actionPerformed (ActionEvent event)
         {
            newGame ();
         }
      };
      newGame.addActionListener (newGameListener);

      // New Round
      JMenuItem newRound = new JMenuItem ("New Round");
      gameMenu.add (newRound);
      newRound.setMnemonic ('R');      
      ActionListener newRoundListener = new ActionListener ()
      {
         public void actionPerformed (ActionEvent event)
         {
            newRound ();
         }
      };
      newRound.addActionListener (newRoundListener);

      // Quit
      JMenuItem quitGame = new JMenuItem ("Quit");
      gameMenu.add (quitGame);
      quitGame.setMnemonic ('Q');
      ActionListener quitListener = new ActionListener ()
      {
         public void actionPerformed (ActionEvent e) 
         {
            quitGame ();
         }         
      };
      quitGame.addActionListener (quitListener);

      setJMenuBar (menu);
   } // setUpMenuBar


   /************************************************************************
    * setUpPanels - This method initializes the Boggle display panels.
    * 
    * @return none
    * @param none
    ************************************************************************/
   private void setUpPanels ()  
   {
      Container contentPane = getContentPane ();
      
      gamePanel = new GamePanel ();
      playPanel = new PlayPanel ();
      scoringPanel = new ScoringPanel ();
      resultsPanel = new ResultsPanel ();
      
      contentPane.add (gamePanel, BorderLayout.NORTH);
      contentPane.add (playPanel, BorderLayout.WEST);
      contentPane.add (scoringPanel, BorderLayout.SOUTH);
      contentPane.add (resultsPanel, BorderLayout.CENTER);
   } // setUpPanels
                   

   /*
    * private embedded classes 
    * *************************
    */
   
   /***************************************************************************
    * GamePanel - The Game Panel contains controls to set the difficulty
    *       level and to set the points needed to win. 
    * 
    * @author Alexander Anderson
    * @version 1.0 (December 5, 2012)
    * 
    * Acknowledgements: I acknowledge that I have neither given nor
    *          received assistance for this assignment except as noted below:
    * 
    *          None
    * 
    * Modifications: None
    ***************************************************************************/

   private class GamePanel extends JPanel
   {
      private static final long serialVersionUID = 1L;
      private ButtonGroup difficultyButtons;
      private JLabel difficultyLabel = new JLabel ("Difficulty: ");
      private JLabel pointsNeededLabel = new JLabel ("Needed To win: ");
      private JSpinner pointsNeededSpinner;
      private SpinnerModel pointsNeededModel;
      
      /************************************************************************
       * GamePanel Constructor
       * 
       * @return none
       * @param none
       ************************************************************************/
      public GamePanel ()  
      {
         BoxLayout boxLayout = new BoxLayout (this, BoxLayout.X_AXIS);
         setLayout (boxLayout);
         setBackground (BACKGROUND_COLOR);
         Dimension panelSize = new Dimension (750, 50);
         setPreferredSize (panelSize);
         setName ("Game");
         
         setupPanel ();
         
         // Add Border
         setBorder (BorderFactory.createLoweredBevelBorder());
         
      } // GamePanel
      
      /*  private methods
       *******************/

      /************************************************************************
       * setupPanel - this method sets up the controls within the panel. The
       *       difficulty setting is a radio button group, and the points
       *       needed to win is a spinner control which allows values from 
       *       1 to 10,000, with a default of 100.
       * 
       * @return none
       * @param none
       ************************************************************************/
      private void setupPanel ()  
      {
         // *****  Difficulty Radio Button Group ******
         ActionListener difficultyListener = new ActionListener ()
         {
            public void actionPerformed (ActionEvent event)
            {
               Integer difficulty = Integer.valueOf (event.getActionCommand()); 
               game.setDifficulty (difficulty.intValue());
            }         
         };
         
         this.add (difficultyLabel);
         
         difficultyButtons = new ButtonGroup ();
         
         JRadioButton r1 = new JRadioButton ("1");
         r1.setBackground (BACKGROUND_COLOR);
         r1.setActionCommand ("1");
         difficultyButtons.add (r1);
         r1.addActionListener (difficultyListener);
         this.add (r1);
         
         JRadioButton r2 = new JRadioButton ("2");
         r2.setBackground (BACKGROUND_COLOR);
         r2.setActionCommand ("2");
         difficultyButtons.add (r2);
         r2.addActionListener (difficultyListener);
         this.add (r2);
         
         JRadioButton r3 = new JRadioButton ("3");
         r3.setBackground (BACKGROUND_COLOR);
         r3.setActionCommand ("3");
         difficultyButtons.add (r3);
         r3.addActionListener (difficultyListener);
         this.add (r3);
         
         JRadioButton r4 = new JRadioButton ("4");
         r4.setBackground (BACKGROUND_COLOR);
         r4.setActionCommand ("4");
         difficultyButtons.add (r4);
         r4.addActionListener (difficultyListener);
         this.add (r4);
         
         JRadioButton r5 = new JRadioButton ("5");
         r5.setBackground (BACKGROUND_COLOR);
         r5.setActionCommand ("5");
         difficultyButtons.add (r5);
         r5.addActionListener (difficultyListener);
         r5.setSelected (true);
         this.add (r5);
         
         JRadioButton r6 = new JRadioButton ("6");
         r6.setBackground (BACKGROUND_COLOR);
         r6.setActionCommand ("6");
         difficultyButtons.add (r6);
         r6.addActionListener (difficultyListener);
         this.add (r6);
         
         JRadioButton r7 = new JRadioButton ("7");
         r7.setBackground (BACKGROUND_COLOR);
         r7.setActionCommand ("7");
         difficultyButtons.add (r7);
         r7.addActionListener (difficultyListener);
         this.add (r7);
         
         JRadioButton r8 = new JRadioButton ("8");
         r8.setBackground (BACKGROUND_COLOR);
         r8.setActionCommand ("8");
         difficultyButtons.add (r8);
         r8.addActionListener (difficultyListener);
         this.add (r8);
         
         JRadioButton r9 = new JRadioButton ("9");
         r9.setBackground (BACKGROUND_COLOR);
         r9.setActionCommand ("9");
         difficultyButtons.add (r9);
         r9.addActionListener (difficultyListener);
         this.add (r9);
         
         JRadioButton r10 = new JRadioButton ("10");
         r10.setBackground (BACKGROUND_COLOR);
         r10.setActionCommand ("10");
         difficultyButtons.add (r10);
         r10.addActionListener (difficultyListener);
         this.add (r10);
               
         difficultyButtons.add (r1);
         difficultyButtons.add (r2);
         difficultyButtons.add (r3);
         difficultyButtons.add (r4);
         difficultyButtons.add (r5);
         difficultyButtons.add (r6);
         difficultyButtons.add (r7);
         difficultyButtons.add (r8);
         difficultyButtons.add (r9);
         difficultyButtons.add (r10);


         // *****  Add Fill ******
         add (Box.createRigidArea (new Dimension (100, 0)));
         
         // *****  Points Needed To Win Spinner ******
         ChangeListener pointsNeededListener = new ChangeListener ()
         {
            @Override
            public void stateChanged (ChangeEvent event)
            {
               SpinnerModel spinnerModel = pointsNeededSpinner.getModel ();
               
               if (spinnerModel instanceof SpinnerNumberModel)
               {
                  Number spinnerValue = (Number) 
                        ((SpinnerNumberModel)spinnerModel).getNumber();
                  pointsNeededToWin = spinnerValue.intValue();
                  BoggleGUI.this.newGame ();
               }
            }
         };
         
         pointsNeededModel = new SpinnerNumberModel (100, 1, 10000, 1);
         pointsNeededSpinner = new JSpinner (pointsNeededModel);
         pointsNeededSpinner.addChangeListener (pointsNeededListener);
         
         this.add (pointsNeededLabel);
         this.add (pointsNeededSpinner);
               
      } // setupDifficultyRadioButtons
      
   } // GamePanel
   

   /***************************************************************************
    * PlayPanel - The Play panel contains the Boggle Board and allows the user
    *       to enter words found. The panel also displays a countdown timer 
    *       progress bar, and allows the user to initiate a new round.
    *        
    * @author Alexander Anderson
    * @version 1.0 (December 5, 2012)
    * 
    * Acknowledgements: I acknowledge that I have neither given nor
    *          received assistance for this assignment except as noted below:
    * 
    * Modifications
    *          None
    * 
    ***************************************************************************/
   private class PlayPanel extends JPanel
   {
      private static final long serialVersionUID = 1L;
      private BoardPanel boardPanel; 
      private JButton newRoundButton;
      private JProgressBar timerProgressBar;
      private JTextPane wordEntryArea;
      private Timer timer;
        
      /************************************************************************
       * PlayPanel Constructor
       * 
       * @return none
       * @param none
       ************************************************************************/
      public PlayPanel ()  
      {
         JSeparator separator = new JSeparator (SwingConstants.HORIZONTAL);
         separator.setSize (new Dimension (10,10));
         
         BoxLayout boxLayout = new BoxLayout (this, BoxLayout.Y_AXIS);
         setLayout (boxLayout);
         setBackground (BACKGROUND_COLOR);
         Dimension panelSize = new Dimension (300, 500);
         setPreferredSize (panelSize);
         
         setupNewRoundButton ();
         add (Box.createRigidArea (new Dimension (0,10)));
         setupLetters ();
         add (Box.createRigidArea (new Dimension (0,10)));
         setupTimerProgressBar ();
         add (Box.createRigidArea (new Dimension (0,10)));
         setupWordEntryArea ();
         
         // Add Border
         TitledBorder title;
         title = BorderFactory.createTitledBorder 
               (BorderFactory.createLoweredBevelBorder(), "Play");
         setBorder (title);
      }

      /*  public methods
       *******************/

      /************************************************************************
       * endRound - this method stores the words entered whne the round ends
       *       and clears the text entry area.
       * 
       * @return none
       * @param none
       ************************************************************************/
      public void endRound ()  
      {
         newRoundButton.setEnabled (true);
         game.addWords (wordEntryArea.getText());
         
         // Clear word entry area
         wordEntryArea.setText ("");
         wordEntryArea.setEditable (false);

         BoggleGUI.this.endRound ();
      }

      /************************************************************************
       * newGame - this method resets the play panel for a new game.
       * 
       * @return none
       * @param none
       ************************************************************************/
      public void newGame ()  
      {
         // Reset board
         boardPanel.newGame ();
         newRoundButton.setEnabled (true);
         
         // Clear word entry area
         wordEntryArea.setText("");
                  
         // Reset Progress Bar and Timer
         if (timer != null)
         {
            if (timer.isRunning ())
            {
               timer.stop();
            }
            timerProgressBar.setValue (0);
            timerProgressBar.setString ("3:00");
         }
         
         this.paintImmediately (this.getVisibleRect());
      }

      /************************************************************************
       * newRound - this method resets the play panel for a new round by
       *       getting a new board and starting the 3 minute timer.
       * 
       * @return none
       * @param none
       ************************************************************************/
      public void newRound ()  
      {
         // Reset board and clear text entry area
         boardPanel.newRound ();
         newRoundButton.setEnabled (false);
         
         ActionListener timerProgressListener = new ActionListener() 
         {
            int progressBarCounter = 0;
            int timeLeftCounter = 180;
             
             public void actionPerformed (ActionEvent ae) 
             {
                 progressBarCounter++;
                 timeLeftCounter--;
                 int minutesPart = timeLeftCounter / 60;
                 int secondsPart = timeLeftCounter % 60;
                 String timeLeft = String.valueOf (minutesPart)
                       + ((secondsPart < 10 ) ? ":0" : ":")
                       + String.valueOf (secondsPart);
                 timerProgressBar.setValue (progressBarCounter);
                 timerProgressBar.setString (timeLeft);
                 
                 if (timeLeftCounter <= 0) 
                 {
                     timer.stop();
                     Toolkit.getDefaultToolkit ().beep ();
                     endRound ();
                 } 
             }
         };

         // Clear text entry area
         wordEntryArea.setText("");
         wordEntryArea.setEditable (true);
         
         // Start the timer
         timerProgressBar.setVisible(true);
         timer = new Timer (1000, timerProgressListener);
         timer.start();   
      }

      /*  private methods
       *******************/

      /************************************************************************
       * setupLetters - this method sets up the letters on the board.
       * 
       * @return none
       * @param none
       ************************************************************************/
      private void setupLetters ()  
      {
         boardPanel = new BoardPanel (); 
         add (boardPanel);    
      } // setupLetters
      

      /************************************************************************
       * setupNewRoundButton - this method defines the start new round button.
       * 
       * @return none
       * @param none
       ************************************************************************/
      private void setupNewRoundButton ()  
      {
         newRoundButton = new JButton ("Start New Round");
         
         ActionListener newRoundListener = new ActionListener ()
         {
            public void actionPerformed (ActionEvent event)
            {
               BoggleGUI.this.newRound ();
            }
         };
         newRoundButton.addActionListener (newRoundListener);
         
         add (newRoundButton);
         
      } // setupNewRoundButton
      
      /************************************************************************
       * setupTimerProgressBar - this method creates the timer progress bar.
       * 
       * @return none
       * @param none
       ************************************************************************/
      private void setupTimerProgressBar ()  
      {
         JLabel label = new JLabel ("Time Left:");
         add (label);
         
         timerProgressBar = new JProgressBar (0, 180);
         timerProgressBar.setValue (0);
         timerProgressBar.setStringPainted (true);
         timerProgressBar.setString ("3:00");
         
         add (timerProgressBar);
           
      } // setupTimerProgressBar
      
      /************************************************************************
       * setupWordEntryArea - this method creates the word text entry area.
       * 
       * @return none
       * @param none
       ************************************************************************/
      private void setupWordEntryArea ()  
      {
         JLabel label = new JLabel ("Enter words:");
         add (label);

         // Create a text entry area
         wordEntryArea = new JTextPane ();

         // Make the text area scrollable
         JScrollPane wordEntryScrollPane = new JScrollPane(wordEntryArea);
         wordEntryScrollPane.setBounds(3, 3, 300, 200);
         wordEntryScrollPane.setHorizontalScrollBarPolicy
               (JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);      
         
         add (wordEntryScrollPane);
      } // setupWordEntryArea


      /*  private classes
       *******************/

      /************************************************************************
       * BoardPanel
       *        
       * @author Alexander Anderson
       * @version 1.0 (December 5, 2012)
       * 
       * Acknowledgements: I acknowledge that I have neither given nor
       *          received assistance for this assignment except as noted below:
       * 
       * Modifications
       *          None
       ************************************************************************/
      private class BoardPanel extends JPanel
      {
         private static final long serialVersionUID = 1L;
         private LetterPanel letters[][] = new LetterPanel[4][4];
         
         /************************************************************************
          * BoardPanel Constructor
          * 
          * @return none
          * @param none
          ************************************************************************/
         public BoardPanel ()
         {
            setLayout (new GridLayout(4, 4, 10, 10));      
            setBackground (BACKGROUND_COLOR);
            
            setupPanel ();
            setBorder (BorderFactory.createRaisedBevelBorder());
         }
         
         /************************************************************************
          * newGame - this method resets the board to display "X" in preparation
          *       for a new game.
          * 
          * @return none
          * @param none
          ************************************************************************/
         public void newGame ()
         {
            for (int i = 0; i < 4; i++)
            {
               for (int j = 0; j < 4; j++)
               {
                  letters[i][j].setLetter ('X');
               }
            }
            this.paintImmediately (this.getVisibleRect());
         }

         
         /************************************************************************
          * newRound - this method populates the board for a new round.
          * 
          * @return none
          * @param none
          ************************************************************************/
         public void newRound ()
         {
            Board board = game.getBoard ();
            
            for (int i = 0; i < 4; i++)
            {
               for (int j = 0; j < 4; j++)
               {
                  letters[i][j].setLetter (board.getLetter (i, j));
               }
            }
            this.paintImmediately (this.getVisibleRect());
         }

         /************************************************************************
          * setupPanel - this method sets up the board panel.
          * 
          * @return none
          * @param none
          ************************************************************************/
         private void setupPanel ()
         {
            for (int i = 0; i < 4; i++)
            {
               for (int j = 0; j < 4; j++)
               {
                  //char letter = board.getLetter (i, j);
                  char letter = 'X';
                  letters[i][j] = new LetterPanel(letter);
                  this.add (letters[i][j]);
               }
            }
         }
      } // BoardPanel


      /************************************************************************
       * LetterPanel
       * 
       * @author Alexander Anderson
       * @version 1.0 (December 5, 2012)
       * 
       * Acknowledgements: I acknowledge that I have neither given nor
       *          received assistance for this assignment except as noted below:
       * 
       * Modifications
       *          None
       ************************************************************************/
      private class LetterPanel extends JPanel
      {
         private static final long serialVersionUID = 1L;
         private final Color LETTER_BACKGROUND = new Color (250, 250, 250);
         private final Color LETTER_FOREGROUND = new Color (150, 150, 150);
         
         private JLabel label;
         
         /************************************************************************
          * LetterPanel Constructor
          * 
          * @return none
          * @param none
          ************************************************************************/
         public LetterPanel (char letter)
         {
            label = new JLabel (String.valueOf (letter), SwingConstants.CENTER);
            setLayout (new BorderLayout());
            setBackground (LETTER_BACKGROUND);
            setForeground (LETTER_FOREGROUND);
            setPreferredSize (new Dimension (50, 50));
            add (label, BorderLayout.CENTER);
            
            // Outline the letter panel
            setBorder (BorderFactory.createLineBorder (Color.black));
         }      
         
         /************************************************************************
          * setLetter - this method updates the letter on the face of this panel.
          * 
          * @return none
          * @param char
          ************************************************************************/
         public void setLetter (char letter)
         {
            String labelText = String.valueOf (letter);
            label.setText (labelText.toUpperCase ());
            this.paintImmediately (this.getVisibleRect ());
         }
      } // LetterPanel

   } // PlayPanel
   
   /***************************************************************************
    * ResultsPanel - This class creates a panel which holds the resulting
    *       word lists from a round of play, and allows the user to reject
    *       entered words. 
    *        
    * @author Alexander Anderson
    * @version 1.0 (December 5, 2012)
    * 
    * Acknowledgements: I acknowledge that I have neither given nor
    *          received assistance for this assignment except as noted below:
    * 
    * Modifications
    *          None
    ***************************************************************************/

   private class ResultsPanel extends JPanel
   {
      private static final long serialVersionUID = 1L;
      private EnteredWordsPanel enteredWordsPanel;
      private JButton rejectWordsButton;
      private JLabel gameStatusLabel;
      
      /************************************************************************
       * ResultsPanel Constructor
       * 
       * @return ResultsPanel
       * @param none
       ************************************************************************/
      public ResultsPanel ()  
      {
         BoxLayout layout = new BoxLayout (this, BoxLayout.Y_AXIS);
         setLayout (layout);
         setBackground (BACKGROUND_COLOR);
         Dimension panelSize = new Dimension (450, 400);
         setPreferredSize (panelSize);
         
         setupPanels ();
         
         // Add Border
         TitledBorder title;
         title = BorderFactory.createTitledBorder 
               (BorderFactory.createLoweredBevelBorder(), "Results");
         setBorder (title);
      }

      /*  public methods 
       *******************/

      /************************************************************************
       * endRound - this methods updates the word lists when a round ends.
       * 
       * @return none
       * @param none
       ************************************************************************/
      public void endRound ()  
      {         
         // Enable reject words button
         rejectWordsButton.setEnabled (true);
         
         // Display word lists
         enteredWordsPanel.updateWordLists ();
         
         // Update Game Status Label
         gameStatusLabel.setText ("Game Status: Round over.");
         
         this.paintImmediately (this.getVisibleRect());
      }

      /************************************************************************
       * gameOver - this method updates the panel when a game has been won.
       * 
       * @return none
       * @param String
       ************************************************************************/
      public void gameOver (String winner)  
      {
         // Update Game Status Label
         gameStatusLabel.setText ("Game Over! " + winner);
         
         // Grey out reject words button
         rejectWordsButton.setEnabled (false);
         
         this.paintImmediately (this.getVisibleRect());
      }

      /************************************************************************
       * newGame - this method clears the word lists for a new game.
       * 
       * @return none
       * @param none
       ************************************************************************/
      public void newGame ()  
      {
         // Clear word lists 
         enteredWordsPanel.resetWordLists ();
         
         // Enable reject words button
         rejectWordsButton.setEnabled (false);
         
         // Update Game Status Label
         gameStatusLabel.setText ("Game Status: Ready for a new round!");
         
         this.paintImmediately (this.getVisibleRect());
      }

      /************************************************************************
       * newRound - this method resets the panel when a new round begins.
       * 
       * @return none
       * @param none
       ************************************************************************/
      public void newRound ()  
      {
         // Clear word lists 
         enteredWordsPanel.resetWordLists ();
         
         // Grey out reject words button
         rejectWordsButton.setEnabled (false);
         
         // Update Game Status Label
         gameStatusLabel.setText ("Game Status: Round in progress.");
         
         this.paintImmediately (this.getVisibleRect());
      }
      
      /*  private methods
       *******************/
      
      /************************************************************************
       * setupPanels - this method creates the results panel's components.
       * 
       * @return none
       * @param none
       ************************************************************************/
      private void setupPanels ()  
      {
         add (Box.createRigidArea (new Dimension (0,10)));
         gameStatusLabel = new JLabel ("Game Status: Ready for a new round!");
         gameStatusLabel.setAlignmentX (SwingConstants.LEFT);
         add (gameStatusLabel);
         add (Box.createGlue());
         
         add (Box.createRigidArea (new Dimension (0,10)));
         enteredWordsPanel = new EnteredWordsPanel();
         add (enteredWordsPanel);
         
         add (Box.createRigidArea (new Dimension (0,10)));
         setupRejectWordsButton ();                
         add (Box.createRigidArea (new Dimension (0,30)));
      }

      /************************************************************************
       * setupRejectWordsButton - this method creates the reject words button.
       * 
       * @return none
       * @param none
       ************************************************************************/
      private void setupRejectWordsButton ()  
      {
         rejectWordsButton = new JButton ("Reject Words");
         rejectWordsButton.setAlignmentX (SwingConstants.LEFT);
         
         ActionListener rejectWordsListener = new ActionListener ()
         {
            public void actionPerformed (ActionEvent event)
            {
               String selectedRejects = enteredWordsPanel.getSelectedWords ();
               rejectWords (selectedRejects);
               enteredWordsPanel.updateWordLists ();
            }
         };
         rejectWordsButton.addActionListener (rejectWordsListener);
         
         add (rejectWordsButton, BorderLayout.SOUTH);
         rejectWordsButton.setEnabled (false);         
      } // setupRejectWordsButton

      /************************************************************************
       * EnteredWordsPanel - this panel contains the list of words entered by
       *       the player and found by the computer. There are four lists
       *       displayed: 
       *               Words generated by both players.
       *               Words typed by the user that are not on the board.
       *               Words generated by the user alone.
       *               Words generated by the program alone.
       * 
       * @author Alexander Anderson
       * @version 1.0 (December 5, 2012)
       * 
       * Acknowledgements: I acknowledge that I have neither given nor
       *          received assistance for this assignment except as noted below:
       * 
       * Modifications
       *          None
       ************************************************************************/
      private class EnteredWordsPanel extends JPanel
      {
         private static final long serialVersionUID = 1L;
         private WordListPanel humanWordList;
         private WordListPanel computerWordList;
         private WordListPanel commonWordList;
         private WordListPanel invalidWordList;
         
         /************************************************************************
          * EnteredWordsPanel Constructor
          * 
          * @return none
          * @param none
          ************************************************************************/
         public EnteredWordsPanel ()
         {
            BoxLayout boxLayout = new BoxLayout (this, BoxLayout.X_AXIS);
            setLayout (boxLayout);
            setBackground (BACKGROUND_COLOR);
            Dimension panelSize = new Dimension (450, 300);
            setPreferredSize (panelSize);
            
            humanWordList = new WordListPanel ("You");
            this.add (humanWordList);
            computerWordList = new WordListPanel ("Computer");
            this.add (computerWordList);
            commonWordList = new WordListPanel ("Common");
            this.add (commonWordList);
            invalidWordList = new WordListPanel ("Invalid");
            this.add (invalidWordList);
         }
         
         /************************************************************************
          * getSelectedWords - this method returns the words selected in the lists
          *       of words generated by the computer, entered by the user, and
          *       common between the computer and user.
          * 
          * @return String
          * @param none
          ************************************************************************/
         public String getSelectedWords ()
         {
            String selectedWords = humanWordList.getSelectedWords () + 
                  computerWordList.getSelectedWords () +
                  commonWordList.getSelectedWords ();
            
            return selectedWords;
         }
         
         /************************************************************************
          * resetWordLists - this method clears the word lists.
          * 
          * @return none
          * @param none
          ************************************************************************/
         public void resetWordLists ()
         {
            humanWordList.reset ();
            computerWordList.reset ();
            commonWordList.reset ();
            invalidWordList.reset ();
            
            this.paintImmediately (this.getVisibleRect());
         }
         
         /************************************************************************
          * updateWordLists - this method updates the word lists.
          * 
          * @return none
          * @param none
          ************************************************************************/
         public void updateWordLists ()
         {
            humanWordList.addWords (humanWordsFound);
            computerWordList.addWords (computerWordsFound);
            commonWordList.addWords (commonWordsFound);
            invalidWordList.addWords (invalidWordsFound);
            
            this.paintImmediately (this.getVisibleRect());
         }
                  
      }

      /************************************************************************
       * WordListPanel - this class provides a container for a single word list.
       * 
       * @author Alexander Anderson
       * @version 1.0 (December 5, 2012)
       * 
       * Acknowledgements: I acknowledge that I have neither given nor
       *          received assistance for this assignment except as noted below:
       * 
       * Modifications
       *          None
       ************************************************************************/
      private class WordListPanel extends JPanel
      {
         private static final long serialVersionUID = 1L;

         private final Dimension listSize = new Dimension (80, 300);
         
         private JList<String> wordJList = new JList<String> ();
         private ArrayList<String> wordList = new ArrayList<String>();
         private ArrayList<String> selectedWordList = new ArrayList<String>();
         
         /************************************************************************
          * WordListPanel Constructor
          * 
          * @return none
          * @param String, ArrayList<String>
          ************************************************************************/
         public WordListPanel (String title)
         {
            BoxLayout boxLayout = new BoxLayout (this, BoxLayout.Y_AXIS);
            setLayout (boxLayout);
            setBackground (BACKGROUND_COLOR);
            
            JLabel label = new JLabel (title);
            this.add (label);
            
            wordJList.addListSelectionListener (new ListSelectionListener ()
            {
               public void valueChanged (ListSelectionEvent listSelectionEvent)
               {
                  boolean adjusting = listSelectionEvent.getValueIsAdjusting();

                  if (!adjusting && !wordJList.isSelectionEmpty())
                  {
                     int selected[] = wordJList.getSelectedIndices ();
                     for (int i = 0; i < selected.length; i++)
                     {
                        String word = (String) wordJList.getModel ().getElementAt (selected[i]);
                        selectedWordList.add (word);
                     }
                  }
               }
            });
            
            JScrollPane computerScrollPane = new JScrollPane (wordJList);
            computerScrollPane.setPreferredSize (listSize);
            this.add (computerScrollPane);
         } // WordListPanel
         
         /************************************************************************
          * addWords - this method adds words to the list
          * 
          * @return none
          * @param StringSet
          ************************************************************************/
         public void addWords (StringSet words)
         {
            wordList = words.getWordList ();
            String[] wordArray = new String[wordList.size ()];
            
            for (int i = 0; i < wordList.size (); i++)
            {
               wordArray[i] = wordList.get (i);
            }
            wordJList.setListData (wordArray);
            this.paintImmediately (this.getVisibleRect());
         }
         
         /************************************************************************
          * getSelectedWords - this method returns selected words in the list.
          * 
          * @return none
          * @param String
          ************************************************************************/
         public String getSelectedWords ()
         {
            return selectedWordList.toString ();
         }
         
         /************************************************************************
          * reset - this method clear the word list.
          * 
          * @return none
          * @param none
          ************************************************************************/
         public void reset ()
         {
            wordJList.setListData (new String[0]);
            this.paintImmediately (this.getVisibleRect());
         }
      }
   } // ResultsPanel

   /***************************************************************************
    * ScoringPanel - This method provides a container for the user's and
    *       computer's scores. It also displays the round number, and allows 
    *       the user to restart a new game or quit the program. 
    *        
    * @author Alexander Anderson
    * @version 1.0 (December 5, 2012)
    * 
    * Acknowledgements: I acknowledge that I have neither given nor
    *          received assistance for this assignment except as noted below:
    * 
    * Modifications
    *          None
    ***************************************************************************/

   private class ScoringPanel extends JPanel
   {
      private static final long serialVersionUID = 1L;
      private JLabel roundValueLabel;
      private ScoreTable scoreTable;
      
      /************************************************************************
       * ScoringPanel Constructor
       * 
       * @return none
       * @param none
       ************************************************************************/
      public ScoringPanel ()  
      {
         BoxLayout boxLayout = new BoxLayout (this, BoxLayout.X_AXIS);
         setLayout (boxLayout);
         setBackground (BACKGROUND_COLOR);
         Dimension panelSize = new Dimension (750, 75);
         setPreferredSize (panelSize);
         
         setupPanels ();
         
         // Add Border
         TitledBorder title;
         title = BorderFactory.createTitledBorder (BorderFactory.createLoweredBevelBorder(), "Scoring");
         setBorder (title);
         
      } // ScoringPanel


      /*  public methods 
       *******************/

      /************************************************************************
       * newGame - this method resets the panel for a new game.
       * 
       * @return none
       * @param none
       ************************************************************************/
      public void newGame ()  
      {
         // Reset round to zero
         roundValueLabel.setText ("0");
         
         // Reset scores
         scoreTable.newGame ();
      }

      /************************************************************************
       * newRound - this method sets the panel when a new round is started.
       * 
       * @return none
       * @param none
       ************************************************************************/
      public void newRound ()  
      {
         // Update round label
         int currentRound = game.getRound ();
         roundValueLabel.setText (String.valueOf (currentRound));
         
         // Clear scores in the table
         scoreTable.newRound ();
         
         this.paintImmediately (this.getVisibleRect());
      }


      /************************************************************************
       * updateScores - this method updates the scores in the table.
       * 
       * @return none
       * @param int, int
       ************************************************************************/
      public void updateScores (int computerRoundScore, int humanRoundScore)  
      {
         // Update the round and total scores
         scoreTable.updateRoundScores (humanRoundScore, computerRoundScore);
         scoreTable.updateTotalScores ();
      } // updateScores

      /*  private methods
       *******************/

      /************************************************************************
       * setupPanel - this method creates the labels for the round and the
       *       scores, and creates the restart and quit buttons.
       * 
       * @return none
       * @param none
       ************************************************************************/
      private void setupPanels ()  
      {
         // *****  Add Round Labels ******
         add (Box.createRigidArea (new Dimension (20, 0)));
         JLabel roundLabel = new JLabel ("Round: ");
         this.add (roundLabel);
         roundValueLabel = new JLabel ("0");
         this.add (roundValueLabel);
         add (Box.createRigidArea (new Dimension (100, 0)));
         
         // *****  Add Scoring Panel ******
         scoreTable = new ScoreTable ();
         this.add (scoreTable);
         add (Box.createRigidArea (new Dimension (50, 0)));
         
         // ***** Add Restart Button ******
         setupRestartButton ();
         add (Box.createRigidArea (new Dimension (20, 0)));
         
         // ****** Add Quit Button *******
         setupQuitButton ();
         
      } // setupDifficultyRadioButtons

      /************************************************************************
       * setupQuitButton - this method create the quit button.
       * 
       * @return none
       * @param none
       ************************************************************************/
      private void setupQuitButton ()  
      {
         JButton quitButton = new JButton ("Quit");
         
         ActionListener quitListener = new ActionListener ()
         {
            public void actionPerformed (ActionEvent event)
            {
               quitGame ();
            }
         };
         quitButton.addActionListener (quitListener);
         
         add (quitButton);
         
      } // setupQuitButton
      

      /************************************************************************
       * setupRestartButton - this method creates the restart button.
       * 
       * @return none
       * @param none
       ************************************************************************/
      private void setupRestartButton ()  
      {
         JButton restartButton = new JButton ("Restart");
         
         ActionListener restartListener = new ActionListener ()
         {
            public void actionPerformed (ActionEvent event)
            {
               BoggleGUI.this.newGame ();
            }
         };
         restartButton.addActionListener (restartListener);
         
         add (restartButton);
         
      } // setupRestartButton

      
      /************************************************************************
       * ScoreTable - this class provides a panel container for the round and
       *       total scores.
       * 
       * @author Alexander Anderson
       * @version 1.0 (December 5, 2012)
       * 
       * Acknowledgements: I acknowledge that I have neither given nor
       *          received assistance for this assignment except as noted below:
       * 
       * Modifications
       *          None
       ************************************************************************/
      private class ScoreTable extends JPanel
      {
         private static final long serialVersionUID = 1L;
         private JLabel humanRoundScoreValueLabel;
         private JLabel humanTotalScoreValueLabel;
         private JLabel computerRoundScoreValueLabel;
         private JLabel computerTotalScoreValueLabel;
         
         /************************************************************************
          * ScoreTable - Constructor for the panel containing the score
          *       information.
          * 
          * @return none
          * @param none
          ************************************************************************/
         public ScoreTable ()
         {
            JLabel youLabel = new JLabel ("You:", JLabel.RIGHT);
            JLabel computerLabel = new JLabel ("Computer:", JLabel.RIGHT);
            JLabel roundScoreLabel = new JLabel ("Round Score", JLabel.CENTER);
            JLabel totalScoreLabel = new JLabel ("Total Score", JLabel.CENTER);
            humanRoundScoreValueLabel = new JLabel ("0", JLabel.CENTER);
            humanTotalScoreValueLabel = new JLabel ("0", JLabel.CENTER);
            computerRoundScoreValueLabel = new JLabel ("0", JLabel.CENTER);
            computerTotalScoreValueLabel = new JLabel ("0", JLabel.CENTER);
            
            setBackground (BACKGROUND_COLOR);         
            setLayout (new GridLayout (3, 3));
            
            add (new JLabel (""));
            add (roundScoreLabel);
            add (totalScoreLabel);
            add (youLabel);
            add (humanRoundScoreValueLabel);
            add (humanTotalScoreValueLabel);
            add (computerLabel);
            add (computerRoundScoreValueLabel);
            add (computerTotalScoreValueLabel);
         }

         /*  public methods 
          *******************/

         /************************************************************************
          * newGame - this method clears the scores when a new game is started.
          * 
          * @return none
          * @param none
          ************************************************************************/
         public void newGame ()  
         {
            // Reset scores
            humanRoundScoreValueLabel.setText ("0");
            humanTotalScoreValueLabel.setText ("0");
            computerRoundScoreValueLabel.setText ("0");
            computerTotalScoreValueLabel.setText ("0");
         }


         /************************************************************************
          * newRound - this method clears the round scores when a new round is 
          *       started.
          * 
          * @return none
          * @param none
          ************************************************************************/
         public void newRound ()  
         {
            // Clear round scores
            humanRoundScoreValueLabel.setText ("0");
            computerRoundScoreValueLabel.setText ("0");
         }

         /************************************************************************
          * updateRoundScores - this method updates the scores for this round.
          * 
          * @return none
          * @param int, int
          ************************************************************************/
         public void updateRoundScores (int humanRoundScore, int computerRoundScore)  
         {
            // Update score labels 
            humanRoundScoreValueLabel.setText (String.valueOf (humanRoundScore));
            computerRoundScoreValueLabel.setText 
                  (String.valueOf (computerRoundScore));
         }

         /************************************************************************
          * updateTotalScores - this method updates the total scores, and checks 
          *     to see if there is a winner.
          * 
          * @return none
          * @param none
          ************************************************************************/
         public void updateTotalScores ()  
         {
            // Update score labels 
            humanTotalScoreValueLabel.setText (String.valueOf (humanTotalScore));
            computerTotalScoreValueLabel.setText 
                  (String.valueOf (computerTotalScore));

            if (humanTotalScore >= pointsNeededToWin ||
                  computerTotalScore >= pointsNeededToWin)
            {
               BoggleGUI.this.gameOver (determineWinner (humanTotalScore, 
                     computerTotalScore));
            }
         }

         /*  private methods 
          *******************/

         /************************************************************************
          * determineWinner - This method determines whether the computer or the
          *       human won the Boggle game based on points, and returns the 
          *       string indicating who won.
          * 
          * @return String
          * @param int, int
          ************************************************************************/
         private String determineWinner (int humanPoints, int computerPoints)
         {
            String winner = "";

            if (humanPoints > computerPoints)
            {
               winner = "You win!";
            }
            else if (computerPoints > humanPoints)
            {
               winner = "The computer wins!";
            }
            else
            {
               winner = "It's a tie!";
            }
            return winner;
         }

      }
   } // ScoringPanel
   
} // BoggleGUI