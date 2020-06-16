package springboot.rpc;

import springboot.rpc.runnable.RpcRunnable;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 1.网络连接
 * 2.服务的注册发现
 * 3.反射
 * 4.序列化、反序列化
 */

//public class Producter implements SmartLifecycle {
public class Producter{

    private static final ExecutorService executor = Executors.newCachedThreadPool();

    public static void main(String[] args){
        start();
    }
//    @Override
    public static void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(9999);
            System.out.println("启动远程服务监听...");
            while (true){
                Socket socket = serverSocket.accept();
                try {
                    Constructor constructor = RpcRunnable.class.getConstructor(Socket.class);
                    Runnable runnable = (Runnable) constructor.newInstance(socket) ;
                    executor.execute(runnable);
                } catch (NoSuchMethodException e){
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    @Override
    public void stop() {

    }

//    @Override
    public boolean isRunning() {
        return false;
    }
}
