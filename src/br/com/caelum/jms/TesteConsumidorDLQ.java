package br.com.caelum.jms;

import java.util.Scanner;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.naming.InitialContext;

public class TesteConsumidorDLQ {
  public static void main(String[] args) throws Exception {
    InitialContext context = new InitialContext();
    ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
    Connection connection = factory.createConnection("user", "senha");

    connection.start();

    Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

    Destination fila = (Destination) context.lookup("DLQ");

    MessageConsumer consumer = session.createConsumer(fila);

    consumer.setMessageListener(new MessageListener() {
      @Override
      public void onMessage(Message message) {
        System.out.println("Recebendo Mensagem: " + message);
      }
    });

    new Scanner(System.in).nextLine(); // parar o programa para testar a conexao

    session.close();
    connection.close();
    context.close();
  }
}
