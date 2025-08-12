package org.sosylab.model;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Manages a game of Wordle.
 */
public class Game {

  public static final int NUMBER_OF_GUESSES = 6;

  public static final int NUMBER_OF_CHARS_IN_WORD = 5;
  private final GameState state;
  private final List<Guess> guesses;
  private final Keyboard keyboard;
  private static final String EMPTY_GUESS = " _  _  _  _  _ ";

  /**
   * create a new game if the input from the user is "NEW".
   */
  public Game() {
    state = new GameState(WordProvider.drawRandomWord(), NUMBER_OF_GUESSES);
    guesses = new ArrayList<>(NUMBER_OF_GUESSES);
    keyboard = new Keyboard();
  }


  /**
   * create a new game with the user's input "wordToGuess" as the solution word.
   *
   * @param wordToGuess solution word set by user
   */
  public Game(String wordToGuess) {
    state = new GameState(wordToGuess, NUMBER_OF_GUESSES);
    guesses = new ArrayList<>(NUMBER_OF_GUESSES);
    keyboard = new Keyboard();
  }


  /**
   * Throw an {@link IllegalArgumentException} if the words has more or less numbers than required.
   *
   * @param numberChars The amount of characters for the word that is to be checked.
   */
  static void throwErrorIfInvalidWordSize(int numberChars) {
    if (numberChars != Game.NUMBER_OF_CHARS_IN_WORD) {
      throw new IllegalArgumentException(
          "Guessed word must consist of  exactly " + Game.NUMBER_OF_CHARS_IN_WORD + " characters");
    }
  }

  /**
   * Get the keyboard.
   *
   * @return the keyboard
   */
  public Keyboard getKeyboard() {
    return keyboard;
  }

  /**
   * Check whether the game is won.
   *
   * @return true if the game is won.
   */
  public boolean isGameWon() {
    return guesses.get(guesses.size() - 1).isWinner();
  }

  /**
   * Make a guess.
   *
   * @param word the word that was guessed.
   * @return a Guess instance if the guess was legal
   */

  public Optional<Guess> guessWord(String word) {

    //check whether input guess is legal
    //return a guess if legal
    if (WordProvider.isValidWord(word)) {

      //check input guess against solution word
      Optional<Guess> inputGuess = Optional.of(state.getSolutionWord().guessWord(word));

      //update the list of guesses
      guesses.add(inputGuess.get());

      for (int j = 0; j < guesses.size(); j++) {
        //loop through all guesses in the list
        Guess singleGuess = guesses.get(j);
        //if input guess matches the solution word
        //set game won
        if (singleGuess.isWinner()) {
          state.setGameWon();
        }

        //create a StringBuilder object "builder"
        //to change the format of guess according to guess results
        StringBuilder builder = new StringBuilder();
        String x = " ";
        builder.append(x);

        for (int i = 0; i < Game.NUMBER_OF_CHARS_IN_WORD; i++) {
          //change the format of guess if guess is correct
          if (singleGuess.getResults()[i] == GuessResult.CORRECT) {
            String y = "[" + singleGuess.getGuessedWord()[i] + "]";
            builder.append(y);
          } else {
            //change the format of guess if guess is contained
            if (singleGuess.getResults()[i] == GuessResult.CONTAINED) {
              String y = "(" + singleGuess.getGuessedWord()[i] + ")";
              builder.append(y);
            } else {
              //change the format of guess if guess is wrong
              String y = " " + singleGuess.getGuessedWord()[i] + " ";
              builder.append(y);
            }
          }
        }
        //print updated guess
        String updatedGuess = builder.toString();
        System.out.println(updatedGuess);
      }

      //update keyboard
      keyboard.updateKeyboard(inputGuess.get());

      //output reduced guess area
      if (!isGameWon()) {
        for (int k = 0; k < Game.NUMBER_OF_GUESSES - guesses.size(); k++) {
          System.out.println(EMPTY_GUESS);
        }
      }

      //if more than 6 wrong guesses, set game lost
      if (guesses.size() > Game.NUMBER_OF_GUESSES) {
        state.abortGame();

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < Game.NUMBER_OF_CHARS_IN_WORD; i++) {
          if (state.getSolutionWord().reveal().getResults()[i] == GuessResult.CORRECT) {
            String y = "[" + state.getSolutionWord().reveal().getGuessedWord()[i] + "]";
            builder.append(y);
          }
        }

        String outputSolution = builder.toString();
        System.out.println(outputSolution);

      }

      //return a guess instance of the guess is legal
      return inputGuess;
    } else {
      //return empty optional if the input guess is not legal
      return Optional.empty();
    }

  }

  /**
   * Get the list of guesses made by an user.
   *
   * @return the guesses made
   */

  public List<Guess> getUserGuesses() {
    return new ArrayList<>(guesses);
  }

  /**
   * Forfeit the game.
   */
  public void forfeit() {
    state.abortGame();

    StringBuilder builder = new StringBuilder();

    for (int i = 0; i < Game.NUMBER_OF_CHARS_IN_WORD; i++) {

      if (state.getSolutionWord().reveal().getResults()[i] == GuessResult.CORRECT) {
        String y = "[" + state.getSolutionWord().reveal().getGuessedWord()[i] + "]";
        builder.append(y);
      }
    }

    String outputSolution = builder.toString();
    System.out.println(outputSolution);

  }

  /**
   * Get the current game state.
   *
   * @return the game state
   */
  public GameState getState() {
    return state.createCopy();
  }
}
