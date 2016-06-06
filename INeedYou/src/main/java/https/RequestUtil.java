package https;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * 
 * @author Administrator
 */
public class RequestUtil {
	
	private static RequestUtil requestUtil;
    private static DefaultHttpClient httpclient;  
    
    static {  
        httpclient = new DefaultHttpClient();  
        httpclient = (DefaultHttpClient) HttpClientConnectionManager.getSSLInstance(httpclient); // 接受任何证书的浏览器客户�? 
    }  
    
    public static RequestUtil getInstance(){
    	if(requestUtil == null)
    		return new RequestUtil();
    	else 
    		return requestUtil;
    }
    
	/**
	 * 
	 * @param type
	 * @param url
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public String request(String url) throws ClientProtocolException, IOException{
        HttpPost httpost = HttpClientConnectionManager.getPostMethod(url);  
		//HttpGet httpGet = HttpClientConnectionManager.getGetMethod(url);
        HttpResponse response = httpclient.execute(httpost);  
        String jsonStr = EntityUtils.toString(response.getEntity(), "utf-8");  
		return jsonStr;
	}
}
