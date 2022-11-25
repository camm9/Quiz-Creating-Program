import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MCQuestion extends Question {
	private List<String> options = new ArrayList<String>();
	private String answer;
	private String type = "MC";
	
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	protected void addTFQuestionToDB(Question tfQuestion) {
		// TODO Auto-generated method stub
		
	}
	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
	public  ArrayList<String> getOptions() {
		return (ArrayList<String>) options;
	}

	public void setOptions(ArrayList<String> options) {
		this.options = options;
	}

	public ArrayList<String> addOptions(String option) {
		// adds option to ArrayList Options
		ArrayList<String> options = getOptions();
		options.add(option);
		return options;
	}


	MCQuestion() {
		//default constructor
	}

	MCQuestion(String questionText, ArrayList<String> options, double point, String type) {
		this.questionText = questionText;
		this.options = options;
		setAnswer(answer);
		this.point = point;
		this.type = type;
	}

	public boolean checkAnswer(String answer) {
		boolean answerTrueOrFalse;
		// return true if answer is equal to MCQuestion answer
		if (answer == getCorrectAnswer()) {
			answerTrueOrFalse = true;
		} else {
			answerTrueOrFalse = false;
		}

		return answerTrueOrFalse;
	}

	public void addMCQuestionDB(Question mcQuestion) {
		//add multiple-choice questions to database
		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String msAccDB = "Question.accdb";
		String dbURL = "jdbc:ucanaccess://" + msAccDB;

		try {
			connection = DriverManager.getConnection(dbURL);
			statement = connection.createStatement();

			String Qtext = mcQuestion.getQuestionText();
			String Answer = "";

			for (int i = 0; i < ((MCQuestion) mcQuestion).getOptions().size(); i++) {// turns arraylist of options into a single string for database
				Answer += ((MCQuestion) mcQuestion).getOptions().get(i);
			}

			double Point = mcQuestion.getPoint();
			String type = "MC";
			String sqlStr = "INSERT INTO QUESTIONS (QText, Answer, Point, Type)" + " VALUES ('" + Qtext + "', '" + Answer
					+ "', '" + Point + "', '" + type + "')";
			
			statement.executeUpdate(sqlStr);


		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			connection.close();
			statement.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public String getCorrectAnswer() {
		// TODO Auto-generated method stub
		return null;
	}

	public static String getCorrectAnswer(Question question) {
		String correctAnswer = null;
		ArrayList<String> possibleAnswers = ((MCQuestion) question).getOptions();
		for (int j = 0; j < possibleAnswers.size(); j++) {
			if (possibleAnswers.get(j).indexOf("*") == 2) {
				correctAnswer = possibleAnswers.get(j);
			}
		}

		return correctAnswer;
	}

	public static double displayOptions(MCQuestion question) {
		//displays multiple-choice questions to preview tests
		char letterChar = 'A';
		ArrayList<String> possibleAnswers;
		double points = question.getPoint();
		possibleAnswers = question.getOptions();
		for (int j = 0; j < possibleAnswers.size(); j++) {
			//if (possibleAnswers.get(j).indexOf("*") == 2 || possibleAnswers.get(j).indexOf("#") != 2) {
			if (possibleAnswers.get(j).indexOf("*") == 2) {
				String str = possibleAnswers.get(j).substring(3);
				System.out.println(letterChar + ": " + str);
			} else if (possibleAnswers.get(j).indexOf("*") != 2 &&possibleAnswers.get(j).indexOf("#") == 0 ){
				String str = possibleAnswers.get(j).substring(2);
				System.out.println(letterChar + ": " + str);
			} else {
				System.out.println(letterChar + ": " + possibleAnswers.get(j));
			}
			
			letterChar++;
		}
		System.out.println("Enter your choice >> ");
		String input = scanner.nextLine();
		String correctAnswer = getCorrectAnswer(question);
		// match the letter options with the correct answer
		letterChar = 'A';
		for (int j = 0; j < possibleAnswers.size(); j++) {
			if (correctAnswer.equalsIgnoreCase(possibleAnswers.get(j))) {
				correctAnswer = Character.toString(letterChar);
			}
			letterChar++;
		}
		//checks if user inputed the correct answer
		if (input.equalsIgnoreCase(correctAnswer)) {
			System.out.println("You are correct!");
		} else {
			System.out.println("You are wrong. The correct answer is " + correctAnswer);
			points = 0;
		}
		return points;
	}


}
