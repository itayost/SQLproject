package test.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import test.listener.QuestionListener;
import test.listener.QuestionUiListener;
import test.model.Answer;
import test.model.Manager;
import test.model.ManagerSQL;
import test.model.Question;
import test.view.Testable;

public class QuestionControllerSQL implements QuestionListener, QuestionUiListener {

	private ManagerSQL modelManager;
	private Testable view;

	public QuestionControllerSQL(ManagerSQL modelManger, Testable view) {
		this.modelManager = modelManger;
		this.view = view;
		this.modelManager.registerListener(this);
		this.view.registerListener(this);
	}

	@Override
	public void questionModifiedToUI(Question oldQuestion, Question newQuestion) {
		view.modifiedQuestionToUI(oldQuestion, newQuestion);
	}

	@Override
	public void questionAddToUI(Question question) {
		view.addQuestionToUI(question);

	}

	public void createdTestToUI(List<Question> questions) {
		view.testCreated(questions);
	}

	@Override
	public void showMessage(String state) {
		view.showMessageToUi(state);
	}

	@Override
	public void addQuestionToModel(Question question) {
		modelManager.addQuestion(question);

	}

	@Override
	public void editQuestionToModel(Question oldQuestion, String newQuestion) {
		modelManager.editQuestion(oldQuestion, newQuestion);

	}

	@Override
	public void deleteAnswerToModel(Question question, Answer answer) {
		modelManager.deleteAnswer(question, answer);

	}

	@Override
	public void createAutoTestToModel(int numOfQuestions) {
		modelManager.autoTestBuilder(numOfQuestions);

	}

	@Override
	public void cloneTestToModel() throws CloneNotSupportedException {
	}

	@Override
	public void saveSourceToModel() {
	}

	@Override
	public void loadSourceToUI(ArrayList<Question> allQuestions) {
		view.loadSource(allQuestions);
	}

	@Override
	public void createManualTestToModel(ArrayList<Question> newTest) {
		modelManager.createManualTest(newTest);

	}

	@Override
	public void editOpenAnswerToModel(Question question, String answer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void editMultiAnswerToModel(Question question, int oldAnswer, Answer newAnswer) {
		// TODO Auto-generated method stub
		
	}

}
