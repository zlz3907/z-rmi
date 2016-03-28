package com.ztools.rmi;

import com.ztools.rmi.service.RmiService;
import com.ztools.rmi.client.RmiClient;

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
        RmiService.main(args);
        RmiService.isConsole = true;
        i++; // shift
        break;
      } else if ("-e".equals(arg) || "--execute".equals(arg)) {
        // -e <handler_name> [arg1 arg2 ...]
        System.out.println("execute: ...");
        sendExecuteTask(args, false);
        break;
      } else if ("-w".equals(arg) || "--waitexe".equals(arg)) {
        System.out.println("waiting response: ...");
        break;
      }
    }
  }

  private static void sendExecuteTask(final String[] args, boolean isWait) {
    RmiClient client = new RmiClient();
    String handler = null;
    String requestMethod = null;
    String taskArgs = null;
    for (int i = 0; (null != args) && i < args.length;) {
      String arg = args[i];
      if ("-h".equals(arg) || "--handler".equals(arg)) {
        handler = arg;
        i++; // shift
        continue;
      } else if ("-m".equals(arg) || "--method".equals(arg)) {
        requestMethod = arg;
        i++;
        continue;
      } else if ("-a".equals(arg) || "--args".equals(arg)) {
        i++;
        // 构造Task并提交执行
        break;
      }
    }

    if (null != handler && null != requestMethod) {
      try {
        System.out.println("----remote method invoke----");
        System.out.println("handler: " + handler);
        System.out.println(" method: " + requestMethod);
        Object obj = client.execute(handler, requestMethod);
        System.out.println(" result: " + obj);
        System.out.println("----end----");
      } catch (Exception ex) {
        ex.printStackTrace();
      }

    }

  }
}
