package com.example.demo.Model.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.Model.Service.SoapConfig;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.util.*;

import static java.lang.Integer.parseInt;

@Service
public class JWTService {

	@Autowired
	private SoapConfig config;

	private Dictionary<Integer,String> Roles = new Hashtable();

	Integer content_m = 0;
	Integer cl = 0;
	Integer adm = 0;
	Integer art = 0;
	public void SetRoles() {
		String response;
		try {
			response = config.soapClient(config.marshaller()).Roles();
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
		String arrayResult[];

		response = response.replace("\n"," ");

		arrayResult = (response.split(" "));

	//	System.out.println(arrayResult.length);
		for(int i = 0 ; i < arrayResult.length; i=i+2) {
//			System.out.println(arrayResult[i]);
//			System.out.println(arrayResult[i+1]);
			Roles.put(parseInt(arrayResult[i]),arrayResult[i+1]);
		}

		for (Enumeration i = Roles.keys(); i.hasMoreElements();)
		{
			Integer x = (Integer)i.nextElement();
			if(Roles.get(x).equals("content_manager"))
				content_m = x;
			if(Roles.get(x).equals("admin"))
				adm = x;
			if(Roles.get(x).equals("artist"))
				art = x;
			if(Roles.get(x).equals("client"))
				cl = x;

		}

	}

	public boolean getExpire(String token){
		try {
			DecodedJWT decodedJWT = JWT.decode(token);
			Date expiresAt = decodedJWT.getExpiresAt();
			Calendar c = Calendar.getInstance();
			c.setTime(expiresAt);
			c.add(Calendar.HOUR, -2);
			Date test = c.getTime();
			System.out.println(test);
			System.out.println(test.compareTo(new Date()));
			if (test.compareTo(new Date()) > 0)
				return false;
			else
				return true;
		}catch(Exception e){
			return true;

		}

	}

	public String GetUserId(String jwt){

		String UIRID = JWTvalidator(jwt);
		if(UIRID.length() == 0)
			return "";
		String result[] = UIRID.split("\\|");

		return result[0];
	}

	public String JWTvalidator(String jwt){
		SetRoles();
		String response = "";
		try{
			response = config.soapClient(config.marshaller()).AuthorizeUser(jwt);
			if(response.contains("Eroare")){
				switch (response){
					case "Eroare, invalid user id!" -> throw new AuthenticationException("Invalid user id ");
					case "Eroare, bad roles my friend!" -> throw new AuthenticationException("Conflicting roles ");
					case "Eroare, Token has invalid signature!" -> throw new AuthenticationException("Bad signature ");
					case "Eroare, invalid JWT, token is in blacklist!" -> throw new AuthenticationException("Token is in blacklist ");
					default -> {}
				}
			}
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		} catch (AuthenticationException e) {
			throw new RuntimeException(e.getMessage());
		}

		return response;
	}

	 public Boolean JWTcontentManager(String jwt){
		// in roles[0] -> user id, in roles[1] -> roles id's
		 String UIRID = JWTvalidator(jwt);
		 if(UIRID.length() == 0)
			 return Boolean.FALSE;

		 String result[] = UIRID.split("\\|");

		 String UID = result[0];
		 //TODO DACA RESULT[1] e gol de verificat
		 if(result.length <= 1)
			 return Boolean.FALSE;


		 String roles[] = result[1].split(" ");
		 for(int i = 0 ; i < roles.length;i++ ){
				//System.out.println(roles[i]);
			 if( parseInt(roles[i]) == content_m ){
				 return Boolean.TRUE;
			 }
		 }


		return Boolean.FALSE;
	 }
	 public Boolean JWTartist(String jwt){

		 String UIRID = JWTvalidator(jwt);
		 if(UIRID.length() == 0)
			 return Boolean.FALSE;

		 String result[] = UIRID.split("\\|");
		 String UID = result[0];
		 //TODO DACA RESULT[1] e gol de verificat
		 if(result.length <= 1)
			 return Boolean.FALSE;


		 String roles[] = result[1].split(" ");
		 for(int i = 0 ; i < roles.length;i++ ){
			 //System.out.println(roles[i]);
			 if( parseInt(roles[i]) == art ){
				 return Boolean.TRUE;
			 }
		 }


		 return Boolean.FALSE;
	 }

	public Boolean JWTclient(String jwt){
		// in roles[0] -> user id, in roles[1] -> roles id's
		//TODO chiar merge??
		String UIRID = JWTvalidator(jwt);
		if(UIRID.length() == 0)
			return Boolean.FALSE;

		String result[] = UIRID.split("\\|");

		String UID = result[0];
		//TODO DACA RESULT[1] e gol de verificat
		if(result.length <= 1)
			return Boolean.FALSE;


		String roles[] = result[1].split(" ");
		for(int i = 0 ; i < roles.length;i++ ){
			//System.out.println(roles[i]);
			if( parseInt(roles[i]) == cl ){
				return Boolean.TRUE;
			}
		}


		return Boolean.FALSE;
	}

	public Boolean JWTadmin(String jwt){
		// in roles[0] -> user id, in roles[1] -> roles id's
		//TODO chiar merge??
		String UIRID = JWTvalidator(jwt);
		if(UIRID.length() == 0)
			return Boolean.FALSE;

		String result[] = UIRID.split("\\|");

		String UID = result[0];
		//TODO DACA RESULT[1] e gol de verificat
		if(result.length <= 1)
			return Boolean.FALSE;


		String roles[] = result[1].split(" ");
		for(int i = 0 ; i < roles.length;i++ ){
			//System.out.println(roles[i]);
			if( parseInt(roles[i]) == adm ){
				return Boolean.TRUE;
			}
		}


		return Boolean.FALSE;
	}





}
