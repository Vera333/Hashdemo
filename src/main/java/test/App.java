package test;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import redis.clients.jedis.Jedis;

public class App {
    @Test
    public void test1(){
        Jedis jedis = new Jedis("192.168.140.129",6379);
        jedis.set("name","gll");
        jedis.hset("books","java","think in java");
        jedis.lpush("class","math","English");
        jedis.sadd("NBA","勇士","骑士");
        jedis.zadd("english:scoreboard",90,"张三");
        jedis.zadd("english:scoreboard",91,"李四");
        jedis.zadd("english:scoreboard",92,"王五");
        String name = jedis.get("name");
        System.out.println(name);
        jedis.close();
    }

    @Test
    public void test2(){
        Jedis jedis = new Jedis("192.168.140.129",6379);
        User user= new User();
        user.setName("gll");
        user.setSex("male");
        String userStr = JSON.toJSONString(user);
        jedis.set("user",userStr);
        String user1 = jedis.get("user");
        System.out.println(user1);
        User user2 = JSON.parseObject(user1, User.class);
        System.out.println(user2);
        jedis.close();
    }

    @Test
    public void test3(){
        //保存文章
        Jedis jedis = new Jedis("192.168.140.129",6379);
        Post post = new Post();
        post.setAuthor("gll");
        post.setContent("这是我的博客");
        post.setTitle("博客");
        Long postId = SavePost(post,jedis);
        GetPost(postId,jedis);
        Post post1 = updateTitle(postId, jedis);
        System.out.println(post1);
        deleteBlog(postId,jedis);
        jedis.close();
    }

    //保存博客
    public Long SavePost(Post post,Jedis jedis){
        Long postId = jedis.incr("posts");
        String myPost = JSON.toJSONString(post);
        jedis.set("post:"+postId+":data",myPost);
        return postId;
    }

    //获取博客
    public Post GetPost(Long postId,Jedis jedis){
        String getPost = jedis.get("post:" + postId + ":data");
        jedis.incr("post:" + postId + ":page.view");
        Post parseObject = JSON.parseObject(getPost, Post.class);
        System.out.println("这是第"+postId+"篇文章"+parseObject);
        return parseObject;
    }

    //修改标题
    public Post updateTitle(Long postId,Jedis jedis){
        Post post = GetPost(postId, jedis);
        post.setTitle("更改后的标题");
        String myPost = JSON.toJSONString(post);
        jedis.set("post:"+postId+":data",myPost);
        System.out.println("修改完成");
        return post;
    }
    //删除文章
    public void deleteBlog(Long postId,Jedis jedis){
        jedis.del("post:" + postId + ":data");
        jedis.del("post:"+postId+":page.view");
        System.out.println("删除成功");
    }
}
