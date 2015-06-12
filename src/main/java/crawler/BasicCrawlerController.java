package crawler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Random;

/**
 * Created by bart on 6/11/15.
 */
public class BasicCrawlerController {
    private static final Logger logger = LoggerFactory.getLogger(BasicCrawlerController.class);

    public static void main(String[] args) throws Exception {
        System.out.println("hell");
        Random random = new Random();

        Document doc;
        try {

            File file = new File("warmNews");
            FileWriter fw = new FileWriter(file, true) ;

            for (int i = 1; i<110; i++) {

                String baseUrl = "http://news.naver.com/main/hotissue/sectionList.nhn?sid1=102&mid=hot&cid=3069&page=" + i;
                logger.info(baseUrl);

                try {
                    doc = Jsoup.connect(baseUrl).get();

                }catch (SocketTimeoutException e) {
                    i--;
                    continue;
                }

                Elements links = doc.select("dl dt a");

                for (Element link : links) {

                    logger.info("title : " + link.text());

                    String newsUrlBase = "http://news.naver.com";
                    try {
                        Document newsDoc = Jsoup.connect(newsUrlBase + link.attr("href")).get();
                        Elements newsBody = newsDoc.select("#articleBodyContents");
                        logger.info("Body: " + newsBody.text());
                        fw.write(link.text() + "\t" + newsBody.text() + "\n");
                        Thread.sleep(random.nextInt(5000));
                    } catch (SocketTimeoutException e) {
                        continue;
                    }
                }
            }
            fw.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        } finally {

        }

    }
}