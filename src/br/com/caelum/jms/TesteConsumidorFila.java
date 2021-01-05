package br.com.caelum.jms;

import java.util.Scanner;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;

public class TesteConsumidorFila {
  public static void main(String[] args) throws Exception {
    InitialContext context = new InitialContext();
    ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
    Connection connection = factory.createConnection("user", "senha");

    connection.start();

    Session session = connection.createSession(true, Session.SESSION_TRANSACTED);

    Destination fila = (Destination) context.lookup("financeiro");

    MessageConsumer consumer = session.createConsumer(fila);

    consumer.setMessageListener(new MessageListener() {
      @Override
      public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        try {
          System.out.println("Recebendo Mensagem: " + textMessage.getText());
//          message.acknowledge();
          session.rollback();
        } catch (JMSException e) {
          e.printStackTrace();
        }
      }
    });

    new Scanner(System.in).nextLine(); // parar o programa para testar a conexao

    session.close();
    connection.close();
    context.close();
  }
}
