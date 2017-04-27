package myRetail

import grails.gorm.annotation.Entity

@Entity
class Price {
	double value
	String currency_code
	String product_id
}
