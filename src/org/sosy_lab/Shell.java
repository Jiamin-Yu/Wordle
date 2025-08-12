package org.sosylab;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.sosylab.model.Game;
import org.sosylab.model.Phase;


/**
 * The Shell manages the interaction between users and the wordle game.
 */
public class Shell {
  private static final String PROMPT = "Wordle> ";

  private static final String EMPTY_GUESS = " _  _  _  _  _ ";
  private Game game;
  private boolean quit;

  /**
   * Start the shell.
   *
   * @throws IOException if an error occurred during an input-output operation.
   * */
  public void run() throws IOException {
    BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
    execute(stdin);
  }

  /**
   * manage inputs and outputs of the shell.
   *
   * @param stdin the BufferedReader that is executed.
   * @throws IOException if an error occurred during an input-output operation.
   */

  private void execute(BufferedReader stdin) throws IOException {
    quit = false;

    while (!quit) {
      //output the prompt to console
      System.out.print(PROMPT);
      //wait for user's input
      String input = stdin.readLine();

      //if no more input from the user, stop the execution
      if (input == null) {
        break;
      }

      //slice the input string on one or more white spaces
      String[] subStrings = input.trim().split("\\s+");

      //assign the first part(before the first white space) of the input string
      // to the variable "command"
      String command = subStrings[0];

      //check what the input command is
      //call corresponding command method
      switch (command) {
        case "NEW":
          commandNew(subStrings);
          break;
        case "G":
          commandG(subStrings);
          break;
        case "FORFEIT":
          commandForfeit(subStrings);
          break;
        case "QUIT":
          commandQuit(subStrings);
          break;
        //if not a valid command, output error message to console
        default:
          System.out.println("Error! Command not found!");
          break;
      }
    }
  }

  private void commandNew(String[] subStrings) {
    //check whether a game is currently active
    //output error message "game already active" if yes
    if (game != null && game.getState().getCurrentPhase() == Phase.RUNNING) {
      System.out.println("Error! Game already active!");
    } else {
      //check whether input starting with "NEW" has more than one argument
      //output error message "too many arguments" if yes
      if (subStrings.length > 2) {
        System.out.println("Error! Too many arguments for command");
      } else { //if no, input has either one or no argument
        //if input has one argument
        if (subStrings.length == 2) {
          //check whether the argument has exactly 5 letters
          //output error message "invalid word to guess" if not
          if (subStrings[1].length() != Game.NUMBER_OF_CHARS_IN_WORD) {
            System.out.println("Error! Invalid word to guess!");
          } else {
            //new a game with the valid argument as the solution of the game
            game = new Game(subStrings[1]);
            //output guess area
            for (int i = 0; i < Game.NUMBER_OF_GUESSES; i++) {
              System.out.println(EMPTY_GUESS);
            }
            //output keyboard area
            System.out.println(game.getKeyboard());
          }
        } else {
          //if input has no argument, new a game
          game = new Game();
          //output guess area
          for (int i = 0; i < Game.NUMBER_OF_GUESSES; i++) {
            System.out.println(EMPTY_GUESS);
          }
          //output keyboard area
          System.out.println(game.getKeyboard());
        }
      }
    }
  }

  private void commandG(String[] subStrings) {
    //check whether there is an active game
    //if no active game, print error message "no active game"
    if (game == null || game.getState().getCurrentPhase() != Phase.RUNNING) {
      System.out.println("Error! No active game!");
    } else {
      //check whether a guess is provided by the user
      //print error message "no guess provided" if not
      if (subStrings.length == 1) {
        System.out.println("Error! No guess provided!");
      } else {
        //check whether more than one argument is passed to G command
        //print error message "too many arguments" if so
        if (subStrings.length > 2) {
          System.out.println("Error! Too many arguments for command \"GUESS\"");
        } else {
          //check whether the guess has exactly five letters
          //print error message "invalid guess" if not
          if (subStrings[1].length() != Game.NUMBER_OF_CHARS_IN_WORD) {
            System.out.println("Error! Invalid guess!");
          } else {
            //command is correct and output the guess
            game.guessWord(subStrings[1]);
            //output keyboard area
            if (!game.isGameWon()) {
              System.out.println(game.getKeyboard());
            }

          }
        }
      }
    }


  }

  private void commandForfeit(String[] subStrings) {
    //check whether there is an active game
    //print error message "no active game" if no
    if (game == null || game.getState().getCurrentPhase() != Phase.RUNNING) {
      System.out.println("Error! No active game!");
    } else {
      //check whether forfeit command has arguments
      //output error message "too many arguments" if yes
      if (subStrings.length > 1) {
        System.out.println("Error! Too many arguments for command \"FORFEIT\"");
      } else {
        //forfeit the game
        for (int i = 0; i < game.getUserGuesses().size(); i++) {
          System.out.println(game.getUserGuesses().get(i));
        }
        game.forfeit();
      }
    }

  }

  private void commandQuit(String[] subStrings) {
    if (subStrings.length == 1) {
      quit = true;
    } else {
      //output error message "too many arguments"
      //if an argument is passed to the command quit
      System.out.println("Error! Too many arguments for command \"QUIT\".");
    }

  }

}
