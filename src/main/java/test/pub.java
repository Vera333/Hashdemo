package test;
import redis.clients.jedis.Jedis;

public class pub {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        jedis.publish("topic:clq.biz", "Hellow World!健康观看");
    }
}
