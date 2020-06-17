
package gusto.fatec.ipping.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;

public class RedesController {

	public RedesController() {
		super();
	}

	public String ip(String so) throws SocketException {
		ArrayList<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
		String comando = "";
		String saida = "";
		String[] resultadoComandoSplit;
		
		//corrigir parte do windows
		if (so.contains("Windows")) {
			comando = "ipconfig";
			resultadoComandoSplit = executarComando(comando).split("\nAdaptador Ethernet");
			for (String lista : resultadoComandoSplit) {
				if (lista.contains("IPv4")) {
					String[] adaptador = lista.split("\n");
					System.out.println(adaptador[0].trim());

					for (String ipv4 : adaptador) {
						if (ipv4.contains("IPv4")) {
							System.out.println("* Endereço IPv4: " + ipv4.substring(49));
						}
					}
				}
			}
		} else if (so.contains("Linux")) {
			comando = "ip a";
			resultadoComandoSplit = executarComando(comando).split("\n");
			for (String linha : resultadoComandoSplit) {
				for (int i = 0; i < interfaces.size(); i++) {
					if (linha.contains(interfaces.get(i).getName() + ":")) {
						saida += "Inteface" + linha.substring(1, linha.lastIndexOf(":"));
						saida += "\n";
					}
					if (linha.contains("inet ")) {
						saida += "IPV4:" + linha.substring(8, linha.lastIndexOf("/"));
						saida += "\n\n";
						break;
					}
				}
			}
		}
		return saida;
	}

	public String ping(String so) {
		String comando = "";
		String saida = "";
		String[] ping;
		double tempoMedio;
		if (so.contains("Windows")) {
			comando = "ping -n 10 www.google.com";
			ping = executarComando(comando).split("\n");
			System.out.println("Tempo médio: " + ping[ping.length - 1].substring(44));
		} else if (so.contains("Linux")) {
			comando = "ifconfig";
			executarComando(comando);
			comando = "ping -c 10 www.google.com";
			ping = executarComando(comando).split("\n");
			for (String saidaPing : ping) {
				if (saidaPing.contains("packets transmitted")) {
					tempoMedio = Double.parseDouble(saidaPing.substring(58).replace("ms", "")) / 10;
					saida += "Tempo Total: " + saidaPing.substring(58) + "\nTempo médio: " + tempoMedio + "ms";
				}
			}
		}
		return saida;
	}

	private String executarComando(String comando) {
		String saida = "";

		try {
			Process processo = Runtime.getRuntime().exec(comando);
			InputStream stream = processo.getInputStream();
			InputStreamReader reader = new InputStreamReader(stream);
			BufferedReader buffer = new BufferedReader(reader);
			String linha = buffer.readLine();
			while (linha != null) {
				saida += linha + "\n";
				linha = buffer.readLine();
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		return saida;
	}
}
