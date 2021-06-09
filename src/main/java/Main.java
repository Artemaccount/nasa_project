import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Main {

    // Ключ удалил на всякий, говорят разные ключи парсят на гитхабе
    public static final String NASA_URI =
            "https://api.nasa.gov/planetary/apod?api_key=ВАШ_КЛЮЧ";


    public static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {

        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();

        HttpGet request = new HttpGet(NASA_URI);
        request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
        CloseableHttpResponse response = httpClient.execute(request);
        NasaRequest input = mapper.readValue(
                response.getEntity().getContent(),
                new TypeReference<NasaRequest>() {
                });

        String jpgName = input.getHdurl().substring(input.getHdurl().lastIndexOf("/") + 1);

        URL url = new URL(input.getHdurl());

        BufferedImage img = ImageIO.read(url);
        File file = new File("C:\\" + jpgName);
        ImageIO.write(img, "jpg", file);

    }
}

