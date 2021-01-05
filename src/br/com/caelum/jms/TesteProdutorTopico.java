package br.com.caelum.jms;

import java.io.StringWriter;
import java.util.Scanner;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.naming.InitialContext;
import javax.xml.bind.JAXB;

import br.com.caelum.modelo.Pedido;
import br.com.caelum.modelo.PedidoFactory;

public class TesteProdutorTopico {
  public static void main(String[] args) throws Exception {
    InitialContext context = new InitialContext();
    ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
    Connection connection = factory.createConnection("user", "senha");

    connection.start();

    Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

    Destination topico = (Destination) context.lookup("loja");

    MessageProducer producer = session.createProducer(topico);

    Pedido pedido = new PedidoFactory().geraPedidoComValores();
//    StringWriter writer = new StringWriter();
//    JAXB.marshal(pedido, writer);
//
//    String xml = writer.toString();
//    Message message = session.createTextMessage(xml);

    Message message = session.createObjectMessage(pedido);

//    message.setBooleanProperty("ebook", false);
    producer.send(message);

    new Scanner(System.in).nextLine(); // parar o programa para testar a conexao

    session.close();
    connection.close();
    context.close();
  }
}
