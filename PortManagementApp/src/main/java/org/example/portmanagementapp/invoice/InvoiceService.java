package org.example.portmanagementapp.invoice;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final BasicInvoiceGenerator generator;

    public List<Invoice> generateForMonth(YearMonth month) {
        return generator.generateInvoices(month);
    }
}
