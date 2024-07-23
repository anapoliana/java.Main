import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

abstract class CalculoLocacao {
    public abstract double calcularValorLocacao(double horas);
    public abstract double calcularImposto(double valorTotal);

    public Map<String, Double> gerarNotaPagamento(double valorLocacao) {
        double imposto = calcularImposto(valorLocacao);
        double valorTotal = valorLocacao + imposto;
        Map<String, Double> notaPagamento = new HashMap<>();
        notaPagamento.put("Valor da locação", valorLocacao);
        notaPagamento.put("Imposto", imposto);
        notaPagamento.put("Valor total do pagamento", valorTotal);
        return notaPagamento;
    }
}

class LocacaoPorHora extends CalculoLocacao {
    private double valorPorHora;

    public LocacaoPorHora(double valorPorHora) {
        this.valorPorHora = valorPorHora;
    }

    @Override
    public double calcularValorLocacao(double horas) {
        return horas <= 12 ? horas * valorPorHora : calcularValorDiaria();
    }

    @Override
    public double calcularImposto(double valorTotal) {
        return valorTotal <= 100 ? 0.2 * valorTotal : 0.15 * valorTotal;
    }

    private double calcularValorDiaria() {
        return 12 * valorPorHora;
    }
}

class LocacaoPorDia extends CalculoLocacao {
    private double valorDiaria;

    public LocacaoPorDia(double valorDiaria) {
        this.valorDiaria = valorDiaria;
    }

    @Override
    public double calcularValorLocacao(double horas) {
        return valorDiaria * Math.ceil(horas / 24);
    }

    @Override
    public double calcularImposto(double valorTotal) {
        return valorTotal <= 100 ? 0.2 * valorTotal : 0.15 * valorTotal;
    }
}

class Locadora {
    private String modelo;
    private LocalDateTime inicio;
    private LocalDateTime fim;
    private CalculoLocacao calculoLocacao;

    public Locadora(String modelo, LocalDateTime inicio, LocalDateTime fim, CalculoLocacao calculoLocacao) {
        this.modelo = modelo;
        this.inicio = inicio;
        this.fim =fim;
        this.calculoLocacao = calculoLocacao;
    }

    private double calcularTotalHoras() {
        return ChronoUnit.HOURS.between(inicio, fim);
    }

    public Map<String, Double> calcularValorTotalLocacao() {
        double horas = calcularTotalHoras();
        double valorLocacao = calculoLocacao.calcularValorLocacao(horas);
        return calculoLocacao.gerarNotaPagamento(valorLocacao);
    }
}

public class Main {
    public static void main(String[] args) {
        double valorPorHora = 10;
        double valorDiaria = 100;

        LocacaoPorHora locacaoPorHora = new LocacaoPorHora(valorPorHora);
        LocacaoPorDia locacaoPorDia = new LocacaoPorDia(valorDiaria);

        LocalDateTime inicio1 = LocalDateTime.of(2024, 7, 23, 10, 0, 0);
        LocalDateTime fim1 = LocalDateTime.of(2024, 7, 23, 15, 0, 0);
        Locadora locadora1 = new Locadora("Sedan", inicio1, fim1, locacaoPorHora);
        Map<String, Double> notaPagamento1 = locadora1.calcularValorTotalLocacao();
        System.out.println("Exemplo de locação por hora:");
        System.out.println(notaPagamento1);

        LocalDateTime inicio2 = LocalDateTime.of(2024, 7, 23, 10, 0, 0);
        LocalDateTime fim2 = LocalDateTime.of(2024, 7, 25, 15, 0, 0);
        Locadora locadora2 = new Locadora("SUV", inicio2, fim2, locacaoPorDia);
        Map<String, Double> notaPagamento2 = locadora2.calcularValorTotalLocacao();
        System.out.println("\nExemplo de locação por dia:");
        System.out.println(notaPagamento2);
    }
