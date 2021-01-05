package br.com.caelum.jms;

import java.util.Enumeration;
import java.util.Scanner;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;

public class TesteQueueBrowserFila {
  public static void main(String[] args) throws Exception {
    InitialContext context = new InitialContext();
    ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
    Connection connection = factory.createConnection("user", "senha");

    connection.start();

    Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

    Destination fila = (Destination) context.lookup("financeiro");

    QueueBrowser browser = session.createBrowser((Queue) fila);
    
    Enumeration msgs = browser.getEnumeration();
    
    while(msgs.hasMoreElements()) {
      TextMessage msg = (TextMessage) msgs.nextElement();
      System.out.println("Message: " + msg.getText());
    }

    new Scanner(System.in).nextLine(); // parar o programa para testar a conexao

    session.close();
    connection.close();
    context.close();
  }
}
