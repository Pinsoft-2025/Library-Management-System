package io.github.SenaUstun_Dev.library_management.service;
import jakarta.servlet.http.HttpServletResponse;

import java.io.ByteArrayInputStream;

public interface BookExportService {
    public void exportAllBooksToExcel(HttpServletResponse response);
    public void exportAllLostBooksToExcel(HttpServletResponse response);
    public void exportAllBorrowedBooksToExcel(HttpServletResponse response);
    public void exportAllActiveBooksToExcel(HttpServletResponse response);
}
