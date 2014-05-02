/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.leolo.fyp;

import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import org.leolo.fyp.tmp.FindWord;
import org.leolo.fyp.util.*;

/**
 *
 * @author leolo
 */
public class PostAnalysis {

    public static ArrayList<Pair> resList;

    static class Pair {

        int sid;
        String name;

        public Pair(int sid, String name) {
            this.sid = sid;
            this.name = name;
        }
    }

    public static void main(String[] args) throws Throwable {
        FindWord fw = new org.leolo.fyp.tmp.FindWord();
        fw.run();
        PostRanker pr = new PostRanker(fw);
        System.out.println("PR Created");
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/label", "root", "rootpass");
        Statement stmt = conn.createStatement();
        resList = new ArrayList<>();

        ResultSet result = stmt.executeQuery("SELECT `comment`.`sid`,`name` FROM `comment`,`shop` WHERE `shop`.`sid`=`comment`.`sid` GROUP BY sid HAVING COUNT(*) >0 ORDER BY `name`;");
        while (result.next()) {
            resList.add(new Pair(result.getInt(1), result.getString(2)));
        }
        result = stmt.executeQuery("SELECT DISTINCT `content`,`cid`,`sid`d FROM `comment` ORDER BY sid ASC; ");
        PrintWriter out = null;;
        int sid = -1;
        System.out.println("Start processing...");
        ArrayList<Post> list = new ArrayList<>();
        while (result.next()) {
            int tsid = result.getInt(3);
            if (tsid != sid) {
                if (list.size() <= 3) {
                    list.add(null);
                    list.add(null);
                    list.add(null);
                    outputPost(new PrintWriter("result/r_" + sid + ".html"), list.get(0), list.get(1), list.get(2), sid);
                    list.clear();
                    sid = tsid;
                } else {
                    //Select Post
                    Post a, b, c;
                    int minEmo = 100;
                    int maxEmo = -100;
                    for (int i = 0; i < list.size(); i++) {
                        RankingResult rr = list.get(i).rr;
                        if (minEmo > rr.getEQ()) {
                            minEmo = rr.getEQ();
                        }
                        if (maxEmo < rr.getEQ()) {
                            maxEmo = rr.getEQ();
                        }
                    }
                    ArrayList<Post> hlist = new ArrayList<>();
                    ArrayList<Post> llist = new ArrayList<>();
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).rr.getEQ() == maxEmo) {
                            hlist.add(list.get(i));
                            continue;
                        }
                        if (list.get(i).rr.getEQ() == minEmo) {
                            llist.add(list.get(i));
                        }
                    }
                    
                        Collections.sort(hlist);
                        a = hlist.get(0);
                        a.reason = "Selected due to best rating to the restaurant.";
                        if(llist.size() > 0){
                            Collections.sort(llist);
                            b = llist.get(0);
                            b.reason = "Selected due to lowest rating to the restaurant.";
                        }else{
                            b=hlist.get(1);
                            b.reason = "Selected due to best rating to the restaurant.";
                        }
                    list.remove(a);
                    list.remove(b);
                    Collections.sort(list);
                    c = list.get(0);
                    c.reason = "Selected because the review are useful from the reminding review.";
                    outputPost(new PrintWriter("result/r_" + sid + ".html"), a, b, c, sid);
                    //Output Post
                    list.clear();
                    sid = tsid;
                }
            }
            String post = result.getString(1);
            RankingResult rr = pr.rank(post);
            list.add(new Post(post, rr, result.getInt(2)));
        }

    }

    private static void outputPost(PrintWriter out, Post a, Post b, Post c, int sid)throws Exception {
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/label", "root", "rootpass");
        Statement stmt = conn.createStatement();
        ResultSet result = stmt.executeQuery("SELECT `name` FROM `shop` WHERE `sid`='"+sid+"';");
        String rname = "";
        if(result.next())
            rname = result.getString(1);
        out.println("<!DOCTYPE html>\n"
                + "<html>\n"
                + "<head>\n"
                + "  <title>"+rname+"</title>\n"
                + "  <link rel=\"stylesheet\" href=\"style.css\" media=\"screen\" />\n"
                + "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\n"
                + "<!--The following script tag downloads a font from the Adobe Edge Web Fonts server for use within the web page. We recommend that you do not modify it.-->\n"
                + "<script>var __adobewebfontsappname__=\"dreamweaver\"</script>\n"
                + "<script src=\"http://use.edgefonts.net/aguafina-script:n4:default;allura:n4:default;aladin:n4:default.js\" type=\"text/javascript\"></script>\n"
                + "<script>\n"
                + "<!--\n"
                + "    function loadPage(){\n"
                + "        obj = document.getElementById(\"restaurantlist\");\n"
                + "        opt = obj.options;\n"
                + "        value = opt[obj.selectedIndex].value;\n"
                + "        if(value != -1){\n"
                + "            //Redirect\n"
                + "            window.location = \"r_\"+value+\".html\";\n"
                + "        }\n"
                + "    }\n"
                + "// -->\n"
                + "</script>\n"
                + "</head>\n"
                + "<body>\n"
                + "<div id=\"container\"> \n"
                + "  <div id=\"header\">\n"
                + "    <h1><strong>LEI 5</strong></h1>\n"
                + "    <strong class=\"header-text\">Open Rice Post Ranker</strong></div>\n"
                + "  \n"
                + "  \n"
                + "  \n"
                + "  <div id=\"content\"> \n"
                + "    \n"
                + "    <table width=\"624\" border=\"1\" style=\"margin-left:50px;\">\n"
                + "  <tr>\n"
                + "    <td width=\"104\"><b>Usefulness</b></td>\n"
                + "    <td width=\"104\"><b>Very Unuseful</b></td>\n"
                + "    <td width=\"104\"><b>Unuseful</b></td>\n"
                + "    <td width=\"104\"><b>Neutral</b></td>\n"
                + "    <td width=\"104\"><b>Useful</b></td>\n"
                + "    <td width=\"104\"><b>Very Useful</b></td>\n"
                + "  </tr>\n"
                + "  <tr>\n"
                + "    <td><b> Color </b></td>\n"
                + "    <td class=\"vb\">&nbsp;</td>\n"
                + "    <td class=\"b\">&nbsp;</td>\n"
                + "    <td class=\"n\">&nbsp;</td>\n"
                + "    <td class=\"g\">&nbsp;</td>\n"
                + "    <td class=\"vg\">&nbsp;</td>\n"
                + "  </tr>\n"
                + "  <tr>\n"
                + "    <td><b>Emotion</b></td>\n"
                + "    <td><b>Very Bad</b></td>\n"
                + "    <td><b>Bad</b></td>\n"
                + "    <td><b>Neutral</b></td>\n"
                + "    <td><b>Good</b></td>\n"
                + "    <td><b>Very Good</b></td>\n"
                + "  </tr>\n"
                + "</table>\n"
                + "<p> </p>\n"
                + "<p> </p>\n"
                + "<p> </p>");
        out.println("<table width=\"900\" border=\"1\" style=\"margin-left:50px;\">\n"
                + "      <tr>\n"
                + "      <TD COLSPAN=3><form name=\"form4\" method=\"post\" action=\"\">\n"
                + "        <label for=\"Restaurant\"></label>\n"
                + "        <p><h2>"+rname+"</b></h2>\n"
                + "      </form>\n"
                + "      </tr>\n"
                + "      \n"
                + "      <tr>\n"
                + "        <TD width=\"110\" ROWSPAN=3 ><form name=\"form5\" method=\"post\" action=\"\">\n"
                + "          <label for=\"restaurantlist\"></label>\n"
                + "          <select name=\"restaurantlist\" id=\"restaurantlist\" onChange=\"loadPage();\">\n"
                + "          <option value=\"-1\">--Select--</option>\n");
        for (Pair p : resList) {
            out.print("<option value=\"" + p.sid + "\"");
            if (sid == p.sid) {
                out.print(" selected=\"selected\"");
            }
            out.print(">" + p.name + "</option>");
        }
        out.println("          </select>\n"
                + "        </form>");
        if (a != null) {
            printPost(out, a);
        }
        if (b != null) {
            printPost(out, b);
        }
        if (c != null) {
            printPost(out, c);
        }
        out.println("</tr>");
        out.println("</table>    \n" +
"    <p>&nbsp;</p>\n" +
"    <p></p>\n" +
"  </div>\n" +
"  \n" +
"  <div id=\"footer\">\n" +
"  &copy;Copyright Chan Shing Hoi, Law Chun Man, Lo Kam Tao Leo 2014 <br>\n" +
"  HKUST  ??br>\n" +
"   Designed by LEI5</div>\n" +
"</div>\n" +
"</body>\n" +
"</html>");
        out.flush();
        out.close();
    }

    public static void printPost(PrintWriter out, Post p) {
        out.println("<th width=\"653\" scope=\"row\"><form name=\"form1\" method=\"post\" action=\"\">\n");
        out.print(p.post.replaceAll("\n", "<br/>\n"));
        out.println("<p><u>"+p.reason+"</u></p>");
        out.println("</form></th>");
        out.println("<td> \n"
                + "  <table max-width=\"500\" border=\"1\" style=\"border:5px ridge rgb(0,0,0)\" >\n"
                + "  <tr>\n"
                + "    <td><b>Keyword</b></td>\n"
                + "    <td>"+p.rr.getKeyword()*40+"</td>\n"
                + "  </tr>\n"
                + "  <tr>\n"
                + "    <td><b>Emotion Word</b></td>\n"
                + "    <td>"+p.rr.getEmotionWord()*20+"</td>\n"
                + "  </tr>\n"
                + "  <tr>\n"
                + "    <td><b>Total Word</b></td>\n"
                + "    <td>"+p.rr.getWord()+"</td>\n"
                + "  </tr>\n"
                + "  <tr>\n"
                + "    <td><b>Meaningful Word</b></td>\n"
                + "    <td>"+p.rr.getWordWithoutStopword()*3+"</td>\n"
                + "  </tr>\n"
                + "  <tr>\n"
                + "    <td><b>Emotion Index</b></td>\n"
                + "    <td class=\""+p.rr.getEmotionClass()+"\">"+p.rr.getEQ()+"</td>\n"
                + "  </tr>\n"
                + "  <tr>\n"
                + "    <td><b>Useful Index</b></td>\n"
                + "    <td class=\""+p.rr.getTotalClass()+"\">"+p.rr.getTotalPoint()+"</td>\n"
                + "  </tr>\n"
                + "</table>\n"
                + "\n"
                + "  \n"
                + "  \n"
                + "  </td></tr><tr>");
    }
}

class Post implements Comparable<Post> {

    String post;
    RankingResult rr;
    int cid;
    String reason = "Selected because too few review available.";

    Post(String post, RankingResult rr, int cid) {
        this.post = post;
        this.rr = rr;
        this.cid = cid;
    }

    @Override
    public int compareTo(Post o) {
        if (rr.getTotalPoint() > o.rr.getTotalPoint()) {
            return -1;
        }
        if (rr.getTotalPoint() < o.rr.getTotalPoint()) {
            return 1;
        }
        if (rr.getKeyword() > o.rr.getKeyword()) {
            return -1;
        }
        if (rr.getKeyword() < o.rr.getKeyword()) {
            return 1;
        }
        if (cid > o.cid) {
            return -1;
        }
        if (cid < o.cid) {
            return 1;
        }
        return 0;
    }
}
