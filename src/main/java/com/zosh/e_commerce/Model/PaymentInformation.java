package com.zosh.e_commerce.Model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class PaymentInformation {

    String cardHolderName;

    String cardNumber;

    LocalDate expiryDate;

    String cvv;
}
