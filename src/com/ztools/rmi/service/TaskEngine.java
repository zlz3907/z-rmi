package com.ztools.rmi.service;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ztools.rmi.beans.Task;
import com.ztools.rmi.beans.TaskReturnFormat;
import com.ztools.util.Util;
import com.ztools.xml.XMLWriter;

public class TaskEngine implements ITaskEngine {

  private Logger logger = LogManager.getLogger(TaskEngine.class);

  private Map<String, Object> executeHandlers = new HashMap<String, Object>();
  private Map<String, Method> acceptMethod = new HashMap<String, Method>();

  public TaskEngine() {

  }

  public TaskEngine(Object... handlers) {
    this(handlers, false);
  }
  
  public TaskEngine(Object[] handlers, boolean isAuto) {
    if (null != handlers) {
      for (Object h : handlers) {
        registryHandler(h);
      }
    }
  }

  @Override
  public Object executeTask(final Task t) {

    try {
      logger.debug("exe: " + t);
      Object exeObject = executeHandlers.get(t.getType());
      TaskReturnFormat trf = null != t.getReturnFormat() ?
        t.getReturnFormat() : TaskReturnFormat.Object;
      if (null != exeObject) {
        Method m = acceptMethod.get(t.getType() + "." + t.getName());
        if (null != m) {
          Object ret = m.invoke(exeObject, t.getArgs());
          if (null != ret)
            if (TaskReturnFormat.XMLString.equals(trf)) {
              return XMLWriter.objectToXmlString(ret);
            } else if (TaskReturnFormat.Object.equals(trf)) {
              return ret;
            } else if (TaskReturnFormat.ByteArray.equals(trf)) {
              return Util.objectToByteStream(ret);
            } else if (TaskReturnFormat.String.equals(trf)) {
              return ret.toString();
            }
          else
            logger.debug("Return is null!");
        } else
          logger.debug("Not found method: " + t.getName());
      } else
        logger.debug("Not found:" + t.getType());
    //exeObject.getClass().
    } catch (Exception e) {
      logger.error(e);
      e.printStackTrace();
    }

    return null;
  }

  public void registryHandler(Object handler) {
    if (null != handler) {
      executeHandlers.put(handler.getClass().getName(), handler);
      Method[] ms = extractMethods(handler);
      if (null != ms) {
        for (int msIndex = 0; msIndex < ms.length; msIndex++) {
          acceptMethod.put(handler.getClass().getName() + "."
                           + ms[msIndex].getName(), ms[msIndex]);
          logger.info("RRM: "
                      + handler.getClass().getName() + "."
                      + ms[msIndex].getName());
        }
      }

    }
  }

  private static Method[] extractMethods(final Object obj) {
    Class<?> c = obj.getClass();
    Method[] ms = null;// = obj.getClass().getDeclaredMethods();
    Method[] temp;
    do {
      Method[] cms = c.getDeclaredMethods();
      if (null == ms) {
        ms = cms;
      } else {
        temp = new Method[ms.length + cms.length];
        System.arraycopy(ms, 0, temp, 0, ms.length);
        System.arraycopy(cms, 0, temp, ms.length, cms.length);
        ms = temp;
      }
      c = c.getSuperclass();
    } while (null != c);
    return ms;
  }

}
