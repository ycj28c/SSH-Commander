package io.ycj28c.sshcommander.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.regex.Pattern;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SSHExecUtil {
	
	/**
	 * Regular Execute command, keep alive
	 * @param channel
	 * @param session
	 * @param command
	 */
	public static void ExecuteCommand(Channel channel, Session session, String command) {
		try {
			channelCheck(channel);
			
			OutputStream ops = channel.getOutputStream();
			PrintStream ps = new PrintStream(ops, true);
			ps.println(command);
			ps.flush();

			InputStream in = channel.getInputStream();
			byte[] bt = new byte[1024];

			while (true) {
				while (in.available() > 0) {
					int i = in.read(bt, 0, 1024);
					if (i < 0)
						break;
					System.out.print(new String(bt, 0, i));
				}
				if (channel.isClosed()) {
					System.out.println("exit-status: " + channel.getExitStatus());
					break;
				}
				sleep(1000);
			}
		} catch (JSchException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Execute the shell command with the free time parameter, break if the
	 * output didn't update in the time limit
	 * 
	 * @param channel
	 * @param session
	 * @param command
	 * @param milliseconds
	 */
	public static void ExecuteCommandByFreezeTime(Channel channel, String command, long milliseconds) {
		try {
			channelCheck(channel);
			
			OutputStream ops = channel.getOutputStream();
			PrintStream ps = new PrintStream(ops, true);
			ps.println(command);
			ps.flush();

			InputStream in = channel.getInputStream();
			byte[] bt = new byte[1024];

			BigDecimal freezeTime = new BigDecimal("0");
			BigDecimal freezeLimit = new BigDecimal(String.valueOf(milliseconds));
			while (true) {
				while (in.available() > 0) {
					int i = in.read(bt, 0, 1024);
					if (i < 0) {
						break;
					} else {
						freezeTime = new BigDecimal("0");
					}
					System.out.print(new String(bt, 0, i));
				}
				if (channel.isClosed()) {
					System.out.println("exit-status: " + channel.getExitStatus());
					break;
				}
				// freeze time counting
				if (freezeTime.compareTo(new BigDecimal("0")) == 0) {
					freezeTime = new BigDecimal(String.valueOf(System.currentTimeMillis()));
				} else {
					// long than input stream no change limit timeout, break;
					if (new BigDecimal(String.valueOf(System.currentTimeMillis())).subtract(freezeTime)
							.compareTo(freezeLimit) > 0) {
						break;
					}
				}
				sleep(1000);
			}
		} catch (JSchException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Execute Shell command and return the matched string in time limit
	 * @param channel
	 * @param command
	 * @param pattern
	 * @param milliseconds
	 * @return
	 */
	public static String ExecuteCommandMatchPattern(Channel channel, String command, String regex, long milliseconds) {
		BigDecimal timeout = new BigDecimal(String.valueOf(milliseconds));
		BigDecimal before = new BigDecimal(String.valueOf(System.currentTimeMillis()));
		
		try {
			channelCheck(channel);
			
			OutputStream ops = channel.getOutputStream();
			PrintStream ps = new PrintStream(ops, true);
			ps.println(command);
			ps.flush();

			InputStream in = channel.getInputStream();
			StringBuilder line = new StringBuilder();
			char toAppend = ' ';
			while (new BigDecimal(String.valueOf(System.currentTimeMillis())).subtract(before).compareTo(timeout) < 0) {
				while (in.available() > 0) {
					toAppend = (char) in.read();
					if (toAppend == '\n'||toAppend == '\r') {
						System.out.print(line.toString());
						if(Pattern.matches(regex, line)){
							return line.toString();
						}
						line.setLength(0);
					} else
						line.append(toAppend);
				}
				
				if (channel.isClosed()) {
					break;
				}
				sleep(1000);
			}
		} catch (JSchException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Execute Shell command and check if the target string is displayed in output in time limit
	 * @param channel
	 * @param command
	 * @param pattern
	 * @param milliseconds
	 * @return
	 */
	public static Boolean ExecuteCommandContainsStr(Channel channel, Session session, String command, String target, long milliseconds) {
		BigDecimal timeout = new BigDecimal(String.valueOf(milliseconds));
		BigDecimal before = new BigDecimal(String.valueOf(System.currentTimeMillis()));
		
		try {
			System.out.println(1111);
			channelCheck(channel);
			
			InputStream in = channel.getInputStream();
//			BufferedReader in = new BufferedReader(new InputStreamReader(channel.getInputStream()));
//			while (in.available() > 0) {
//				System.out.println("xxxxx");
//				SSHSignalUtil.sendCommand(channel, KeyBoardEnum.CTRL_C);
//			}
			OutputStream ops = channel.getOutputStream();
			PrintStream ps = new PrintStream(ops, true);
			ps.println(command);
			ps.flush();
//			ops.write((command+"\n").getBytes());
//			ops.flush();
			
			StringBuilder line = new StringBuilder();
			char toAppend = ' ';
			Boolean flag = false;
			while (new BigDecimal(String.valueOf(System.currentTimeMillis())).subtract(before).compareTo(timeout) < 0) {
				System.out.println(2222);
				while (in.available() > 0) {
					System.out.println(in.available()+"^^^^^^^^^^^"+in.available());
//					System.out.println(3333);
					toAppend = (char) in.read();
					if (toAppend == '\n'||toAppend == '\r') {
//						System.out.print(line.toString());
						System.out.print(fixedLengthString(line.toString(), 100));
						if(line.toString().contains(target)){
							flag = true;
						}
						line.setLength(0);
					} else
						line.append(toAppend);
				}
				System.out.println(3333);
				if (channel.isClosed()) {
					break;
				}
//				while (channel.getExitStatus() == -1){
//			        try{Thread.sleep(1000);}catch(Exception e){System.out.println(e);}
//			     }
				
				sleep(1000);
			}
			if(flag == true){
				return true;
			}
//			
//			SSHSignalUtil.sendCommand(channel, KeyBoardEnum.CTRL_C);
		} catch (JSchException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Execute Shell command and check if the target string is not displayed in output in time limit
	 * @param channel
	 * @param command
	 * @param pattern
	 * @param milliseconds
	 * @return
	 */
	public static Boolean ExecuteCommandNotContainsStr(Channel channel, Session session, String command, String target, long milliseconds) {
		System.out.println(milliseconds +"!!!!!");
		return !ExecuteCommandContainsStr(channel, session, command, target, milliseconds);
	}
	
	/**
	 * Execute Shell command and check if the target string is equaled in output in time limit
	 * @param channel
	 * @param command
	 * @param pattern
	 * @param milliseconds
	 * @return
	 */
	public static Boolean ExecuteCommandEqualsStr(Channel channel, String command, String target, long milliseconds) {
		BigDecimal timeout = new BigDecimal(String.valueOf(milliseconds));
		BigDecimal before = new BigDecimal(String.valueOf(System.currentTimeMillis()));
		
		try {
			channelCheck(channel);
			
			OutputStream ops = channel.getOutputStream();
			PrintStream ps = new PrintStream(ops, true);
			ps.println(command);
			ps.flush();

			InputStream in = channel.getInputStream();
			StringBuilder line = new StringBuilder();
			char toAppend = ' ';
			while (new BigDecimal(String.valueOf(System.currentTimeMillis())).subtract(before).compareTo(timeout) < 0) {
				while (in.available() > 0) {
					toAppend = (char) in.read();
					if (toAppend == '\n'||toAppend == '\r') {
						System.out.print(line.toString());
//						System.out.print(fixedLengthString(line.toString(), 100));
						if(line.toString().trim().equals(target.trim())){
							return true;
						}
						line.setLength(0);
					} else
						line.append(toAppend);
				}
				
				if (channel.isClosed()) {
					break;
				}
				sleep(1000);
			}
		} catch (JSchException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void ExecuteCommandByTotalTime(Channel channel, String command, int milliseconds) {
		BigDecimal timeout = new BigDecimal(String.valueOf(milliseconds));
		BigDecimal before = new BigDecimal(String.valueOf(System.currentTimeMillis()));
		try {
			channelCheck(channel);
			
			OutputStream ops = channel.getOutputStream();
			PrintStream ps = new PrintStream(ops, true);
			ps.println(command);
			ps.flush();

			InputStream in = channel.getInputStream();
			byte[] bt = new byte[1024];
			while (new BigDecimal(String.valueOf(System.currentTimeMillis())).subtract(before).compareTo(timeout) < 0) {
				while (in.available() > 0) {
					int i = in.read(bt, 0, 1024);
					if (i < 0)
						break;
					String str = new String(bt, 0, i);
					// displays the output of the command executed.
					System.out.print(str);
				}
				if (channel.isClosed()) {
					break;
				}
				sleep(1000);
				// channel.disconnect();
				// session.disconnect();
			}
		} catch (JSchException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void channelCheck(Channel channel) throws JSchException{
		if (channel == null) {
			System.out.println("Error, the channel is null");
			return;
		}
		if (!channel.isConnected()) {
			System.out.println("The channel is not connect, connecting...");
			channel.connect();
		}
	}

	public static void sleep(long milliseconds) {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static String fixedLengthString(String pString, int length) {
		if (pString == null || length <= 0) {
			return null;
		}
		pString = pString.replaceAll("\r|\n", "");
		String newString = new String();
		int index = 0;
	    for (; index+length<pString.length(); index+=length){
	    	newString += pString.substring(index, index+length);
	    	newString += "\n";
	    }
	    if(index < pString.length()){
	    	newString += pString.substring(index, pString.length());
	    	newString += "\n";
	    }
	    return newString;
	}
}
