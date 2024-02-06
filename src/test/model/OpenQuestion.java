package test.model;

import java.io.Serializable;

public class OpenQuestion extends Question implements Serializable, Cloneable {
	
	private static final long serialVersionUID = 1L;
	private Answer answer;

	public OpenQuestion(String text, Answer answer) {
		super(text);
		this.answer = answer;
	}

	public boolean deleteAnswer(Answer answer) {
		if (this.answer.equals(answer)) {
			answer.setText("");
			return true;
		}
		return false;
	}

	public String getAnsText() {
		return answer.getText();
	}

	public void setAnswer(String answer) {
		this.answer.setText(answer);
	}

	public int getStringLength() {
		return answer.getStringLength();
	}

	public OpenQuestion clone() throws CloneNotSupportedException {
		OpenQuestion temp = (OpenQuestion) super.clone();
		temp.answer = answer.clone();
		return temp;
	}

	public String toString() {
		StringBuilder ans = new StringBuilder();
		ans.append("OpenQuestion: \ntext=" + super.getText() + "\n");
		ans.append("Answer [text=" + answer.getText() + "]\n");
		return ans.toString();
	}

	public String toStringWithoutTrue() {
		return ("OpenQuestion: \ntext=" + super.getText() + "\n");
	}
	
	public Answer getAnswer()
	{
		return this.answer;
	}

}
