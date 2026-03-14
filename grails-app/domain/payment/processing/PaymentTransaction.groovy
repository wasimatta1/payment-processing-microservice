package payment.processing

import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
class PaymentTransaction {
    String reference
    BigDecimal amount
    String currency
    String description
    PaymentStatus status = PaymentStatus.PENDING
    Merchant merchant

    Date dateCreated
    Date lastUpdated

    static belongsTo = [merchant: Merchant]

    static constraints = {
        reference unique: true, blank: false ,nullable: false
        amount min: 0.01, nullable: false
        currency blank: false, nullable: false
        description nullable: true
        status nullable: false
        merchant nullable: false
    }
}