package io.ycj28c.sshcommander.scripts;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.Session;

import io.ycj28c.sshcommander.util.SSHExecUtil;
import io.ycj28c.sshcommander.util.SSHSignalUtil;
import io.ycj28c.sshcommander.vo.KeyBoardEnum;

public class SSHScriptRunner {

	public static void run(Channel channel, Session session, SSHScript sss) {
		if(sss == null){
			return;
		}
		if(sss.getCommand().equals("Run")){
			System.out.println("**** "+sss.getCommand()+"**** "+sss.paramters[0]);
			 SSHExecUtil.ExecuteCommandByFreezeTime(channel, sss.paramters[0], 3 * 1000);
			 SSHExecUtil.sleep(2000);
			 System.out.println("\n=====================================================");
		} else if(sss.getCommand().equals("Contains")){
			System.out.println("**** "+sss.getCommand()+"**** "+sss.paramters[0]+"*** "+sss.paramters[1]);
			Boolean isContained = SSHExecUtil.ExecuteCommandContainsStr(channel, session, sss.paramters[0], sss.paramters[1], 6 * 1000);
			SSHSignalUtil.sendCommand(channel, KeyBoardEnum.CTRL_C);
			SSHExecUtil.sleep(2000);
			System.out.println("\nTarget Str '"+sss.paramters[1]+"' is " + (isContained == true? "contained" : "not contained"));
			System.out.println("\n=====================================================");
			System.out.println("======================Test "+(isContained == true? "Pass" : "Fail")+"======================");
		} else if(sss.getCommand().equals("NotContains")){
			System.out.println("**** "+sss.getCommand()+"**** "+sss.paramters[0]+"*** "+sss.paramters[1]);
			Boolean isNotContained = SSHExecUtil.ExecuteCommandNotContainsStr(channel, session, sss.paramters[0], sss.paramters[1], 10 * 1000);
			SSHSignalUtil.sendCommand(channel, KeyBoardEnum.CTRL_C);
			SSHExecUtil.sleep(2000);
			System.out.println("\nTarget Str '"+sss.paramters[1]+"' is " + (isNotContained == true? "not contained": "contained"));
			System.out.println("\n=====================================================");
			System.out.println("======================Test "+(isNotContained == true? "Pass" : "Fail")+"======================");
		} 
		else if(sss.getCommand().equals("Equals")){
			System.out.println("**** "+sss.getCommand()+"**** "+sss.paramters[0]+"*** "+sss.paramters[1]);
			Boolean isEqualed = SSHExecUtil.ExecuteCommandEqualsStr(channel, sss.paramters[0], sss.paramters[1], 3 * 1000);
//			SSHSignalUtil.sendCommand(channel, KeyBoardEnum.CTRL_C);
			SSHExecUtil.sleep(2000);
			System.out.println("\nTarget Str '"+sss.paramters[1]+"' is " + (isEqualed == true? "equaled" : "not equaled"));
			System.out.println("\n=====================================================");
			System.out.println("======================Test "+(isEqualed == true? "Pass" : "Fail")+"======================");
		}
	}

}
