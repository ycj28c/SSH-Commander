package io.ycj28c.sshcommander.scripts;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SSHScriptParser {

//	public static void main(String[] args) throws JSchException {
//		String testStr = "Run(\"ls -l; cd /opt/tomcat/logs;ls -l\")";
//		SSHScriptParser sp = new SSHScriptParser();
//		SSHScript sss = sp.parseLine(testStr);
////		System.out.println(sss.toString());
//		
//		String host = "1.1.1.1";
//		String user = "111";
//		String password = "111";
//		
//		SSHConnection sshConn = new SSHConnection(host, user, password);
//		Session session = sshConn.initializeSession();
//		Channel channel = sshConn.createChannel("shell");
//		channel.connect();
//		
//		SSHScriptRunner.run(channel, session, sss);
//		
//		channel.disconnect();
//		session.disconnect();
//	}

	@SuppressWarnings("resource")
	public ArrayList<SSHScript> parse(File scriptFile) {
		ArrayList<SSHScript> list = new ArrayList<SSHScript>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(scriptFile));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while((line = br.readLine()) != null){
				/* ignore the empty and # line */
				if(line.trim().isEmpty()){
					continue;
				}
				if(line.trim().charAt(0)=='#'){
					continue;
				}
				sb.append(line);
		        sb.append(System.lineSeparator());
		        System.out.println(line);
		        SSHScript sss = parseLine(line);
		        list.add(sss);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	//so far just consider single simple command situation
	private SSHScript parseLine(String line){
		line = line.trim();
		System.out.println("** Start parsing line: " + line);
		String regex = "^(Run|Contains|Equals|NotContains)(\\()(.*)(\\))";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(line);
		
		String command = null;
		String content = null;
		if(m.find()){
			System.out.println(m.group(1));
			command = m.group(1);
			System.out.println(m.group(3));
			content = m.group(3);
		}
		
		SSHScript sss = new SSHScript();
		switch(command){
			case ("Run"):
				content = formatContent(content);
				sss.setCommand(command);
				String[] runParameters = {content};
				sss.setParamters(runParameters);
				break;
			case ("Contains"):
				String[] containsParameters = content.split(","); //not good, just for test so far
				for(int i=0;i<containsParameters.length;i++){
					containsParameters[i] = formatContent(containsParameters[i].trim());
				}
				sss.setCommand(command);
				sss.setParamters(containsParameters);
				break;
			case ("NotContains"):
				String[] notContainsParameters = content.split(","); //not good, just for test so far
				for(int i=0;i<notContainsParameters.length;i++){
					notContainsParameters[i] = formatContent(notContainsParameters[i].trim());
				}
				sss.setCommand(command);
				sss.setParamters(notContainsParameters);
				break;
			case ("Equals"):
				String[] equalParameters = content.split(","); //not good, just for test so far
				for(int i=0;i<equalParameters.length;i++){
					equalParameters[i] = formatContent(equalParameters[i].trim());
				}
				sss.setCommand(command);
				sss.setParamters(equalParameters);
				break;
			default:
				throw new java.lang.IllegalStateException("Error, command " + command + " is not support");
		}
		
		return sss;
	}

	private String formatContent(String content){
		if(content == null || content.isEmpty()){
			return null;
		}
		if(content.charAt(0)=='"' && content.charAt(content.length()-1)=='"'){
			content = content.substring(0,content.length()-1).substring(1);
		}
		System.out.println("Content after format: " + content);
		return content;
	}
}
