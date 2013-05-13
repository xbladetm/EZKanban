package ezkanban.HTTPServer;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

class HTTPRequest extends Thread{

	public static String contentType(String filename){
		if (filename.endsWith(".htm") || filename.endsWith(".html"))
			return "text/html";
		else if (filename.endsWith(".txt"))
			return "text/plain";
		else if (filename.endsWith(".gif"))
			return "image/gif";
		else if (filename.endsWith(".jpg") || filename.endsWith(".jpeg"))
			return "image/jpeg";
		else if (filename.endsWith(".mp3"))
			return "audio/x-mp3";
		else if (filename.endsWith(".wav"))
			return "audio/x-wav";
		else if (filename.endsWith(".mid"))
			return "audio/x-midi";
		else if (filename.endsWith(".pdf"))
			return "application/pdf";
		else
			return "application/octet-stream";
	}

	@SuppressWarnings("resource")
	public static int getFile(String filename, Socket client, PrintWriter output)
			throws UnknownHostException, IOException {
		FileInputStream finput = null;
		byte[] buffer = new byte[1024];
		int bytes;

		filename = "./"+filename;

		try{
			finput = new FileInputStream(filename);
		} catch (FileNotFoundException e) {
			System.out.println("[NOT FOUND]");
			output.println("HTTP/1.0 404 Not Found");
			output.println();
			output.println("<html><body><h1>404 Not Found</h1></body></html>");
			return 1;
		}

		output.println("HTTP/1.0 200 OK");

		File file = new File(filename);
		String type = contentType(filename);
		long size = file.length();

		output.println("Content-Length: "+size);
		output.println("Content-Type: "+type);
		output.println();

		if (filename.length() > 2)
			while((bytes = finput.read(buffer)) != -1 )
				client.getOutputStream().write(buffer, 0, bytes); 

		return 0;
	}

	Socket client;
	BufferedReader input;
	PrintWriter output;

	public HTTPRequest(Socket s){
		//get communication channel
		this.client = s;

		System.setProperty("line.separator", "\r\n"); 
		try {
			this.input = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
			this.output = new PrintWriter(this.client.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

	public void run(){

		String command = null;
		String filename = null;
		String protocol = null;

		try{
			//get client query
			String query = this.input.readLine();
			StringTokenizer tokenizer = new StringTokenizer(query);

			//wait for blank line
			String crlf = this.input.readLine();
			while(!crlf.equals(""))
				crlf = this.input.readLine();

			command = tokenizer.nextToken();
			filename = tokenizer.nextToken();
			protocol = tokenizer.nextToken();

			if (command.equals("GET")){
				System.out.println("[GET] "+filename);
				getFile(filename, this.client, this.output);
			}

			else {
				System.out.println("[PROTOCOL NOT SUPPORTED] "+protocol);
				this.output.println("HTTP/1.0 501 Not Implemented");
			}

			this.client.close();

		} catch (NoSuchElementException e) {
			System.out.println("[BAD REQUEST]");
			this.output.println("HTTP/1.0 400 Bad Request");
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
