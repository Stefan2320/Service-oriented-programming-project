import { Link } from 'react-router-dom';
import { useNavigate } from 'react-router-dom';
import { XMLParser } from 'react-xml-parser';
import React, { useEffect, useState } from "react";

function autorizare(jwt){
    return new Promise( function (resolve,reject){
                var sr = '<soap11env:Envelope xmlns:soap11env="http://schemas.xmlsoap.org/soap/envelope/" xmlns:sample="services.bd.soap">' +
                                                '<soap11env:Body>' +
                                                    '<sample:autorizare>'+
                                                        '<sample:jwt1>'+jwt+'</sample:jwt1>' +
                                                    '</sample:autorizare>' +
                                                '</soap11env:Body>' +
                                            '</soap11env:Envelope>';
                                  var xmlhttp = new XMLHttpRequest();
                                  var url = "http://127.0.0.1:8000"
                                  xmlhttp.open('POST',url);
                                  xmlhttp.onload = resolve;
                                  xmlhttp.onerror = reject;
                                  xmlhttp.send(sr);
                                  xmlhttp.onload = () => {
                                          if(xmlhttp.status===200) {
                                            resolve(xmlhttp.response)
                                          }
                                        }

                    });
    }



function login(username,password){
    return new Promise( function (resolve,reject){
    var sr = '<soap11env:Envelope xmlns:soap11env="http://schemas.xmlsoap.org/soap/envelope/" xmlns:sample="services.bd.soap">' +
                                '<soap11env:Body>' +
                                    '<sample:login>'+
                                        '<sample:username>'+username+'</sample:username>' +
                                        '<sample:password>'+password+'</sample:password>' +
                                    '</sample:login>' +
                                '</soap11env:Body>' +
                            '</soap11env:Envelope>';
                  var xmlhttp = new XMLHttpRequest();
                  var url = "http://127.0.0.1:8000"
                  xmlhttp.open('POST',url);
                  xmlhttp.onload = resolve;
                  xmlhttp.onerror = reject;
                  xmlhttp.send(sr);
                  xmlhttp.onload = () => {
                          if(xmlhttp.status===200) {
                            resolve(xmlhttp.response)
                          }
                        }

    });
}


const Login = ({setJWT,setId,setRole}) => {
  const [error, setError] = useState(false);
  const navigate = useNavigate();
  const  handleSubmit = (e) =>{

          e.preventDefault();
          const username = e.target.elements.username.value
          const password = e.target.elements.password.value
          login(username,password).then(function(value){

               var XMLParser = require('react-xml-parser');
               var xml = new XMLParser().parseFromString(value);
               const jwt = xml.getElementsByTagName('tns:loginResult')[0].value;
               if(jwt == "Date gresite"){
                    setError(true);
               }else{

                        setJWT(jwt)
                        autorizare(jwt).then(function(value){
                        var XMLParser = require('react-xml-parser');
                        var xml = new XMLParser().parseFromString(value);
                        const role_id = xml.getElementsByTagName('tns:autorizareResult')[0].value;
                        //console.log(role_id.split("|"))
                        var temp = role_id.split("|")
                        var s = temp[1].split(" ")
                        setId(temp[0])
                        var roles = []
                        for(let i = 1 ; i < s.length ;i++)
                            roles.push(s[i])
                         setRole(roles)
                        navigate('/home')
                    })

               }

          });

        }

    return (
    <div>
         <form onSubmit={handleSubmit} >
             <label for="username">Username:</label><br></br>
             <input type="text" id="username" name="username"></input><br></br>
             <label for="password">Password</label><br></br>
             <input type="password" id="password" name="password"></input>
             <button type="submit">Login</button>
         </form>
         <p>
              { error == true &&
                <h2>Error, failed to login</h2>
              }
          </p>
    </div>
    );
};

export default Login;
