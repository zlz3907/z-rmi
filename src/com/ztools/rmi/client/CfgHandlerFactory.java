package com.ztools.rmi.client;

import java.util.Properties;

import com.ztools.beans.RmiHandler;
import com.ztools.conf.Configuration;
import com.ztools.conf.ConfigureReader;
import com.ztools.rmi.beans.ConfigureKey;
import com.ztools.rmi.service.ITaskEngine;
import com.ztools.util.LogWrapper;

public class CfgHandlerFactory implements IHandlerFactory {

  private Class<?> clazz = CfgHandlerFactory.class;

  public CfgHandlerFactory() {
    // TODO Auto-generated constructor stub
  }

  public CfgHandlerFactory(Object invoker) {
    if (null != invoker) {
      clazz = invoker.getClass();
    }
  }

  public Class<?> getClazz() {
    return clazz;
  }

  @Override
  public void setClazz(Class<?> clazz) {
    this.clazz = clazz;
  }

  @Override
  public RmiHandler fetchHandler() {
    // 获取这个项目的配置文件路径
    String cfgFilePath = Configuration
        .getConfigureFilePath(ConfigureKey.KEY_CFG_PATH);
    // 读取该模块相关的配置文件（这是一个配置入口，原则上代码里只会写死这一个路径)
    Properties prop = ConfigureReader.getPropByFilePath(cfgFilePath, clazz);

    // 从配置文件中获取RmiHandler配置文件的路径
    String rmiHdlFile = prop.getProperty(ConfigureKey.KEY_RMI_HANDLER);
    LogWrapper.trace(CfgHandlerFactory.class, "RmiHandleFile: " + rmiHdlFile);

    // 读取RmiHandler文件
    Object obj = ConfigureReader.getXmlResourceObjectByPath(rmiHdlFile,
        ITaskEngine.class);
    if (obj instanceof RmiHandler) {
      return (RmiHandler) obj;
    } else {
      LogWrapper.debug(CfgHandlerFactory.class, "RmiHandler is not found! "
          + obj);
    }
    return null;
  }
}
