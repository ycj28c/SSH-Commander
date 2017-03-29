package system_check;

import java.io.File;
import java.util.ArrayList;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import io.ycj28c.sshcommander.scripts.SSHScript;
import io.ycj28c.sshcommander.scripts.SSHScriptParser;
import io.ycj28c.sshcommander.scripts.SSHScriptRunner;
import io.ycj28c.sshcommander.util.SSHConnection;
import io.ycj28c.sshcommander.vo.Machine;
import io.ycj28c.sshcommander.vo.ReadJson;

/**
 * a test read everything from configuration
 *
 */
public class SSHTest3 {
	
	public static void main(String[] args) throws JSchException {
		runFromScript();
	}
	
	public static void runFromScript() {
		File machineConfig = new File("C:/git/SSH-Commander/src/main/resources/machine/qa-tomcat.json");
		Machine ma = ReadJson.readMachineFromFile(machineConfig);
		SSHConnection sshConn = new SSHConnection(ma);
		
		try {
			Session session = sshConn.initializeSession();
			Channel channel = sshConn.createChannel("shell"); //exec not work?
			channel.setXForwarding(true);
			channel.connect();

			File scriptFile = new File("C:/git/SSH-Commander/src/main/resources/scripts/script-example2.sss");
			SSHScriptParser ssp = new SSHScriptParser();
			ArrayList<SSHScript> scripts = ssp.parse(scriptFile);
			for (int i = 0; i < scripts.size(); i++) {
				SSHScriptRunner.run(channel, session, scripts.get(i));
			}
			
			channel.disconnect();
			session.disconnect();
		} catch (JSchException e) {
			e.printStackTrace();
		}
	}
}
