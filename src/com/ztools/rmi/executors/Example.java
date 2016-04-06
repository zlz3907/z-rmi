package com.ztools.rmi.executors;

public class Example {
  public void sayHello() {
    System.out.println("RemoteServer:sayHello/> Hello!");
  }

  public void print(String str) {
    System.out.println("RemoteServer:print/> " + str);
  }

  public int giveMeNumber() {
    return (int) (Math.random() * Integer.MAX_VALUE);
  }
}
