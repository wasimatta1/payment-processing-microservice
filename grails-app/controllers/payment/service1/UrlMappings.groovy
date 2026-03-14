package payment.service1

class UrlMappings {
    static mappings = {
        "/api/merchants"(controller: "merchant", action: "save", method: "POST")

        "/api/payments"(resources: "payment", excludes: ['show'])

        "/api/payments/$reference/capture"(controller: "payment", action: "capture", method: "POST")
        "/api/payments/$reference/refund"(controller: "payment", action: "refund", method: "POST")
        "/api/payments/$reference"(controller: "payment", action: "show", method: "GET")

        "500"(controller: "error", action: "handle")
        "404"(controller: "error", action: "notFound")
    }
}

// capture/refund mappings as before