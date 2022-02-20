import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;

public class Excecute {
    private HashSet<String> links;

    public Excecute() {
        links = new HashSet<String>();
    }

    public static void main(String[] args) throws IOException {
        final String URL = "https://shopee.vn/top_products?catId=VN_BITL0_26";
        Excecute excecute = new Excecute();
        excecute.getPageLinks(URL);

    }

    public void getPageLinks(String URL) {
        //4. Check if you have already crawled the URLs
        //(we are intentionally not checking for duplicate content in this example)
        if (!links.contains(URL)) {
            try {
                //4. (i) If not add it to the index
                if (links.add(URL)) {
                    System.out.println(URL);
                }
                Connection.Response response = Jsoup.connect(URL)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.102 Safari/537.36")
                        .execute();

                Document doc = Jsoup.connect(URL)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.102 Safari/537.36")
                        .header("Accept-Language", "en-US")
                        .header("X-Requested-With", "XMLHttpRequest")
                        .header("Referer", URL)
                        .cookies(response.cookies())
                        .get();


                //2. Fetch the HTML code
                Document document = Jsoup.connect(URL).get();
                //3. Parse the HTML to extract links to other URLs
                Elements linksOnPage = document.select("a[href]");

                //5. For each extracted URL... go back to Step 4.
                for (Element page : linksOnPage) {
                    getPageLinks(page.attr("abs:href"));
                }
            } catch (IOException e) {
                System.err.println("For '" + URL + "': " + e.getMessage());
            }
        }
    }

    public void test() throws IOException {
        String userAgent = "Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1667.0 Safari/537.36";
// get a session for tr and en domain
        String tempobetSession = Jsoup.connect("https://www.tempobet.com/").userAgent(userAgent).execute().cookie("GAMBLINGSESS");
        String tempobet22Session = Jsoup.connect("https://www.tempobet22.com/").userAgent(userAgent).execute().cookie("GAMBLINGSESS");
// tell tr domain that we wont to go to en without following the redirect
        String redirect = Jsoup.connect("https://www.tempobet22.com/?change_lang=https://www.tempobet.com/")
                .userAgent(userAgent).cookie("GAMBLINGSESS", tempobet22Session).followRedirects(false).execute().header("Location");
// Redirect goes to en domain including our hashed tr-cookie as parameter - but this redirect needs a en-cookie
        Connection.Response response = Jsoup.connect(redirect).userAgent(userAgent).cookie("GAMBLINGSESS", tempobetSession).execute();
// finally...
        Document doc = Jsoup.connect("https://www.tempobet.com/league191_5_0.html").userAgent(userAgent).cookies(response.cookies()).get();
    }

}
