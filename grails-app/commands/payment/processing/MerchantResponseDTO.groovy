package payment.processing

class MerchantResponseDTO {

    Long id
    String name
    String email
    String apiKey

    MerchantResponseDTO(Merchant merchant) {
        this.id = merchant.id
        this.name = merchant.name
        this.email = merchant.email
        this.apiKey = merchant.apiKey
    }
}