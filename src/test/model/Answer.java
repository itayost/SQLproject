package test.model;

import java.io.Serializable;

public class Answer implements Serializable, Cloneable{
	
	private static final long serialVersionUID = 1L;
	private String text;
	private boolean isTrue;
	
	public Answer(String text, boolean isTrue) {
		this.text = text;
		this.isTrue = isTrue;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isTrue() {
		return isTrue;
	}

	public void setTrue(boolean isTrue) {
		this.isTrue = isTrue;
	}
	
	public int getStringLength() {
		return text.length();
	}
	
	public Answer clone() throws CloneNotSupportedException {
		return (Answer) super.clone();
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Answer other = (Answer) obj;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		return true;
	}

	public String toString() {
		return "Answer [text=" + text + ", isTrue=" + isTrue + "]";
	}
	
	public String toStringWithoutTrue() {
		return "Answer [text=" + text+ "]";
	}
	
}

