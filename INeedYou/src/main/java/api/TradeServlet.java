package api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import config.PrivateConfig;

@WebServlet(name="TradeServlet",urlPatterns="/api/*")
public class TradeServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
	  response.setContentType("text/html;charset=utf-8");
		
	  String ACCESS_KEY = PrivateConfig.ACCESSKEY;
		String SECRET_KEY = PrivateConfig.SECRETKEY;
		
		PrintWriter out = response.getWriter();
		
		String method = request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/") + 1);
		response.setContentType("text/html"); 
		String url = "https://trade.chbtc.com/api/";
		String params = "";
		SECRET_KEY = EncryDigestUtil.digest(SECRET_KEY);
		if("order".equals(method)){
			String price = request.getParameter("price");
			String amount = request.getParameter("amount");
			String tradeType = request.getParameter("tradeType");
			String currency = request.getParameter("currency");
			params = "method=order&accesskey="+ACCESS_KEY + "&price="+price+"&amount="+amount+"&tradeType="+tradeType+"&currency=" + currency;
		}
		else if("cancelOrder".equals(method)){
			String id = request.getParameter("id");
			String currency = request.getParameter("currency");
			params = "method=cancelOrder&accesskey="+ACCESS_KEY + "&id="+id+"&currency=" + currency;
		}
		else if("getOrder".equals(method)){
			String id = request.getParameter("id");
			String currency = request.getParameter("currency");
			params = "method=getOrder&accesskey="+ACCESS_KEY + "&id="+id+"&currency=" + currency;
		}
		else if("getOrders".equals(method)){
			String tradeType = request.getParameter("tradeType");
			String currency = request.getParameter("currency");
			String pageIndex = request.getParameter("pageIndex");
			params = "method=getOrders&accesskey="+ACCESS_KEY +"&tradeType="+tradeType+"&currency=" + currency + "&pageIndex=" + pageIndex;
		}
		else if("getAccountInfo".equals(method)){
			params = "method=getAccountInfo&accesskey="+ACCESS_KEY;
		}
		else if("getUnfinishedOrdersIgnoreTradeType".equals(method)){
		  params = "method=getUnfinishedOrdersIgnoreTradeType&accesskey="+ACCESS_KEY+"&currency=btc&pageIndex=1&pageSize=50";
		}
		else {
			out.print("��Ч������");
		}
		
		String sign = EncryDigestUtil.hmacSign(params, SECRET_KEY);
		url += method +"?" + params + "&sign=" + sign + "&reqTime=" + System.currentTimeMillis();
		
		try {
			System.out.println(url);
			String result = testRequest(url);
			//String result = RequestUtil.getInstance().request(url);
			System.out.println(result);
			out.print(result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			out.flush();
			out.close();
		}
			
	}

	public String testRequest(String reqUrl) throws Exception {  
	        URL url = new URL(reqUrl);  
	        URLConnection connection = url.openConnection();  
	        connection.setDoOutput(true);  
	        OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "utf-8");
	        
	        out.flush();  
	        out.close();  
	        
	        String sCurrentLine;  
	        String sTotalString;  
	        sCurrentLine = "";  
	        sTotalString = "";  
	        InputStream l_urlStream;  
	        l_urlStream = connection.getInputStream();  
	        BufferedReader l_reader = new BufferedReader(new InputStreamReader(  
	                l_urlStream));  
	        while ((sCurrentLine = l_reader.readLine()) != null) {  
	            sTotalString += sCurrentLine;  
	        }  
	        return sTotalString;
	}
}
