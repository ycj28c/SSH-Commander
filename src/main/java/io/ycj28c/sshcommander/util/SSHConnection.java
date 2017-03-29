package io.ycj28c.sshcommander.util;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import io.ycj28c.sshcommander.vo.Machine;

public class SSHConnection {
	
	private Session session = null;
	@SuppressWarnings("unused")
	private Channel channel = null;
	private String host = "";
	private String user = "";
	private String password = "";
	private JSch jsch = null;
	private java.util.Properties config = null;
	
	public SSHConnection(String host, String user, String pw) {
		this.host = host;
		this.user = user;
		this.password = pw;
	}
	
	public SSHConnection(Machine ma) {
		this.host = ma.getIp();
		this.user = ma.getUsername();
		this.password = ma.getPassword();
	}

	public Session initializeSession() {
		try {
			config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			jsch = new JSch();
			session = jsch.getSession(user, host, 22);
			session.setPassword(password);
			session.setConfig(config);
			session.connect();
			System.out.println("Session Connected...");
			return session;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Channel createChannel(String channelName) {
		if(session == null){
			initializeSession();
		}
		try {
			System.out.println("Channel Established...");
			return session.openChannel(channelName);
		} catch (JSchException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
