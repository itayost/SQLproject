package test.view;

import java.util.ArrayList;
import java.util.List;

import test.listener.QuestionUiListener;
import test.model.Question;

public interface Testable {
	void addQuestionToUI(Question question);

	void modifiedQuestionToUI(Question oldQuestion, Question newQuestion);

	void addQuestion();

	void deleteAnswer();

	void editQuestion();

	void createManualtest();

	void createAutoTest();

	void createCloneTest();

	void testCreated(List<Question> questions);

	void saveSource();

	void showMessageToUi(String state);

	void registerListener(QuestionUiListener listener);

	void loadSource(ArrayList<Question> newSource);

}