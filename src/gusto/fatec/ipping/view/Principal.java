package gusto.fatec.ipping.view;

import java.net.SocketException;

import javax.swing.JOptionPane;

import gusto.fatec.ipping.controller.RedesController;

public class Principal {

	public static void main(String[] args) throws SocketException {
		String so = System.getProperty("os.name");
		RedesController controller = new RedesController();
		String opcao = "0";
		while (!opcao.equals("3")) {
			opcao = JOptionPane.showInputDialog("1 - Listar IP's\n2 - Ping\n3 - Sair");
			switch (opcao) {
			case "1":
				JOptionPane.showMessageDialog(null, controller.ip(so), "IPs por interface", JOptionPane.PLAIN_MESSAGE);
				break;
			case "2":
				JOptionPane.showMessageDialog(null, controller.ping(so), "Ping", JOptionPane.PLAIN_MESSAGE);
				break;
			default:
				opcao = JOptionPane.showInputDialog("1 - Listar IP's\n2 - Ping\n3 - Sair");
				break;
			}
		}

	}
}