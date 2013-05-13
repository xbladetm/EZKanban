package ezkanban.HTTPServer;
import java.net.*;

public class HTTPServer{

	public static void crash(String error){
		System.err.println(error);
		System.exit(1);        
	}


	@SuppressWarnings("unused")
	public static void main(String[] args)
			throws Exception
			{
		if (args.length < 1){ 
			System.out.println("Usage: httpdConcurrent [port]"); 
			return;
		}

		String host = "localhost";
		int port = Integer.parseInt(args[0]);

		@SuppressWarnings("resource")
		ServerSocket server = new ServerSocket(port);
		while (true){
			Socket client = server.accept();
			HTTPRequest request = new HTTPRequest(client);
			request.start();                     
		}
			}
}


