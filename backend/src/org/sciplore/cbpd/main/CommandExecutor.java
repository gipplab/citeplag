package org.sciplore.cbpd.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.sciplore.preamble.License;


/**
* Executor that executes a UNIX-style command and returns output in exec() method.<br> 
* The code was partially adopted from http://www.rgagnon.com/javadetails/java-0014.html.
*/
@License (author="Mario Lipinski")

public class CommandExecutor {
	
	/** The input command. */
	private ProcessBuilder pb;
	
	/**
	 * Instantiates a new command executor.
	 *
	 * @param command Unix-style command input
	 */
	public CommandExecutor(String... command) {
		pb = new ProcessBuilder(command);
	}
	
	public CommandExecutor(ProcessBuilder pb) {
		this.pb = pb;
	}
	
	/**
	 * Executes the command in the runtime environment.
	 *
	 * @return Output as a result of the command execution.
	 * @throws Exception 
	 * @throws IOException 
	 */
	public String exec() throws Exception {
		Process p = null;
		StringBuilder output = new StringBuilder();
		
		try {
			p = pb.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		InputStream stdout = p.getInputStream();
		BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(stdout, "UTF-8"));
		
		InputStream stderr = p.getErrorStream();
		BufferedReader stderrReader = new BufferedReader(new InputStreamReader(stderr, "UTF-8"));
		
		int timeout = 10;
		while (timeout > 0) {
			try {
				int character;
				while ((character = stdoutReader.read()) >= 0) {
					output.append((char)character);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				p.exitValue();
				break;
			} catch (IllegalThreadStateException e) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e2) {
					e2.printStackTrace();
				} finally {
					timeout--;
				}
			}
		}
		
		try {
			int character;
			while ((character = stdoutReader.read()) >= 0) {
				output.append((char)character);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				stdoutReader.close();
				stdout.close();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		
		StringBuilder error = new StringBuilder();
		try {
			int character;
			while ((character = stderrReader.read()) >= 0) {
				error.append((char)character);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				stderrReader.close();
				stderr.close();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			if (p.exitValue() != 0) {
				System.err.println(pb.command());
				System.err.println(error);
				System.err.println(output);
				throw new Exception("Process exited with status " + p.exitValue() + ".");
			}
		} catch (IllegalThreadStateException e) {
			System.err.println("Timout.");
			p.destroy();
		}
		
		return output.toString();
	}
}
