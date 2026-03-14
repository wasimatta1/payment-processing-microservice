package payment.processing

import grails.validation.Validateable

class CreateMerchantCommand implements Validateable {
    String name
    String email

    static constraints = {
        name blank: false, nullable: false
        email email: true, blank: false,nullable: false
    }
}