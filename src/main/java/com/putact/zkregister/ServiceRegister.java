package com.putact.zkregister;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;

public class ServiceRegister {

    private  static final String REGISTER_SERVICES = "/services";
    private static final String  SERVICE_NAME="/products";
    private static final String  connectString="127.0.0.1:2181";



    public static void registerService(String ip,String port){

        try {
            ZooKeeper zk = new ZooKeeper(connectString,5000,null);
            Stat state = zk.exists(REGISTER_SERVICES+SERVICE_NAME,false);
            if (state==null){
                zk.create(REGISTER_SERVICES+SERVICE_NAME,"".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
            }
            zk.create(REGISTER_SERVICES+SERVICE_NAME+"/child",(ip+port).getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
