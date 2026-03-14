package payment.processing

import grails.validation.Validateable

class CreatePaymentCommand implements Validateable {
    String reference
    BigDecimal amount
    String currency
    String description

    static constraints = {
        reference blank: false
        amount min: 0.01
        currency blank: false
    }
}