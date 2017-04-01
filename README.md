# SSH-Commander [![Build Status](https://travis-ci.org/ycj28c/SSH-Commander.svg?branch=master)](https://travis-ci.org/ycj28c/SSH-Commander)
A SSH scripts tools able to run in command line by configured machine and scripts

# Machine setting
```json
{
	"hostname":	"qa-scripts.ycj28c.io",
	"ip":		"1.1.1.1",
	"port": 	22,
	"username": 	"111",
	"password": 	"111"
}
```

# Script Setting
```text
###########################################################
# the target machine
###########################################################
Machine:
	qa-tomcat
	
Group:?
Suite:?
###########################################################
# the running mode: 
# SingleTerminal: run commands in sequence in one channel
# MultipleTerminal: run commands in multiple channel
###########################################################
Mode:
	SingleTerminal
	
###########################################################
# Timeout setting for this test
###########################################################
TimeOut(seconds):
	global[timeout]

###########################################################
# The command run in terminal
###########################################################	
Commands:
	Run("ls -l; cd /opt/tomcat/logs;ls -l")
	Equals("hostname", "tomcat.ycj28c.io", 60)
	Run("grep Git-Commit /opt/tomcat/webapps/ws/META-INF/MANIFEST.MF", 60)
	Contains("cd /opt/tomcat/logs;tail -10f ws.log", "startup", 60)
```
