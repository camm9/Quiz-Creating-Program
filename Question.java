
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public abstract class Question {
	// Represents the generic form of a question

	static Connection connection = null;
	static Statement statement = null;
	static ResultSet resultSet = null;

	static Scanner scanner = new Scanner(System.in);

	protected String questionText;
	protected double point;
	private String type;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getQuestionText() {
		return questionText;
	}

	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}

	public double getPoint() {
		return point;
	}

	public void setPoint(double point) {
		this.point = point;
	}

	Question() {
		// default constructor

	}
	protected abstract void addTFQuestionToDB(Question tfQuestion);
	
	protected abstract void addMCQuestionDB(Question mcQuestion);
	
	public abstract boolean checkAnswer(String answer);

	public abstract String getCorrectAnswer();

	public static void displayMenu() {
		// This method displays the user interface that will help the user create their quiz.

		String input = null;
		Boolean exit = false;
		ArrayList<Question> questions = new ArrayList<Question>(); //this will store the sessions created questions
		
		while (exit != true) {
			System.out.println("Please choose (c)reate a question, (p)review or (e)xit >>");
			input = scanner.nextLine();
			if (input.equalsIgnoreCase("c")) {
				// MC or TF question will be created and then saved to Access database
				System.out.println("Enter the type of question (MC or TF) >> ");
				String questionInput = scanner.nextLine();
				String questionText = "";
				if (questionInput.equalsIgnoreCase("mc")) {

					System.out.println("Enter the question text >>");
					questionText = scanner.nextLine();
					System.out.println("How many options? ");
					int numOfOptions = Integer.parseInt(scanner.nextLine());
					char letterChar = 'A';
					ArrayList<String> optionsList = new ArrayList<String>();
					for (int i = 0; i <= numOfOptions - 1; i++) {
						System.out.println("Enter Option " + letterChar + " (Start with * for correct answer) >>");
						String option = scanner.nextLine();
						//save incorrect answers with # and correct answers with * 
						if (option.indexOf("#") != 0) {
							if (option.indexOf("*") != 0) {
								option = "#"+option;
							}
						}
						optionsList.add(option);
						letterChar++;
					}

					System.out.println("How many points? ");
					double point = Double.parseDouble(scanner.nextLine());
					String type = "MC";
					Question mcQuestion = new MCQuestion(questionText, optionsList, point, type);
					questions.add(mcQuestion);
					//add question to database
					mcQuestion.addMCQuestionDB(mcQuestion);

				}

				if (questionInput.equalsIgnoreCase("tf")) {
					boolean answer;
					System.out.println("Enter the question text >>");
					questionText = scanner.nextLine();
					System.out.println("Answer is True or False? ");
					String answerTF = scanner.nextLine();
					if (answerTF.equalsIgnoreCase("true")) {
						answer = true;
					} else {
						answer = false;
					}
					System.out.println("How many points? ");
					double point = Double.parseDouble(scanner.nextLine());
					String type = "TF";
					Question tfQuestion = new TFQuestion(questionText, answer, point, type);
					questions.add(tfQuestion);
					//add question to database
					tfQuestion.addTFQuestionToDB(tfQuestion);
				}

			}
			if (input.equalsIgnoreCase("p")) {
				//user does the test they just created
				previewTest(questions);
			}
			if (input.equalsIgnoreCase("e")) {
				System.out.println("Goodbye!");
				System.exit(0);
			}
		}
	}

	public static void previewTest(ArrayList<Question> questions) {
		//this method allows the user to try the quiz they have created and receive a score
		char letterChar = 'A';
		ArrayList<String> possibleAnswers;
		double totalPoints = 0;

		for (int i = 0; i < questions.size(); i++) {
			System.out.println(questions.get(i).getQuestionText() + " (" + questions.get(i).getPoint() + " Points)");
			String questionType = questions.get(i).getType();
			if (questionType.equalsIgnoreCase("mc")) {
				double mcPoints = MCQuestion.displayOptions((MCQuestion) questions.get(i));
				totalPoints += mcPoints;
			}
			
			if (questionType.equalsIgnoreCase("tf")) {
				double tfPoints = TFQuestion.displayTF(questions.get(i));
				totalPoints += tfPoints;
			}

		}
		System.out.println("The quiz ends. Your score is " + totalPoints + ".");
	}

}