package io.ycj28c.sshcommander.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import io.ycj28c.sshcommander.util.SSHConnection;
import io.ycj28c.sshcommander.util.SSHExecUtil;
import io.ycj28c.sshcommander.util.SSHSignalUtil;
import io.ycj28c.sshcommander.vo.KeyBoardEnum;

/**
 * a test example to hard code server and script 
 *
 */
public class SSHTest1 {
	
	public static void main(String[] args) throws JSchException {
		String host = "1.1.1.1";
		String user = "111";
		String password = "111";
		
/*		JSch sshSingleton = new JSch();
		Session session = sshSingleton.getSession(user, host);
		session.setPassword(password);
		Properties config = new Properties();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config);
		session.connect();
		Channel channel = session.openChannel("shell");*/

		SSHConnection sshConn = new SSHConnection(host, user, password);
		Session session = sshConn.initializeSession();
		Channel channel = sshConn.createChannel("shell");
		channel.connect();
		
		runStepByStepTest(channel, session);
		
		channel.disconnect();
		session.disconnect();
	}
	
	public static void runStepByStepTest(Channel channel, Session session) {
		String command1 = "ls -l; cd /opt/tomcat/logs;ls -l";
		String command2 = "tail -10f ws.log";
		String command3 = "cd /; pwd";
		String command4 = "grep Git-Commit /opt/tomcat/webapps/ws/META-INF/MANIFEST.MF";
		String command5 = "cd /opt/tomcat/logs;tail -10f ws.log";

		SSHExecUtil.ExecuteCommandByFreezeTime(channel, command1, 3 * 1000);
		System.out.println("\n *** the command1 finish");
		SSHExecUtil.sleep(2000);
		
		SSHExecUtil.ExecuteCommandByTotalTime(channel, command2, 3 * 1000);
		System.out.println("\n *** the command2 finish");
		SSHExecUtil.sleep(2000);
		
		SSHSignalUtil.sendCommand(channel, KeyBoardEnum.CTRL_C);
		System.out.println("\n *** send command ctrl+c done");
		SSHExecUtil.sleep(2000);
		
		SSHExecUtil.ExecuteCommandByFreezeTime(channel, command3, 3 * 1000);
		System.out.println("\n *** the command3 finish");
		SSHExecUtil.sleep(2000);
		
		String strr = "(Git-Commit:)(\\s)(\\S+)";
		String str = SSHExecUtil.ExecuteCommandMatchPattern(channel, command4, strr, 5 * 1000);
		System.out.println("\n *** Get String:"+str);
		Pattern p = Pattern.compile(strr);
		Matcher m = p.matcher(str);
		if(m.find()){
			System.out.println("Find the Git Commit:" + m.group(3));
		}
		
		String xxx = "startup";
		boolean hasStr = SSHExecUtil.ExecuteCommandContainsStr(channel, session, command5, xxx, 5 * 1000);
		System.out.println("\n *** Contains string:"+hasStr);
	}
}
