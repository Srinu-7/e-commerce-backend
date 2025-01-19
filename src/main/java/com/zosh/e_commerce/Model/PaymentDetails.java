package com.zosh.e_commerce.Model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentDetails {

    String paymentMethod;

    String status;

    String paymentId;

    String razorPaymentId;

    String razorPaymentLinkReferenceId;

    String razorPaymentLinkId;

    String razorPaymentStatus;


}
