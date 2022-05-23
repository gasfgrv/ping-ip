package gusto.fatec.ipping;

import gusto.fatec.ipping.controller.RedesController;
import java.net.SocketException;
import javax.swing.JOptionPane;

import static java.lang.System.getProperty;
import static javax.swing.JOptionPane.PLAIN_MESSAGE;

public class Principal {

    public static void main(String[] args) throws SocketException {
        RedesController controller = new RedesController();
        String so = getProperty("os.name");

        String menu = "1 - Listar IP's\n2 - Ping\n3 - Sair";
        String opcao = "0";

        while (!opcao.equals("3")) {
            opcao = JOptionPane.showInputDialog(menu);

            switch (opcao) {
                case "1":
                    JOptionPane.showMessageDialog(null, controller.ip(so), "IPs por interface", PLAIN_MESSAGE);
                    break;
                case "2":
                    JOptionPane.showMessageDialog(null, controller.ping(so), "Ping", PLAIN_MESSAGE);
                    break;
                case "3":
                    System.exit(0);
                    break;
                default:
                    opcao = JOptionPane.showInputDialog(menu);
                    break;
            }
        }

    }
}