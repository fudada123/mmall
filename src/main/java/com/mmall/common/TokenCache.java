package com.mmall.common;


import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author fudada
 * @date 2019/5/15 - 13:45
 */
public class TokenCache {

    private static Logger logger = LoggerFactory.getLogger(TokenCache.class);

    public static final String TOKEN_PREFIX = "token_";

    //LRU算法
    private static LoadingCache<String, String> localcache = CacheBuilder.newBuilder().initialCapacity(1000).maximumSize(1000).expireAfterAccess(12, TimeUnit.HOURS)
            .build(new CacheLoader<String, String>() {
                //默认的数据加载实现，当调用get取值时候，如果key没有对应的值，就调用这个方法进行加载
                @Override
                public String load(String key) throws Exception {
                    return "null";
                }
            });

    public static void setKey(String key,String value){
        localcache.put(key,value);
    }

    public static String getKey(String key){
        String value = null;
        try {
            value = localcache.get(key);
            if ("null".equals(value)) {
                return null;
            }
            return value;
        } catch (Exception e) {
            logger.error("localcache get error",e);
        }
        return null;
    }
}
