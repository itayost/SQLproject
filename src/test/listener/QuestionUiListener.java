package test.listener;

import java.util.ArrayList;
import test.model.Answer;
import test.model.Question;

public interface QuestionUiListener {

	void addQuestionToModel(Question question);

	void editQuestionToModel(Question oldQuestion, String newQuestion);

	void editOpenAnswerToModel(Question question, String answer);

	void editMultiAnswerToModel(Question question, int oldAnswer, Answer newAnswer);

	void deleteAnswerToModel(Question question, Answer answer);

	void createManualTestToModel(ArrayList<Question> newTest);

	void createAutoTestToModel(int numOfQuestions);

	void cloneTestToModel() throws CloneNotSupportedException;

	void saveSourceToModel();

}
