package tr.com.innova.tester;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.MessageFormat;

/**
 * Created by mehmet on 2.02.2017.
 */
@Controller
@Slf4j
public class MainController {

    @Autowired
    private VakifConnection vakifConnection;

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/tester", method = RequestMethod.POST, produces = MediaType.TEXT_HTML_VALUE)
    public void tester(@ModelAttribute TestRequest request, HttpServletResponse response) throws Exception {
        log.info("Request: {}", request);

        response.setContentType("text/html;charset=UTF-8");

        String req = generateCardTestRequest(request);

        PrintWriter writer = response.getWriter();

        writer.write("<html><head>");
        writer.write("<link href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css\" rel=\"stylesheet\"\n" +
                "          integrity=\"sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u\" crossorigin=\"anonymous\"/>\n" +
                "    ");
        writer.write("</head><body>");
        writer.write("<div class=\"container\">");
        writer.write("<h2>TTNET VPOS Vakıfbank Bağlantı Kontrol Uygulaması</h2> <p class=\"text-muted\">Yeni işlem denemek için tarayıcınızın geri tuşunu kullanınız.</p> ");
        writer.write("<b>İstek</b><br/>");
        writer.write("<code>");
        writer.write(StringEscapeUtils.escapeXml(req));
        writer.write("</code>");
        writer.write("<br/><br/>");
        writer.write("<b>Cevap</b><br/>");
        writer.write("<code>");
        writer.write(StringEscapeUtils.escapeXml(vakifConnection.execute(req)));
        writer.write("</code>");
        writer.write("</body></html>");
        writer.flush();
    }

    private String generateCardTestRequest(TestRequest request) {
        String template = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<VposRequest>\n" +
                "<MerchantId>%s</MerchantId>\n" +
                "<Password>%s</Password>\n" +
                "<TerminalNo>%s</TerminalNo>\n" +
                "<TransactionType>CardTest</TransactionType>\n" +
                "<ClientIp>127.0.0.1</ClientIp>\n" +
                "<Pan>%s</Pan>\n" +
                "<Expiry>%s</Expiry>\n" +
                "<Cvv>%s</Cvv>\n" +
                "<TransactionDeviceSource>0</TransactionDeviceSource>\n" +
                "</VposRequest>";

        return String.format(template,
                request.getMerchantid(),
                request.getTermpasswd(),
                request.getTerminalid(),
                request.getCcno(),
                request.getCcexp(),
                request.getCvv());
    }
}
