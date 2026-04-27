import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class Hangman {

    // ─── ASCII art for the gallows (0 = fresh, 6 = game over) ───────────────────
    private static final String[] HANGMAN_STAGES = {
        // Stage 0
        "  +---+\n" +
        "  |   |\n" +
        "      |\n" +
        "      |\n" +
        "      |\n" +
        "      |\n" +
        "=========",
        // Stage 1
        "  +---+\n" +
        "  |   |\n" +
        "  O   |\n" +
        "      |\n" +
        "      |\n" +
        "      |\n" +
        "=========",
        // Stage 2
        "  +---+\n" +
        "  |   |\n" +
        "  O   |\n" +
        "  |   |\n" +
        "      |\n" +
        "      |\n" +
        "=========",
        // Stage 3
        "  +---+\n" +
        "  |   |\n" +
        "  O   |\n" +
        " /|   |\n" +
        "      |\n" +
        "      |\n" +
        "=========",
        // Stage 4
        "  +---+\n" +
        "  |   |\n" +
        "  O   |\n" +
        " /|\\  |\n" +
        "      |\n" +
        "      |\n" +
        "=========",
        // Stage 5
        "  +---+\n" +
        "  |   |\n" +
        "  O   |\n" +
        " /|\\  |\n" +
        " /    |\n" +
        "      |\n" +
        "=========",
        // Stage 6 – dead
        "  +---+\n" +
        "  |   |\n" +
        "  O   |\n" +
        " /|\\  |\n" +
        " / \\  |\n" +
        "      |\n" +
        "========="
    };

    private static final int MAX_WRONG = HANGMAN_STAGES.length - 1; // 6

    // ─── Word bank ───────────────────────────────────────────────────────────────
    private static final String[][] WORD_BANK = {
        // {word, hint}
        {"PROGRAMMING", "The act of writing code"},
        {"HANGMAN",     "The name of this game"},
        {"KEYBOARD",    "You use this to type"},
        {"ALGORITHM",   "Step-by-step problem-solving instructions"},
        {"VARIABLE",    "Stores a value in code"},
        {"INTERFACE",   "A contract in Java programming"},
        {"EXCEPTION",   "An error thrown at runtime"},
        {"COMPILER",    "Translates source code to bytecode"},
        {"DATABASE",    "Organized collection of data"},
        {"FRAMEWORK",   "A reusable software skeleton"},
        {"INHERITANCE", "OOP concept: child extends parent"},
        {"RECURSION",   "A function that calls itself"},
        {"POLYMORPHISM","One interface, many implementations"},
        {"ENCAPSULATION","Hiding internal object details"},
        {"ABSTRACTION", "Exposing only essential features"},
        {"JAVADOC",     "Java documentation tool"},
        {"MULTITHREADING","Running multiple threads concurrently"},
        {"SERIALIZATION","Converting an object to a byte stream"},
        {"GENERICS",    "Type-safe collections in Java"},
        {"ANNOTATION",  "Metadata added to Java code"},
    };

    // ─── Fields ──────────────────────────────────────────────────────────────────
    private final String word;
    private final String hint;
    private final Set<Character> guessedLetters = new LinkedHashSet<>();
    private int wrongGuesses = 0;

    // ─── Constructor ─────────────────────────────────────────────────────────────
    public Hangman(String[] entry) {
        this.word = entry[0].toUpperCase();
        this.hint = entry[1];
    }

    // ─── Core helpers ────────────────────────────────────────────────────────────
    /** Returns the word with un-guessed letters hidden as underscores. */
    private String getMaskedWord() {
        StringBuilder sb = new StringBuilder();
        for (char c : word.toCharArray()) {
            if (guessedLetters.contains(c)) {
                sb.append(c);
            } else {
                sb.append('_');
            }
            sb.append(' ');
        }
        return sb.toString().trim();
    }

    /** True when every letter has been guessed. */
    private boolean isWordGuessed() {
        for (char c : word.toCharArray()) {
            if (!guessedLetters.contains(c)) return false;
        }
        return true;
    }

    /** Processes a single-letter guess; returns feedback message. */
    private String processGuess(char letter) {
        final char upper = Character.toUpperCase(letter);
        if (guessedLetters.contains(upper)) {
            return "  ⚠  You already guessed '" + upper + "'. Try a different letter.";
        }
        guessedLetters.add(upper);
        if (word.indexOf(upper) >= 0) {
            long count = word.chars().filter(ch -> ch == upper).count();
            return "  ✔  '" + upper + "' is in the word! (" + count + " occurrence" + (count > 1 ? "s" : "") + ")";
        } else {
            wrongGuesses++;
            return "  ✘  '" + upper + "' is NOT in the word. (" + (MAX_WRONG - wrongGuesses) + " guesses left)";
        }
    }

    /** Renders the full game board to stdout. */
    private void printBoard(String feedback) {
        clearScreen();
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║              H A N G M A N               ║");
        System.out.println("╚══════════════════════════════════════════╝");
        System.out.println();
        System.out.println(HANGMAN_STAGES[wrongGuesses]);
        System.out.println();
        System.out.println("  Word  : " + getMaskedWord());
        System.out.println("  Hint  : " + hint);
        System.out.println("  Wrong : " + wrongGuesses + " / " + MAX_WRONG);
        System.out.println("  Used  : " + formatGuessed());
        System.out.println();
        if (feedback != null) System.out.println(feedback);
        System.out.println();
    }

    /** Formats the set of guessed letters nicely. */
    private String formatGuessed() {
        if (guessedLetters.isEmpty()) return "(none yet)";
        StringBuilder sb = new StringBuilder();
        for (char c : guessedLetters) {
            sb.append(c).append(' ');
        }
        return sb.toString().trim();
    }

    /** Clears the console (works in most terminals). */
    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    // ─── Main game loop ──────────────────────────────────────────────────────────
    public void play(Scanner scanner) {
        printBoard(null);

        while (!isWordGuessed() && wrongGuesses < MAX_WRONG) {
            System.out.print("  Enter a letter (or type WORD to guess the whole word): ");
            String input = scanner.nextLine().trim().toUpperCase();

            if (input.isEmpty()) {
                printBoard("  ⚠  Please enter a letter.");
                continue;
            }

            String feedback;

            if (input.equals("WORD")) {
                // Full-word guess
                System.out.print("  Enter your word guess: ");
                String wordGuess = scanner.nextLine().trim().toUpperCase();
                if (wordGuess.equals(word)) {
                    // Reveal all letters
                    for (char c : word.toCharArray()) guessedLetters.add(c);
                    feedback = "  🎉  Correct! You guessed the whole word!";
                } else {
                    wrongGuesses++;
                    feedback = "  ✘  Wrong word guess! (-1 life)";
                }
            } else if (input.length() == 1 && Character.isLetter(input.charAt(0))) {
                feedback = processGuess(input.charAt(0));
            } else {
                feedback = "  ⚠  Invalid input. Please enter a single letter.";
            }

            printBoard(feedback);
        }

        // ── End-of-game summary ──────────────────────────────────────────────────
        if (isWordGuessed()) {
            System.out.println("  ╔══════════════════════════════╗");
            System.out.println("  ║   🎉  YOU WIN!  Well done!   ║");
            System.out.println("  ╚══════════════════════════════╝");
        } else {
            System.out.println(HANGMAN_STAGES[MAX_WRONG]);
            System.out.println();
            System.out.println("  ╔══════════════════════════════╗");
            System.out.println("  ║   💀  GAME OVER!             ║");
            System.out.println("  ╚══════════════════════════════╝");
            System.out.println("  The word was: " + word);
        }
        System.out.println();
    }

    // ─── Entry point ─────────────────────────────────────────────────────────────
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random   = new Random();

        System.out.println("\n  Welcome to Hangman!");
        System.out.println("  ─────────────────────────");
        System.out.println("  Rules:");
        System.out.println("   • Guess the hidden word one letter at a time.");
        System.out.println("   • You may also type WORD to guess the whole word at once.");
        System.out.println("   • " + MAX_WRONG + " wrong guesses and it's game over!\n");

        boolean playAgain = true;
        while (playAgain) {
            // Pick a random word
            String[] entry = WORD_BANK[random.nextInt(WORD_BANK.length)];
            Hangman game = new Hangman(entry);
            game.play(scanner);

            System.out.print("  Play again? (Y / N): ");
            String again = scanner.nextLine().trim().toUpperCase();
            playAgain = again.startsWith("Y");
        }

        System.out.println("\n  Thanks for playing! Goodbye. 👋\n");
        scanner.close();
    }
}