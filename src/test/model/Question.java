package test.model;

import java.io.Serializable;

public abstract class Question implements Serializable, Cloneable {
	
	private static final long serialVersionUID = 1L;
	private static int counter=0;
	private int serialNum=0;
	private String text;
	
	
	public Question(String text) {
		this.text = text;
		serialNum=counter++;
	}



	public String getText() {
		return text;
	}



	public void setText(String text) {
		this.text = text;
	}



	public int getSerialNum() {
		return serialNum;
	}

	public abstract boolean deleteAnswer(Answer answer);
	
	public Question clone() throws CloneNotSupportedException {
		return (Question)super.clone();
	}
	



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Question other = (Question) obj;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		return true;
	}
	
	public abstract int getStringLength();

	public String toString() {
		return "Question [serialNum=" + serialNum + ", text=" + text + "]";
	}
	
	public abstract String toStringWithoutTrue();
}
