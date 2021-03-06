package hangman;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;
import java.util.SortedSet;

public class EvilHangman {

    public static void main(String[] args) {
        EvilHangmanGame ehGame = new EvilHangmanGame();

        // Read the dictionary file into the instance of EvilHangmanGame
        try {
            File dictionary = new File(args[0]);
            int wordLength = Integer.parseInt(args[1]);
            ehGame.startGame(dictionary, wordLength);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(1);
        } catch (EmptyDictionaryException ex) {
            System.out.println(ex);
            System.exit(1);
        }

        Scanner input = new Scanner(System.in);
        int numGuesses = Integer.parseInt(args[2]);
        while (numGuesses > 0) {
            try {
                // Print # of guesses remaining
                if(numGuesses > 1) {
                    System.out.println("You have " + numGuesses + " guesses left");
                } else {
                    System.out.println("You have " + numGuesses + " guess left");
                }

                // Print alphabetized list of used guesses
                System.out.print("Used letters:");
                for(Character letter : ehGame.getGuessedLetters()) {
                    System.out.print(" " + letter);
                }
                System.out.println();

                // Show the current word pattern
                System.out.println("Word: " + ehGame.getPattern());

                // Prompt for guess
                System.out.print("Enter guess: ");
                String guess = input.next();

                // Check for valid input
                if (guess.isBlank()) {
                    throw new IllegalArgumentException("You must enter a letter as a guess");
                }
                if (!Character.isLetter(guess.charAt(0))) {
                    throw new IllegalArgumentException(guess.charAt(0) + " is not valid input. Please guess a letter.");
                }

                // Run makeGuess
                ehGame.makeGuess(guess.charAt(0));

                // Print whether the guess is in the word
                int guessOccurrences = ehGame.getNumOccurrences();
                if(guessOccurrences == 0) {
                    System.out.print("Sorry, there are no " + guess.charAt(0) + "'s");
                    numGuesses--;
                } else if (guessOccurrences == 1) {
                    System.out.print("Yes, there is " + guessOccurrences + " " + guess.charAt(0));
                } else {
                    System.out.print("Yes, there are " + guessOccurrences + " " + guess.charAt(0));
                }
            } catch (GuessAlreadyMadeException | IllegalArgumentException ex) {
                System.out.print(ex);
            }
            System.out.println("\n");

            // Exit if word has been guessed correctly
            if (!ehGame.getPattern().contains("_")) {
                numGuesses = 0;
            }
        }
        input.close();

        if (ehGame.getPattern().contains("_")) {
            System.out.println("You lose!");
        } else {
            System.out.println("You win!");
        }
        System.out.print("The word was: " + ehGame.getWord());
    }

}
