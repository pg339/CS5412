package cornell.cs5412;

/**
 * Wrapper for some sort of HTTP response
 * Created by Pablo on 3/10/2016.
 */
public class HttpResponse {
    public String content;
    public int responseCode;

    public HttpResponse(String content, int responseCode) {
        this.content = content;
        this.responseCode = responseCode;
    }
}
