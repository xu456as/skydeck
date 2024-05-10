package io.skydeck.gserver.test;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    static volatile int time = 0;
    static ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
    public static void main(String[] args) {
        service.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (time++ == 3) {
                    throw new RuntimeException("Error");
                }
                System.out.println(time);
            }
        }, 30, 30, TimeUnit.SECONDS);
        Scanner in = new Scanner(System.in);
        String str = in.next();

    }
}
