package org.example.portmanagementapp.invoice;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.YearMonth;

@Data
@AllArgsConstructor
public class Invoice {
    private String username;
    private double totalAmount;
    private YearMonth month;
}
