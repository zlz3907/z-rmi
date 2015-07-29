package com.ztools.rmi.service;

import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import com.ztools.beans.RmiHandler;
import com.ztools.conf.Configuration;
import com.ztools.conf.ConfigureReader;
import com.ztools.rmi.beans.ConfigureKey;
import com.ztools.rmi.client.CfgHandlerFactory;
import com.ztools.util.LogWrapper;

public class RmiService {
  private static final String JAVA_RMI_SERVER_HOSTNAME = "java.rmi.server.hostname";
  //private static final String RMIHDL_PATH = "cfg.xml.ztools.z-rmi.rmihandler";
  private static ITaskEngine engine;

  private static Object[] fetchRemoteExecutors() {
    // 获取这个项目的配置文件路径
    String cfgFilePath = Configuration
        .getConfigureFilePath(ConfigureKey.KEY_CFG_PATH);
    // 读取该模块相关的配置文件（这是一个配置入口，原则上代码里只会写死这一个路径)
    Properties prop = ConfigureReader.getPropByFilePath(cfgFilePath, RmiService.class);

    // 从配置文件中获取RmiHandler配置文件的路径
    Set<Object> keys = prop.keySet();
    List<Object> executors = new ArrayList<>();
    for (Object k : keys) {
      if (k.toString().startsWith(ConfigureKey.KEY_PREFIX_REMOTE_EXECUTOR)) {
        String v = prop.getProperty(k.toString());
        LogWrapper.trace(RmiService.class, "find executor: " + v);
        try {
          Class<?> c = Class.forName(v);
          Object o = c.newInstance();
          executors.add(o);
        } catch (Exception e) {
          // TODO Auto-generated catch block
          //e.printStackTrace();
          LogWrapper.error(RmiService.class, e);
        }
      }
    }
    return executors.toArray();
  }
  
  public static void main(String[] args) {
    // if (System.getSecurityManager() == null) {
    // System.setSecurityManager(new SecurityManager());
    // }
    
    
    // String rmiHdlFile = Configuration.getConfigureFilePath(RMIHDL_PATH);
    //
    // LogWrapper.trace(RmiService.class, "RmiHandleFile: " + rmiHdlFile);
    //
    // Object obj = ConfigureReader.getXmlResourceObjectByPath(rmiHdlFile,
    // ITaskEngine.class);
    Object obj = new CfgHandlerFactory(RmiService.class).fetchHandler();
    if (obj instanceof RmiHandler)
      try {

        RmiHandler rmiHandler = (RmiHandler) obj;
        // FIXME: 添加配置文件，实现功能扩展
        // 核心包关闭，功能扩展通过配置完整的类名来开放。
        // 使用类的反射机制来加载扩展功能，本引擎不考虑
        // 安全问题，所有的安全问题（如扩展类是否能被注册
        // 执行等）都交由安全框架统一考虑。
        // User st = new User("Bliss");
        
        
        engine = new TaskEngine(fetchRemoteExecutors());
        System.setProperty(JAVA_RMI_SERVER_HOSTNAME, rmiHandler.getHost());
        LogWrapper.debug(RmiService.class, rmiHandler);
        Registry registry = LocateRegistry.createRegistry(rmiHandler.getPort());
        Remote stub = UnicastRemoteObject.exportObject(engine, 0);
        // registry = LocateRegistry.getRegistry();

        registry.bind(rmiHandler.getName(), stub);
        LogWrapper.info(RmiService.class, "TaskEngine bound");

      } catch (Exception e) {
        // System.err.println("TaskEngine exception:");
        // e.printStackTrace();
        LogWrapper.error(RmiService.class, e);
      }
  }
}
