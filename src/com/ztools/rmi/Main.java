package com.ztools.rmi;

import com.ztools.rmi.service.RmiService;
import com.ztools.rmi.client.RmiClient;
import java.util.List;
import java.util.ArrayList;
import com.ztools.rmi.beans.Task;

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
        sendExecuteTask(args, true);
        break;
      }
    }
  }

  private static void sendExecuteTask(final String[] args, boolean isWait) {

    RmiClient client = new RmiClient();
    String handler = null;
    String requestMethod = null;
    //String taskArgs = null;
    List<String> taskArgs = new ArrayList<>();
    for (int i = 0; (null != args) && i < args.length;) {
      String arg = args[i];
      if ("-e".equals(arg) || "--execute".equals(arg)) {
        i++; // shift
        if (i < args.length) {
          handler = args[i];
          i++;
        }
        continue;
      } else if ("-m".equals(arg) || "--method".equals(arg)) {
        i++;
        if (i < args.length) {
          requestMethod = args[i];
          i++;
        }
        continue;
      } else if ("-a".equals(arg) || "--args".equals(arg)) {
        i++;
        // 构造Task并提交执行
        break;
      } else {
        taskArgs.add(arg);
        i++;
      }
    }

    if (null != handler && null != requestMethod) {
      try {
        System.out.println("----remote method invoke----");
        System.out.println("handler: " + handler);
        System.out.println(" method: " + requestMethod);
        Task t = new Task(handler, requestMethod,
                          taskArgs.toArray(new Object[taskArgs.size()]));
        Object obj = client.execute(t, isWait);
        System.out.println(" result: " + obj);
        System.out.println("----end----");
      } catch (Exception ex) {
        ex.printStackTrace();
      }

    } else {
      System.out.println("error!");
    }

  }
}
