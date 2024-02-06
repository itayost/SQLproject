package test.model;

import java.io.Serializable;

public class MultiChoiceQuestion extends Question implements Serializable, Cloneable{
	
	private static final long serialVersionUID = 1L;
	private Set<Answer> answers;

	public MultiChoiceQuestion(String text, Set<Answer> answers) {
		super(text);
		this.answers = answers;
	}
	
	public MultiChoiceQuestion(String text) {
		super(text);
		answers = new Set<Answer>();
	}
	
	public Answer getAnswer(int ansPlace) { 
		return answers.getElement(ansPlace);
	}
		
	public int getNumOfAnswers() {
		return answers.getSize();
	}

	public boolean addAnswer(Answer answer) {
		return answers.add(answer);
	}
	
	public void setAnswer(int oldAnswer, Answer newAnswer) {
		answers.set(oldAnswer, newAnswer);
	}
	
	public boolean deleteAnswer(Answer answer) {
		return answers.delete(isAnswerExist(answer));
		
	}
	
	public int isAnswerExist(Answer answer) {
		return answers.getIndex(answer);
	}
	
	public int howManyAreTrue() {
		int count=0;
		for (int i = 0; i < answers.getSize(); i++) {
			if(answers.getElement(i).isTrue()) {
				count++;
			}
		}
		return count;
	}
	
	public int getStringLength() {
		int length = 0;
		for (int i = 0; i < answers.getSize(); i++) {
			length += answers.getElement(i).getStringLength();
		}
		return length;
	}
	
	public MultiChoiceQuestion clone() throws CloneNotSupportedException {
		MultiChoiceQuestion temp = (MultiChoiceQuestion)super.clone();
		Set<Answer> ansTemp = new Set<Answer>();
		for (int i = 0; i < this.answers.getSize(); i++) {
			ansTemp.add(answers.getElement(i).clone());
		}
		temp.answers = ansTemp;
		return temp;
	}

	public String toString() {
		StringBuilder ans = new StringBuilder();
		ans.append("MultiChoiceQuestion: \ntext="+ super.getText() +"\n");
		for (int i = 0; i < answers.getSize(); i++) {
			ans.append((i+1) + ") " + answers.getElement(i).toString() + "\n");
		}
		return ans.toString();
	}
	
	public String toStringWithoutTrue() {
		StringBuilder ans = new StringBuilder();
		ans.append("MultiChoiceQuestion: \ntext="+ super.getText() +"\n");
		for (int i = 0; i < answers.getSize(); i++) {
			ans.append((i+1) + ") " + answers.getElement(i).toStringWithoutTrue() + "\n");
		}
		return ans.toString();
	}
	
}
