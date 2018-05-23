package cn.bupt.device.Metrics;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

/**
 * Created by tangjialiang on 2018/5/24.
 */
public class TestCounter {

    public final Counter pendingJobs = new Counter() ;
    private final Queue<String> queue = new LinkedList<String>();

    public TestCounter(MetricRegistry metrics) {
        metrics.register("counter", pendingJobs) ;
    }

    public void add(String str) {
        pendingJobs.inc();
        queue.offer(str);
    }

    public String take() {
        pendingJobs.dec();
        return queue.poll();
    }
    /**
     * TODO
     * @author scutshuxue.chenxf
     * @param args
     * void
     * @throws InterruptedException
     */
    public static void main(String[]args) throws InterruptedException {
        // TODOAuto-generated method stub
        MetricRegistry metrics = new MetricRegistry();
        ConsoleReporter build = ConsoleReporter.forRegistry(metrics).build();
        build.start(3, TimeUnit.SECONDS);

        TestCounter tc =new TestCounter(metrics);


        while(true){
            tc.add("1");
            Thread.sleep(1000);
            System.out.println("counter: " + tc.pendingJobs.getCount());
        }
    }

}