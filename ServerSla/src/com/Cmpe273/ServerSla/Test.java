package com.Cmpe273.ServerSla;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

import org.w3c.dom.NodeList;

//import com.sun.jersey.core.util.Base64;

import java.util.Base64.Encoder;
import org.jsoup.*;
import java.io.*;

import org.apache.commons.codec.binary.Base64;
import java.util.Map;

public interface Test {


	public Map<String,Integer> htmlParse() throws IOException;
	
	
	
}
