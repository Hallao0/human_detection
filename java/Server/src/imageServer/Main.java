package imageServer;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.imageio.ImageIO;

class StreamImages implements Runnable {
	private Socket connectionSocket;
	private BufferedOutputStream str;
	private String filename;
	public StreamImages(Socket connectionSocket, String filename) throws IOException {
		this.connectionSocket = connectionSocket;
		str = new BufferedOutputStream(connectionSocket.getOutputStream());
		this.filename = filename;
	}

	@Override
	public void run() {
		while(true) {
			BufferedImage img;
			try {
				img = ImageIO.read(new File(filename));
				ByteArrayOutputStream bb = new ByteArrayOutputStream();
				
				for (int i = 0; i < img.getHeight(); i++) {
					String s = "";
					for (int j = 0; j < img.getWidth(); j++) {
						s = s + img.getRGB(j, i) + " ";
					}
					if (i == img.getHeight() - 1) {
						s = s + "\n";
					}
					else {
						s = s + ",";
					}
					
					bb.write(s.getBytes());
				}
				bb.flush();
				
				str.write(bb.toByteArray());
				str.flush();
				
				bb.close();
				
				//Thread.sleep(5000);
				Thread.sleep(1000);
				
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
				try {
					str.close();
					connectionSocket.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				return;
			}
		}
	}
	
}

public class Main {
	
	public static String serialize(BufferedImage img) {
		String res = "" + img.getWidth() + "\n" + img.getHeight();
		for (int i = 0; i < img.getHeight(); i++) {
			for (int j = 0; j < img.getWidth(); j++) {
				res = res + "\n" + img.getRGB(j, i); 
			}
		}
		return res;
	}
	
	public static void main(String [] args) throws IOException {
		ServerSocket welcomeSocket = new ServerSocket(Integer.parseInt(args[1]));
        
		while (true) {
			Socket connectionSocket = welcomeSocket.accept();
			new Thread(new StreamImages(connectionSocket, args[0])).start();
		}
		
        
        
        /*BufferedOutputStream str = new BufferedOutputStream(connectionSocket.getOutputStream());
        
        System.out.println("connection");
        
        int index = 1;
        while (true) {
        	
			try {
				BufferedImage img = ImageIO.read(new File("/home/v/Desktop/cam/asd.png"));
				
				//ByteArrayOutputStream baos = new ByteArrayOutputStream();
				//ImageIO.write( img, "png", baos );
				//baos.flush();
				//byte[] imageInByte = baos.toByteArray();
				//baos.close();
				ByteArrayOutputStream bb = new ByteArrayOutputStream();
				
				for (int i = 0; i < img.getHeight(); i++) {
					String s = "";
					for (int j = 0; j < img.getWidth(); j++) {
						s = s + img.getRGB(j, i) + " ";
					}
					s = s + "\n";
					
					bb.write(s.getBytes());
				}
				bb.flush();
				
				str.write(bb.toByteArray());
				str.flush();
				
				bb.close();
				
				Thread.sleep(5000);
			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
	
		*/
	}
}
