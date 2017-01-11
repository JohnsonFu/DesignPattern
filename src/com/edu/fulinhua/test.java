package com.edu.fulinhua;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Map;

//import org.apache.http.client.CookieStore;
/**
 * Created by fulinhua on 2016/12/18.
 */
public class test{

//    public static void main(String[] args) throws ClientProtocolException,IOException {
//        String loginurl="http://219.219.120.48/jiaowu/login.do";
//        String username="141250036";
//        String password="mangguo";
//       System.out.println(test.posturl(loginurl,username,password));
//    //    testPost(loginurl,username,password);
//    }

    private static String url = "http://blog.csdn.net";
   private static String blogName = "guoxiaolongonly";

    public static void getArticleListFromUrl(String listurl) {
        Document doc = null;
        try {
            doc = Jsoup.connect(listurl).userAgent("Mozilla/5.0").timeout(3000).post();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // System.out.println(doc);
        Elements elements = doc.getElementsByTag("a");//找到所有a标签
        for (Element element : elements) {
            String relHref = element.attr("href"); // == "/"这个是href的属性值，一般都是链接。这里放的是文章的连接
            String linkHref = element.text();
            //用if语句过滤掉不是文章链接的内容。因为文章的链接有两个，但评论的链接只有一个，反正指向相同的页面就拿评论的链接来用吧
            if (!relHref.startsWith("http://") && relHref.contains("details") && relHref.endsWith("comments"))
            {
                StringBuffer sb = new StringBuffer();
                sb.append(url).append(relHref);
                System.out.println(sb.substring(0, sb.length() - 9));//去掉最后的#comment输出
                getArticleFromUrl(sb.substring(0, sb.length() - 9));//可以通过这个url获取文章了
            }
//          System.out.println(linkHref);
            if(linkHref.equals("下一页"))//如果有下一页
            {
                getArticleListFromUrl(url + relHref);//获取下一页的列表
            }
        }

    }

    public static void getArticleFromUrl(String detailurl) {
        try {
            Document document = Jsoup.connect(detailurl).userAgent("Mozilla/5.0").timeout(3000).post();
            Element elementTitle = document.getElementsByClass("link_title").first();//标题。 这边根据class的内容来过滤
            System.out.println(elementTitle.text());
            String filename = elementTitle.text().replaceAll("/", "或");
            Element elementContent = document.getElementsByClass("article_content").first();//内容。
            System.out.println(elementContent.text());
            // String Content =elementContent.te  xt().replaceAll(" ", "\t");
            // System.out.println(elementContent.text()+"\n");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    public static void main(String[] args) throws IOException {
//    String surl = "http://219.219.120.48/jiaowu/login.do?userName=141250036&password=mangguo";
//    URL url = new URL(surl);
//    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//
//    connection.setDoOutput(true);
//    connection.setDoInput(true);
//    connection.setRequestMethod("POST");
//    connection.setUseCaches(false);
//    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//    connection.setRequestProperty("User-Agent","Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; .NET CLR 1.1.4322; .NET4.0C; .NET4.0E)");
//    connection.setRequestProperty("Accept-Language","zh-CN");
//    connection.setRequestProperty("Accept-Encoding","gzip, deflate");
//
//    OutputStreamWriter out = new OutputStreamWriter(
//            connection.getOutputStream(), "UTF-8");
//    out.flush();
//    out.close();
//
//    connection.connect();
//
//    InputStream in = connection.getInputStream();
//    String surl2 = "http://219.219.120.48/jiaowu/student/studentinfo/achievementinfo.do?method=searchTermList";
//    URL url2 = new URL(surl);
//    connection = (HttpURLConnection) url2.openConnection();
//    InputStream in2 = connection.getInputStream();
//
//
//
//    StringBuilder retStr = new StringBuilder();
//    BufferedReader br = new BufferedReader(new InputStreamReader(in));
//    String temp = br.readLine();
//    while (temp != null) {
//        retStr.append(temp);
//        retStr.append("\n");
//        temp = br.readLine();
//    }
//    br.close();
//    in.close();
//
//    System.out.println(retStr);
//    for(Map.Entry<String, List<String>> header: connection.getHeaderFields().entrySet()){
//        System.out.println(header.getKey() +" " + header.getValue());
//    }

        // getArticleListFromUrl("http://219.219.120.48/jiaowu/login.do?userName=141250036&password=mangguo");
//        Document doc = Jsoup.connect("http://219.219.120.48/jiaowu/login.do?userName=141250036&password=mangguo").get();
//        Elements elements = doc.getElementsByTag("a");//找到所有a标签
//        for (Element element : elements) {
//            String relHref = element.attr("abs:href"); // == "/"这个是href的属性值，一般都是链接。这里放的是文章的连接
//            String linkHref = element.text();
//
//        }


        Connection.Response res = Jsoup.connect("http://219.219.120.48/jiaowu/login.do")
                .data("userName", "141250036", "password", "mangguo")
                .method(Connection.Method.POST)
                .execute();
        Map cookies = null;
        if (res.statusCode() == 200) {
            cookies = res.cookies();
            System.out.println(cookies);
        }
        Document doc = res.parse();
//这儿的SESSIONID需要根据要登录的目标网站设置的session Cookie名字而定
        String sessionId = res.cookie("user_id");
        Elements elements = doc.getElementsByTag("a");//找到所有a标签
        for (Element element : elements) {
            String relHref = element.attr("abs:href"); // == "/"这个是href的属性值，一般都是链接。这里放的是文章的连接
            String linkHref = element.text();
           System.out.println(relHref);
        }
//
        Document objectDoc = Jsoup.connect("http://219.219.120.48:80/jiaowu/student/index.do")
                .cookie("JSESSIONID", res.cookies().get("JSESSIONID"))
                .get();
        System.out.println(res.cookies().get("JSESSIONID"));
        System.out.println(objectDoc.body());
    }




}