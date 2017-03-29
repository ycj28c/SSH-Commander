package io.ycj28c.sshcommander.scripts;

public class SSHScript {
	
	String command;
	String[] paramters;
	Long timeout;
	
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public String[] getParamters() {
		return paramters;
	}
	public void setParamters(String[] paramters) {
		this.paramters = paramters;
	}
	public Long getTimeout() {
		return timeout;
	}
	public void setTimeout(Long timeout) {
		this.timeout = timeout;
	}

}
