package test;

import com.alibaba.fastjson.JSON;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;

public class Hashdemo {
    public static void main(String[] args) {
        //连接本地的 Redis 服务
        Jedis jedis = new Jedis("localhost");
        Post post =new Post();
        post.setAuthor("smq");
        post.setContent("blog");
        post.setTitle("my blog");
        Long postId = savePost(post, jedis);
        System.out.println("保存成功");
        Post myPost=getPost(postId,jedis);
        System.out.println(myPost);
    }

    static Long savePost(Post post,Jedis jedis){
        Long postId=jedis.incr("posts");
        Map<String,String> blog=new HashMap<String, String>();
        blog.put("title",post.getTitle());
        blog.put("content",post.getContent());
        blog.put("author",post.getAuthor());
        jedis.hmset("post:"+postId+":data",blog);
        return postId;
    }
    static Post getPost(Long postId,Jedis jedis){
        Map<String,String> myBlog=jedis.hgetAll("post:"+postId+":data");
        Post post=new Post();
        post.setTitle(myBlog.get("title"));
        post.setContent(myBlog.get("content"));
        post.setAuthor(myBlog.get("author"));
        return post;
    }

    //修改标题
    public Post updateTitle(Long postId,Jedis jedis){
        Post post = getPost(postId, jedis);
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
