package br.com.siswbrasil.integrator.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/system")
public class SystemInfoController {

    @GetMapping("/memory")
    public ResponseEntity<Map<String, Object>> getMemoryInfo() {
        Map<String, Object> memoryInfo = new HashMap<>();
        
        // Runtime memory info (heap summary)
        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory() / (1024 * 1024);
        long totalMemory = runtime.totalMemory() / (1024 * 1024);
        long freeMemory = runtime.freeMemory() / (1024 * 1024);
        long usedMemory = totalMemory - freeMemory;
        
        memoryInfo.put("max", maxMemory);
        memoryInfo.put("total", totalMemory);
        memoryInfo.put("free", freeMemory);
        memoryInfo.put("used", usedMemory);
        memoryInfo.put("usagePercentage", Math.round((double) usedMemory / totalMemory * 100));

        // More detailed memory breakdown
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        
        // Heap memory details
        MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
        Map<String, Long> heapInfo = new HashMap<>();
        heapInfo.put("init", heapMemoryUsage.getInit() / (1024 * 1024));
        heapInfo.put("used", heapMemoryUsage.getUsed() / (1024 * 1024));
        heapInfo.put("committed", heapMemoryUsage.getCommitted() / (1024 * 1024));
        heapInfo.put("max", heapMemoryUsage.getMax() / (1024 * 1024));
        memoryInfo.put("heap", heapInfo);

        // Non-heap memory (includes metaspace, etc)
        MemoryUsage nonHeapMemoryUsage = memoryMXBean.getNonHeapMemoryUsage();
        Map<String, Long> nonHeapInfo = new HashMap<>();
        nonHeapInfo.put("init", nonHeapMemoryUsage.getInit() / (1024 * 1024));
        nonHeapInfo.put("used", nonHeapMemoryUsage.getUsed() / (1024 * 1024));
        nonHeapInfo.put("committed", nonHeapMemoryUsage.getCommitted() / (1024 * 1024));
        nonHeapInfo.put("max", nonHeapMemoryUsage.getMax() / (1024 * 1024));
        memoryInfo.put("nonHeap", nonHeapInfo);

        // Try to get real process memory usage (Linux only)
        try {
            String pid = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
            Process process = Runtime.getRuntime().exec("ps -o rss= -p " + pid);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = reader.readLine();
            if (line != null && !line.trim().isEmpty()) {
                // RSS in KB, convert to MB
                long rss = Long.parseLong(line.trim()) / 1024;
                memoryInfo.put("processRealMemoryMB", rss);
            }
        } catch (Exception e) {
            log.warn("Failed to get process memory: {}", e.getMessage());
        }

        log.info("Memory info: {}", memoryInfo);
        
        return ResponseEntity.ok(memoryInfo);
    }
}
