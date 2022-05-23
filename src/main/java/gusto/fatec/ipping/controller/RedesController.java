
package gusto.fatec.ipping.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RedesController {

    public String ip(String so) throws SocketException {
        ArrayList<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());

        StringBuilder saida = new StringBuilder();

        if (so.contains("Linux")) {
            saida.append("SO: Linux").append("\n");
        }

        if (so.contains("Windows")) {
            saida.append("SO: Windows").append("\n");
        }

        interfaces.forEach(networkInterface -> {
            String nome = networkInterface.getName();
            List<InetAddress> enderecos = networkInterface.inetAddresses().collect(Collectors.toList());

            saida.append("Inteface: ").append(nome).append("\n");
            saida.append("IPv4: ").append(enderecos.get(enderecos.size() - 1)).append("\n");
            saida.append("\n");
        });

        return saida.toString();
    }

    public String ping(String so) {
        StringBuilder saida = new StringBuilder();

        if (so.contains("Windows")) {
            saida.append(getPingWindows());
        } else if (so.contains("Linux")) {
            saida.append(getPingLinux());
        }

        return saida.toString();
    }

    private String getPingLinux() {
        List<String> ping = Arrays.stream(executarComando("ping -c 3 www.google.com").split("\n"))
                .collect(Collectors.toList());

        String resultado = filtrarResultado(ping);

        if (resultado == null) {
            throw new NullPointerException();
        }

        String tempo = resultado
                .substring(63)
                .replace("ms", "");

        double tempoMedio = Double.parseDouble(tempo) / 10;

        return String.format("Tempo Total: %s ms, Tempo médio: %s ms", tempo, tempoMedio);
    }

    private String filtrarResultado(List<String> ping) {
        return ping.stream()
                .filter(s -> s.contains("pacotes transmitidos"))
                .findFirst()
                .orElse(null);
    }


    private String getPingWindows() {
        String comando = "ping -n 10 www.google.com";
        String[] ping = executarComando(comando).split("\n");

        String tempoMedio = ping[ping.length - 1].substring(44);

        double tempoTotal = Double.parseDouble(tempoMedio) * 10;

        return String.format("Tempo Total: %s ms.Tempo médio: %s ms", tempoTotal, tempoMedio);
    }

    private String executarComando(String comando) {
        try {
            Process exec = Runtime.getRuntime().exec(comando);

            InputStreamReader inputStreamReader = new InputStreamReader(exec.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            return montarSaida(bufferedReader).toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private StringBuilder montarSaida(BufferedReader bufferedReader) throws IOException {
        String linha = bufferedReader.readLine();
        StringBuilder saida = new StringBuilder();

        while (linha != null) {
            saida.append(linha).append("\n");
            linha = bufferedReader.readLine();
        }

        return saida;
    }

}
