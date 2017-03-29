package io.ycj28c.sshcommander.util;

import java.io.IOException;
import java.io.OutputStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSchException;

import io.ycj28c.sshcommander.vo.KeyBoardEnum;

public class SSHSignalUtil {

	public static void sendCommand(Channel channel, KeyBoardEnum ctrlC) {
		try {
			SSHExecUtil.channelCheck(channel);

			OutputStream ops = channel.getOutputStream();
			ops.write(ctrlC.getNumber());
			ops.flush();
			
		} catch (JSchException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void killUAll(){
//		channel.sendSignal("2"); // CTRL + C - interrupt
//		channel.sendSignal("9"); // KILL
//		
//		File machineConfig = new File("C:/git/SSH-Commander/src/main/resources/machine/qa-tomcat.json");
//		Machine ma = ReadJson.readMachineFromFile(machineConfig);
//		SSHConnection sshConn = new SSHConnection(ma);
//		
//		try {
//			Session session1 = sshConn.initializeSession();
//			Channel channel1 = sshConn.createChannel("shell");
//			channel1.connect();
//
//			OutputStream opsxxx = channel1.getOutputStream();
//			PrintStream psxxx = new PrintStream(opsxxx, true);
//			psxxx.println("ps -ef|grep cat|xargs kill");
//			psxxx.flush();
//			
//			channel1.disconnect();
//			session1.disconnect();
//		} catch (JSchException e) {
//			e.printStackTrace();
//		}
	}
}
