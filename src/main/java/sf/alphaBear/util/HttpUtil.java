package sf.alphaBear.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

/**
 * 
 */
public class HttpUtil {
	public static void main(String[] args) {
	}

	private HttpUtil() {
	}

	public static final String executeGetWithHeader(String url, Header[] headers) {
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		try (CloseableHttpClient client = httpClientBuilder.build();) {

			HttpGet get = new HttpGet(url);

			// 设置超时时间
			RequestConfig conf = RequestConfig.custom().setConnectTimeout(10 * 1000)
					.setConnectionRequestTimeout(10 * 1000).setSocketTimeout(30 * 1000).build();
			get.setConfig(conf);

			if (headers != null && headers.length > 0) {
				get.setHeaders(headers);
			}
			HttpResponse httpResponse = client.execute(get);
			HttpEntity entity = httpResponse.getEntity();

			if (entity != null) {
				return EntityUtils.toString(entity);
			} else
				return "unknown";

		} catch (Exception e) {
			return "unknown";
		}
	}

	public static final boolean downloadFile(String localPath, String remoteUrl) {
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();

		InputStream input = null;
		FileOutputStream out = null;

		try (CloseableHttpClient client = httpClientBuilder.build();) {
			HttpGet get = new HttpGet(remoteUrl);
			// 设置超时时间
			RequestConfig conf = RequestConfig.custom().setConnectTimeout(10 * 1000)
					.setConnectionRequestTimeout(10 * 1000).setSocketTimeout(30 * 1000).build();
			get.setConfig(conf);

			HttpResponse response = client.execute(get);
			HttpEntity entity = response.getEntity();
			if (entity == null) {
				return false;
			}
			input = entity.getContent();
			out = new FileOutputStream(new File(localPath));

			byte[] buff = new byte[4 * 1024];
			int byteRead = -1;
			while ((byteRead = input.read(buff)) != -1) {
				out.write(buff, 0, byteRead);
			}
			out.flush();
			EntityUtils.consume(entity);
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}

	public static final String executeGet(String url) {
		return executeGetWithHeader(url, null);
	}

	public static final String executePost(String url, String json) {
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		try (CloseableHttpClient client = httpClientBuilder.build();) {
			HttpPost post = new HttpPost(url);
			
			StringEntity stringEntity = new StringEntity(json, "UTF-8");
			stringEntity.setContentType("application/json");
			post.setEntity(stringEntity);
			
			// 设置超时时间
			RequestConfig conf = RequestConfig.custom().setConnectTimeout(10 * 1000)
					.setConnectionRequestTimeout(10 * 1000).setSocketTimeout(30 * 1000).build();
			post.setConfig(conf);

			HttpResponse response = client.execute(post);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				return EntityUtils.toString(entity);
			} else
				return "unknown";

		} catch (Exception e) {
			return "unknown";
		}
	}

	public static final String getLocationUrl(String orgUrl) {
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		try (CloseableHttpClient client = httpClientBuilder.build();) {
			HttpPost post = new HttpPost(orgUrl);
			// 设置超时时间
			RequestConfig conf = RequestConfig.custom().setConnectTimeout(10 * 1000)
					.setConnectionRequestTimeout(10 * 1000).setSocketTimeout(30 * 1000).build();
			post.setConfig(conf);

			HttpResponse httpResponse = client.execute(post);
			// 获取响应消息实体
			Header header = httpResponse.getFirstHeader("Location");
			// 判断响应实体是否为空
			if (header != null) {
				return header.getValue();
			} else
				return orgUrl;
		} catch (Exception e) {
			return "unknown";
		}
	}

}
