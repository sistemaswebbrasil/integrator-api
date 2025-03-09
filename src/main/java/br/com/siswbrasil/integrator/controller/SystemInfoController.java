package br.com.siswbrasil.integrator.controller;

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
        Runtime runtime = Runtime.getRuntime();
        
        long maxMemory = runtime.maxMemory() / (1024 * 1024);
        long totalMemory = runtime.totalMemory() / (1024 * 1024);
        long freeMemory = runtime.freeMemory() / (1024 * 1024);
        long usedMemory = totalMemory - freeMemory;
        
        Map<String, Object> memoryInfo = new HashMap<>();
        memoryInfo.put("max", maxMemory);
        memoryInfo.put("total", totalMemory);
        memoryInfo.put("free", freeMemory);
        memoryInfo.put("used", usedMemory);
        memoryInfo.put("usagePercentage", Math.round((double) usedMemory / totalMemory * 100));

        log.info("Memory info: {}", memoryInfo);
        log.info("Max: {} MB", maxMemory);
        log.info("Total: {} MB", totalMemory);
        log.info("Free: {} MB", freeMemory);
        log.info("Used: {} MB", usedMemory);
        log.info("Usage percentage: {}%", memoryInfo.get("usagePercentage"));
        
        
        
        return ResponseEntity.ok(memoryInfo);
    }
}
