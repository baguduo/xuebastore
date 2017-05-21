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
     * ��Ƭ�ͻ�������
     */
    private ShardedJedis shardedJedis;

    /**
     * ��Ƭ���ӳ�
     */
    private ShardedJedisPool shardedJedisPool;

    /**
     * ����ip
     */
    private String ip1 = "127.0.0.1";
    private String ip2 = "127.0.0.1";

    /**
     *���ӳ�������Ϣ����
     */
    private  static JedisPoolConfig config;
    static {

        // �ػ�������
        config = new JedisPoolConfig();

        //��������ʵ���������Ŀ��Ĭ��ֵΪ8��
        //�����ֵΪ-1�����ʾ�����ƣ����pool�Ѿ�������maxActive��jedisʵ�������ʱpool��״̬Ϊexhausted(�ľ�)��
        config.setMaxTotal(MAX_ACTIVE);

        //����һ��pool����ж��ٸ�״̬Ϊidle(���е�)��jedisʵ����Ĭ��ֵҲ��8��
        config.setMaxIdle(MAX_IDLE);

        //�ȴ��������ӵ����ʱ�䣬��λ���룬Ĭ��ֵΪ-1����ʾ������ʱ����������ȴ�ʱ�䣬��ֱ���׳�JedisConnectionException��
        config.setMaxWaitMillis(MAX_WAIT);

        //��borrowһ��jedisʵ��ʱ���Ƿ���ǰ����validate���������Ϊtrue����õ���jedisʵ�����ǿ��õģ�
        config.setTestOnBorrow(TEST_ON_BORROW);
    }

    /**
     * ���캯��
     */

    public NewRedisUtil() {

        // slave����
        List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
        JedisShardInfo infoA = new JedisShardInfo(ip1, 6379);
        JedisShardInfo infoB = new JedisShardInfo(ip2, 6379);
        //infoA.setPassword("123456");
        shards.add(infoA);
        shards.add(infoB);
        shardedJedisPool = new ShardedJedisPool(config, shards);

    }


    /**
     * �ж�key�Ƿ����
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
     * ��ȡkey��Ӧ��value
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
     * ���ü�ֵ��
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
     * ����key�洢��value����
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
     * ���ù���ʱ��
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
     * ������ĳ��ʱ���ʧЧ
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
     * ����ʣ����ʱ��
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
     * ����ĳλ�ϵ�ֵ
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
     * ����ĳλ�ϵ�ֵ
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
     * ����ָ��key��valueֵ
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
     * ��ȡָ��key�Ĵ�ƫ��valueֵ
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
