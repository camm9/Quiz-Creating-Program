

import java.sql.DriverManager;
import java.sql.SQLException;

public class TFQuestion extends Question {
	private boolean answer;
	private String type = "TF";
	

	public boolean getAnswer() {
		return answer;
	}

	public void setAnswer(boolean answer) {
		this.answer = answer;
	}
	
	public String getCorrectAnswer() {
		String correctAnswer = null;
		return correctAnswer;
	}

	@Override
	protected void addMCQuestionDB(Question mcQuestion) {
		// TODO Auto-generated method stub
		
	}
	
	TFQuestion(){
		//default constructor
	}
	
	TFQuestion(String questionText, boolean answer, double point, String type){
		super.questionText = questionText;
		this.answer = answer;
		super.point = point;
		this.type = type;
	}
	

	
	public static double displayTF(Question question) {
		System.out.println("True(T) or False(F) >> ");
		String answer = scanner.nextLine();
		//get correct answer
		double points = question.getPoint();
		boolean checkingAnswer = question.checkAnswer(answer);
		boolean correctAnswer = ((TFQuestion) question).getAnswer();
		if(checkingAnswer == correctAnswer) {
			System.out.println("You are correct!");
		}
		else {
			System.out.println("You are wrong. The correct answer is " + correctAnswer );
			points = 0;
		}
		
		return points;
	}


	@Override
	public boolean checkAnswer(String answer) {
		if(answer.equalsIgnoreCase("t")) {
			return true;
		}else if (answer.equalsIgnoreCase("f")){
			return false;
		}
		else {
			System.out.println("System could not check answer");
			return false;
		}
		
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	protected void addTFQuestionToDB(Question tfQuestion) {
		//add true-false question to database
		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Problem loading driver");
			e.printStackTrace();
		}

		String msAccDB = "Question.accdb";
		String dbURL = "jdbc:ucanaccess://" + msAccDB;

		try {
			connection = DriverManager.getConnection(dbURL);
			statement = connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Problem connecting to database");
			e.printStackTrace();
		}
		String Qtext = tfQuestion.getQuestionText();
		Boolean Answer = ((TFQuestion) tfQuestion).getAnswer();
		double Point = tfQuestion.getPoint();
		String type = "TF";
		String sqlStr = "INSERT INTO QUESTIONS (QText, Answer, Point, Type) VALUES " + "('" + Qtext + "', '" + Answer+ "', '" + Point + "', '" +type + "')";
	
		try {
			statement.executeUpdate(sqlStr);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Problem updating database");
			e.printStackTrace();
		}
		try {
			connection.close();
			statement.close();
			

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Problem closing database");
			e.printStackTrace();
		}
		
	}

	
}