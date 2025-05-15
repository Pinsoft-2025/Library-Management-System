package io.github.SenaUstun_Dev.library_management.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import io.github.SenaUstun_Dev.library_management.entity.Book;
import io.github.SenaUstun_Dev.library_management.entity.enums.BookStatus;
import io.github.SenaUstun_Dev.library_management.repository.BookRepository;
import io.github.SenaUstun_Dev.library_management.service.BookExportService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookExportServiceImpl implements BookExportService {

    private final BookRepository bookRepository;

    @Override
    public void exportAllBooksToExcel(HttpServletResponse response) {
        try {
            // Tüm kitapları getir
            List<Book> books = bookRepository.findAll();
            
            // Excel dosyası oluştur
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Tüm Kitaplar");
            
            // Başlık satırını oluştur
            createHeaderRow(sheet, workbook);
            
            // Veri satırlarını oluştur
            fillBookData(sheet, books);
            
            // Response'u ayarla ve dosyayı yaz
            setupResponseAndWriteWorkbook(response, workbook, "tum_kitaplar.xlsx");
            
        } catch (IOException e) {
            throw new RuntimeException("Excel dosyası oluşturulurken hata oluştu: " + e.getMessage());
        }
    }

    @Override
    public void exportAllLostBooksToExcel(HttpServletResponse response) {
        try {
            // Kayıp kitapları getir
            List<Book> lostBooks = bookRepository.findByStatus(BookStatus.LOST);
            
            // Excel dosyası oluştur
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Kayıp Kitaplar");
            
            // Başlık satırını oluştur
            createHeaderRow(sheet, workbook);
            
            // Veri satırlarını oluştur
            fillBookData(sheet, lostBooks);
            
            // Response'u ayarla ve dosyayı yaz
            setupResponseAndWriteWorkbook(response, workbook, "kayip_kitaplar.xlsx");
            
        } catch (IOException e) {
            throw new RuntimeException("Excel dosyası oluşturulurken hata oluştu: " + e.getMessage());
        }
    }

    @Override
    public void exportAllBorrowedBooksToExcel(HttpServletResponse response) {
        try {
            // Ödünç alınmış kitapları getir
            List<Book> borrowedBooks = bookRepository.findByStatus(BookStatus.BORROWED);
            
            // Excel dosyası oluştur
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Ödünç Alınmış Kitaplar");
            
            // Başlık satırını oluştur
            createHeaderRow(sheet, workbook);
            
            // Veri satırlarını oluştur
            fillBookData(sheet, borrowedBooks);
            
            // Response'u ayarla ve dosyayı yaz
            setupResponseAndWriteWorkbook(response, workbook, "odunc_alinmis_kitaplar.xlsx");
            
        } catch (IOException e) {
            throw new RuntimeException("Excel dosyası oluşturulurken hata oluştu: " + e.getMessage());
        }
    }

    @Override
    public void exportAllActiveBooksToExcel(HttpServletResponse response) {
        try {
            // Aktif kitapları getir
            List<Book> activeBooks = bookRepository.findByStatus(BookStatus.ACTIVE);
            
            // Excel dosyası oluştur
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Aktif Kitaplar");
            
            // Başlık satırını oluştur
            createHeaderRow(sheet, workbook);
            
            // Veri satırlarını oluştur
            fillBookData(sheet, activeBooks);
            
            // Response'u ayarla ve dosyayı yaz
            setupResponseAndWriteWorkbook(response, workbook, "aktif_kitaplar.xlsx");
            
        } catch (IOException e) {
            throw new RuntimeException("Excel dosyası oluşturulurken hata oluştu: " + e.getMessage());
        }
    }
    
    // Yardımcı Metodlar
    
    private void createHeaderRow(Sheet sheet, Workbook workbook) {
        // Başlık stili oluştur
        CellStyle headerStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        headerStyle.setFont(font);
        
        // Başlık satırını oluştur
        Row headerRow = sheet.createRow(0);
        
        // Başlık hücrelerini oluştur
        Cell idCell = headerRow.createCell(0);
        idCell.setCellValue("ID");
        idCell.setCellStyle(headerStyle);
        
        Cell nameCell = headerRow.createCell(1);
        nameCell.setCellValue("Kitap Adı");
        nameCell.setCellStyle(headerStyle);
        
        Cell statusCell = headerRow.createCell(2);
        statusCell.setCellValue("Durum");
        statusCell.setCellStyle(headerStyle);
        
        Cell authorsCell = headerRow.createCell(3);
        authorsCell.setCellValue("Yazarlar");
        authorsCell.setCellStyle(headerStyle);
        
        Cell publishersCell = headerRow.createCell(4);
        publishersCell.setCellValue("Yayıncılar");
        publishersCell.setCellStyle(headerStyle);
        
        Cell genresCell = headerRow.createCell(5);
        genresCell.setCellValue("Türler");
        genresCell.setCellStyle(headerStyle);
        
        // Sütun genişliklerini ayarla
        sheet.setColumnWidth(0, 3000);  // ID
        sheet.setColumnWidth(1, 6000);  // Kitap Adı
        sheet.setColumnWidth(2, 4000);  // Durum
        sheet.setColumnWidth(3, 8000);  // Yazarlar
        sheet.setColumnWidth(4, 8000);  // Yayıncılar
        sheet.setColumnWidth(5, 6000);  // Türler
    }
    
    private void fillBookData(Sheet sheet, List<Book> books) {
        int rowNum = 1;
        
        for (Book book : books) {
            Row row = sheet.createRow(rowNum++);
            
            // Kitap kimliği (ID)
            row.createCell(0).setCellValue(book.getId());
            
            // Kitap adı
            row.createCell(1).setCellValue(book.getName());
            
            // Kitap durumu
            row.createCell(2).setCellValue(book.getStatus().toString());
            
            // Kitabın yazarları (virgülle ayrılmış)
            String authors = book.getAuthors().stream()
                    .map(author -> {
                        if (author.getPenName() != null && !author.getPenName().isEmpty()) {
                            return author.getPenName();
                        } else {
                            return author.getFirstName() + " " + author.getSecondName();
                        }
                    })
                    .collect(Collectors.joining(", "));
            row.createCell(3).setCellValue(authors);
            
            // Kitabın yayıncıları (virgülle ayrılmış)
            String publishers = book.getPublishers().stream()
                    .map(publisher -> publisher.getName())
                    .collect(Collectors.joining(", "));
            row.createCell(4).setCellValue(publishers);
            
            // Kitap türleri (virgülle ayrılmış)
            String genres = book.getGenres().stream()
                    .map(genre -> genre.getName())
                    .collect(Collectors.joining(", "));
            row.createCell(5).setCellValue(genres);
        }
    }
    
    private void setupResponseAndWriteWorkbook(HttpServletResponse response, Workbook workbook, String filename) throws IOException {
        // Response başlıklarını ayarla
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);
        
        // Dosyayı yaz
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}
