package com.ztools.rmi.client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Properties;

import com.ztools.beans.RmiHandler;
import com.ztools.conf.Configuration;
import com.ztools.conf.ConfigureReader;
import com.ztools.rmi.beans.ConfigureKey;
import com.ztools.rmi.beans.Task;
import com.ztools.rmi.service.ITaskEngine;
import com.ztools.util.LogWrapper;

public class RmiClient {
  private static final String RMIHDL_PATH = "cfg.xml.elco.rmihandler";
  private static ITaskEngine controler = null;

  private Class<?> clazz = RmiClient.class;

  private static Object lock = new Object();

  private int findCount = 0;

  private RmiHandler rmiHandler = null;

  public RmiClient() {
    this(null);
  }

  public RmiClient(RmiHandler handler) {
    this(handler, null);
  }

  public RmiClient(RmiHandler handler, Class<?> clazz) {
    LogWrapper.entry(RmiClient.class, "Initial RmiClient");
    if (null != clazz)
      this.clazz = clazz;

    if (null == handler) {

      // // 找协同服务来获取服务器的句柄
      // ElccClient elcc = new ElccClient();
      //
      // try {
      // AbsHandler remoteHandler = elcc.getResourceHandler("ELCO");
      // if (remoteHandler instanceof RmiHandler) {
      // this.rmiHandler = (RmiHandler) remoteHandler;
      // LogWrapper.trace(RmiClient.class, this.rmiHandler.toString());
      // return;
      // }
      // } catch (RemoteException e) {
      // LogWrapper.error(this.getClass(), e);
      // //e.printStackTrace();
      // }

      // 获取这个项目的配置文件路径
      String cfgFilePath = Configuration
          .getConfigureFilePath(ConfigureKey.KEY_CFG_PATH);
      // 读取该模块相关的配置文件（这是一个配置入口，原则上代码里只会写死这一个路径)
      Properties prop = ConfigureReader.getPropByFilePath(cfgFilePath,
          this.clazz);

      // 从配置文件中获取RmiHandler配置文件的路径
      String handlerFactoryPath = prop.getProperty(IHandlerFactory.class
          .getName());

      if (null != handlerFactoryPath)
        try {
          Class<?> c = Class.forName(handlerFactoryPath);
          Object o = c.newInstance();
          if (o instanceof IHandlerFactory) {
            IHandlerFactory hf = (IHandlerFactory) o;
            hf.setClazz(RmiClient.class);
            this.rmiHandler = hf.fetchHandler();
            if (null != rmiHandler)
              return;
          }
        } catch (Exception e) {
          // e.printStackTrace();
          LogWrapper.error(RmiClient.class, e);
        }

      Object obj = new CfgHandlerFactory(RmiClient.class).fetchHandler();
      if (obj instanceof RmiHandler) {
        handler = (RmiHandler) obj;
      }

    }
    this.rmiHandler = handler;
  }

  public ITaskEngine findService(boolean isMustBeExecuted) {

    if (++findCount > Integer.MAX_VALUE) {
      return controler;
    }

    if (null == controler) {
      synchronized (lock) {
        if (null == controler) {
          Registry registry;
          try {
            registry = LocateRegistry.getRegistry(rmiHandler.getHost(),
                rmiHandler.getPort());
            controler = (ITaskEngine) registry.lookup(rmiHandler.getName());
          } catch (RemoteException e) {
            e.printStackTrace();
            LogWrapper.error(this.getClass(), e.getMessage());
          } catch (NotBoundException e) {
            e.printStackTrace();
            LogWrapper.error(this.getClass(), e.getMessage());
          }
        }
      }
    }

    if (null == controler) {
      try {
        if (rmiHandler != null) {
          LogWrapper.trace(this.getClass(),
              "Remote server no response ... please wait [" + rmiHandler + "] "
                  + findCount);
        } else {
          LogWrapper
              .trace(this.getClass(),
                  "rmiHandler is null, please check configure file: "
                      + RMIHDL_PATH);
        }

        if (isMustBeExecuted) {
          Thread.sleep(15000);
          findService(isMustBeExecuted);
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    } else {
      findCount = 0;
    }
    return controler;
  }

  public Object execute(Task t) throws RemoteException {
    return execute(t, false);
  }

  public Object execute(Task t, boolean isMustBeExecuted)
      throws RemoteException {
    if (null != t) {
      findService(isMustBeExecuted);
      if (null != controler) {
        try {
          Object ret = controler.executeTask(t);
          return ret;
        } catch (RemoteException e) {
          LogWrapper.error(this.getClass(), e);
          controler = null;
          if (isMustBeExecuted) {
            try {
              Thread.sleep(1500);
            } catch (InterruptedException e1) {
              LogWrapper.error(this.getClass(), e1);
            }
            return execute(t, isMustBeExecuted);
          }
        }
      }
    }
    return null;
  }

  public Object execute(String executeHandlerKey, String method, Object... args)
      throws RemoteException {
    Task t = new Task(executeHandlerKey, method, args);
    return execute(t, false);
  }

  public static void main(String[] args) {
    RmiClient client = new RmiClient();
    try {
      client.execute(new Task("com.ztools.rmi.executors.Example", "sayHello"), true);
      client.execute(new Task("com.ztools.rmi.executors.Example", "print",
          "Hello"));

    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

}
