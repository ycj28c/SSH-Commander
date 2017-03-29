package io.ycj28c.sshcommander.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import io.ycj28c.sshcommander.util.SSHConnection;
import io.ycj28c.sshcommander.util.SSHExecUtil;

/**
 * a test example to hard code server, read configure from script
 *
 */
public class SSHTest2 {
	
	public static void main(String[] args) throws JSchException {
		String host = "1.1.1.1";
		String user = "111";
		String password = "111";
		
		SSHConnection sshConn = new SSHConnection(host, user, password);
		Session session = sshConn.initializeSession();
		Channel channel = sshConn.createChannel("shell");
		channel.connect();
		
		runFromScript(channel, session);
		
		channel.disconnect();
		session.disconnect();
	}
	
	public static void runFromScript(Channel channel, Session session) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader("C:/git/SSH-Commander/src/main/resources/scripts/script-example1.sss"));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while((line = br.readLine()) != null){
				sb.append(line);
		        sb.append(System.lineSeparator());
		        System.out.println(line);
		        SSHExecUtil.ExecuteCommandByFreezeTime(channel, line, 3 * 1000);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
