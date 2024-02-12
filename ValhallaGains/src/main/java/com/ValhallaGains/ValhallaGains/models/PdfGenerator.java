
package com.ValhallaGains.ValhallaGains.models;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class PdfGenerator {

    public static byte[] generatePdf(String productsData,String purchaseData,String formattedTotalAmount,String totalAmountLabel) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try (PdfWriter writer = new PdfWriter(outputStream);
             PdfDocument pdfDocument = new PdfDocument(writer);
             Document document = new Document(pdfDocument)) {
            PdfFont fuenteTimesRoman = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);
            float fontSize = 18F;
            // Crear una tabla para organizar la imagen y el texto
            Table table = new Table(2);
            table.setWidth(UnitValue.createPercentValue(100)); // Ajustar al 100% del ancho

            Image logo = new Image(ImageDataFactory.create("https://res.cloudinary.com/dfbffoqjc/image/upload/v1706633237/imagenes%20ValhallaGains/nraqdvaclzefevil9mey.png"));
            logo.setWidth(70);
            logo.setHeight(50);
            Cell logoCell = new Cell().add(logo);
            logoCell.setBorder(Border.NO_BORDER);
            logoCell.setVerticalAlignment(VerticalAlignment.MIDDLE); // Alinear verticalmente al centro
            table.addCell(logoCell);

            Paragraph title = new Paragraph("Valhalla Gains");
            title.setFont(fuenteTimesRoman);
            title.setFontSize(40F);
            title.setFontColor(ColorConstants.ORANGE);
            Cell titleCell = new Cell().add(title);
            titleCell.setBorder(Border.NO_BORDER);
            titleCell.setVerticalAlignment(VerticalAlignment.MIDDLE); // Alinear verticalmente al centro
            table.addCell(titleCell);
            table.setHorizontalAlignment(HorizontalAlignment.LEFT);
            document.add(table);
            Paragraph subTitle = new Paragraph("Purchase Details:");
            subTitle.setFont(fuenteTimesRoman);
            subTitle.setFontSize(20F);
            document.add(subTitle);
            // Obtener datos para la segunda tabla (detalles de productos)
            String[] lines = productsData.split("\n");
            List<String[]> products = new ArrayList<>();
            for (String line : lines) {
                String[] columns = line.split("\\s+");
                if (columns.length == 3) {
                    products.add(columns);
                }

            }

            // Crear la segunda tabla
            Table detailsTable = new Table(3);
            detailsTable.setWidth(UnitValue.createPercentValue(100));
            detailsTable.setHorizontalAlignment(HorizontalAlignment.CENTER);
            Cell nameHeaderCell = new Cell().add(new Paragraph("Name Product").setFont(fuenteTimesRoman).setFontSize(fontSize));
            Cell quantityHeaderCell = new Cell().add(new Paragraph("Quantity").setFont(fuenteTimesRoman).setFontSize(fontSize));
            Cell priceHeaderCell = new Cell().add(new Paragraph("Unit Price").setFont(fuenteTimesRoman).setFontSize(fontSize));
            detailsTable.addCell(nameHeaderCell);
            detailsTable.addCell(quantityHeaderCell);
            detailsTable.addCell(priceHeaderCell);
            for (String[] columns : products) {
                detailsTable.addCell(columns[0]);
                detailsTable.addCell(columns[1]);
                detailsTable.addCell(columns[2]);
            }
            Cell totalAmountHeaderCell = new Cell(1, 2).add(new Paragraph(totalAmountLabel).setFont(fuenteTimesRoman).setFontSize(14F));
            Cell totalAmountValueCell = new Cell().add(new Paragraph(formattedTotalAmount).setFont(fuenteTimesRoman).setFontSize(14F));
            detailsTable.addCell(totalAmountHeaderCell);
            detailsTable.addCell(totalAmountValueCell);
            document.add(detailsTable.setFont(fuenteTimesRoman).setFontSize(14F));
            document.add(new Paragraph(purchaseData).setFont(fuenteTimesRoman).setFontSize(14F));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return outputStream.toByteArray();
    }
}
