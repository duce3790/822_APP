import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private static int serverport = 5050;     //自訂的 Port
    private static ServerSocket serverSocket; //伺服端的Socket
    private static int count=0; //計算有幾個 Client 端連線

    // 用 ArrayList 來儲存每個 Client 端連線
    private static ArrayList clients = new ArrayList();

    public static void main(String[] args) {
        try {
            serverSocket = new ServerSocket(serverport);
            System.out.println("Server is start.");
            // 顯示等待客戶端連接
            System.out.println("Waiting for client connect");
            // 當Server運作中時
            while (!serverSocket.isClosed()) {
                // 呼叫等待接受客戶端連接
                waitNewClient();
            }
        } catch (IOException e) {
            System.out.println("Server Socket ERROR");
        }
    }

    // 等待接受 Client 端連接
    public static void waitNewClient() {
        try {
            Socket socket = serverSocket.accept();
            ++count;
            System.out.println("現在使用者個數："+count);

            // 呼叫加入新的 Client 端
            addNewClient(socket);

        } catch (IOException e) {}
    }

    // 加入新的 Client 端
    public static void addNewClient(final Socket socket) throws IOException {
        // 以新的執行緒來執行
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 增加新的 Client 端
                    clients.add(socket);
                    // 取得網路串流
                    BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    // 當Socket已連接時連續執行
                    while (socket.isConnected()) {
                        // 取得網路串流的訊息
                        String msg= br.readLine();

                        if(msg==null){
                            System.out.println("Client Disconnected!");
                            break;
                        }
                        //輸出訊息
                        System.out.println(msg);
                        // 廣播訊息給其它的客戶端
                        castMsg(msg);
                    }
                } catch (IOException e) {
                    e.getStackTrace();
                }
                finally{
                    // 移除客戶端
                    clients.remove(socket);
                    --count;
                    System.out.println("現在使用者個數："+count);
                }
            }
        });

        // 啟動執行緒
        t.start();
    }

    // 廣播訊息給其它的客戶端
    public static void castMsg(String Msg){
        // 創造socket陣列
        Socket[] clientArrays =new Socket[clients.size()];
        // 將 clients 轉換成陣列存入 clientArrays
        clients.toArray(clientArrays);
        // 走訪 clientArrays 中的每一個元素
        for (Socket socket : clientArrays ) {
            try {
                // 創造網路輸出串流
                BufferedWriter bw;
                bw = new BufferedWriter( new OutputStreamWriter(socket.getOutputStream()));
                // 寫入訊息到串流
                bw.write(Msg+"\n");
                // 立即發送
                bw.flush();
            } catch (IOException e) {}
        }
    }
}
