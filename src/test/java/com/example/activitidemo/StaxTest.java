package com.example.activitidemo;


import org.apache.commons.io.input.XmlStreamReader;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.io.Reader;

/**
 * chapter4 流程文档解析原理
 */
public class StaxTest {
    public static void main(String[] args) throws Exception {
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        InputStream is = StaxTest.class.getClassLoader().getResourceAsStream("processes/chapter4/stax.xml");
        Reader reader = new XmlStreamReader(is);
        XMLStreamReader xsr = xmlInputFactory.createXMLStreamReader(reader);
        while (xsr.hasNext()) {
            int event = xsr.next();
            if (event == XMLStreamConstants.START_DOCUMENT) {

            } else if (event == XMLStreamConstants.END_DOCUMENT) {

            } else if (event == XMLStreamConstants.START_ELEMENT) {
                System.out.println("节点开始解析:" + xsr.getLocalName());
                if (xsr.getLocalName().equals("userTask") || xsr.getLocalName().equals("endEvent")) {
                    for (int i = 0; i < xsr.getAttributeCount();i++) {
                        if(xsr.getAttributeName(i).toString().startsWith("{")){
                            System.out.print("--->>>"+xsr.getAttributeValue("http://activiti.org/bpmn","assignee"));
                        } else {
                            System.out.print(xsr.getAttributeName(i)+":");
                            System.out.print(xsr.getAttributeValue(i)+";");
                        }
                    }
                    System.out.println();
                }
            } else if(event == XMLStreamConstants.END_ELEMENT){

            } else if(event == XMLStreamConstants.CHARACTERS){
                String text = xsr.getText();
                if(!text.isEmpty() && text.trim().length()>0){
                    System.out.println(text);
                }
            }
        }
    }
}
