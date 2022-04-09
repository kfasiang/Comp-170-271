import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

public class WheelOfFortune3 {

    // class constants for clarity
    public static final int INDEX_MATCHER = 33;
    public static final int SHORT_PAUSE = 1000;
    public static final int MED_PAUSE = 1750;
    public static final int LONG_PAUSE = 2500;
    public static final int LINES_IN_FILE = 113;
    public static final int VOWEL_COST = 250;

    // store the random number as a constant so it can be used throughout the program without changing
    public static final int REGULAR_PUZZLE_NUMBER = randomNumber();

    // driver method for the code
    public static void main(String[] args) throws FileNotFoundException, InterruptedException {
        welcome();
        int bank = regularGame();
        bonusRound(bank);
    }

    // this method prints a welcoming message to the user
    // timed out to give them time to read. Also includes an option for a more verbose explanation of rules.
    public static void welcome() throws InterruptedException, FileNotFoundException {
        Scanner console = new Scanner(System.in);

        System.out.println("\nWelcome to \"A Wheel-less Fortune Game!\"\n"); Thread.sleep(LONG_PAUSE);
        System.out.println("This game has one round where you attempt to fill in the blanks in the puzzle presented"); Thread.sleep(MED_PAUSE);
        System.out.println("You can choose to:"); Thread.sleep(MED_PAUSE);
        System.out.println("(1) spin the wheel and guess a consonant"); Thread.sleep(MED_PAUSE);
        System.out.println("(2) buy a vowel for $250, or"); Thread.sleep(MED_PAUSE);
        System.out.println("(3) solve the puzzle"); Thread.sleep(MED_PAUSE);
        System.out.print("For more rules, type \"rules\". Otherwise, type \"go\" to begin! ");
        String decision = console.next();
        while (!decision.equalsIgnoreCase("RULES") && !decision.equalsIgnoreCase("GO")){
            System.out.println("Invalid input, please try again!");
            System.out.print("For more rules, type \"rules\". Otherwise, type \"go\" to begin! ");
            decision = console.next();
        } if (decision.equalsIgnoreCase("RULES")) {
            Scanner readFile = new Scanner(new File("WoFMoreRules.txt")); // read file to print rules
            while (readFile.hasNextLine()){
                System.out.println(readFile.nextLine()); // ends with a prompt to begin game
            }
            decision = console.next();
            while (!decision.equalsIgnoreCase("GO")){
                System.out.println("Invalid input, please try again!");
                System.out.print("Type \"go\" to start the game!");
                decision = console.next();
            }
        }
        System.out.println("\nLet's take a look at our puzzle!\n"); Thread.sleep(MED_PAUSE);
    }

    // pick a random number to correspond to a line number in the puzzle bank (WheelOfFortune.txt file)
    public static int randomNumber() {
        Random newPuzzle2 = new Random();
        return newPuzzle2.nextInt(LINES_IN_FILE);
    }

    // this method uses the randomly generated puzzle number to scan through the file full of puzzles
    // and returns the puzzle we end up with
    public static String pickPuzzle (int puzzleNumber) throws FileNotFoundException {
        Scanner input = new Scanner(new File("WheelOfFortune.txt"));
        String thisPuzzle2 = input.nextLine();
        int count = 0;

        while (count < puzzleNumber && input.hasNext()){ // scan through file until we reach desired line number
            thisPuzzle2 = input.nextLine();
            count ++;
        }
        return thisPuzzle2.substring(1); // cuts off the first character, the category identifier.
    }

    // this method takes an input of the puzzle number and scans through the file full of puzzles again,
    // this time looking for a character at the beginning of the line and using that to return the category
    public static String category (int puzzleNumber) throws FileNotFoundException {
        Scanner readFile = new Scanner(new File("WheelOfFortune.txt"));
        String line = readFile.nextLine();
        int count = 0;
        while (count < puzzleNumber && readFile.hasNext()){ // scan through the file until we reach the right line
            line = readFile.nextLine();
            count ++;
        }
        // we look at the first character and use its ASCII number - 33 to begin the counting at 0, which allows us
        // to match directly to the index of a category in the array
        int symbol = line.charAt(0);
        String [] category = {"Thing", "Living Thing", "Phrase", "Place", "Food and Drink", "Song Lyrics", "Person",
                "What Are You Doing?", "Movie Title", "Event", "Fun and Games"};
        // the ASCII symbols go sequentially from 33-43, so subtracting 33 lines them up with the indices of the array
        return category[symbol - INDEX_MATCHER];
    }

    // this method converts the chosen puzzle into underscores (_) and spaces ( ).
    public static String createBlankPuzzle(String puzzleToBlank) {
        String blankPuzzle = "";
        for (int i = 0; i < puzzleToBlank.length(); i++) {
            if (puzzleToBlank.charAt(i) == 32) {
                blankPuzzle = blankPuzzle + " ";
            } else {
                blankPuzzle = blankPuzzle + "_";
            }
        }
        return blankPuzzle;
    }

    /* this method is the main driver for the regular game
       it accepts blankPuzzle as its initial workingPuzzle which will be updated with each
       cycle through the while loop.
    The basic logic structure is:
    while the puzzle hasn't been solved {
        ask whether the user would like to spin (if there are consonants left)
        buy a vowel (if they have the money AND there are vowels left)
        or solve
        if SPIN {
            read in their guess
            update the working puzzle, used letter board, and bank accordingly
       } if SOLVE {
            read in their solution
            if it matches, the puzzle if solved
            else the puzzle is not solved, and we go through the loop again
       } if VOWEL {
            read in their guess
            update the working puzzle, used letter board, and bank accordingly
       }
     */
    public static int regularGame() throws FileNotFoundException, InterruptedException {
        Scanner console = new Scanner(System.in);

        // establish variables for the while loop
        boolean puzzleSolved = false;
        int bank = 0;
        String usedLetterBoard = "";
        String regularPuzzle = pickPuzzle(REGULAR_PUZZLE_NUMBER);
        String workingPuzzle = createBlankPuzzle(regularPuzzle);

        // present the blank puzzle and category
        System.out.println(workingPuzzle); Thread.sleep(MED_PAUSE);
        System.out.println("The category is: " + category(REGULAR_PUZZLE_NUMBER) + "\n");

        while (!puzzleSolved) {

            // prompt questions
            Thread.sleep(SHORT_PAUSE);
            if (moreConsonants(workingPuzzle, regularPuzzle)) {
                System.out.println("Would you like to spin the wheel (type \"spin\")");
            } else { // there are no more consonants, no option to spin
                System.out.println("NO MORE CONSONANTS!");
            }
            if (bank >= VOWEL_COST){ // only let the user see + use this option if have enough money and there are vowels left
                if (noMoreVowels(workingPuzzle, regularPuzzle)){ // there are no more vowels, no vowel option
                    System.out.println("NO MORE VOWELS!");
                } else { // otherwise they have enough money and there are vowel left, so present the option
                    System.out.println("buy a vowel for $250 (type \"vowel\")");
                }
            } // always allow the user to choose to solve
            System.out.print("or solve the puzzle (type \"solve\")? ");
            String decision = console.next(); // read in the decision
            System.out.println();

            if (decision.equalsIgnoreCase("SPIN") && moreConsonants(workingPuzzle, regularPuzzle)) {
                // this branch is only followed if the decision is SPIN and there are still consonants to be guessed
                int thisSpin = spin(); // spin the wheel

                // report the spin and prompt for guess
                if (thisSpin == 0) { // if landed on bankrupt
                    System.out.println("You landed on bankrupt!"); Thread.sleep(MED_PAUSE);
                    bank = 0;
                } else {
                    System.out.println("You landed on $" + thisSpin); Thread.sleep(SHORT_PAUSE);
                    System.out.print("What is your guess? ");

                    // read in the guess
                    char guessedLetter = console.next().toUpperCase().charAt(0);

                    // as long as the input is a vowel, prompt to try again
                    while (isVowel(guessedLetter)){
                        System.out.println("Not a consonant; try again!");
                        System.out.print("What is your guess? ");
                        guessedLetter = console.next().toUpperCase().charAt(0);
                    }

                    // updates the working puzzle, count, and used letter board
                    workingPuzzle = guess(workingPuzzle, guessedLetter, usedLetterBoard, regularPuzzle);
                    int count = howManyInPuzzle(guessedLetter, usedLetterBoard);
                    usedLetterBoard = usedLetterBoard + guessedLetter + " ";

                    // update bank to keep track of the user's money
                    if (thisSpin > 0 && count > 0) { // guessed char was in the puzzle
                        bank = bank + (count * thisSpin);
                    } else { // guessed value was not in the puzzle, lose amount of the spin from bank
                        bank = Math.max(bank - (thisSpin), 0); // makes sure bank never goes negative
                    }
                }
                System.out.println("You now have $" + bank); Thread.sleep(SHORT_PAUSE);

            } else if (decision.equalsIgnoreCase("VOWEL") && bank >= VOWEL_COST && !noMoreVowels(workingPuzzle, regularPuzzle)){
                // this branch is only followed if the decision was VOWEL, they have enough money, and there are still vowels to be guessed
                System.out.print("What vowel would you like to purchase? ");
                char guessedLetter = console.next().toUpperCase().charAt(0);

                // ensures a vowel was guessed
                while (!isVowel(guessedLetter)){
                    System.out.println("Not a vowel; try again!");
                    System.out.print("What vowel would you like to purchase? ");
                    guessedLetter = console.next().toUpperCase().charAt(0);
                }
                // update working puzzle, count, and used letter board
                workingPuzzle = guess(workingPuzzle, guessedLetter, usedLetterBoard, regularPuzzle);
                howManyInPuzzle(guessedLetter, usedLetterBoard);
                usedLetterBoard = usedLetterBoard + guessedLetter + " ";

                // update and report bank, subtract 250 (cost of vowel) no matter what
                bank = bank - VOWEL_COST;
                System.out.println("You now have $" + bank); Thread.sleep(SHORT_PAUSE);

            } else if (decision.equalsIgnoreCase("SOLVE")) {
                puzzleSolved = solvePuzzle(regularPuzzle); // method returns true if their solution is right, false otherwise
                if (!puzzleSolved){
                    System.out.println("Sorry, that was incorrect."); Thread.sleep(MED_PAUSE);
                } else {
                    workingPuzzle = regularPuzzle;
                }

            } else { // if the input wasn't one of those 3 things above, keep asking the user to try again
                System.out.println("Invalid input. Please try again!");
            }
            // report working puzzle and used letter board
            System.out.println("\n" + workingPuzzle); Thread.sleep(SHORT_PAUSE);
            System.out.println("\nUsed Letter Board: " + usedLetterBoard + "\n");
            System.out.println();

        } // end while loop, puzzle has been solved
        System.out.println("Congratulations! You have solved the puzzle!");
        System.out.println();
        System.out.println("Your total earnings were $" + bank);

        return bank;
    } // end method regularGameMethod

    // if the user decides to solve the puzzle, execute this method
    public static boolean solvePuzzle(String puzzle) {
        System.out.print("Solve the puzzle: ");
        Scanner console = new Scanner(System.in);
        String solution = console.nextLine();
        return solution.equalsIgnoreCase(puzzle);
    }

    // helper method which determines if a guessed letter was a vowel or not
    public static boolean isVowel (char guessedLetter){
        return (guessedLetter == 'A' || guessedLetter == 'E' || guessedLetter =='I' ||
                guessedLetter == 'O' || guessedLetter == 'U');
    }

    // This method takes in the current workingPuzzle and guessedLetter,
    // then updates the working puzzle accordingly and returns the new version of it.
    public static String guess(String workingPuzzle, char guessedLetter, String usedLetterBoard, String completePuzzle) {
        if (usedLetterBoard.contains("" + guessedLetter)) { // the letter has been guessed already, exit
            System.out.println("This letter has been guessed already.");
            return workingPuzzle;
        }
        for (int i = 0; i < completePuzzle.length(); i++) { // scan through completePuzzle and update working puzzle
            if (completePuzzle.charAt(i) == guessedLetter) {
                workingPuzzle = workingPuzzle.substring(0, i) + guessedLetter + workingPuzzle.substring(i + 1);
            }
        }
        return workingPuzzle;
    }

    // this method determines whether there are any more vowels in the puzzle
    // by looking for vowels in the completed puzzle and seeing if there are any
    // characters in the same position in the working puzzle that are not vowels--
    // meaning there are still some vowels left to be guessed!
    public static boolean noMoreVowels (String workingPuzzle, String finalPuzzle){
        for (int i = 0; i < finalPuzzle.length(); i++) {
            if (isVowel(finalPuzzle.charAt(i))){
                if (!isVowel(workingPuzzle.charAt(i))) {
                    return false;
                }
            }
        }
        return true;
    }

    // works much like the noMoreVowels method above, but with consonants
    public static boolean moreConsonants(String workingPuzzle, String finalPuzzle){
        for (int i = 0; i < finalPuzzle.length(); i++) {
            if (!isVowel(finalPuzzle.charAt(i))){
                if ((workingPuzzle.charAt(i) == '_')) {
                    return true;
                }
            }
        }
        return false;
    }

    // this method takes in the guessedLetter and returns how many times that letter appears in the puzzle.
    public static int howManyInPuzzle (char guessedLetter, String usedLetterBoard) throws FileNotFoundException {
        int count = 0;
        String completePuzzle = pickPuzzle(REGULAR_PUZZLE_NUMBER);
        if (usedLetterBoard.contains("" + guessedLetter)){ // there are none to be added to the puzzle, return 0
            return count;
        }
        for (int i = 0; i < completePuzzle.length(); i++) { // scan through complete puzzle and count how many times the guessedLetter appears
            if (completePuzzle.charAt(i) == guessedLetter) {
                count++;
            }
        }
        System.out.println("\nThere are " + count + " " + guessedLetter + "'s.");
        return count;
    }

    // method to generate a spun amount based on (slightly tweaked) but real wheel of fortune
    // wheel values. It uses a random number generator and an array.
    public static int spin(){
        Random wheel = new Random();
        int[] potentialMoney = {500, 500, 500, 500, 550, 550, 550, 600, 600, 600, 650, 650, 650, 700, 700, 700,
                800, 900, 2500, 0, 0, 0, 0};
        int spin = wheel.nextInt(potentialMoney.length);
        return potentialMoney[spin];
    }

    // The bonus round method takes a parameter of bank which was built in the regular round in order to double it
    // if the user correctly solves this puzzle.
    // The round has a new puzzle that gets filled with R, S, T, L, N, and E, and the user guesses 3 more consonants
    // and 1 more vowel. Then, they are prompted to solve.
    public static void bonusRound(int bank) throws FileNotFoundException, InterruptedException {
        // setting up Scanner and variables
        Scanner console = new Scanner (System.in);
        int bonusPuzzleNumber = randomNumber();
        String bonusPuzzle = pickPuzzle(bonusPuzzleNumber);

        // welcome the user to the bonus round
        System.out.println("\n\nWelcome to the bonus round. If you solve the puzzle correctly,"); Thread.sleep(MED_PAUSE);
        System.out.println("you can double your money! Otherwise, your bank will remain as it is."); Thread.sleep(MED_PAUSE);
        System.out.println("Let's take a look at our puzzle."); Thread.sleep(MED_PAUSE);
        System.out.println("\nThe category is: " + category(bonusPuzzleNumber)); Thread.sleep(MED_PAUSE);

        // establish and print the working (currently blank) puzzle
        String workingPuzzle = createBlankPuzzle(bonusPuzzle);
        System.out.println(workingPuzzle); Thread.sleep(MED_PAUSE);

        // set up array and traverse it to update working puzzle with the standard bonus round letters
        char [] bonusLetters = {'R', 'S', 'T', 'L', 'N', 'E'};
        for (char bonusLetter : bonusLetters) {
            workingPuzzle = guess(workingPuzzle, bonusLetter, "", bonusPuzzle);
        }
        // print the newly filled puzzle and prompt for consonants and vowels
        System.out.println("\nHere is the puzzle with R, S, T, L, N, and E guessed:\n");
        System.out.println(workingPuzzle); Thread.sleep(MED_PAUSE);
        System.out.println("\nFirst, guess 3 consonants.");

        for (int i = 0; i < 3; i++) { // prompt for 3 consonants, updating working puzzle each time
            System.out.print("Consonant " + (i + 1) + ": ");
            char consonant = console.next().toUpperCase().charAt(0);
            while (isVowel(consonant)) { // ensures guess is a consonant
                System.out.println("Invalid input, please try again.");
                System.out.print("Consonant " + (i + 1) + ": ");
                consonant = console.next().toUpperCase().charAt(0);
            }
            workingPuzzle = guess(workingPuzzle, consonant, "", bonusPuzzle);
        }
        System.out.print("Now, guess a vowel: "); // prompt for a vowel
        char vowel = console.next().toUpperCase().charAt(0);
        while (!isVowel(vowel)){ // ensures guess is a vowel
            System.out.println("Invalid input, please try again.");
            System.out.print("Guess a vowel: ");
            vowel = console.next().toUpperCase().charAt(0);
        }
        workingPuzzle = guess(workingPuzzle, vowel, "", bonusPuzzle);
        // present the final working puzzle
        System.out.println("Here is your final puzzle. Good luck!\n");
        System.out.println(workingPuzzle);
        if (solvePuzzle(bonusPuzzle)){ // prompt for and evaluate solution
            System.out.println(bonusPuzzle);
            System.out.println("\nCongratulations!! Your earnings are now $" + (bank * 2) + "!!");
        } else {
            System.out.println("\nSorry, the correct answer was " + bonusPuzzle);
        }
        System.out.println("\n\nThank you for playing!");
    }
}
