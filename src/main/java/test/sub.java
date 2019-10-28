package test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class sub {
    public static void main(String []args) {
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        jedis.subscribe(new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                System.out.println(message);
                super.onMessage(channel, message);
            }
        }, "topic:clq.biz");

    }

    public void savePost(Post post,Jedis jedis){
        Long posts=jedis.incr("posts");

    }
}

