package com.ml.util;
import java.io.IOException;
import java.io.InputStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;
import com.zehon.FileTransferStatus;
import com.zehon.scp.SCPClient;

/**
 * This SSH Library offers convenient methods for uploading/downloading files using command scp 
 * and exec command through ssh.
 */
public class SSHUtil {
	public static final String ROBOT_LIBRARY_SCOPE = "GLOBAL";
	public static final String ROBOT_LIBRARY_VERSION = "1.0.0";
	
	Session session = null;
	SCPClient sCliet = null;
    /** for waiting for the command to finish */
    private Thread thread = null;
	/** units are milliseconds, default is 0=infinite */
    private long maxwait = 0;
    
	/**
	 * Login SSH. The keyword could be used as test setup.
	 * 
	 * Example: 
	 * | sshLogin | 10.204.192.112 | root | 123123 |
	 * 
	 */
	public void sshLogin(String host, String user, String pwd) throws JSchException {
		JSch jsch = new JSch();
		session = jsch.getSession(user, host, 22);
		
		UserInfo ui = new DefaultUserInfo();
		
		session.setPassword(pwd);
		session.setConfig("StrictHostKeyChecking", "no");
		session.setUserInfo(ui);
		session.connect();
		
		sCliet = new SCPClient(host, user, pwd);
	}

	/**
	 * Execute command on remote linux os through ssh, Depends on sshLogin.
	 * Keyword will return the execute result( include all standard and error information)
	 * 
	 * Example: 
	 * | sshExec | ls -l |
	 * | sshExec | cat /root/test.txt |
	 * 
	 */
	public String sshExec(String command) throws IOException, JSchException, InterruptedException, SSHException {
		final Channel channel = session.openChannel("exec");
		((ChannelExec)channel).setPty(true);
		((ChannelExec)channel).setPtyType("vt100");
		((ChannelExec)channel).setCommand(command);
		channel.setInputStream(null);
		((ChannelExec)channel).setErrStream(System.err);
	    InputStream in  = channel.getInputStream();
	    InputStream errStream = ((ChannelExec)channel).getErrStream();
	    channel.connect();
	    
        // wait for it to finish
        thread =
            new Thread() {
                public void run() {
                    while (!channel.isEOF()) {
                        if (thread == null) {
                            return;
                        }
                        try {
                            sleep(500);
                        } catch (Exception e) {
                            // ignored
                        }
                    }
                }
            };
        thread.start();
        thread.join(maxwait);
        
    	StringBuffer strBuffer = new StringBuffer();
        if (thread.isAlive()) {
            // run out of time
            thread = null;
            strBuffer.append("timeout");
        } else {
            // completed successfully
        	byte[] tmp = new byte[1024];
    	    while (true) {
    	    	 while (errStream.available() > 0) {
					int i = errStream.read(tmp, 0, 1024);
					if (i < 0) {
						break;
					}
					strBuffer.append(new String(tmp, 0, i));
				}
    	    	while (in.available() > 0){
    	    		int i = in.read(tmp, 0, 1024);
    	    		if(i < 0) break;
    	    		strBuffer.append(new String(tmp, 0, i));
    	        }
    	    	System.out.println(strBuffer.toString());
    	    	if(channel.isClosed()){
    	        	int ec = channel.getExitStatus();
    	        	if (ec != 0) {
    	        		try{
    	                	throw new SSHException("Remote command failed with exit status " + ec);
    	                } catch (SSHException e){
    	                	System.out.println(e.getMessage());
    	                	break;
    	                }
    	        	}
    	        	System.out.println("exit-status: " + channel.getExitStatus());
    	        	break;
    	        }
    	        try{Thread.sleep(1000); }catch(Exception ee){}
    	    }
    	    channel.disconnect();
        }
        if(strBuffer.toString().contains("timeout")){
        	throw new SSHException("Timeout period exceeded, connection dropped.");
        }
        return strBuffer.toString();
	    
	}
	
	protected String sshExecNoPty(String command) throws IOException, JSchException, InterruptedException, SSHException {
		final Channel channel = session.openChannel("exec");
		((ChannelExec)channel).setCommand(command);
		channel.setInputStream(null);
		((ChannelExec)channel).setErrStream(System.err);
	    InputStream in  = channel.getInputStream();
	    InputStream errStream = ((ChannelExec)channel).getErrStream();
	    channel.connect();
	    
        // wait for it to finish
        thread =
            new Thread() {
                public void run() {
                    while (!channel.isEOF()) {
                        if (thread == null) {
                            return;
                        }
                        try {
                            sleep(500);
                        } catch (Exception e) {
                            // ignored
                        }
                    }
                }
            };
        thread.start();
        thread.join(maxwait);
        
    	StringBuffer strBuffer = new StringBuffer();
        if (thread.isAlive()) {
            // run out of time
            thread = null;
            strBuffer.append("timeout");
        } else {
            // completed successfully
        	byte[] tmp = new byte[1024];
    	    while (true) {
    	    	 while (errStream.available() > 0) {
					int i = errStream.read(tmp, 0, 1024);
					if (i < 0) {
						break;
					}
					strBuffer.append(new String(tmp, 0, i));
				}
    	    	while (in.available() > 0){
    	    		int i = in.read(tmp, 0, 1024);
    	    		if(i < 0) break;
    	    		strBuffer.append(new String(tmp, 0, i));
    	        }
    	    	System.out.println(strBuffer.toString());
    	    	if(channel.isClosed()){
    	        	int ec = channel.getExitStatus();
    	        	if (ec != 0) {
    	        		try{
    	                	throw new SSHException("Remote command failed with exit status " + ec);
    	                } catch (SSHException e){
    	                	System.out.println(e.getMessage());
    	                	break;
    	                }
    	        	}
    	        	System.out.println("exit-status: " + channel.getExitStatus());
    	        	break;
    	        }
    	        try{Thread.sleep(1000); }catch(Exception ee){}
    	    }
    	    channel.disconnect();
        }
        if(strBuffer.toString().contains("timeout")){
        	throw new SSHException("Timeout period exceeded, connection dropped.");
        }
        return strBuffer.toString();
	    
	}
	
	/**
	 * Disconnect the SSH connect. The keyword could be used as test teardown. 
	 * 
	 * Example: 
	 * | sshExit |
	 */
	public void sshExit() {
		if(session != null) {
			session.disconnect();
		}
		if(sCliet != null) {
			sCliet = null;
		}
	}
	
	/**
	 * Retrieve a remote file specified by remoteFolder/remoteFile 
	 * and buffer it and write a local folder called localFolder.
	 * 
	 * Example: 
	 * | scpDownloadFile | /root | test.txt | c:\\myfiles\ |
	 */
	public void scpDownloadFile(String remoteFolder, String remoteFile, String localFolder) throws Exception {
		if(sCliet == null) {
			throw new SSHException("please login first!");
		}
		int status = sCliet.getFile(remoteFile, remoteFolder, localFolder);
		if (FileTransferStatus.SUCCESS != status) {
			throw new SSHException("SCP get File Failed");
		}
	}
	
	/**
	 * Read in a file specified by localFilePath, 
	 * and scp it to a remote scp folder specified by scpDestFolder, 
	 *
	 * Example: 
	 * | scpUploadFile | /root | test.txt | c:\\myfiles\text.txt |
	 * | scpUploadFile | /root | test2.jpg | /home/joe/images/img.jpg |
	 * 
	 */
	public void scpUploadFile(String destFolder, String newFileName, String localFilePath) throws Exception {
		if(sCliet == null) {
			throw new SSHException("please login first!");
		}
		int status = sCliet.sendFile(localFilePath, destFolder, newFileName);
		if (FileTransferStatus.SUCCESS != status) {
			throw new SSHException("SCP upload File Failed");
		}
	}
	
	/*
	public static void main(String[] args) throws JSchException, IOException, Exception {
		SSHLibrary ssh = new SSHLibrary();
		//ssh.scpUploadFile("10.204.193.112", "root", "123123", "/root", "case.txt", "d:\\case.txt");
		ssh.sshLogin("10.204.192.64", "root", "111111");
		//ssh.sshExec("10.204.192.112", "root", "123123", "scp -t test.log");
		//ssh.scpUploadFile("/root", "case.txt", "d:\\case.txt");
		//ssh.execSQLInFile("d:\\test.sql","wsg", "", "test");
		//ssh.execSQLInRobot("","wsg", "wsg", "test");
		ssh.sshExec("nohup /opt/trend/wsg/script/devdetect.sh restart");
		ssh.sshExit();
	}
	*/
	
	
	
	
	private static class DefaultUserInfo implements UserInfo, UIKeyboardInteractive {
		public String[] promptKeyboardInteractive(String destination, String name,
				String instruction, String[] prompt, boolean[] echo) {
			return null;
		}

		public String getPassphrase() {
			return null;
		}

		public String getPassword() {
			return null;
		}

		public boolean promptPassphrase(String message) {
			return false;
		}

		public boolean promptPassword(String message) {
			return false;
		}

		public boolean promptYesNo(String message) {
			return false;
		}

		public void showMessage(String message) {	
		}
		
	}
}