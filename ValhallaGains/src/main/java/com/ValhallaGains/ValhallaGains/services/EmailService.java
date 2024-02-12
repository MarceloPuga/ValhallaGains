package com.ValhallaGains.ValhallaGains.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class EmailService {
    @Autowired
    JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")

    private String email;
    public void sendListEmail(String emailTo, byte[] pdfAttachment){
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(email);
            helper.setTo(emailTo);
            helper.setSubject(" ¡Tu comprobante de compra de ValhallaGains, esta listo!");
            helper.setText("¡Saludos, muchas gracias por confiar en ValhallaGains!");
            helper.addAttachment("purchase_details.pdf", new ByteArrayResource(pdfAttachment));

            javaMailSender.send(message);

        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    public void sendMessageAdmin(String emailTo, String messageAdmin){
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(email);
            helper.setTo(emailTo);
            helper.setSubject(" ¡ESTE ES UN MENSAJE DEL ADMINISTRADOR DE VALHALLAGAINS!");
            helper.setText(messageAdmin);

            javaMailSender.send(message);

        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    public void sendWelcome(String emailTo, String accessCode){
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(email);
            helper.setTo(emailTo);
            helper.setSubject(" ¡Bienvenido a ValhallaGains, Guerrero/a Valiente!");
            helper.setText("¡Saludos, intrépido guerrero!\n" +
                    "\n" +
                    "En nombre de todo el equipo de ValhallaGains, te damos la más cordial bienvenida a nuestro reino de fuerza y valentía. Nos llena de orgullo tenerte como parte de nuestra comunidad, donde la determinación y el espíritu guerrero se entrelazan para forjar cuerpos y mentes más fuertes.\n" +
                    "\n" +
                    "En ValhallaGains, nos esforzamos por brindarte la experiencia más épica y motivadora mientras te embarcas en tu viaje de transformación física y mental. Inspirados por la cultura nórdica y vikinga, nuestro gimnasio es un lugar donde la fuerza interior se encuentra con la disciplina exterior, donde cada levantamiento es un tributo a tu propia valentía.\n" +
                    "\n" +
                    "Tu Membresía:\n" +
                    "Tu membresía en ValhallaGains te otorga acceso exclusivo a nuestras instalaciones de entrenamiento de clase mundial, sesiones de acondicionamiento físico diseñadas por expertos y una comunidad de guerreros dedicados que comparten tus objetivos. ¡Prepárate para conquistar cada desafío que se presente!\n" +
                    "\n" +
                    "Código de Acceso a Valhalla:\n" +
                    "Como guerrero recién llegado, te otorgamos el código de acceso a Valhalla. Utiliza este código con orgullo para abrir las puertas de nuestra fortaleza y sumergirte en un viaje de transformación inigualable.\n" +
                    "\n" +
                    "Código de Acceso: " + accessCode + "\n" +
                    "\n" +
                    "Recuerda, en ValhallaGains, no solo esculpimos cuerpos, sino que también forjamos camaradería. Estamos aquí para apoyarte en cada paso del camino. Si tienes alguna pregunta o necesitas orientación, no dudes en contactarnos. ¡Juntos conquistaremos nuevos horizontes!\n" +
                    "\n" +
                    "¡Que los dioses te guíen en tu viaje hacia la grandeza!\n" +
                    "\n" +
                    "Con fuerza y honor,\n" +
                    "El Equipo de ValhallaGains");

            javaMailSender.send(message);

        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

}
