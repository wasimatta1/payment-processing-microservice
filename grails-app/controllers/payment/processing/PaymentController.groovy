package payment.processing

class PaymentController {

    PaymentTransactionService paymentTransactionService

    private Merchant getCurrentMerchant() {
        String apiKey = request.getHeader("X-API-KEY")
        if (!apiKey) throw new BusinessException("401", "Missing X-API-KEY header")
        Merchant merchant = Merchant.findByApiKeyAndActive(apiKey, true)
        if (!merchant) throw new BusinessException("401", "Invalid or inactive API key")
        merchant
    }

    def save(CreatePaymentCommand cmd) {
        Merchant merchant = getCurrentMerchant()
        try {
            PaymentTransaction tx = paymentTransactionService.createPayment(cmd, merchant)
            respond tx, [status: 201]
        } catch (e) {
            handleError(e)
        }
    }

    def capture(String reference) {
        try {
            respond paymentTransactionService.capturePayment(reference)
        } catch (e) {
            handleError(e)
        }
    }

    def refund(String reference) {
        try {
            respond paymentTransactionService.refundPayment(reference)
        } catch (e) {
            handleError(e)
        }
    }

    def show(String reference) {
        try {
            respond paymentTransactionService.getPayment(reference)
        } catch (e) {
            handleError(e)
        }
    }

    def index() {
        try {
            respond paymentTransactionService.listPayments(params)
        } catch (e) {
            handleError(e)
        }
    }

    private void handleError(Exception e) {
        if (e instanceof BusinessException) {
            respond([errorCode: e.errorCode, error: e.message], status: 400)
        } else if (e instanceof ValidationException) {
            respond([errorCode: "400", error: e.message], status: 400)
        } else {
            respond([errorCode: "500", error: e.message], status: 500)
        }
    }
}