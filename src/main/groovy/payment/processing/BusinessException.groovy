package payment.processing

class BusinessException extends RuntimeException {
    String errorCode
    BusinessException(String code, String msg) {
        super(msg)
        this.errorCode = code
    }
}

class ValidationException extends RuntimeException {
    Object errors
    ValidationException(String msg, Object e) {
        super(msg)
        this.errors = e
    }
}