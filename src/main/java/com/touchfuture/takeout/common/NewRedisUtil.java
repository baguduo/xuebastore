package com.touchfuture.takeout.common;

import redis.clients.jedis.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2017/5/21.
 */
public class NewRedisUtil {

    private static String ADDR = "127.0.0.1";
    private static int PORT = 6379;
    private static int MAX_ACTIVE = 1024;
    private static int MAX_IDLE = 200;
    private static int MAX_WAIT = 10000;
    private static boolean TEST_ON_BORROW = true;
    /**
     * 切片客户端链接
     */
    private ShardedJedis shardedJedis;

    /**
     * 切片链接池
     */
    private ShardedJedisPool shardedJedisPool;

    /**
     * 主机ip
     */
    private String ip1 = "127.0.0.1";
    private String ip2 = "127.0.0.1";

    /**
     *连接池配置信息对象
     */
    private  static JedisPoolConfig config;
    static {

        // 池基本配置
        config = new JedisPoolConfig();

        //可用连接实例的最大数目，默认值为8；
        //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
        config.setMaxTotal(MAX_ACTIVE);

        //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
        config.setMaxIdle(MAX_IDLE);

        //等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
        config.setMaxWaitMillis(MAX_WAIT);

        //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
        config.setTestOnBorrow(TEST_ON_BORROW);
    }

    /**
     * 构造函数
     */

    public NewRedisUtil() {

        // slave链接
        List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
        JedisShardInfo infoA = new JedisShardInfo(ip1, 6379);
        JedisShardInfo infoB = new JedisShardInfo(ip2, 6379);
        //infoA.setPassword("123456");
        shards.add(infoA);
        shards.add(infoB);
        shardedJedisPool = new ShardedJedisPool(config, shards);

    }


    /**
     * 判断key是否存在
     * @param key
     * @return
     */
    public boolean exitKey (String key) {
        boolean result = false;
        try {
            shardedJedis = shardedJedisPool.getResource();
            if (shardedJedis == null) {
                System.out.println("sharedJedis connect error!");
                return false;
            }

            if (shardedJedis.exists(key)) {
                result = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            shardedJedis.close();
        }


        return  result;
    }

    /**
     * 获取key对应的value
     * @param key
     * @return
     */
    public String getValue (String key) {
        String result = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            if (shardedJedis == null) {
                System.out.println("sharedJedis connect error!");
                return  result;
            }
            result = shardedJedis.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            shardedJedis.close();
        }

        return result;
    }

    /**
     * 设置键值对
     * @param key
     * @param value
     */
    public void setValue (String key, String value) {
        try {
            shardedJedis = shardedJedisPool.getResource();
            if (shardedJedis == null) {
                System.out.println("sharedJedis connect error!");
                return;
            }
            shardedJedis.set(key,value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            shardedJedis.close();
        }

    }

    /**
     * 返回key存储的value类型
     * @param key
     * @return
     */
    public String type (String key) {
        String result = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            if (shardedJedis == null) {
                return  result;
            }
            result = shardedJedis.type(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            shardedJedis.close();
        }

        return  result;
    }

    /**
     * 设置过期时间
     * @param key
     * @param seconds
     * @return
     */
    public Long expire (String key, int seconds) {
        Long result = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            if (shardedJedis == null) {
                return null;
            }
            result = shardedJedis.expire(key, seconds);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            shardedJedis.close();
        }

        return  result;
    }

    /**
     * 设置在某个时间点失效
     * @param key
     * @param unixTime
     * @return
     */
    public Long expireAt (String key, long unixTime) {
        Long result = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            if (shardedJedis == null) {
                return  result;
            }
            shardedJedis.expireAt(key,unixTime);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            shardedJedis.close();
        }

        return result;
    }

    /**
     * 返回剩余存活时间
     * @param key
     * @return
     */
    public Long ttl (String key) {
        Long result = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            if (shardedJedis == null) {
                return result;
            }
            result  = shardedJedis.ttl(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            shardedJedis.close();
        }

        return  result;

    }

    /**
     * 设置某位上的值
     * @param key
     * @param offset
     * @param value
     * @return
     */
    public boolean setbit (String key, long offset, boolean value) {
        boolean result = false;
        try {
            shardedJedis = shardedJedisPool.getResource();
            if (shardedJedis == null) {
                return  result;
            }
            result = shardedJedis.setbit(key, offset, value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            shardedJedis.close();
        }

        return result;
    }

    /**
     * 设置某位上的值
     * @param key
     * @param offset
     * @param
     * @return
     */
    public boolean getbit (String key, long offset) {
        boolean result = false;
        try {
            shardedJedis = shardedJedisPool.getResource();
            if (shardedJedis == null) {
                return  result;
            }
            result = shardedJedis.getbit(key, offset);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            shardedJedis.close();
        }

        return result;
    }

    /**
     * 覆盖指定key的value值
     * @param key
     * @param offset
     * @param value
     * @return
     */
    public long setRange (String key, long offset, String value) {
        Long result = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            if (shardedJedis == null) {
                return  result;
            }
            result = shardedJedis.setrange(key, offset, value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            shardedJedis.close();
        }

        return  result;


    }

    /**
     * 获取指定key的带偏移value值
     * @param key
     * @param
     * @param
     * @return
     */
    public String getRange (String key, long startOffset, long endOffset) {
        String result = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            if (shardedJedis == null) {
                return  result;
            }
            result = shardedJedis.getrange(key, startOffset, endOffset);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            shardedJedis.close();
        }

        return  result;


    }

}
