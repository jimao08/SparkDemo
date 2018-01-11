import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.XMLWriter;

import java.io.FileOutputStream;

public class XmlDemo {
    public static void main(String[] args) throws Exception{
        Element root = DocumentHelper.createElement("books");
        Document doucment = DocumentHelper.createDocument(root);
        //根节点
        root.addAttribute("name", "bookvalue");
        //子节点
        Element element1 = root.addElement("author1 ");
        element1.addAttribute("name", "James1");
        element1.addAttribute("location1", "UK1");
        element1.addText("James Strachan1");


        Element element = root.addElement("author2 ");
        element.addAttribute("name", "chen");
        element.addAttribute("kenken", "ZK");
        element.addText("chen kenken");
        //添加
        FileOutputStream file = new FileOutputStream("books.xml");
        XMLWriter xmlwriter2 = new XMLWriter(file);
        xmlwriter2.write(doucment);


        XMLWriter xml = new XMLWriter(file);

        xml.flush();
        xml.close();

        file.close();
    }
}
