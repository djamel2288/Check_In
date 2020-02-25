package sample;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;


public class pdfGenerator {


    public void creatPDF(String pdf, String logo_event, String qrc, String id, String partner)throws Exception{
        String logo_itc;
        logo_itc = "src/assets/Events/itc_logo.png";

        Document doc = new Document();
        try {
            PdfWriter.getInstance(doc, new FileOutputStream(pdf));
            doc.open();

            /* HEAD */
            String filename = logo_itc;
            Image image = Image.getInstance(filename);
            image.setAbsolutePosition(25, 650);
            image.scaleToFit(200f, 100f);
            doc.add(image);

            filename = logo_event;
            image = Image.getInstance(filename);
            image.setAbsolutePosition(210, 660);
            image.scaleToFit(200f, 100f);
            doc.add(image);

            filename = logo_itc;
            image = Image.getInstance(filename);
            image.setAbsolutePosition(460, 650);
            image.scaleToFit(200f, 100f);
            doc.add(image);
            /* HEAD */

            /*alignement*/
            // Creates a check for the paragraphs contents
            Chunk chunk = new Chunk(ParagraphAlignment.CONTENT);

            // Creates paragraphs and set the alignment of the paragraph.
            // We use the Paragraph.ALIGN_LEFT, Paragraph.ALIGN_CENTER
            // and Paragraph.ALIGN_RIGHT
            Paragraph para1 = new Paragraph(chunk);
            para1.setFirstLineIndent(45);
            para1.setAlignment(Paragraph.ALIGN_LEFT);
            para1.setSpacingBefore(220);
            para1.setSpacingAfter(50);
            doc.add(para1);
            /*alignement*/


            /*img pos*/
            Paragraph para2 = new Paragraph();
            filename = qrc;
            image = Image.getInstance(filename);
            image.scaleToFit(150f, 150f);
            para2.add(image);
            para2.add(id);
            image.setAlignment(Paragraph.ALIGN_CENTER);
            para2.setAlignment(Paragraph.ALIGN_CENTER);
            doc.add(para2);
            /*img pos*/

            /* font */
            Font largeBold = new Font(Font.FontFamily.TIMES_ROMAN, 26,
                    Font.BOLD);

            Paragraph para3 = new Paragraph("ITC's PARTNER", largeBold);
            para3.setSpacingAfter(20);
            para3.setSpacingBefore(30);
            para3.setAlignment(Paragraph.ALIGN_CENTER);
            doc.add(para3);
            /* font */

            /* partber */
            filename = partner;
            image = Image.getInstance(filename);
            image.scaleToFit(200f, 100f);
            image.setAlignment(Paragraph.ALIGN_CENTER);
            doc.add(image);
            /* partber */

        } catch (DocumentException | IOException e) {
            //e.printStackTrace();
            System.out.println("chemain d'accés spécifié est introuvable! "+e);
        } finally {
            doc.close();
        }

    }

}
