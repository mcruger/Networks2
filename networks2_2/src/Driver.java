import java.io.IOException;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: danshiff
 * Date: 3/18/14
 * Time: 11:58 AM
 * To change this template use File | Settings | File Templates.
 */
public class Driver{

    private static Map<String, byte[]> resourceMap;
    private static Map<String, String> redirectMap;

    public static void main(String[] args){

        Map<String, String> flags = Utils.parseCmdlineFlags(args);


        if (!flags.containsKey("--serverPort") || !flags.containsKey("--sslServerPort")) {
            System.out.println("usage: Server --serverPort=12345 SSLServer --sslServerPort=23456");
            System.exit(-1);
        }


        int serverPort = -1;
        int sslServerPort = -1;
        try {
            serverPort = Integer.parseInt(flags.get("--serverPort"));
            sslServerPort = Integer.parseInt(flags.get("--sslServerPort"));
        } catch (NumberFormatException e) {
            System.out.println("Invalid port number! Must be an integer.");
            System.exit(-1);
        }

        try{
            loadResources();
        } catch(IOException e){
            System.out.println("Resources won't load");
            e.printStackTrace();
        }

        Thread normalServer = new ServerThread(serverPort, false, "Normal_Thread", resourceMap, redirectMap);
        Thread sslServer = new ServerThread(sslServerPort, true, "SSL_Thread", resourceMap, redirectMap);
        normalServer.start();
        sslServer.start();
    }

    public static void loadResources() throws IOException{
        resourceMap = ResourceMap.loadFiles();
        redirectMap = ResourceMap.loadRedirects();
    }
}
