package com.ztools.rmi;

public class Main {
  public static void main(String[] args) {
    System.out.println("开始了");
    for (int i = 0; (null != args) && i < args.length; i++)
      System.out.println("参数d：" + args[i]);

    for (int i = 0; (null != args) && i < args.length;) {
      String arg = args[i];
      if ("-s".equals(arg) || "--server".equals(arg)) {
        // TODO: start services
        System.out.println("start service...");
        i++; // shift
        continue;
      } else if ("-e".equals(arg) || "--execute".equals(arg)) {
        // -e <handler_name> [arg1 arg2 ...]
        System.out.println("execute: ...");
        break;
      } else if ("-w".equals(arg) || "--waitexe".equals(arg)) {
        System.out.println("waiting response: ...");
        break;
      }
    }
  }
}
