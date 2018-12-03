package com.putact.zkregister;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.net.InetAddress;

@SpringBootApplication
public class ZkStudyApplicationT {
    private  static final String REGISTER_SERVICES = "/services";
    private static final String  SERVICE_NAME="/products";
    private static final String  connectString="127.0.0.1:2181";
   @Value("${server.port}")
   private int port;
    public static void main(String[] args) {
        SpringApplication.run(ZkStudyApplicationT.class, args);
    }

    @Bean
    public  ServletListenerRegistrationBean servletListenerRegistrationBean(){
        ServletListenerRegistrationBean servletListenerRegistrationBean = new ServletListenerRegistrationBean();
        servletListenerRegistrationBean.setListener(new ServletContextListener(){

            @Override
            public void contextInitialized(ServletContextEvent sce) {
                WebApplicationContextUtils.getRequiredWebApplicationContext(sce.getServletContext()).getAutowireCapableBeanFactory().autowireBean(this);

                try {
                    ZooKeeper zk = new ZooKeeper(connectString,5000,(watchedEvent)->{});
                    Stat state = zk.exists(REGISTER_SERVICES+SERVICE_NAME,false);
                    if (state==null){
                        zk.create(REGISTER_SERVICES+SERVICE_NAME,"".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
                    }

                    String hostName =InetAddress.getLocalHost().getHostAddress();
                    zk.create(REGISTER_SERVICES+SERVICE_NAME+"/child",(hostName+":"+port).getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return servletListenerRegistrationBean;
    }


}
