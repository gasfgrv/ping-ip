package gusto.fatec.ipping.view;

import gusto.fatec.ipping.controller.RedesController;

import java.net.SocketException;

import static java.lang.System.getProperty;
import static javax.swing.JOptionPane.*;

public class Principal {

	public static void main(String[] args) throws SocketException {
		RedesController controller = new RedesController();
		String so = getProperty("os.name");

		String menu = "1 - Listar IP's\n2 - Ping\n3 - Sair";
		String opcao = "0";

		while (!opcao.equals("3")) {
			opcao = showInputDialog(menu);
			switch (opcao) {
			case "1":
				showMessageDialog(null,
						controller.ip(so),
						"IPs por interface",
						PLAIN_MESSAGE);
				break;
			case "2":
				showMessageDialog(null,
						controller.ping(so),
						"Ping",
						PLAIN_MESSAGE);
				break;
			default:
				opcao = showInputDialog(menu);
				break;
			}
		}

	}
}