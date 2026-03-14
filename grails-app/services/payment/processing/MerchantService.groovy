package payment.processing

import grails.gorm.transactions.Transactional

@Transactional
class MerchantService {

    Merchant createMerchant(CreateMerchantCommand cmd) {
        if (cmd.hasErrors()) {
            throw new ValidationException("Invalid merchant data", cmd.errors)

        }
        Merchant merchant = new Merchant(name: cmd.name, email: cmd.email)

        if (!merchant.apiKey) {
            merchant.apiKey = UUID.randomUUID().toString().replace("-", "")
        }

        if (!merchant.save(flush: true)) {
            throw new ValidationException("Failed to save merchant", merchant.errors)
        }

        merchant
    }
}