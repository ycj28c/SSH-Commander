package io.ycj28c.sshcommander.vo;

public enum KeyBoardEnum {
	CTRL_C(3);

	private final Integer number;

	KeyBoardEnum(int number) {
		this.number = number;
	}
	
	public Integer getNumber(){
		return number;
	}
}
