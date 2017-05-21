package com.touchfuture.takeout.common;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import org.apache.log4j.spi.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.util.logging.Logger;

/**
 * Created by user on 2017/5/21.
 */


interface JedisDataSource {
    ShardedJedis getRedisClient ();
    void returnResource (ShardedJedis shardedJedis);
    void returnResource (ShardedJedis shardedJedis,boolean broken);
}
class JedisDataSourceImpl implements  JedisDataSource{
    private static final Log log = LogFactory.getLog(JedisDataSourceImpl.class);

    @Autowired
    private ShardedJedisPool shardedJedisPool;

    @Override
    public ShardedJedis getRedisClient () {
        ShardedJedis shardedJedis;
        try {
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis;
        } catch (Exception e) {
            log.error("get redis client error " + e.getMessage());
        } finally {
            //shardedJedis.disconnect();
        }
        return  null;

    }

    @Override
    public void  returnResource (ShardedJedis shardedJedis) {
        //shardedJedis.disconnect();
    }

    @Override
    public void returnResource (ShardedJedis shardedJedis, boolean broken) {
        //shardedJedis.disconnect();
    }


}

@Component
public class RedisUtil {
    private static final Log log = LogFactory.getLog(RedisUtil.class);

    @Autowired
    private JedisDataSource redisDataSource = new JedisDataSourceImpl();

    public void disconnect () {
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        shardedJedis.disconnect();;
    }

    public String set (String key, String value) {
        String result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return  result;
        }
        boolean broken = false;
        try {

            result = shardedJedis.set(key,value);
        } catch (Exception e) {
            log.error(e.getMessage());
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis,broken);
        }

        return  result;
    }

    public String get (String key) {
        String result = null;
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        if (shardedJedis == null) {
            return  result;
        }

        boolean broken = false;

        try {
            result = shardedJedis.get(key);
        } catch (Exception e) {
            log.error(e.getMessage());
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis,true);
        }
        return  result;

    }

    public boolean exitKey (String key) {
        ShardedJedis shardedJedis = redisDataSource.getRedisClient();
        boolean result = false;
        if (shardedJedis == null) {
            return  false;
        }

        boolean broken = false;
        try {
            result  = shardedJedis.exists(key);
        } catch (Exception e) {
            log.error(e.getMessage());
            broken = true;
        } finally {
            redisDataSource.returnResource(shardedJedis,broken);
        }
        return  result;
    }

}
