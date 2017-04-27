import static ratpack.groovy.Groovy.ratpack

import myRetail.Price
import org.grails.datastore.mapping.mongo.MongoDatastore
import service.ProductNameService


MongoDatastore datastore = new MongoDatastore(Price)
ProductNameService productNameService = new ProductNameService()

ratpack {
    handlers {
        get("products/:id") {
            try {
                Price price = Price.findByProduct_id(pathTokens.id)

                Map json = [
                    id           : pathTokens.id,
                    name         : productNameService.getProductName(pathTokens.id),
                    price        : price.value,
                    currency_code: price.currency_code
                ]

                render json.toString()
            } catch (Exception e) {
                String error = e.toString()
                render "Exception thrown [$error]"
            }
        }
        get() {
            if (Price.count == 0) {
                Price price = new Price(value: 13.49, currency_code: 'USD', product_id: 13860428)
                Price price1 = new Price(value: 19.99, currency_code: 'USD', product_id: 16483589)
                Price price2 = new Price(value: 24.99, currency_code: 'USD', product_id: 16696652)
                Price price3 = new Price(value: 13.99, currency_code: 'USD', product_id: 16752456)
                Price price4 = new Price(value: 15.99, currency_code: 'USD', product_id: 15643793)
                price.save(flush: true)
                price1.save(flush: true)
                price2.save(flush: true)
                price3.save(flush: true)
                price4.save(flush: true)
            }
            render("There are ${Price.count()} prices")
        }
    }
}
