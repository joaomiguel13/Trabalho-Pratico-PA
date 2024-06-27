package pt.isec.pa.javalife.model.data;

import java.io.Serializable;

public record Area(double cima, double esquerda, double baixo, double direita) implements Serializable {
    public static boolean isAdjacent(Area a1, Area a2) {
        return (Math.abs(a1.esquerda() - a2.direita()) <= 1 || Math.abs(a1.direita() - a2.esquerda()) <= 1) &&
                (Math.abs(a1.cima() - a2.baixo()) <= 1 || Math.abs(a1.baixo() - a2.cima()) <= 1);
    }



    public boolean intersects(Area area) {
        return cima < area.baixo() && baixo() > area.cima() && esquerda < area.direita() && direita() > area.esquerda();
    }
    public static double calcularDistancia(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
    public static double calcularMenorDistancia(Area area1, Area area2) {
        double[][] pontosArea1 = {
                {area1.cima(), area1.esquerda()},
                {area1.cima(), area1.direita()},
                {area1.baixo(), area1.esquerda()},
                {area1.baixo(), area1.direita()}
        };

        double[][] pontosArea2 = {
                {area2.cima(), area2.esquerda()},
                {area2.cima(), area2.direita()},
                {area2.baixo(), area2.esquerda()},
                {area2.baixo(), area2.direita()}
        };

        double menorDistancia = Double.MAX_VALUE;

        for (double[] ponto1 : pontosArea1) {
            for (double[] ponto2 : pontosArea2) {
                double distancia = calcularDistancia(ponto1[1], ponto1[0], ponto2[1], ponto2[0]);
                if (distancia < menorDistancia) {
                    menorDistancia = distancia;
                }
            }
        }

        return menorDistancia;
    }
}

