package org.sosylab.model;

import static java.util.Objects.requireNonNull;

import java.util.HashSet;
import java.util.Set;

/**
 * The keyboard shows a player the remaining available letters.
 * If a letter turns out to not be part of a word, it gets removed from the
 * keyboard.
 */
class Keyboard {

  private static final String[] INITIAL_LAYOUT = {
      "q,w,e,r,t,z,u,i,o,p",
      "a,s,d,f,g,h,j,k,l",
      "y,x,c,v,b,n,m"
  };

  private final Set<String> deactivatedLetters;

  Keyboard() {
    deactivatedLetters = new HashSet<>();
  }

  /**
   * Update the remaining letters after a guess.
   *
   * @param guess the guess to be used for the update
   */
  void updateKeyboard(Guess guess) {
    requireNonNull(guess);
    Game.throwErrorIfInvalidWordSize(guess.getGuessedWord().length);

    Set<String> lettersToRemove = new HashSet<>();
    for (int i = 0; i < Game.NUMBER_OF_CHARS_IN_WORD; i++) {
      if (guess.getResults()[i] == GuessResult.WRONG) {
        lettersToRemove.add(String.valueOf(guess.getGuessedWord()[i]));
      }
    }

    for (String row : INITIAL_LAYOUT) {
      String[] letters = row.split(",");
      for (String letter : letters) {
        if (lettersToRemove.contains(letter)) {
          deactivatedLetters.add(letter);
        }
      }
    }
  }

  @Override
  public String toString() {
    // create a StringBuilder object "builder"
    StringBuilder builder = new StringBuilder();

    //divide the initial keyboard layout into three lines
    //append the initial keyboard layout to builder
    for (int i = 0; i < INITIAL_LAYOUT.length; i++) {
      String x = INITIAL_LAYOUT[i] + "\n";
      builder.append(x);
    }
    //remove the "," in the first line of the initial keyboard
    for (int i = 1; i < 19; i = i + 2) {
      builder.replace(i, i + 1, " ");
    }
    //remove the "," in the second line of the initial keyboard
    for (int i = 21; i < 37; i = i + 2) {
      builder.replace(i, i + 1, " ");
    }
    //remove the "," in the last line of the initial keyboard
    for (int i = 39; i < 51; i = i + 2) {
      builder.replace(i, i + 1, " ");
    }

    //remove letters that are wrong from the keyboard
    //check whether any letter should be removed
    //print initial keyboard if no letters should be removed
    if (deactivatedLetters.isEmpty()) {
      String keyBoardOutput = builder.toString();
      return keyBoardOutput;

    } else {
      //remove wrong letters from initial keyboard
      for (int i = 0; i < builder.length(); i++) {
        String deleteLetter = builder.substring(i, i + 1);
        if (deactivatedLetters.contains(deleteLetter)) {
          builder.replace(builder.indexOf(deleteLetter), builder.indexOf(deleteLetter) + 1, " ");
        }
      }
      //print updated keyboard
      String keyBoardOutput = builder.toString();
      return keyBoardOutput;
    }
  }
}

