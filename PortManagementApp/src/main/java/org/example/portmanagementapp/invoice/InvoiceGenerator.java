package org.example.portmanagementapp.invoice;

import java.time.YearMonth;
import java.util.List;

public interface InvoiceGenerator {
    List<Invoice> generateInvoices(YearMonth month);
}
