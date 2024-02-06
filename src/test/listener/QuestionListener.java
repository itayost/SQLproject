package test.listener;

import java.util.ArrayList;
import java.util.List;
import test.model.Question;

public interface QuestionListener {

	public void questionModifiedToUI(Question oldQuestion, Question newQuestion);

	public void questionAddToUI(Question question);

	public void createdTestToUI(List<Question> questions);

	public void showMessage(String state);

	public void loadSourceToUI(ArrayList<Question> allQuestions);

}
