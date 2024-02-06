package test.model;

import java.sql.*;
import java.util.ArrayList;
import test.controller.QuestionControllerSQL;

public class ManagerSQL extends Manager {

	private static final long serialVersionUID = 1L;
	private Connection conn1;

	public ManagerSQL() {

		super();
		setConnection();
	}

	public ManagerSQL(ArrayList<Question> newTest) {

		super();
		setConnection();
	}

	public void setConnection() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver"); // start the driver
		} catch (Exception ex1) {
			while (ex1 != null) {
				System.out.println("SQL exception: + " + ex1.getMessage());
			}
		}
		try {
			String dbUrl = "jdbc:mysql://localhost:3306/mysqlproject";
			conn1 = DriverManager.getConnection(dbUrl, "root", "171296");// set up a connection
		} catch (SQLException ex) {
			while (ex != null) {
				System.out.println("SQL exception: + " + ex.getMessage());
			}
		}

	}

	public void registerListener(QuestionControllerSQL questionController) {

		super.registerListener(questionController);

	}

	public boolean addQuestion(Question question) {

		try {
			if (isQuestionExcistInSQL(question)) {
				System.out.println("question already exist");
				return false;
			}
			Statement stmt2 = conn1.createStatement();
			String s2 = ("INSERT INTO Question (Qtext) VALUES('" + question.getText() + "')");
			int numAffectedRows = stmt2.executeUpdate(s2);
			System.out.println("numAffectedRows=" + numAffectedRows);
			if (question instanceof OpenQuestion) {
				addOpenQuestion((OpenQuestion) question);
				questions.add(question);
				fireAddedQuestion(question);
			} else {
				addMultiChoiceQuestion((MultiChoiceQuestion) question);
				questions.add(question);
				fireAddedQuestion(question);
			}

		} catch (SQLException ex) {
			while (ex != null) {
				System.out.println("SQL exception: + " + ex.getMessage());
			}
		}
		return true;
	}

	private void addMultiChoiceQuestion(MultiChoiceQuestion question) {

		try {
			Statement stmt = conn1.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT serialNum FROM Question WHERE Question.Qtext = '" + question.getText() + "'");
			if (rs.next()) {
				int serialNum = rs.getInt("serialNum");
				String s2 = ("INSERT INTO MultiChoiceQuestion VALUES(" + serialNum + ")");
				int numAffectedRows = stmt.executeUpdate(s2);
				System.out.println("numAffectedRows=" + numAffectedRows);

				for (int i = 0; i < question.getNumOfAnswers(); i++) {
					int answer_serial_num = isAnswerExcist(question.getAnswer(i));
					if (answer_serial_num == -1) {
						answer_serial_num = addAnswer(question.getAnswer(i));
					}
					String s3 = ("INSERT INTO MultiChoiceAnswers VALUES(" + serialNum + " , " + answer_serial_num
							+ ")");
					numAffectedRows = stmt.executeUpdate(s3);
					System.out.println("numAffectedRows=" + numAffectedRows);
				}
			}

		} catch (SQLException ex) {
			while (ex != null) {
				System.out.println("SQL exception: + " + ex.getMessage());
			}
		}
	}

	public void addOpenQuestion(OpenQuestion Openquestion) {

		try {
			Statement stmt = conn1.createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT serialNum FROM Question WHERE Question.Qtext = '" + Openquestion.getText() + "'");
			if (rs.next()) {
				int serialNum = rs.getInt("serialNum");
				int answer_serial_num = isAnswerExcist(Openquestion.getAnswer());
				if (answer_serial_num == -1) {
					answer_serial_num = addAnswer(Openquestion.getAnswer());
				}
				String s2 = ("INSERT INTO OpenQuestion VALUES(" + serialNum + "," + answer_serial_num + ")");
				int numAffectedRows = stmt.executeUpdate(s2);
				System.out.println("numAffectedRows=" + numAffectedRows);
			}
		} catch (SQLException ex) {
			while (ex != null) {
				System.out.println("SQL exception: + " + ex.getMessage());
			}

		}
	}

	private int addAnswer(Answer answer) {

		try {
			Statement stmt2 = conn1.createStatement();
			String s2 = ("INSERT INTO Answer(Atext, isTrue) VALUES('" + answer.getText() + "'," + answer.isTrue()
					+ " )");
			int numAffectedRows = stmt2.executeUpdate(s2);
			System.out.println("numAffectedRows=" + numAffectedRows);
			Statement stmt = conn1.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT answer_id FROM Answer WHERE Answer.Atext = '" + answer.getText()
					+ "' AND answer.isTrue = " + answer.isTrue());
			if (rs.next()) {
				return rs.getInt("answer_id");
			}
		} catch (SQLException ex) {
			while (ex != null) {
				System.out.println("SQL exception: + " + ex.getMessage());
			}
		}
		return -1;
	}

	private int isAnswerExcist(Answer answer) {

		try {
			Statement stmt = conn1.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT answer_id FROM Answer WHERE Answer.Atext = '" + answer.getText()
					+ "' AND answer.isTrue = " + answer.isTrue());
			if (rs.next()) {
				return rs.getInt("answer_id");
			}
		} catch (SQLException ex) {
			while (ex != null) {
				System.out.println("SQL exception: + " + ex.getMessage());
			}
		}
		return -1;
	}

	private boolean isQuestionExcistInSQL(Question question) {
		try {
			Statement stmt = conn1.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT Qtext FROM Question WHERE Question.Qtext = '" + question.getText() + "'");
			if (rs.next()) {
				return true;
			}
		} catch (SQLException ex) {
			while (ex != null) {
				System.out.println("SQL exception: + " + ex.getMessage());
			}
		}
		return false;
	}

	public void editQuestionInSQL(Question oldQuestion, String newQuestion) {

		try {
			if (!isQuestionExcistInSQL(oldQuestion)) {
				System.out.println("question do not exist");
				return;
			}
			Statement stmt2 = conn1.createStatement();
			String s2 = ("UPDATE Question SET  Qtext ='" + newQuestion + "' WHERE Question.Qtext = '"
					+ oldQuestion.getText() + "'");
			int numAffectedRows = stmt2.executeUpdate(s2);
			System.out.println("numAffectedRows=" + numAffectedRows);
		} catch (SQLException ex) {
			while (ex != null) {
				System.out.println("SQL exception: + " + ex.getMessage());
			}
		}

	}

	public void DeleteAnswerInSQL(Question question, Answer answer) {

		try {
			int answer_serial_num = isAnswerExcist(answer);
			if (answer_serial_num == -1) {
				System.out.println("Answer do not exist");
				return;
			}
			Statement stmt = conn1.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT serialNum FROM Question WHERE Question.Qtext = '" + question.getText() + "'");
			if (rs.next()) {
				int question_id = rs.getInt("serialNum");

				Statement stmt2 = conn1.createStatement();
				String s2 = ("DELETE FROM MultiChoiceAnswers WHERE MultiChoiceAnswers.MultiAnswer_id = " + answer_serial_num
						+ " AND MultiChoiceAnswers.MultiQuestion_id = " + question_id);
				int numAffectedRows = stmt2.executeUpdate(s2);
				System.out.println("numAffectedRows=" + numAffectedRows);
			}
		} catch (SQLException ex) {
			while (ex != null) {
				System.out.println("SQL exception: + " + ex.getMessage());
			}
		}

	}

	public boolean editQuestion(Question oldQuestion, String newQuestion) {
		int whereIsQuestion = isQuestionExist(oldQuestion);
		if (whereIsQuestion == -1) {
			return false;
		}
		for (Question question : questions) {
			if (question.getText().equals(newQuestion)) {
				System.out.println("Question Didn't Change \nSame Question With That Name");
				return false;
			}
		}
		editQuestionInSQL(oldQuestion, newQuestion);
		questions.get(whereIsQuestion).setText(newQuestion);
		fireModifiedQuestion(oldQuestion, questions.get(whereIsQuestion));
		return true;
	}

	public boolean deleteAnswer(Question question, Answer answer) {
		int whereIsQuestion = isQuestionExist(question);
		if (whereIsQuestion == -1) {
			return false;
		}
		if (questions.get(whereIsQuestion).deleteAnswer(answer)) {
			DeleteAnswerInSQL(question, answer);
			fireModifiedQuestion(question, questions.get(whereIsQuestion));
			return true;
		}
		return false;
	}

	public boolean LoadOpenQuestionSourceFromSQL() {

		String questionText = "";
		String answerText = "";
		int question_id = -1;
		int answer_id = -1;
		boolean IsTrue = true;

		try {
			Statement stmt = conn1.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM OpenQuestion");
			while (rs.next()) {
				question_id = rs.getInt("OpenQuestion_serialNum");
				answer_id = rs.getInt("OpenAnswer_id");
				Statement stmt2 = conn1.createStatement();
				ResultSet rs2 = stmt2
						.executeQuery("SELECT Qtext FROM Question WHERE Question.serialNum = " + question_id);
				Statement stmt3 = conn1.createStatement();
				ResultSet rs3 = stmt3.executeQuery("SELECT * FROM answer WHERE answer.answer_id = " + answer_id);
				if (rs2.next() && rs3.next()) {
					questionText = rs2.getString("Qtext");
					answerText = rs3.getString("Atext");
					IsTrue = rs3.getBoolean("isTrue");
					OpenQuestion temp = new OpenQuestion(questionText, new Answer(answerText, IsTrue));
					questions.add(temp);
				}
			}
			return true;

		} catch (SQLException ex) {
			while (ex != null) {
				System.out.println("SQL exception: + " + ex.getMessage());
			}
		}
		return false;
	}

	public Answer LoadAnswerSourceFromSQL(int answerId) {

		String answer_text = "";
		boolean answerIsTrue;

		try {
			Statement stmt = conn1.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Answer WHERE Answer.answer_id =" + answerId + "");
			if (rs.next()) {
				answerIsTrue = rs.getBoolean("isTrue");
				answer_text = rs.getString("Atext");
				Answer temp = new Answer(answer_text, answerIsTrue);
				return temp;
			}
		} catch (SQLException ex) {
			while (ex != null) {
				System.out.println("SQL exception: + " + ex.getMessage());
			}

		}
		return null;
	}

	public boolean LoadMultiChoiceQuestionsSourceFromSQL() {

		String questionText = "";
		int question_id = -1;
		int answer_id = -1;

		Set<Answer> answers = new Set<Answer>();
		try {
			Statement stmt = conn1.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM MultiChoiceQuestion");
			while (rs.next()) {
				question_id = rs.getInt("MultiQuestion_serialNum");
				Statement stmt2 = conn1.createStatement();
				ResultSet rs2 = stmt2
						.executeQuery("SELECT Qtext FROM Question WHERE Question.serialNum = " + question_id);
				if (rs2.next()) {
					questionText = rs2.getString("Qtext");
					Statement stmt3 = conn1.createStatement();
					ResultSet rs3 = stmt3.executeQuery(
							"SELECT MultiAnswer_id FROM MultiChoiceAnswers WHERE MultiChoiceAnswers.MultiQuestion_id ="
									+ question_id);
					while (rs3.next()) {

						answer_id = rs3.getInt("MultiAnswer_id");
						answers.add(LoadAnswerSourceFromSQL(answer_id));
					}

					questions.add(new MultiChoiceQuestion(questionText, answers));
					answers = new Set<Answer>();

				}
			}
			return true;
		} catch (SQLException ex) {
			while (ex != null) {
				System.out.println("SQL exception: + " + ex.getMessage());
			}

		}
		return false;
	}

	public void LoadSourceFromSQL() {

		boolean flag = LoadOpenQuestionSourceFromSQL();
		if (!flag) {
			System.out.println("could not load Open Question");
		}
		flag = LoadMultiChoiceQuestionsSourceFromSQL();
		if (!flag) {
			System.out.println("could not load American Question");
		}

		fireLoadSourceToModel((ArrayList<Question>) questions);

	}
}
