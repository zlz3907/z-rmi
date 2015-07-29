package com.ztools.rmi.service;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.ztools.rmi.beans.Task;

public interface ITaskEngine extends Remote {
  Object executeTask(final Task t) throws RemoteException;
}
