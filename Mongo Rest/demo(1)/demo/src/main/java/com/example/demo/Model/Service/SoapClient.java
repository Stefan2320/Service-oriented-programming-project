package com.example.demo.Model.Service;

import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import wsdl.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class SoapClient extends WebServiceGatewaySupport{
    public String AuthorizeUser(String token) throws JAXBException{
        JAXBContext context = JAXBContext.newInstance(Autorizare.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        Autorizare request = new Autorizare();

        ObjectFactory objectFactory = new ObjectFactory();
        JAXBElement<String> jaxbElement = objectFactory.createAutorizareJwt1(token);
        request.setJwt1(jaxbElement);
        JAXBElement<Autorizare> auth = objectFactory.createAutorizare(request);

        JAXBElement<AutorizareResponse> authorizeResponse = (JAXBElement<AutorizareResponse>) getWebServiceTemplate().marshalSendAndReceive("http://127.0.0.1:8000", auth, null);

        return authorizeResponse.getValue().getAutorizareResult().getValue();
    }

    public String Roles() throws JAXBException{
        JAXBContext context = JAXBContext.newInstance(GetRoles.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        GetRoles request = new GetRoles();

        ObjectFactory objectFactory = new ObjectFactory();
        GetRoles jaxbElement = objectFactory.createGetRoles();
        JAXBElement<GetRoles> auth = objectFactory.createGetRoles(request);

        JAXBElement<GetRolesResponse> authorizeResponse = (JAXBElement<GetRolesResponse>) getWebServiceTemplate().marshalSendAndReceive("http://127.0.0.1:8000", auth, null);

        return authorizeResponse.getValue().getGetRolesResult().getValue();
    }



}
