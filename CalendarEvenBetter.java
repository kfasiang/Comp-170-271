public class CalendarEvenBetter {

    // Driver method to test the code
    public static void main(String[] args) {
        printCalendarMonth(6, 30);
        printCalendarMonth(3, 31);
        printCalendarMonth(1, 28);
        printCalendarMonth(3, 30);
        printCalendarMonth(7, 31);
    }

    // Method to print the calendar given the firstSunday and numberOfDays in the month.
    public static void printCalendarMonth (int firstSunday, int numberOfDays){
        printHeader();
        printBlankDaysBeg(firstSunday);
        printFirstWeek(firstSunday);
        printOtherWeeks(firstSunday, numberOfDays);
        printLastWeek(firstSunday, numberOfDays);
        printBlankDaysEnd(firstSunday, numberOfDays);
        printFooter();
    }

    // Prints the header and a line of - and +
    public static void printHeader(){
        System.out.println("  Sun    Mon    Tue    Wed    Thu    Fri    Sat   ");
        printFooter();
    }

    // Prints a line of - and +
    public static void printFooter(){
        System.out.println("+------".repeat(7) + "+");
    }

    /* Prints the number of blank days at the beginning of the calendar.
       It calculates how many of these to do by seeing how many days pass until the first digit is printed (7 - firstSunday +1)
       Then, we take this mod 7 to make 7 blanks, or a full empty week, equivalent to having no blank days at all
       while leaving the rest as is.
       We add the | to make the right formatting, but we print it (one time) only if first Sunday is greater than 1 */
    public static void printBlankDaysBeg(int firstSunday){
        int blanksAtBeginning = (8 - firstSunday) % 7;

        System.out.print("|      ".repeat( blanksAtBeginning ) + "|".repeat(Math.min(firstSunday-1, 1)));
    }

    // Prints the first week, taking into account the number of blanks at the beginning of the month
    public static void printFirstWeek(int firstSunday){
        for (int day = 1; day <= firstSunday - 1 ; day++) {
            System.out.print(padded(day, 5) + " |");
        } // Prints squares with dates in them, continuing on the same line as printBlankDays

        for (int j = firstSunday; j > 1; j = j - 100) {
            System.out.println();
        } // Only go to a new line if we printed anything in the first loop.
        // If not, our first week stars on Sunday and it will be taken care of by printOtherWeeks.
    }

    // Prints the more standard weeks
    public static void printOtherWeeks(int firstSunday, int numberOfDays){
        int numberOfRows = (int) Math.ceil((numberOfDays - firstSunday + 1) / 7.0);
        // Tells us the number of weeks we'll need, since if 7 doesn't divide evenly into our numberOfDays
        // we need to round it up to print the final, shorter week

        for (int week = 0; week < numberOfRows - 1 ; week++) {
            int firstDayOfWeek = firstSunday + (week * 7);
            // Adds 7 to firstSunday to give us the first day of each week
            int lastDayOfWeek = Math.min((firstSunday + (week * 7) + 6), numberOfDays);
            // The last day of the week is whichever is smaller, the total numberOfDays in the month
            // or the Saturday of that week. This takes care of what happens if the month ends in the middle of a week.

            System.out.print("|"); // left edge formatting

            for (int day = firstDayOfWeek; day <= lastDayOfWeek ; day++) {
                System.out.print(padded(day, 5) + " |");
            } // This loop prints the days in padded formatting

            System.out.println();

        }
    }

    //Prints the final week of the calendar
    public static void printLastWeek(int firstSunday, int numberOfDays){
        int numberOfRows = (int) Math.ceil((numberOfDays - firstSunday + 1) / 7.0);

        System.out.print("|"); // left edge formatting

        for (int day = firstSunday + ((numberOfRows - 1) * 7) ; day <= numberOfDays; day++) {
            System.out.print(padded(day, 5) + " |");
        }
    }


    // Prints the blank days at the end of the calendar.
    public static void printBlankDaysEnd(int firstSunday, int numberOfDays){
        int blanksAtBeginning = ((8 - firstSunday) % 7 );
        int numberOfRows2 = (int) Math.ceil((numberOfDays - firstSunday + 1 ) / 7.0) + Math.min(blanksAtBeginning, 1);
            /* This is just like our other int numberOfRows used earlier, except for the Math.min statement at the end
               which adds an additional row iff we have any blanks at the beginning.
               We need it to do this otherwise it will print one extra row at the bottom if the firstSunday is 1. */

        System.out.println("      |".repeat((numberOfRows2 * 7) - (numberOfDays + blanksAtBeginning)));
            /* The repeat statement comes from the fact that the total number of squares in the calendar (numberOfRows2 * 7)
               minus the total numberOfDays and blanksAtBeginning will tell us how many leftover squares we have at the end
               that need to be printed blank. */
    }

    //helper method provided by the book assignment
    public static String padded (int n, int width){
        String s = "" + n;
        for (int i = s.length(); i < width; i++) {
            s = " " + s;
        }
        return s;
    }


}
