package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;

public class StepikNetworkService {

    private static final String API_URL = "https://stepik.org:443/api/courses";
    private static final String PAGE_PARAMETER = "page";

    private final String baseUrl;
    private final Gson gson;

    private final AtomicInteger lastPage;
    private volatile boolean isClosed = false;

    public StepikNetworkService() {
        baseUrl = API_URL + "?" + PAGE_PARAMETER + "=";
        gson = new GsonBuilder().setPrettyPrinting().create();
        lastPage = new AtomicInteger(1);
    }

    private String loadContent(String url) throws IOException {
        HttpResponse response = Request.Get(url).execute().returnResponse();

        if (response.getStatusLine().getStatusCode() == 404) {
            throw new NoSuchElementException("no requested page. baseUrl=" + url);
        }

        HttpEntity entity = response.getEntity();
        if (entity == null) {
            throw new NoSuchElementException("No entity. no requested page. baseUrl=" + url);
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
        return reader.readLine();
    }

    /**
     * Возвращает следующую страницу с курсами если она есть
     * @return лист курсов находящихся на указанной странице (pageNumber) на stepik.org
     * @throws NoSuchElementException   если не существует страницы с данным pageNumber
     * @throws IOException если произошла ошибка ввода вывода не связанная с реальным существованием страницы
     */
    public List<Course> loadNextPage() throws IOException, NoSuchElementException {
        if (isClosed) {
            throw new NoSuchElementException("Object is closed");
        }

        int pageNumber = lastPage.getAndIncrement();
        String content;

        try {
            content = loadContent(baseUrl + pageNumber);
        } catch (NoSuchElementException e) {
            isClosed = true;
            throw e;
        }

        StepikResponse stepikResponse = gson.fromJson(content, StepikResponse.class);
        if (!stepikResponse.meta.has_next) {
            isClosed = true;
        }
        return Arrays.asList(stepikResponse.courses);
    }

}