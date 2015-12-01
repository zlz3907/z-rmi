package com.ztools.rmi.beans;

import com.ztools.beans.ZBean;

/**
 *
 *
 * @author Zhong Lizhi
 */
public class Task extends ZBean {
  private static final long serialVersionUID = 1L;
  private String name; // method name
  private String type; // class name
  private Object[] args;

  private TaskReturnFormat returnFormat;

  public Task() {

  }

  public Task(String type, String name) {
    this.type = type;
    this.name = name;
  }

  public Task(String type, String name, Object... args) {
    this.type = type;
    this.name = name;
    this.args = args;
  }

  @Override
  public String toString() {
    StringBuilder sbd = new StringBuilder();
    if (null != args)
      for (Object o : args) {
        if (null != o)
          sbd.append(o.toString()).append("/");
      }
    return "Task: {type:" + type + ", name:" + name + ", args:"
        + sbd.toString();
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name
   *          the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return the type
   */
  public String getType() {
    return type;
  }

  /**
   * @param type
   *          the type to set
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * @return the args
   */
  public Object[] getArgs() {
    return args;
  }

  /**
   * @param args
   *          the args to set
   */
  public void setArgs(Object[] args) {
    this.args = args;
  }

  /**
   * @return the returnFormat
   */
  public TaskReturnFormat getReturnFormat() {
    return returnFormat;
  }

  /**
   * @param returnFormat
   *          the returnFormat to set
   */
  public void setReturnFormat(TaskReturnFormat returnFormat) {
    this.returnFormat = returnFormat;
  }
}
