package com.ztools.rmi.client;

import com.ztools.beans.RmiHandler;

public interface IHandlerFactory {
  RmiHandler fetchHandler();
  void setClazz(Class<?> clazz);
}
