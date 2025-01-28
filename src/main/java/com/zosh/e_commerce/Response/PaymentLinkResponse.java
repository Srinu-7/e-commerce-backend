package com.zosh.e_commerce.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class PaymentLinkResponse {
     String paymentLinkId;
     String paymentLinkUrl;
}
