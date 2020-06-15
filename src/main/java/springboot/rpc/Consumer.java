package springboot.rpc;

import springboot.rpc.entity.User;
import springboot.rpc.service.UserService;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;

/**
 * 1.动态代理
 * 2.网络连接socket
 * 3.序列化、反序列化
 */
public class Consumer {

    public static void main(String[] args){
        UserService userService = (UserService)getProxyObject(UserService.class);
        User user = userService.findUser();
        System.out.println(user);
    }

    public static Object getProxyObject(Class clazz){
        return Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Object o = null;
                ObjectOutputStream objectOutputStream = null;
                ObjectInputStream objectInputStream = null;
                try {
                    //获取服务列表，负载均衡

                    Socket socket = new Socket("10.6.245.163", 9999);
                    String className = clazz.getName();//api类名
                    String methodName = method.getName();//api 类成员方法名
                    Class<?>[] parameterTypes = method.getParameterTypes(); //类成员方法参数类型集合

                    objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                    objectOutputStream.writeUTF(className);
                    objectOutputStream.writeUTF(methodName);
                    objectOutputStream.writeObject(parameterTypes);
                    objectOutputStream.writeObject(args);

                    objectInputStream = new ObjectInputStream(socket.getInputStream());
                    o = objectInputStream.readObject();
                }catch (Exception e){
                    e.printStackTrace();
                } finally {
                    if(objectInputStream != null) {
                        objectInputStream.close();
                    }
                    if(objectOutputStream != null) {
                        objectOutputStream.close();
                    }
                }

                return o;
            }
        });
    }

}
