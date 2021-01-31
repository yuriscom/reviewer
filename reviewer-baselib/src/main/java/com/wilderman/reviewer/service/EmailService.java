package com.wilderman.reviewer.service;

import org.springframework.stereotype.Service;

@Service
public class EmailService extends BaseService {


//    @Value("${sendgrid.api.key}")
//    private String sendgridApiKey;
//
//    @Value("${sendgrid.mail.sender}")
//    private String senderEmailAddr;

//    @Autowired
//    private final ThymeLeafUtils thymeLeaf = null;

//    @Autowired
//    private final SendGrid sendgrid = null;

//    @Bean
//    public SendGrid createSendGrid() {
//        return new SendGrid(sendgridApiKey);
//    }

//    public void sendEmailTemplate(String templateName, String toEmail, String subject, Map<String, Object> params) throws ServiceException {
//        String mailContents = thymeLeaf.renderTemplate(templateName, params);
//
//        Email from = new Email(senderEmailAddr);
//        Email to = new Email(toEmail);
//        Content content = new Content("text/html", mailContents);
//        Mail mail = new Mail(from, subject, to, content);
//
//        Request request = new Request();
//        try {
//            request.setMethod(Method.POST);
//            request.setEndpoint("mail/send");
//            request.setBody(mail.build());
//            Response response = sendgrid.api(request);
//
//            if (response.getStatusCode() > 250) {
//                throw new ServiceException(response.getBody());
//            }
//        } catch (IOException e) {
//            throw new ServiceException(e);
//        }
//    }

}
