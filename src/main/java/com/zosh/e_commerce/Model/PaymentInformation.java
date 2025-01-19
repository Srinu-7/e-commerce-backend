package com.zosh.e_commerce.Model;

import jakarta.persistence.Embeddable;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Embeddable
public class PaymentInformation {

    String cardHolderName;

    String cardNumber;

    LocalDate expiryDate;

    String cvv;
}
