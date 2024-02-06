package test.model;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import test.listener.QuestionListener;

public class Manager implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;
	protected List<Question> questions;
	protected List<QuestionListener> listeners;

	public Manager(ArrayList<Question> questions) {
		this.questions = questions;
		listeners = new ArrayList<QuestionListener>();

	}

	public Manager() {
		questions = new ArrayList<Question>();
		listeners = new ArrayList<QuestionListener>();

	}

	public void registerListener(QuestionListener listener) {
		listeners.add(listener);
	}

	public int getNumOfQuestions() {
		return questions.size();
	}

	public List<Question> getQuestions() {
		return questions;
	}

	public boolean deleteAnswer(Question question, Answer answer) {
		int whereIsQuestion = isQuestionExist(question);
		if (whereIsQuestion == -1) {
			return false;
		}
		if (questions.get(whereIsQuestion).deleteAnswer(answer)) {
			fireModifiedQuestion(question, questions.get(whereIsQuestion));
			return true;
		}
		return false;
	}

	public void fireModifiedQuestion(Question oldQuestion, Question newQuestion) {
		for (QuestionListener questionListener : listeners) {
			questionListener.questionModifiedToUI(oldQuestion, newQuestion);

		}
	}

	public boolean editQuestion(Question oldQuestion, String newQuestion) {
		int whereIsQuestion = isQuestionExist(oldQuestion);
		if (whereIsQuestion == -1) {
			return false;
		}
		for(Question question : questions) {
			if (question.getText().equals(newQuestion) && (question.getClass() == oldQuestion.getClass())) {
				fireShowMessage("Question Didn't Change \nSame Question With That Name");
				return false;
			}
		}
		questions.get(whereIsQuestion).setText(newQuestion);
		fireModifiedQuestion(oldQuestion, questions.get(whereIsQuestion));
		fireShowMessage("Question Changed");
		return true;
	}

	public boolean addQuestion(Question question) {
		if (isQuestionExist(question) == -1) {
			questions.add(question);
			fireAddedQuestion(question);
			return true;
		}
		return false;
	}

	public void fireAddedQuestion(Question question) {
		for (QuestionListener questionListener : listeners) {
			questionListener.questionAddToUI(question);
		}
	}

	public boolean addOpenQuestion(String question, String answer) {
		Answer newAnswer = new Answer(answer, true);
		OpenQuestion newQuestion = new OpenQuestion(question, newAnswer);
		return addQuestion(newQuestion);
	}

	public boolean editOpenAnswer(Question question, String answer) {
		int whereIsQuestion = isQuestionExist(question);
		if (whereIsQuestion == -1) {
			return false;
		} else if (question instanceof OpenQuestion) {
			((OpenQuestion) questions.get(whereIsQuestion)).setAnswer(answer);
			fireModifiedQuestion(questions.get(whereIsQuestion), questions.get(whereIsQuestion));
			return true;
		}
		return false;
	}

	public boolean editMultiAnswer(Question question, int oldAnswer, Answer newAnswer) {
		int whereIsQuestion = isQuestionExist(question);
		if (whereIsQuestion == -1) {
			return false;
		} else if (question instanceof MultiChoiceQuestion) {
			((MultiChoiceQuestion) questions.get(whereIsQuestion)).setAnswer(oldAnswer, newAnswer);
			fireModifiedQuestion(questions.get(whereIsQuestion), questions.get(whereIsQuestion));
			return true;
		}
		return false;
	}

	public boolean addMultiChoiceAnswer(MultiChoiceQuestion question, Answer answer) {
		return question.addAnswer(answer);
	}

	public int isQuestionExist(Question question) {
		int flag = -1;
		for (int i = 0; i < questions.size(); i++) {
			if (question.equals(questions.get(i))) {
				flag = i;
			}
		}
		return flag;
	}

	public Manager autoTestBuilder(int numOfQuestions) {
		Manager managerTest = new Manager();
		int questionsLeft = numOfQuestions;
		while (questionsLeft > 0) {
			int randQuestion = (int) (Math.random() * (questions.size()));
			if (managerTest.addAutoQuestion(this.getQuestions().get(randQuestion))) {
				questionsLeft--;
			}
		}
		fireCreatedTestInModel(managerTest.toTest().questions);
		return managerTest;
	}


	public boolean addAutoQuestion(Question question) {
		if (question instanceof MultiChoiceQuestion) {
			if (((MultiChoiceQuestion) question).getNumOfAnswers() <= 4) {
				return this.addQuestion(question);
			} else if ((((MultiChoiceQuestion) question).getNumOfAnswers()
					- ((MultiChoiceQuestion) question).howManyAreTrue()) >= 3) {
				MultiChoiceQuestion newQuestion = new MultiChoiceQuestion(question.getText());
				int place = 0;
				boolean isOneTrue = false;
				while (place < 4) {
					int randAns = (int) (Math.random() * (((MultiChoiceQuestion) question).getNumOfAnswers()));
					if (((MultiChoiceQuestion) question).getAnswer(randAns).isTrue() == false || !isOneTrue) {
						if (newQuestion.addAnswer(((MultiChoiceQuestion) question).getAnswer(randAns))) {
							if (((MultiChoiceQuestion) question).getAnswer(randAns).isTrue()) {
								isOneTrue = true;
							}
							place++;
						}
					}
				}
				return this.addQuestion(newQuestion);
			}
		}
		return this.addQuestion(question);
	}

	public Manager toTest() {
		Manager Test = new Manager();
		Question clonedQuestion=null;
		for (int i = 0; i < questions.size(); i++) {
			try {
				clonedQuestion = questions.get(i).clone();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
			if (clonedQuestion instanceof MultiChoiceQuestion) {
				if(((MultiChoiceQuestion)clonedQuestion).howManyAreTrue() > 1){
					for (int j = 0; j <((MultiChoiceQuestion) clonedQuestion).getNumOfAnswers(); j++) {
						((MultiChoiceQuestion)clonedQuestion).getAnswer(j).setTrue(false);
					}
					addMultiChoiceAnswer((MultiChoiceQuestion) clonedQuestion, new Answer("None of the above",false));
					addMultiChoiceAnswer((MultiChoiceQuestion) clonedQuestion, new Answer("More than one answer",true));
				}
				else {
					addMultiChoiceAnswer((MultiChoiceQuestion) clonedQuestion, new Answer("None of the above",
							((MultiChoiceQuestion) questions.get(i)).howManyAreTrue() == 0));
					addMultiChoiceAnswer((MultiChoiceQuestion) clonedQuestion, new Answer("More than one answer",false));
				}
			}
			Test.addQuestion(clonedQuestion);
		}
		try {
			Test.exportTest();
		} catch (IOException e) {
			System.out.println("Didn't save the test");
		}
		return Test;
	}

	public void insertionSortByAlphabeticOrder() {
		for (int i = 1; i < questions.size(); i++) {
			for (int j = i; j > 0
					&& alphabeticOrder(questions.get(j).getText(), questions.get(j - 1).getText()) < 0; j--) {
				Question temp = questions.get(j);
				questions.set(j, questions.get(j - 1));
				questions.set(j - 1, temp);
			}
		}
	}

	public void insertionSortByLength() {
		for (int i = 1; i < questions.size(); i++) {
			for (int j = i; j > 0 && questions.get(j).getStringLength() < questions.get(j - 1).getStringLength(); j--) {
				Question temp = questions.get(j);
				questions.set(j, questions.get(j - 1));
				questions.set(j - 1, temp);
			}
		}
	}

	public int alphabeticOrder(String word1, String word2) {
		String lowerCaseWord1 = word1.toLowerCase();
		String lowerCaseWord2 = word2.toLowerCase();
		return lowerCaseWord1.compareTo(lowerCaseWord2);
	}

	public String getDate() {
		LocalDateTime thisDate = LocalDateTime.now();
		DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("yyyy_MM_dd");
		return thisDate.format(formatDate);
	}

	public void exportTest() throws IOException {
		String testName = "exam_" + this.getDate() + ".txt";
		String solName = "solution_" + this.getDate() + ".txt";
		File test = new File("./" + testName);
		File sol = new File("./" + solName);
		PrintWriter testWriter = new PrintWriter(test);
		PrintWriter solWriter = new PrintWriter(sol);
		test.createNewFile();
		sol.createNewFile();
		testWriter.print(this.toStringWithoutTrue());
		solWriter.print(this.toString());
		testWriter.close();
		solWriter.close();
	}

	public void outFile() throws FileNotFoundException, IOException, ClassNotFoundException {
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("./source.dat"));
		out.writeObject(questions);
		fireShowMessage("Success");
		out.close();
	}

	private void fireShowMessage(String message) {
		for (QuestionListener questionListener : listeners) {
			questionListener.showMessage(message);

		}

	}

	public void inFile() throws FileNotFoundException, IOException, ClassNotFoundException {
		ObjectInputStream in = new ObjectInputStream(new FileInputStream("./source.dat"));
		ArrayList<Question> questionsToRead = (ArrayList<Question>) in.readObject();
		questions=questionsToRead;
		fireLoadSourceToModel(questionsToRead);
		in.close();
	}
	
	public void fireLoadSourceToModel(ArrayList<Question> newQuestions) {
		for (QuestionListener listener : listeners) {
			listener.loadSourceToUI(newQuestions);
		}
	}

	public String toStringWithoutTrue() {
		StringBuilder str = new StringBuilder();
		str.append("Test \n" + "Number of Questions: " + questions.size() + "\n");
		for (int i = 0; i < questions.size(); i++) {
			str.append("Question number " + (i + 1) + ":\n");
			str.append(questions.get(i).toStringWithoutTrue() + "\n");
		}
		return str.toString();
	}

	public Manager clone() throws CloneNotSupportedException {
		Manager temp = (Manager) super.clone();
		List<Question> quesTemp = new ArrayList<Question>();
		for (int i = 0; i < this.questions.size(); i++) {
			quesTemp.add(this.questions.get(i).clone());
		}
		temp.questions = quesTemp;
		fireCreatedTestInModel(temp.toTest().questions);
		return temp;
	}

	private void fireCreatedTestInModel(List<Question> questions) {
		for (QuestionListener questionListener : listeners) {
			questionListener.createdTestToUI(questions);
		}
	}

	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("Test \n" + "Number of Questions: " + questions.size() + "\n");
		for (int i = 0; i < questions.size(); i++) {
			str.append("Question number " + (i + 1) + ":\n");
			str.append(questions.get(i).toString() + "\n");
		}
		return str.toString();
	}

	public Manager createManualTest(ArrayList<Question> newTest) {
		Manager manualTest = new Manager(newTest);
		for (QuestionListener listener : listeners) {
			fireCreatedTestInModel(manualTest.toTest().questions);
		}
		return manualTest;
	}
	
	public void initial() {
		Answer a = new Answer("yes", false);
	     Answer b = new Answer("no", true);
		 Answer c = new Answer("probably", false);
		 Answer d = new Answer("skip", true);
		 Answer e = new Answer("peleg and amir", false);
		 Answer f = new Answer("peleg", false);
		 Answer g = new Answer("amir", true);
		 Answer h = new Answer("no one does", false);
		 Answer i = new Answer("definetly", false);
		 Answer j = new Answer("abseloutly no", true);
		Answer[] array1 = { a, b, c, d };
		Answer[] array2 = { e, f, g, h };
		Set<Answer> list1 = new Set<Answer>();
		Set<Answer> list2 = new Set<Answer>();
		list1.add(array1);
		list2.add(array2);
		MultiChoiceQuestion q1 = new MultiChoiceQuestion("do you like object oriented class?" , list1);
		 MultiChoiceQuestion q2 = new MultiChoiceQuestion("who was attended in more classes?" , list2);
		 MultiChoiceQuestion q3 = new MultiChoiceQuestion("will corona virus is to be over someday?",list1);
		 OpenQuestion q4 = new OpenQuestion("who did this project?", e);
		 OpenQuestion q5 = new OpenQuestion("is this a work of an A+?", c);
		Question[] arrayOfQuestions = { q1, q2, q3, q4, q5 };
		ArrayList<Question> listOfQuestions = new ArrayList<Question>(Arrays.asList(arrayOfQuestions));
		questions=listOfQuestions;
		fireLoadSourceToModel(listOfQuestions);
	}

}
