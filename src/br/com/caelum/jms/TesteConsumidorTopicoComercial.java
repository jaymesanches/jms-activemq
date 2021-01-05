package br.com.caelum.jms;

import java.util.Scanner;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.Topic;
import javax.naming.InitialContext;

import org.apache.activemq.ActiveMQConnectionFactory;

import br.com.caelum.modelo.Pedido;

public class TesteConsumidorTopicoComercial {
  public static void main(String[] args) throws Exception {
//    System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES","java.lang,br.com.caelum.modelo");
    System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES","*");
    
    InitialContext context = new InitialContext();
    ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
    
//    ActiveMQConnectionFactory factory = (ActiveMQConnectionFactory) context.lookup("ConnectionFactory");
//    factory.setTrustAllPackages(true);
    
    Connection connection = factory.createConnection("user", "senha");
    connection.setClientID("comercial");

    connection.start();

    Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

    Topic topico = (Topic) context.lookup("loja");

    MessageConsumer consumer = session.createDurableSubscriber(topico, "assinatura");

    consumer.setMessageListener(new MessageListener() {
      @Override
      public void onMessage(Message message) {
        ObjectMessage objectMessage = (ObjectMessage) message;
        
        try {
          Pedido pedido = (Pedido) objectMessage.getObject();
          System.out.println("Recebendo Mensagem: " + pedido.getCodigo());
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
