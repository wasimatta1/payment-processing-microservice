package payment.processing

import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
class Merchant {
    String name
    String email
    String apiKey
    Boolean active = true

    Date dateCreated
    Date lastUpdated

    static hasMany = [paymentTransactions: PaymentTransaction]

    static constraints = {
        name blank: false, nullable: false
        email email: true, blank: false, unique: true
        apiKey nullable: false, unique: true
        active nullable: false
    }

}