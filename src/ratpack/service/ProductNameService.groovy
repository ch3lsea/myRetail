package service

import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils

@Slf4j
class ProductNameService {
	static getProductName(String productId) throws RuntimeException {
		try {
			// creates an httpClient, does a get to redsky.target.com for a particular productId
			CloseableHttpClient httpclient = HttpClients.createDefault()
			HttpGet httpGet = new HttpGet("http://redsky.target.com/v2/pdp/tcin/$productId")
			CloseableHttpResponse response = httpclient.execute(httpGet)
			def entity = response.getEntity()
			String responseString = EntityUtils.toString(entity, "UTF-8")

			def status = response.getStatusLine().toString()
			log.info("response status: $status")

			// checks to make sure it got a success response or throws an exception
			if (status.contains("200")) {
				// parses response body into a map
				def slurper = new JsonSlurper()
				def result = slurper.parseText(responseString)
				log.debug("result: $result")

				// returns the product title
				return result.product.item.product_description.title
			}
			if (status.contains("404")) {
				throw new RuntimeException("404 Product not found")
			}

		} catch (Exception e) {
			log.debug("get-product-name-service-error")
			if (e.message.contains("404")) {
				throw new RuntimeException("Unable to find product with id $productId", e)
			} else {
				throw new RuntimeException("{productId: $productId, message: ${e.message}")
			}
		}
	}
}
