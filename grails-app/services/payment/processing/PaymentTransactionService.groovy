package payment.processing

import grails.gorm.transactions.Transactional

@Transactional
class PaymentTransactionService {

    PaymentTransaction createPayment(CreatePaymentCommand cmd, Merchant merchant) {
        if (cmd.hasErrors()) throw new ValidationException("Invalid payment data", cmd.errors)

        if (PaymentTransaction.findByReference(cmd.reference)) {
            throw new BusinessException("10", "Reference already exists")
        }

        PaymentTransaction tx = new PaymentTransaction(
                reference: cmd.reference,
                amount: cmd.amount,
                currency: cmd.currency,
                description: cmd.description,
                merchant: merchant,
                status: PaymentStatus.PENDING
        )
        if (!tx.save(flush: true)) {
            throw new ValidationException("Failed to save payment", tx.errors)
        }
        tx
    }

    @Transactional
    PaymentTransaction capturePayment(String reference) {
        PaymentTransaction tx = findPayment(reference)
        if (tx.status != PaymentStatus.PENDING) {
            throw new BusinessException("10", "Payment already captured")
        }
        tx.status = PaymentStatus.SUCCESS
        tx.save(flush: true)
        tx
    }

    @Transactional
    PaymentTransaction refundPayment(String reference) {
        PaymentTransaction tx = findPayment(reference)
        if (tx.status != PaymentStatus.SUCCESS) {
            throw new BusinessException("10", "Only SUCCESS payments can be refunded")
        }
        tx.status = PaymentStatus.REFUNDED
        tx.save(flush: true)
        tx
    }

    PaymentTransaction getPayment(String reference) {
        String cleanRef = reference?.trim()

        PaymentTransaction tx = PaymentTransaction.findByReference(cleanRef)
        if (!tx) {
            // Fallback search
            tx = PaymentTransaction.findByReferenceIlike("%${cleanRef}%")
            if (tx) println "Found with ilike: ${tx.reference}"
        }
        if (!tx) throw new BusinessException("404", "Payment not found for reference: ${cleanRef}")
        tx
    }

    List<PaymentTransaction> listPayments(Map params) {
        PaymentTransaction.createCriteria().list(max: params.int('max') ?: 10, offset: params.int('offset') ?: 0) {
            if (params.status) eq('status', PaymentStatus.valueOf(params.status.toString()))
            if (params.fromDate) ge('dateCreated', params.fromDate as Date)
            if (params.toDate) le('dateCreated', params.toDate as Date)
            order('dateCreated', 'desc')
        } as List<PaymentTransaction>
    }


}