import java.util.Scanner;
// DO NOT import anything else

/**
 * This class forms Java Assignment 1, 2021
 */
public class StudentMarking
{

	/**
	 * The message that the main menu must display --- DO NOT CHANGE THIS
	 */
	public final String MENU_TEMPLATE =
			"%nWelcome to the Student System. Please enter an option 0 to 3%n"
					+ "0. Exit%n"
					+ "1. Generate a student ID%n"
					+ "2. Capture marks for students%n"
					+ "3. List student IDs and average mark%n";
	/**
	 * DO NOT CHANGE THIS
	 */
	public final String NOT_FOUND_TEMPLATE =
			"No student id of %s exists";


   /* Do NOT change the two templates ABOVE this comment.
      DO CHANGE the templates BELOW.
   */

	public final String ENTER_MARK_TEMPLATE = "Please enter mark %d for student %s%n";
	public final String STUDENT_ID_TEMPLATE = "%c%c%02d%c%c";
	public final String NAME_RESPONSE_TEMPLATE = "You entered a given name of %s and a surname of %s%nYour studID is %s";
	public final String LOW_HIGH_TEMPLATE = " %3d         %3d%n";
	public final String AVG_MARKS_TEMPLATE = "Student ***%s*** has an average of %5.02f%n";
	public final String COLUMN_1_TEMPLATE = "%c";
	public final String COLUMN_2_TEMPLATE = "%c";
	public final String CHART_KEY_TEMPLATE = "%s     %s%n";
	public final String REPORT_PER_STUD_TEMPLATE = "| Student ID   %d is %s | Average is %5.02f |%n";


	/**
	 * The controlling logic of the program. Creates and re-uses a {@link Scanner} that reads from {@link System#in}.
	 *
	 * @param args Command-line parameters (ignored)
	 */
	public static void main(String[] args)
	{
		// DO NOT change sc, sm, EXIT_CODE, and MAX_STUDENTS
		Scanner sc = new Scanner(System.in);
		StudentMarking sm = new StudentMarking();
		final int EXIT_CODE = 0;
		final int MAX_STUDENTS = 5;

		String[] keepStudId = new String[MAX_STUDENTS];
		double[] avgArray = new double[MAX_STUDENTS];
		int studCount = 0;
		int input;
		sm.displayMenu();
		
		while ((input = sc.nextInt()) != EXIT_CODE)
		{
			sc.nextLine();
			switch (input)
			{
				case 1:
				{
					if (studCount >= MAX_STUDENTS)
					{
						System.out.printf("You have reached the max size for students");
						break;
					}
					String studID = sm.generateStudId(sc);
					if (studID == null)
						break;
					keepStudId[studCount++] = studID;
				}
				break;
				case 2:
				{
					System.out.printf(
							"Please enter the studId to capture their marks (Enter 0 to return to main menu)%n");

					String studID = sc.nextLine();
					if (studID.charAt(0) == '0')
					{
						break;
					}

					findStudent:
					{
						for (int i = 0; i < studCount; i++)
						{
							if (keepStudId[i].equals(studID))
							{
								avgArray[i] = sm.captureMarks(sc, studID);
								break findStudent;
							}
						}
						System.out.printf(sm.NOT_FOUND_TEMPLATE, studID);
					}
				}
				break;
				case 3:
				{
					sm.reportPerStud(keepStudId, studCount, avgArray);
				}
				break;
				default:
				{
					// Handle invalid main menu input
					System.out.printf(
							"You have entered an invalid option. Enter 0, 1, 2 or 3%n");// Skeleton: keep, unchanged
					break;
				}
			}
			sm.displayMenu();
		}


		System.out.printf("Goodbye%n");
	}

	/**
	 * Creates a student ID based on user input
	 *
	 * @param sc Scanner reading {@link System#in} re-used from {@link StudentMarking#main(String[])}
	 * @return a studentID according to the pattern specified in {@link StudentMarking#STUDENT_ID_TEMPLATE}
	 */
	public String generateStudId(Scanner sc)
	{
		System.out.printf(
				"Please enter your given name and surname (Enter 0 to return to main menu)%n");
		String input = sc.nextLine();
		if (input.charAt(0) == '0') return null;
		String[] names = input.split(" ");
		String givenName = names[0];
		String familyName = names[names.length - 1];
		char givenInit = (char) (givenName.charAt(0) & ~(1 << 5));
		char familyInit = (char) (familyName.charAt(0) & ~(1 << 5));

		String studId = String.format(STUDENT_ID_TEMPLATE, givenInit, familyInit, familyName.length(), givenName.charAt(givenName.length() / 2), familyName.charAt((familyName.length() / 2)));
		System.out.printf(NAME_RESPONSE_TEMPLATE, givenName, familyName, studId);
		return studId;
	}

	/**
	 * Reads three marks (restricted to a floor and ceiling) for a student and returns their mean
	 *
	 * @param sc     Scanner reading {@link System#in} re-used from {@link StudentMarking#main(String[])}
	 * @param studId a well-formed ID created by {@link StudentMarking#generateStudId(Scanner)}
	 * @return the mean of the three marks entered for the student
	 */
	public double captureMarks(Scanner sc, String studId)
	{
		// DO NOT change MAX_MARK and MIN_MARK
		final int MAX_MARK = 100, MIN_MARK = 0;
		int low = Integer.MAX_VALUE, high = Integer.MIN_VALUE;
		int sum = 0;
		for (int i = 1; i <= 3; i++)
		{
			int mark;
			do
			{
				System.out.printf(ENTER_MARK_TEMPLATE, i, studId);
				mark = sc.nextInt();
				sc.nextLine();

			} while (mark > MAX_MARK || mark < MIN_MARK);
			sum += mark;
			if (mark > high)
				high = mark;
			if (mark < low)
				low = mark;
		}
		double avg = sum / (double) 3;

		System.out.printf("Student %s has a lowest mark of %d%nA highest mark of %d%n", studId, low, high);

		System.out.printf(AVG_MARKS_TEMPLATE, studId, avg);

		System.out.printf("Would you like to print a bar chart? [y/n]%n");
		char responseChart = sc.next().charAt((0));
		if (responseChart == 'y' || responseChart == 'Y')
		{
			printBarChart(studId, high, low);
		}

		return avg;
	}

	/**
	 * outputs a simple character-based vertical bar chart with 2 columns
	 *
	 * @param studId a well-formed ID created by {@link StudentMarking#generateStudId(Scanner)}
	 * @param high   a student's highest mark
	 * @param low    a student's lowest mark
	 */
	public void printBarChart(String studId, int high, int low)
	{
		System.out.printf("Student id statistics: %s%n", studId);
		final char chartChar = '*';
		for (int i = high; i > 0; i--)
		{
			if (i > low)
			{
				System.out.printf("   " + COLUMN_1_TEMPLATE + "%n", chartChar);
			} else
			{
				System.out.printf("   " + COLUMN_1_TEMPLATE + "           " + COLUMN_2_TEMPLATE + "%n", chartChar, ((i > low) ? ' ' : chartChar));
			}
		}
		System.out.printf(CHART_KEY_TEMPLATE, "Highest", "Lowest");
		System.out.printf(LOW_HIGH_TEMPLATE, high, low);
	}


	/**
	 * Prints a specially formatted report, one line per student
	 *
	 * @param studList student IDs originally generated by {@link StudentMarking#generateStudId(Scanner)}
	 * @param count    the total number of students in the system
	 * @param avgArray mean (average) marks
	 */
	public void reportPerStud(String[] studList,
	                          int count,
	                          double[] avgArray)
	{
		for (int student = 0; student < count; student++)
		{
			System.out.printf(REPORT_PER_STUD_TEMPLATE, student + 1, studList[student], avgArray[student]);
		}
	}

	/**
	 * The main menu
	 */
	public void displayMenu()
	{
		System.out.printf(MENU_TEMPLATE);
	}
}

/*
    TODO Before you submit:
         1. ensure your code compiles
         2. ensure your code does not print anything it is not supposed to
         3. ensure your code has not changed any of the class or method signatures from the skeleton code
         4. check the Problems tab for the specific types of problems listed in the assignment document
         5. reformat your code: Code > Reformat Code
         6. ensure your code still compiles (yes, again)
 */