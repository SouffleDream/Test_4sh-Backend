package com.example.test4sh.services;

import com.example.test4sh.models.Movement;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

import static java.lang.System.exit;

@Service
public class MailSenderService {

    private final JavaMailSender mailSender;

    public MailSenderService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Update XML then send mail with the XML in attachment
     * @param movementType
     * @param movement
     * @param destinataire
     */
    public void SendMovement(String movementType, Movement movement, String destinataire) {

        String filePath="";
        if (movementType.equals("entry")){
            filePath = "templates/entry_movement.xml";
        } else if (movementType.equals("out")) {
            filePath = "templates/out_movement.xml";
        } else {
            exit(-1);
        }

        File xmlFile = new File(filePath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try{
            dBuilder = dbFactory.newDocumentBuilder();

            // parse xml file and load into document
            Document doc = dBuilder.parse(xmlFile);

            doc.getDocumentElement().normalize();
            updateXMLValue(doc, movement);

            writeXMLFile(doc, filePath);

            sendMailwithXMLAttachment(filePath, destinataire);

        } catch (ParserConfigurationException | IOException | TransformerException | SAXException | MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Send mail with XML Attachment
     * Based on the website https://www.codejava.net/frameworks/spring-boot/email-sending-tutorial
     * @param doc
     * @param mailTo
     * @throws MessagingException
     */
    private void sendMailwithXMLAttachment(String doc, String mailTo) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setSubject("New movement registered");
        helper.setFrom("souffledream.ventreveur@gmail.com");
        helper.setTo(mailTo);

        helper.setText("<b>Hi</b>,<br> Attached you will find your last recorded movement. <br> <b>The Cargo Gestion Company</b>");
        FileSystemResource file = new FileSystemResource(new File(doc));
        helper.addAttachment(doc, file);

        mailSender.send(message);
    }

    /**
     * Based on the website https://www.javaguides.net/2018/10/how-to-modify-or-update-xml-file-in-java-dom-parser.html
     * @param doc
     * @param filePath
     * @throws TransformerFactoryConfigurationError
     * @throws TransformerConfigurationException
     * @throws TransformerException
     */
    private static void writeXMLFile(Document doc, String filePath)
            throws TransformerFactoryConfigurationError, TransformerConfigurationException, TransformerException {
        doc.getDocumentElement().normalize();
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(filePath));
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(source, result);
        System.out.println("XML file updated successfully");
    }

    /**
     * Update XML CargoMessage
     * Based on the website https://www.javaguides.net/2018/10/how-to-modify-or-update-xml-file-in-java-dom-parser.html
     * @param doc
     */
    private static void updateXMLValue(Document doc, Movement movement) {
        NodeList messages = doc.getElementsByTagName("CargoMessage");
        Element message = null;
        LocalDateTime time = LocalDateTime.now();
        for(int i = 0; i < messages.getLength(); i++){
            message = (Element) messages.item(i);
            Node header = message.getElementsByTagName("Header").item(0);
            NamedNodeMap attrib = header.getAttributes();
            Node messageTime = attrib.getNamedItem("messageTime");
            messageTime.setTextContent(time.toString());
            Node id = attrib.getNamedItem("messageId");
            id.setTextContent(movement.getId().toString());

            // WarehouseMovementIn
            NodeList infos = message.getElementsByTagName("WarehouseMovementIn");
            Element info = null;
            for(int j = 0; j < infos.getLength(); j++){
                info = (Element) infos.item(j);
                //Movement Time
                Node movementTime = info.getElementsByTagName("movementTime").item(0);
                movementTime.setTextContent(String.valueOf(movement.getMovementTime()));

                // From
                Node fromWarehouse = info.getElementsByTagName("from").item(0);
                NamedNodeMap fromWarehouseAttrib = fromWarehouse.getAttributes();
                Node codeFrom = fromWarehouseAttrib.getNamedItem("code");
                codeFrom.setTextContent(movement.getCodeWarehouse());
                Node labelFrom = fromWarehouseAttrib.getNamedItem("label");
                labelFrom.setTextContent(movement.getLabelWarehouse());

                // Goods
                NodeList goods = info.getElementsByTagName("goods");
                Element good = null;
                for(int x = 0; x < goods.getLength(); x++) {
                    good = (Element) goods.item(x);
                    // Ref
                    Node ref = good.getElementsByTagName("ref").item(0);
                    NamedNodeMap refAttrib = ref.getAttributes();
                    Node type = refAttrib.getNamedItem("type");
                    type.setTextContent(movement.getRefType());
                    Node codeRef = refAttrib.getNamedItem("codeRef");
                    codeRef.setTextContent(movement.getRef());

                    // Amount
                    Node amount = good.getElementsByTagName("amount").item(0);
                    NamedNodeMap amountAttrib = amount.getAttributes();
                    Node quantity = amountAttrib.getNamedItem("quantity");
                    quantity.setTextContent(movement.getQuantity().toString());
                    Node weight = amountAttrib.getNamedItem("weight");
                    weight.setTextContent(movement.getWeight().toString());

                    // Description
                    Node description = good.getElementsByTagName("description").item(0);
                    description.setTextContent(movement.getDescription());

                    // Total Ref Amount
                    Node totalRefAmount = good.getElementsByTagName("totalRefAmount").item(0);
                    NamedNodeMap totalRefAmountAttrib = totalRefAmount.getAttributes();
                    Node totalQuantity = totalRefAmountAttrib.getNamedItem("quantity");
                    totalQuantity.setTextContent(movement.getTotalQuantity().toString());
                    Node totalWeight = totalRefAmountAttrib.getNamedItem("weight");
                    totalWeight.setTextContent(movement.getTotalWeight().toString());
                }
                // Customs Status
                Node description = info.getElementsByTagName("customsStatus").item(0);
                description.setTextContent(movement.getStatus());
            }
        }
    }
}
