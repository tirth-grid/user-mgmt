package com.reactive.programming;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.net.URI;
import java.net.http.*;
import java.time.Duration;
import java.util.concurrent.*;

public class ThreadBenchmark {

    static final int TOTAL_REQUESTS = 1000;
    static final String URL = "https://httpbin.org/get";

    public static void main(String[] args) throws Exception {
        System.out.println("Running benchmarks with " + TOTAL_REQUESTS + " HTTP requests...");
        System.out.println();

        benchmark("🧵 Traditional Threads", Executors.newFixedThreadPool(100));
        System.out.println();
        benchmark("🪶 Virtual Threads", Executors.newVirtualThreadPerTaskExecutor());
    }

    private static void benchmark(String label, ExecutorService executor) throws InterruptedException {
        System.out.println("Benchmarking: " + label);

        // Prepare metrics
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        long beforeCpu = getProcessCpuTime(threadMXBean);
        long beforeUsedMemory = getUsedHeap(memoryMXBean);
        int beforeThreadCount = Thread.activeCount();

        // Run benchmark
        long timeMs = runBenchmark(executor);

        // Post metrics
        long afterCpu = getProcessCpuTime(threadMXBean);
        long afterUsedMemory = getUsedHeap(memoryMXBean);
        int afterThreadCount = Thread.activeCount();

        System.out.println("⏱  Elapsed Time: " + timeMs + " ms");
        System.out.println("🧠 Memory Used:  " + formatBytes(afterUsedMemory - beforeUsedMemory));
        System.out.println("🧮 CPU Time:     " + (afterCpu - beforeCpu) / 1_000_000 + " ms (approx)");
        System.out.println("🧵 Threads:      " + beforeThreadCount + " → " + afterThreadCount);
    }

    private static long runBenchmark(ExecutorService executor) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(TOTAL_REQUESTS);
        HttpClient client = HttpClient.newHttpClient();

        long start = System.currentTimeMillis();

        for (int i = 0; i < TOTAL_REQUESTS; i++) {
            executor.submit(() -> {
                try {
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(URL))
                            .timeout(Duration.ofSeconds(10))
                            .build();
                    client.send(request, HttpResponse.BodyHandlers.ofString());
                } catch (Exception ignored) {
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();
        return System.currentTimeMillis() - start;
    }

    // CPU time of JVM process
    private static long getProcessCpuTime(ThreadMXBean bean) {
        return bean.isCurrentThreadCpuTimeSupported() ? bean.getCurrentThreadCpuTime() : 0;
    }

    // Heap memory used
    private static long getUsedHeap(MemoryMXBean bean) {
        MemoryUsage heap = bean.getHeapMemoryUsage();
        return heap.getUsed();
    }

    // Byte formatter
    private static String formatBytes(long bytes) {
        long kb = bytes / 1024;
        long mb = kb / 1024;
        return mb > 0 ? mb + " MB" : kb + " KB";
    }
}
//Report:
//Running benchmarks with 1000 HTTP requests...
//
//Benchmarking: 🧵 Traditional Threads
//⏱  Elapsed Time: 14445 ms
//🧠 Memory Used:  78 MB
//🧮 CPU Time:     148 ms (approx)
//🧵 Threads:      1 → 206
//
//Benchmarking: 🪶 Virtual Threads
//⏱  Elapsed Time: 4208 ms
//🧠 Memory Used:  7 MB
//🧮 CPU Time:     11 ms (approx)
//🧵 Threads:      106 → 149