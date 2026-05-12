package com.test.twincarbonboot.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.test.twincarbonboot.pojo.preview.PreviewResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class PreviewCache
{

    @Data
    @AllArgsConstructor
    public static class CacheEntry {
        private Integer userId;
        private PreviewResult data;
    }

    private final  Cache<String, CacheEntry> cache;


    public PreviewCache() {
        this.cache = Caffeine.newBuilder()
                .maximumSize(1000)           // 最多缓存 1000 条
                .expireAfterWrite(30, TimeUnit.MINUTES)  // 30 分钟后自动过期
                .build();
    }

    public void put(String batchId, PreviewResult previewData, Integer userId) {
        cache.put(batchId, new CacheEntry(userId, previewData));
    }

    public CacheEntry get(String batchId) {
        return  cache.getIfPresent(batchId);
    }

    public void remove(String batchId) {
        cache.invalidate(batchId);
    }

}
