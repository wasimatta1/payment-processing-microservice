package payment.processing

class MerchantController {

    MerchantService merchantService

    def save(CreateMerchantCommand cmd) {
        try {
            Merchant merchant = merchantService.createMerchant(cmd)
            MerchantResponseDTO response = new MerchantResponseDTO(merchant)
            respond response, [status: 201]
        } catch (ValidationException e) {
            respond([errorCode: "400", error: e.message, details: e.errors], status: 400)
        }
    }
}