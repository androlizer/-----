package org.chinamil.ui.library;

import java.util.LinkedList;

/**
 * 定义一个解压服务 当zip文件下载完后启动
 * 
 * @author Administrator
 * 
 */

public class zipDown extends Thread {

    private static LinkedList<Integer> donwList = null;
    public static zipDown down = null;
    static {
        donwList = new LinkedList<Integer>();
        ;
    }

    private zipDown(Runnable r) {
        super(r);
    }

    public static zipDown getinstace(Runnable r) {
        if (down == null) {
            down = new zipDown(r);
        }
        return down;

    }
    /**
     * 判断是否有任务在下载
     * null 则为无
     * 
     * @return
     */
    public static zipDown getinstace() {
              return down;
           }
    @SuppressWarnings("rawtypes")
    public static LinkedList getList() {
        
        return donwList;
       }

    @Override
    public void run() {
        while (!donwList.isEmpty()) {
            System.out.println(donwList.get(0));
         //   donwList.remove(0);
            /*
             * System.out.println("zipdownrun");
             * System.out.println(Thread.getAllStackTraces().size() + "总线程大小");
             */
            super.run();// 调回到自己的run里面
        }
        System.out.println();
    
        System.out.println(donwList.size() + "donwList的大小");
    }

    /**
     * 添加下载任务
     * 
     * @param pp
     */
    public static void addTask(Integer pp) {
        donwList.add(pp);
    }
    public static Integer getIndex() {
        if (donwList.size()>0) {
         return   donwList.get(0);
        }else return null;

    }

  
}
