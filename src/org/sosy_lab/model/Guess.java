package org.sosylab.model;

import java.util.Arrays;

/**
 * A guess made by the user including the evaluation of the guess.
 */
public class Guess {

  private final char[] guessedWord;
  private final GuessResult[] results;

  /**
   * Create a guess from the entered word and the results of the guess.
   *
   * @param guessedWord the initial guess
   * @param results     an array of results
   */
  Guess(char[] guessedWord, GuessResult[] results) {
    this.guessedWord = Arrays.copyOf(guessedWord, guessedWord.length);
    this.results = Arrays.copyOf(results, results.length);
  }

  /**
   * Return the guessed word.
   *
   * @return the guessed word
   */
  public char[] getGuessedWord() {
    return Arrays.copyOf(guessedWord, guessedWord.length);
  }

  /**
   * Return the guess result.
   *
   * @return the guess result
   */
  public GuessResult[] getResults() {
    return Arrays.copyOf(results, results.length);
  }

  /**
   * Check if this guess was the word searched for.
   *
   * @return if this guess won the game
   */
  boolean isWinner() {
    return Arrays.stream(results).allMatch(GuessResult.CORRECT::equals);
  }

  @Override
  public String toString() {
    //create a StringBuilder object "builder"
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < Game.NUMBER_OF_CHARS_IN_WORD; i++) {
      //if the letter is correct
      if (results [i] == GuessResult.CORRECT) {
        String y = "[" + guessedWord [i] + "]";
        builder.append(y);
      } else {
        //if the letter is contained
        if (results [i] == GuessResult.CONTAINED) {
          String y = "(" + guessedWord [i] + ")";
          builder.append(y);
        } else {
          //if the letter is wrong
          String y = " " + guessedWord [i] + " ";
          builder.append(y);
        }
      }
    }
    String updatedGuess = builder.toString();
    return updatedGuess;
  }

}
