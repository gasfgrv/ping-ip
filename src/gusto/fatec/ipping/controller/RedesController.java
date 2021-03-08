
package gusto.fatec.ipping.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Logger;

import static java.lang.Double.parseDouble;
import static java.lang.Runtime.getRuntime;
import static java.lang.String.format;
import static java.util.logging.Logger.getLogger;

public class RedesController {

    private static final Logger LOG = getLogger(RedesController.class.getName());

    public String ip(String so) throws SocketException {
        ArrayList<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
        StringBuilder saida = new StringBuilder();

        //corrigir parte do windows
        if (so.contains("Windows")) {
            getIpWindows();
        } else if (so.contains("Linux")) {
            getIpLinux(interfaces, saida);
        }
        return saida.toString();
    }

    private void getIpWindows() {
        String comando = "ipconfig";
        String[] resultadoComandoSplit = executarComando(comando).split("\nAdaptador Ethernet");

        for (String lista : resultadoComandoSplit) {
            if (lista.contains("IPv4")) {
                String[] adaptador = lista.split("\n");

                String logInfo = adaptador[0].trim();
                LOG.info(logInfo);

                for (String ipv4 : adaptador) {

                    if (ipv4.contains("IPv4")) {
                        String enderecoIpv4 = format("* Endereço IPv4: %s", ipv4.substring(49));
                        LOG.info(enderecoIpv4);
                    }
                }
            }
        }
    }

    private void getIpLinux(ArrayList<NetworkInterface> interfaces, StringBuilder saida) {
        String comando = "ip a";
        String[] resultadoComandoSplit = executarComando(comando).split("\n");

        for (String linha : resultadoComandoSplit) {

            for (NetworkInterface anInterface : interfaces) {
                if (linha.contains(anInterface.getName() + ":")) {
                    saida.append("Inteface").append(linha.substring(1, linha.lastIndexOf(":"))).append("\n");
                }
                if (linha.contains("inet ")) {
                    saida.append("IPV4:").append(linha.substring(8, linha.lastIndexOf("/"))).append("\n\n");
                    break;
                }
            }
        }
    }

    public String ping(String so) {
        StringBuilder saida = new StringBuilder();

        if (so.contains("Windows")) {
            getPingWindows();
        } else if (so.contains("Linux")) {
            getPingLinux(saida);
        }

        return saida.toString();
    }

    private void getPingLinux(StringBuilder saida) {
        String[] ping = executarComando("ping -c 10 www.google.com").split("\n");

        for (String saidaPing : ping) {
            if (saidaPing.contains("packets transmitted")) {
                double tempoMedio = (parseDouble(saidaPing.substring(58).replace("ms", "")) / 10);

                saida.append("Tempo Total: ")
                        .append(saidaPing.substring(58))
                        .append("\nTempo médio: ")
                        .append(tempoMedio).append("ms");
            }
        }
    }

    private void getPingWindows() {
        String comando = "ping -n 10 www.google.com";
        String[] ping = executarComando(comando).split("\n");

        String logMessage = format("Tempo médio: %s", ping[ping.length - 1].substring(44));
        LOG.info(logMessage);
    }

    private String executarComando(String comando) {
        StringBuilder saida = new StringBuilder();

        try {
            Process processo = getRuntime().exec(comando);
            InputStream stream = processo.getInputStream();
            InputStreamReader reader = new InputStreamReader(stream);
            BufferedReader buffer = new BufferedReader(reader);
            String linha = buffer.readLine();

            while (linha != null) {
                saida.append(linha).append("\n");
                linha = buffer.readLine();
            }
        } catch (IOException e) {
            LOG.warning(e.getMessage());
        }

        return saida.toString();
    }
}
