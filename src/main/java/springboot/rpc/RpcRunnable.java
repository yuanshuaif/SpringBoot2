package springboot.rpc;

import org.springframework.beans.factory.annotation.Autowired;
import springboot.rpc.service.UserService;
import springboot.rpc.service.impl.UserServiceIml;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

public class RpcRunnable implements Runnable{


    private Socket socket;

    public RpcRunnable(){

    }

    public RpcRunnable(Socket socket){
        this.socket = socket;
    }

    @Autowired
    public void run(){
        ObjectInputStream objectInputStream = null;
        ObjectOutputStream objectOutputStream = null;

        try {
            //1.读取客户端传输协议包
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            String className = objectInputStream.readUTF();
            String methodName = objectInputStream.readUTF();
            Class<?>[] parameterTypes = (Class<?>[]) objectInputStream.readObject();
            Object[] arguments = (Object[]) objectInputStream.readObject();

            //2.服务注册：API到具体实现的映射(使用注册中心，注册表)
            Class clazz = null;
            if (className.equals(UserService.class.getName())) {
                clazz = UserServiceIml.class;
            }

            //3.反射
            Method method = clazz.getMethod(methodName, parameterTypes);
            Object result = method.invoke(clazz.newInstance(), arguments);

            //4.序列化返回数据
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(result);
            objectOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e1){
            e1.printStackTrace();
        } catch (NoSuchMethodException e2){
            e2.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } finally {
            System.out.println("本次服务调用成功");
            try {
                if (objectInputStream != null) {
                    objectInputStream.close();
                }
                if (objectOutputStream != null) {
                    objectOutputStream.close();
                }
                if (socket != null) {
                    socket.close();
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
